package com.lothrazar.cyclic.block.shapebuilder;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.data.BlockPosDim;
import com.lothrazar.cyclic.data.RelativeShape;
import com.lothrazar.cyclic.item.datacard.LocationGpsCard;
import com.lothrazar.cyclic.item.datacard.ShapeCard;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilPlaceBlocks;
import com.lothrazar.cyclic.util.UtilShape;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileStructure extends TileEntityBase implements INamedContainerProvider, ITickableTileEntity {

  public static IntValue POWERCONF;
  static final int SLOT_BUILD = 0;
  protected static final int SLOT_SHAPE = 1;
  protected static final int SLOT_GPS = 2;
  public static final int MAXHEIGHT = 100;

  static enum Fields {
    TIMER, BUILDTYPE, SIZE, HEIGHT, REDSTONE, RENDER;
  }

  static final int MAX = 64000;
  CustomEnergyStorage energy = new CustomEnergyStorage(MAX, MAX);
  ItemStackHandler inventory = new ItemStackHandler(3) {

    @Override
    public ItemStack getStackInSlot(int slot) {
      if (slot < 0 || slot >= this.stacks.size()) {
        return ItemStack.EMPTY; // failsafe for slot not in range legacy worlds
      }
      return super.getStackInSlot(slot); //this.stacks.get(slot);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      if (slot == SLOT_BUILD) {
        return Block.getBlockFromItem(stack.getItem()) != null;
      }
      else if (slot == SLOT_SHAPE) {
        return stack.getItem() instanceof ShapeCard;
      }
      else { // if SLOT_GPS
        return stack.getItem() instanceof LocationGpsCard;
      }
    }
  };
  private LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);
  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);
  // START of build settings
  private BuildStructureType buildType = BuildStructureType.FACING;
  private int buildSize = 3;
  private int height = 2;
  //machine settings
  private int shapeIndex = 0;

  public TileStructure() {
    super(TileRegistry.STRUCTURE);
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    energy.deserializeNBT(tag.getCompound(NBTENERGY));
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    int t = tag.getInt("buildType");
    buildType = BuildStructureType.values()[t];
    buildSize = tag.getInt("buildSize");
    height = tag.getInt("height");
    shapeIndex = tag.getInt("shapeIndex");
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.putInt("buildType", buildType.ordinal());
    tag.putInt("buildSize", buildSize);
    tag.putInt("height", height);
    tag.putInt("shapeIndex", shapeIndex);
    tag.put(NBTENERGY, energy.serializeNBT());
    tag.put(NBTINV, inventory.serializeNBT());
    return super.write(tag);
  }

  @Override
  public AxisAlignedBB getRenderBoundingBox() {
    return TileEntity.INFINITE_EXTENT_AABB;
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerStructure(i, world, pos, playerInventory, playerEntity);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityEnergy.ENERGY) {
      return energyCap.cast();
    }
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventoryCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void invalidateCaps() {
    energyCap.invalidate();
    inventoryCap.invalidate();
    super.invalidateCaps();
  }

  @Override
  public void setField(int field, int value) {
    switch (Fields.values()[field]) {
      case TIMER:
        this.timer = value;
      break;
      case BUILDTYPE:
        if (value >= BuildStructureType.values().length) {
          value = 0;
        }
        this.buildType = BuildStructureType.values()[value];
      break;
      case SIZE:
        this.buildSize = value;
      break;
      case HEIGHT:
        if (value > MAXHEIGHT) {
          value = MAXHEIGHT;
        }
        this.height = Math.max(1, value);
      break;
      case REDSTONE:
        this.needsRedstone = value % 2;
      break;
      case RENDER:
        this.render = value % 2;
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
        return this.needsRedstone;
      case RENDER:
        return this.render;
    }
    return 0;
  }

  @Override
  public void tick() {
    this.syncEnergy();
    if (this.requiresRedstone() && !this.isPowered()) {
      return;
    }
    List<BlockPos> shape = this.getShape();
    if (shape.size() == 0) {
      return;
    }
    if (this.shapeIndex < 0 || this.shapeIndex >= shape.size()) {
      this.shapeIndex = 0;
    }
    BlockPos nextPos = shape.get(this.shapeIndex);
    //start at current position and validate
    //does my shape exist? if so copy to it
    ItemStack stack = inventory.getStackInSlot(SLOT_BUILD);
    //    if (stack.isEmpty()) {
    //      return;
    //    }
    Block stuff = Block.getBlockFromItem(stack.getItem());
    if (stuff == null) {
      return;
    }
    int cost = POWERCONF.get();
    for (int i = 0; i < spotsSkippablePerTrigger; i++) {
      if (energy.getEnergyStored() < cost && cost > 0) {
        break;
        //if repair is free dont break
      }
      //true means bounding box is null in the check. entit falling sand uses true
      //used to be exact air world.isAirBlock(nextPos)
      if (!World.isOutsideBuildHeight(nextPos)
          && world.isAirBlock(nextPos)) { // check if this spot is even valid
        BlockState placeState = stuff.getDefaultState();
        if (world.isRemote == false && UtilPlaceBlocks.placeStateSafe(world, null, nextPos, placeState)) {
          //build success
          this.incrementPosition(shape);
          stack.shrink(1);
          energy.extractEnergy(cost, false);
        }
        break;
        //ok , target position is valid, we can build only into air
      }
      else {
        //cant build here. move up one
        nextPos = shape.get(this.shapeIndex);
        this.incrementPosition(shape);
      } //but after inrementing once, we may not yet be valid so skip at most ten spots per tick
    }
  }

  private static final int spotsSkippablePerTrigger = 50;

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
    if (SLOT_GPS < inventory.getSlots()) {
      //before going to nextpos
      //do we have a center offset
      BlockPosDim loc = LocationGpsCard.getPosition(inventory.getStackInSlot(SLOT_GPS));
      if (loc != null && loc.getPos() != null) {
        return loc.getPos();
      }
    }
    return this.getPos();
  }

  public BlockPos getTargetFacing() {
    //move center over that much, not including exact horizontal
    return this.getPosTarget().offset(this.getCurrentFacing(), this.buildSize + 1);
  }

  public List<BlockPos> getShape() {
    if (SLOT_SHAPE < inventory.getSlots()) {
      ItemStack shapeCard = inventory.getStackInSlot(SLOT_SHAPE);
      if (shapeCard.getItem() instanceof ShapeCard) {
        RelativeShape shape = RelativeShape.read(shapeCard);
        if (shape != null) {
          shape.setWorldCenter(world, getPosTarget());
          if (shape.getShape() != null && shape.getShape().size() > 0) {
            return shape.getShape();
          }
        }
      }
    }
    List<BlockPos> shape = new ArrayList<BlockPos>();
    // ITEMSTACK / CARD storing what shape and settings to use
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
