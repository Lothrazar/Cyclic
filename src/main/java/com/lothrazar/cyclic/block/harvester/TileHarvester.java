package com.lothrazar.cyclic.block.harvester;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.HarvestUtil;
import com.lothrazar.cyclic.util.UtilShape;
import java.util.List;
import net.minecraft.block.BlockState;
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
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileHarvester extends TileEntityBase implements ITickableTileEntity, INamedContainerProvider {

  static enum Fields {
    REDSTONE, RENDER, SIZE, HEIGHT, DIRECTION;
  }

  public static final int MAX_SIZE = 12;
  static final int MAX_ENERGY = 640000;
  public static final int MAX_HEIGHT = 16;
  public static IntValue POWERCONF;
  private int radius = MAX_SIZE / 2;
  private int shapeIndex = 0;
  private int height = 1;
  private boolean directionIsUp = false;
  CustomEnergyStorage energy = new CustomEnergyStorage(MAX_ENERGY, MAX_ENERGY / 4);
  private LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);

  public TileHarvester() {
    super(TileRegistry.HARVESTER);
    timer = 1;
  }

  @Override
  public void tick() {
    this.syncEnergy();
    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      return;
    }
    final int cost = POWERCONF.get();
    if (energy.getEnergyStored() < cost && cost > 0) {
      setLitProperty(false);
      return;
    }
    setLitProperty(true);
    if (this.world.isRemote) {
      return;
    }
    //get and update target
    BlockPos targetPos = getShapeTarget();
    shapeIndex++;
    //does it exist
    if (targetPos != null && HarvestUtil.tryHarvestSingle(this.world, targetPos)) {
      //energy is per action
      energy.extractEnergy(cost, false);
    }
  }

  private BlockPos getShapeTarget() {
    List<BlockPos> shape = this.getShape();
    if (shape.size() == 0) {
      return null;
    }
    if (this.shapeIndex < 0 || this.shapeIndex >= shape.size()) {
      this.shapeIndex = 0;
    }
    return shape.get(shapeIndex);
  }

  private int heightWithDirection() {
    Direction blockFacing = this.getBlockState().get(BlockStateProperties.FACING);
    int diff = directionIsUp ? 1 : -1;
    if (blockFacing.getAxis().isVertical()) {
      diff = (blockFacing == Direction.UP) ? 1 : -1;
    }
    return diff * height;
  }

  //for harvest
  public List<BlockPos> getShape() {
    BlockPos center = getFacingShapeCenter(radius);
    List<BlockPos> shape = UtilShape.cubeSquareBase(center, radius, 0);
    int heightWithDirection = heightWithDirection();
    if (heightWithDirection != 0) {
      shape = UtilShape.repeatShapeByHeight(shape, heightWithDirection);
    }
    return shape;
  }

  //for render
  public List<BlockPos> getShapeHollow() {
    BlockPos center = getFacingShapeCenter(radius);
    List<BlockPos> shape = UtilShape.squareHorizontalHollow(center, radius);
    int heightWithDirection = heightWithDirection();
    if (heightWithDirection != 0) {
      shape = UtilShape.repeatShapeByHeight(shape, heightWithDirection);
    }
    return shape;
  }

  @Override
  public AxisAlignedBB getRenderBoundingBox() {
    return TileEntity.INFINITE_EXTENT_AABB;
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        return this.needsRedstone;
      case RENDER:
        return render;
      case SIZE:
        return radius;
      case HEIGHT:
        return height;
      case DIRECTION:
        return directionIsUp ? 1 : 0;
    }
    return 0;
  }

  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        this.needsRedstone = value % 2;
      break;
      case RENDER:
        this.render = value % 2;
      break;
      case SIZE:
        radius = Math.min(value, MAX_SIZE);
      break;
      case DIRECTION:
        this.directionIsUp = value == 1;
      break;
      case HEIGHT:
        height = Math.min(value, MAX_HEIGHT);
      break;
    }
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityEnergy.ENERGY && POWERCONF.get() > 0) {
      return energyCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void invalidateCaps() {
    energyCap.invalidate();
    super.invalidateCaps();
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    radius = tag.getInt("radius");
    height = tag.getInt("height");
    directionIsUp = tag.getBoolean("directionIsUp");
    shapeIndex = tag.getInt("shapeIndex");
    energy.deserializeNBT(tag.getCompound(NBTENERGY));
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.putInt("radius", radius);
    tag.putInt("shapeIndex", shapeIndex);
    tag.putInt("height", height);
    tag.putBoolean("directionIsUp", directionIsUp);
    tag.put(NBTENERGY, energy.serializeNBT());
    return super.write(tag);
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerHarvester(i, world, pos, playerInventory, playerEntity);
  }
}
