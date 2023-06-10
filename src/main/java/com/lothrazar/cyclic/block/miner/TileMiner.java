package com.lothrazar.cyclic.block.miner;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.capabilities.block.CustomEnergyStorage;
import com.lothrazar.cyclic.item.datacard.BlockStateMatcher;
import com.lothrazar.cyclic.item.datacard.BlockstateCard;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.ShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileMiner extends TileBlockEntityCyclic implements MenuProvider {

  static enum Fields {
    REDSTONE, RENDER, SIZE, HEIGHT, DIRECTION;
  }

  public static IntValue POWERCONF;
  private int shapeIndex = 0;
  static final int SLOT_TOOL = 0;
  static final int SLOT_FILTER = 1;
  static final int MAX_HEIGHT = 64;
  public static final int MAX_SIZE = 12; //radius 7 translates to 15x15 area (center block + 7 each side)
  private int height = MAX_HEIGHT / 2;
  private int radius = 5;
  static final int MAX = 64000;
  CustomEnergyStorage energy = new CustomEnergyStorage(MAX, MAX);
  ItemStackHandler inventory = new ItemStackHandler(2) {

    @Override
    public ItemStack getStackInSlot(int slot) {
      if (slot < 0 || slot >= this.stacks.size()) {
        return ItemStack.EMPTY; // failsafe for slot not in range legacy worlds
      }
      return super.getStackInSlot(slot); //this.stacks.get(slot);
    }

    @Override
    public int getSlotLimit(int slot) {
      return 1;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      if (slot == SLOT_FILTER && stack.getItem() != ItemRegistry.STATECARD.get()) {
        return false;
      }
      return true;
    }
  };
  private LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);
  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);
  private WeakReference<FakePlayer> fakePlayer;
  private boolean isCurrentlyMining;
  private float curBlockDamage;
  private BlockPos targetPos = BlockPos.ZERO;
  private boolean directionIsUp = false;

  public TileMiner(BlockPos pos, BlockState state) {
    super(TileRegistry.MINER.get(), pos, state);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileMiner e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileMiner e) {
    e.tick();
  }

  @Override
  public AABB getRenderBoundingBox() {
    return BlockEntity.INFINITE_EXTENT_AABB;
  }

  @Override
  public Component getDisplayName() {
    return BlockRegistry.MINER.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerMiner(i, level, worldPosition, playerInventory, playerEntity);
  }

  @Override
  public void invalidateCaps() {
    energyCap.invalidate();
    inventoryCap.invalidate();
    super.invalidateCaps();
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == ForgeCapabilities.ENERGY && POWERCONF.get() > 0) {
      return energyCap.cast();
    }
    if (cap == ForgeCapabilities.ITEM_HANDLER) {
      return inventoryCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void load(CompoundTag tag) {
    radius = tag.getInt("size");
    height = tag.getInt("height");
    isCurrentlyMining = tag.getBoolean("isCurrentlyMining");
    directionIsUp = tag.getBoolean("directionIsUp");
    energy.deserializeNBT(tag.getCompound(NBTENERGY));
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    super.load(tag);
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    tag.putInt("size", radius);
    tag.putInt("height", height);
    tag.putBoolean("isCurrentlyMining", isCurrentlyMining);
    tag.putBoolean("directionIsUp", directionIsUp);
    tag.put(NBTENERGY, energy.serializeNBT());
    tag.put(NBTINV, inventory.serializeNBT());
    super.saveAdditional(tag);
  }

  public void tick() {
    this.syncEnergy();
    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      return;
    }
    if ((level instanceof ServerLevel) && fakePlayer == null) {
      fakePlayer = setupBeforeTrigger((ServerLevel) level, "miner");
    }
    try {
      TileBlockEntityCyclic.tryEquipItem(inventoryCap, fakePlayer, 0, InteractionHand.MAIN_HAND);
      List<BlockPos> shape = getShape();
      if (shape.size() == 0) {
        return;
      }
      setLitProperty(true);
      updateMiningProgress(shape);
    }
    catch (Exception e) {
      ModCyclic.LOGGER.error("Miner action error", e);
    }
  }

  private boolean updateMiningProgress(List<BlockPos> shape) {
    if (fakePlayer == null) {
      return false;
    }
    //    if (isCurrentlyMining == false) { //we can mine but are not currently. so try moving to a new position
    ////  This is making me skip block zero and other stuff randomly? i think this is bad
    //      updateTargetPos(shape);
    //    }
    if (isTargetValid()) { //if target is valid, allow mining (no air, no blacklist, etc)
      isCurrentlyMining = true;
      //then keep current target
    }
    else { // no valid target, back out 
      updateTargetPos(shape);
      resetProgress();
    }
    Integer cost = POWERCONF.get();
    if (energy.getEnergyStored() < cost && cost > 0) {
      return false;
    }
    //currentlyMining may have changed, and we are still turned on:
    if (isCurrentlyMining) {
      BlockState targetState = level.getBlockState(targetPos);
      float relative = targetState.getDestroyProgress(fakePlayer.get(), level, targetPos);
      //state.getPlayerRelativeBlockHardness(player, worldIn, pos);UtilItemStack.getPlayerRelativeBlockHardness(targetState.getBlock(), targetState, fakePlayer.get(), world, targetPos);
      curBlockDamage += relative;
      //
      //if hardness is relative, jus fekin break it like air eh
      if (curBlockDamage >= 1.0f || relative == 0) {
        boolean harvested = fakePlayer.get().gameMode.destroyBlock(targetPos);
        if (!harvested) {
          //            world.destroyBlock(targetPos, true, fakePlayer.get());
          //removedByPlayer
          harvested = level.getBlockState(targetPos).onDestroyedByPlayer(level, worldPosition, fakePlayer.get(), true, level.getFluidState(worldPosition));
          //   ModCyclic.LOGGER.info("Miner:removedByPlayer hacky workaround " + targetPos);
        }
        if (harvested) {
          // success 
          energy.extractEnergy(cost, false);
          resetProgress();
        }
        else {
          level.destroyBlockProgress(fakePlayer.get().getUUID().hashCode(), targetPos, (int) (curBlockDamage * 10.0F) - 1);
        }
      }
    }
    else { //is mining is false 
      level.destroyBlockProgress(fakePlayer.get().getUUID().hashCode(), targetPos, (int) (curBlockDamage * 10.0F) - 1);
    }
    return false;
  }

  private boolean isValidTarget(BlockState targetState) {
    ItemStack filter = inventory.getStackInSlot(SLOT_FILTER);
    if (filter.isEmpty()) {
      return true; //ya go
    }
    for (BlockStateMatcher m : BlockstateCard.getSavedStates(level, filter)) {
      BlockState st = m.getState();
      if (targetState.getBlock() == st.getBlock()) {
        if (m.isExactProperties() == false) {
          // the blocks DO match, isExact is flagged as no, so we are good
          return true;
        }
        //tag DOES want to match Exactly on Properties
        return this.propertiesMatch(targetState, st);
      }
    }
    return false;
  }

  private boolean propertiesMatch(BlockState targetState, BlockState st) {
    try {
      for (Property<?> p : st.getProperties()) {
        if (!st.getValue(p).equals(targetState.getValue(p))) {
          return false;
        }
      }
    }
    catch (Exception e) {
      return false;
    }
    //none had a mismatch
    return true;
  }

  /***
   * Unbreakable blocks and fluid blocks are not valid. Otherwise checks if player:canHarvestBlock using its equipped item
   */
  private boolean isTargetValid() {
    if (targetPos == null || level.isEmptyBlock(targetPos) || fakePlayer == null) {
      return false; //dont mine air or liquid. 
    }
    //is this valid
    BlockState blockSt = level.getBlockState(targetPos);
    if (blockSt.destroySpeed < 0) {
      return false; //unbreakable 
    }
    //water logged is 
    if (blockSt.getFluidState() != null && blockSt.getFluidState().isEmpty() == false) {
      //am i PURE liquid? or just a WATERLOGGED block
      if (blockSt.hasProperty(BlockStateProperties.WATERLOGGED) == false) {
        //    ModCyclic.LOGGER.info(targetPos + " Mining FLUID is not valid  " + blockSt);
        //pure liquid. but this will make canHarvestBlock go true , which is a lie actually so, no. dont get stuck here
        return false;
      }
    }
    if (!this.isValidTarget(blockSt)) {
      return false;
    }
    //its a solid non-air, non-fluid block (but might be like waterlogged stairs or something)
    boolean canHarvest = blockSt.canHarvestBlock(level, targetPos, fakePlayer.get());
    if (!canHarvest) {
      //      ModCyclic.LOGGER.info(targetPos + " Mining target is not valid  " + blockSt);
    }
    return canHarvest;
  }

  private void updateTargetPos(List<BlockPos> shape) {
    shapeIndex++;
    if (this.shapeIndex < 0 || this.shapeIndex >= shape.size()) {
      this.shapeIndex = 0;
    }
    targetPos = shape.get(shapeIndex);
  }

  private void resetProgress() {
    isCurrentlyMining = false;
    curBlockDamage = 0;
    if (fakePlayer != null && targetPos != null) {
      //BlockPos targetPos = pos.offset(state.getValue(BlockMiner.PROPERTYFACING));
      getLevel().destroyBlockProgress(fakePlayer.get().getUUID().hashCode(), targetPos, -1);
    }
  }

  public List<BlockPos> getShape() {
    List<BlockPos> shape = ShapeUtil.squareHorizontalFull(this.getCurrentFacingPos(radius + 1), radius);
    if (height > 0) {
      int diff = directionIsUp ? 1 : -1;
      shape = ShapeUtil.repeatShapeByHeight(shape, diff * height);
    }
    return shape;
  }

  public List<BlockPos> getShapeHollow() {
    List<BlockPos> shape = new ArrayList<BlockPos>();
    shape = ShapeUtil.squareHorizontalHollow(this.getCurrentFacingPos(radius + 1), radius);
    if (height > 0) {
      int diff = directionIsUp ? 1 : -1;
      shape = ShapeUtil.repeatShapeByHeight(shape, diff * height);
    }
    if (targetPos != null) {
      shape.add(targetPos);
    }
    return shape;
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        return this.needsRedstone;
      case RENDER:
        return render;
      case DIRECTION:
        return (directionIsUp) ? 1 : 0;
      case HEIGHT:
        return height;
      case SIZE:
        return radius;
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
      case DIRECTION:
        this.directionIsUp = value == 1;
      break;
      case HEIGHT:
        height = Math.min(value, MAX_HEIGHT);
      break;
      case SIZE:
        radius = Math.min(value, MAX_SIZE);
      break;
    }
  }
}
