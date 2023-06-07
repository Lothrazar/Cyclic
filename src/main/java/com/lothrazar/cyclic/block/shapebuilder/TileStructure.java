package com.lothrazar.cyclic.block.shapebuilder;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.capabilities.block.CustomEnergyStorage;
import com.lothrazar.cyclic.data.BlockPosDim;
import com.lothrazar.cyclic.data.RelativeShape;
import com.lothrazar.cyclic.item.datacard.LocationGpsCard;
import com.lothrazar.cyclic.item.datacard.ShapeCard;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.BlockUtil;
import com.lothrazar.cyclic.util.ShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileStructure extends TileBlockEntityCyclic implements MenuProvider {

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
        return Block.byItem(stack.getItem()) != null;
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

  public TileStructure(BlockPos pos, BlockState state) {
    super(TileRegistry.STRUCTURE.get(), pos, state);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileStructure e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileStructure e) {
    e.tick();
  }

  @Override
  public void load(CompoundTag tag) {
    energy.deserializeNBT(tag.getCompound(NBTENERGY));
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    int t = tag.getInt("buildType");
    buildType = BuildStructureType.values()[t];
    buildSize = tag.getInt("buildSize");
    height = tag.getInt("height");
    shapeIndex = tag.getInt("shapeIndex");
    super.load(tag);
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    tag.putInt("buildType", buildType.ordinal());
    tag.putInt("buildSize", buildSize);
    tag.putInt("height", height);
    tag.putInt("shapeIndex", shapeIndex);
    tag.put(NBTENERGY, energy.serializeNBT());
    tag.put(NBTINV, inventory.serializeNBT());
    super.saveAdditional(tag);
  }

  @Override
  public AABB getRenderBoundingBox() {
    return BlockEntity.INFINITE_EXTENT_AABB;
  }

  @Override
  public Component getDisplayName() {
    return BlockRegistry.STRUCTURE.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerStructure(i, level, worldPosition, playerInventory, playerEntity);
  }

  @Override
  public void invalidateCaps() {
    energyCap.invalidate();
    inventoryCap.invalidate();
    super.invalidateCaps();
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == ForgeCapabilities.ENERGY) {
      return energyCap.cast();
    }
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventoryCap.cast();
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

  //  @Override
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
    Block stuff = Block.byItem(stack.getItem());
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
      if (!level.isOutsideBuildHeight(nextPos)
          && level.isEmptyBlock(nextPos)) { // check if this spot is even valid
        BlockState placeState = stuff.defaultBlockState();
        if (level.isClientSide == false && BlockUtil.placeStateSafe(level, null, nextPos, placeState)) {
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
    return this.getBlockPos();
  }

  public BlockPos getTargetFacing() {
    //move center over that much, not including exact horizontal
    return this.getPosTarget().relative(this.getCurrentFacing(), this.buildSize + 1);
  }

  public List<BlockPos> getShape() {
    if (SLOT_SHAPE < inventory.getSlots()) {
      ItemStack shapeCard = inventory.getStackInSlot(SLOT_SHAPE);
      if (shapeCard.getItem() instanceof ShapeCard) {
        RelativeShape shape = RelativeShape.read(shapeCard);
        if (shape != null) {
          shape.setWorldCenter(level, getPosTarget());
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
        shape = ShapeUtil.circleHorizontal(this.getPosTarget(), this.getSize() * 2);
        shape = ShapeUtil.repeatShapeByHeight(shape, getHeight() - 1);
      break;
      case FACING:
        shape = ShapeUtil.line(this.getPosTarget(), this.getCurrentFacing(), this.getSize());
        shape = ShapeUtil.repeatShapeByHeight(shape, getHeight() - 1);
      break;
      case SQUARE:
        shape = ShapeUtil.squareHorizontalHollow(this.getPosTarget(), this.getSize());
        shape = ShapeUtil.repeatShapeByHeight(shape, getHeight() - 1);
      break;
      case SOLID:
        shape = ShapeUtil.squareHorizontalFull(this.getTargetFacing(), this.getSize());
        shape = ShapeUtil.repeatShapeByHeight(shape, getHeight() - 1);
      break;
      case SPHERE:
        shape = ShapeUtil.sphere(this.getPosTarget(), this.getSize());
      break;
      case DOME:
        shape = ShapeUtil.sphereDome(this.getPosTarget(), this.getSize());
      break;
      case CUP:
        shape = ShapeUtil.sphereCup(this.getPosTarget().above(this.getSize()), this.getSize());
      break;
      case DIAGONAL:
        shape = ShapeUtil.diagonal(this.getPosTarget(), this.getCurrentFacing(), this.getSize() * 2, true);
      break;
      case PYRAMID:
        shape = ShapeUtil.squarePyramid(this.getPosTarget(), this.getSize(), getHeight());
      break;
      case TUNNEL:
        shape = ShapeUtil.circleVertical(this.getPosTarget(), this.getSize(), getHeight(), this.getCurrentFacing());
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
