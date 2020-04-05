package com.lothrazar.cyclic.block.buildershape;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.util.UtilShape;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileStructure extends TileEntityBase implements INamedContainerProvider, ITickableTileEntity {

  public static int maxSize = 10;
  public static int maxHeight = 10;

  public static enum Fields {
    TIMER, BUILDTYPE, SIZE, HEIGHT, REDSTONE, RENDERPARTICLES, ROTATIONS, OX, OY, OZ;
  }

  static final int MAX = 64000;
  private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);
  private LazyOptional<IItemHandler> inventory = LazyOptional.of(this::createHandler);
  private BuildStructureType buildType;
  private int buildSize = 3;
  private int height;
  private int shapeIndex = 0;// current index of shape array
  //  private int renderParticles = 1;
  private int rotations = 0;
  private int offsetX = 0;
  private int offsetY = 0;
  private int offsetZ = 0;
  private int renderParticles;
  private int timer;

  public TileStructure() {
    super(BlockRegistry.Tiles.structure);
  }

  @Override
  public void read(CompoundNBT tag) {
    energy.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("energy")));
    inventory.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("inv")));
    int t = tag.getInt("buildType");
    buildType = BuildStructureType.values()[t];
    buildSize = tag.getInt("buildSize");
    height = tag.getInt("height");
    shapeIndex = tag.getInt("shapeIndex");
    rotations = tag.getInt("rotations");
    offsetX = tag.getInt("offsetX");
    offsetY = tag.getInt("offsetY");
    offsetZ = tag.getInt("offsetZ");
    super.read(tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.putInt("buildType", buildType.ordinal());
    tag.putInt("buildSize", buildSize);
    tag.putInt("height", height);
    tag.putInt("shapeIndex", shapeIndex);
    tag.putInt("rotations", rotations);
    tag.putInt("offsetX", offsetX);
    tag.putInt("offsetY", offsetY);
    tag.putInt("offsetZ", offsetZ);
    energy.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("energy", compound);
    });
    inventory.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("inv", compound);
    });
    return super.write(tag);
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public AxisAlignedBB getRenderBoundingBox() {
    return TileEntity.INFINITE_EXTENT_AABB;
  }

  private IEnergyStorage createEnergy() {
    return new CustomEnergyStorage(MAX, MAX);
  }

  private IItemHandler createHandler() {
    return new ItemStackHandler(16);
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Nullable
  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerStructure(i, world, pos, playerInventory, playerEntity);
  }

  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
    if (cap == CapabilityEnergy.ENERGY) {
      return energy.cast();
    }
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventory.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void setField(int field, int value) {
    switch (Fields.values()[field]) {
      case TIMER:
        this.timer = value;
      break;
      case BUILDTYPE:
        //??toggleSizeShape
        if (value >= BuildStructureType.values().length) {
          value = 0;
        }
        this.buildType = BuildStructureType.values()[value];
      break;
      case SIZE:
        this.buildSize = value;
      break;
      case HEIGHT:
        if (value > maxHeight) {
          value = maxHeight;
        }
        this.height = Math.max(1, value);
      break;
      case REDSTONE:
        this.setNeedsRedstone(value);
      break;
      case RENDERPARTICLES:
        this.renderParticles = value % 2;
      break;
      case ROTATIONS:
        this.rotations = Math.max(0, value);
      break;
      case OX:
        this.offsetX = value;
      break;
      case OY:
        this.offsetY = value;
      break;
      case OZ:
        this.offsetZ = value;
      break;
    }
  }

  @Override
  public int getField(int field) {
    switch (Fields.values()[field]) {
      case TIMER:
        return timer;
      case BUILDTYPE:
        return this.buildType.ordinal();
      case SIZE:
        return this.buildSize;
      case HEIGHT:
        return this.height;
      case REDSTONE:
        return this.getNeedsRedstone();
      case RENDERPARTICLES:
        return this.renderParticles;
      case ROTATIONS:
        return this.rotations;
      case OX:
        return this.offsetX;
      case OY:
        return this.offsetY;
      case OZ:
        return this.offsetZ;
    }
    return super.getField(field);
  }

  @Override
  public void tick() {
    //    if (this.isPowered() == false) {
    //      return;
    //    }
  }

  private void incrementPosition(List<BlockPos> shape) {
    if (shape == null || shape.size() == 0) {
      return;
    }
    else {
      int c = shapeIndex + 1;
      if (c < 0 || c >= shape.size()) {
        c = 0;
      }
      shapeIndex = c;
    }
  }

  private BlockPos getPosTarget() {
    return this.getPos().add(this.offsetX, this.offsetY, this.offsetZ);
  }

  public BlockPos getTargetFacing() {
    //move center over that much, not including exact horizontal
    return this.getPosTarget().offset(this.getCurrentFacing(), this.buildSize + 1);
  }

  private Direction getCurrentFacing() {
    return this.getBlockState().get(BlockStateProperties.FACING);
  }

  public List<BlockPos> getShape() {
    List<BlockPos> shape = new ArrayList<BlockPos>();
    // only rebuild shapes if they are different
    switch (buildType) {
      case CIRCLE:
        shape = UtilShape.circleHorizontal(this.getPosTarget(), this.getSize() * 2);
        shape = UtilShape.repeatShapeByHeight(shape, getHeight() - 1);
      break;
      case FACING:
        shape = UtilShape.line(this.getPosTarget(), this.getCurrentFacing(), this.getSize());
        shape = UtilShape.repeatShapeByHeight(shape, getHeight() - 1);
      break;
      case SQUARE:
        shape = UtilShape.squareHorizontalHollow(this.getPosTarget(), this.getSize());
        shape = UtilShape.repeatShapeByHeight(shape, getHeight() - 1);
      break;
      case SOLID:
        shape = UtilShape.squareHorizontalFull(this.getTargetFacing(), this.getSize());
        shape = UtilShape.repeatShapeByHeight(shape, getHeight() - 1);
      break;
      case SPHERE:
        shape = UtilShape.sphere(this.getPosTarget(), this.getSize());
      break;
      case DOME:
        shape = UtilShape.sphereDome(this.getPosTarget(), this.getSize());
      break;
      case CUP:
        shape = UtilShape.sphereCup(this.getPosTarget().up(this.getSize()), this.getSize());
      break;
      case DIAGONAL:
        shape = UtilShape.diagonal(this.getPosTarget(), this.getCurrentFacing(), this.getSize() * 2, true);
      break;
      case PYRAMID:
        shape = UtilShape.squarePyramid(this.getPosTarget(), this.getSize(), getHeight());
      break;
    }
    return shape;
  }

  private int getHeight() {
    return height;
  }

  private int getSize() {
    return buildSize;
  }
}
