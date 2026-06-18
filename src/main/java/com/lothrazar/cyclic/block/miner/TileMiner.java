package com.lothrazar.cyclic.block.miner;

import java.lang.ref.WeakReference;
import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.data.DataTags;
import com.lothrazar.cyclic.data.PreviewOutlineType;
import com.lothrazar.cyclic.item.datacard.BlockStateMatcher;
import com.lothrazar.cyclic.item.datacard.BlockstateCard;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.library.cap.EnergyStorageWrapper;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import com.lothrazar.library.util.ShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;

public class TileMiner extends TileBlockEntityCyclic implements MenuProvider {

  static enum Fields {
    REDSTONE, RENDER, SIZE, HEIGHT, DIRECTION;
  }

  public static ModConfigSpec.IntValue POWERCONF;
  private int shapeIndex = 0;
  static final int SLOT_TOOL = 0;
  static final int SLOT_FILTER = 1;
  static final int MAX_HEIGHT = 64;
  public static final int MAX_SIZE = 12; //radius 7 translates to 15x15 area (center block + 7 each side)
  private int height = MAX_HEIGHT / 2;
  private int radius = 5;
  static final int MAX = 64000;
  EnergyStorageWrapper energy = new EnergyStorageWrapper(MAX, MAX);
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
      if (slot == SLOT_FILTER && stack.getItem() != ItemRegistry.BLOCKSTATE_DATA.get()) {
        return false;
      }
      return true;
    }
  };
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

  @Override
  public Component getDisplayName() {
    return BlockRegistry.MINER.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerMiner(i, level, worldPosition, playerInventory, playerEntity);
  }

  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    radius = tag.getInt("size");
    height = tag.getInt("height");
    isCurrentlyMining = tag.getBoolean("isCurrentlyMining");
    shapeIndex = tag.getInt("shapeIndex");
    directionIsUp = tag.getBoolean("directionIsUp");
    if (tag.contains(NBTENERGY)) {
      energy.deserializeNBT(registries, tag.get(NBTENERGY));
    }
    inventory.deserializeNBT(registries,tag.getCompound(NBTINV));
    super.loadAdditional(tag,registries);
  }

  @Override
  public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    tag.putInt("size", radius);
    tag.putInt("height", height);
    tag.putBoolean("isCurrentlyMining", isCurrentlyMining);
    tag.putInt("shapeIndex", shapeIndex);
    tag.putBoolean("directionIsUp", directionIsUp);
    tag.put(NBTENERGY, energy.serializeNBT(registries));
    tag.put(NBTINV, inventory.serializeNBT(registries));
    super.saveAdditional(tag,registries);
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
      TileBlockEntityCyclic.tryEquipItem(inventory, fakePlayer, 0, InteractionHand.MAIN_HAND);
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
    FakePlayer fakePlayer = this.fakePlayer.get();
    //currentlyMining may have changed, and we are still turned on:
    if (isCurrentlyMining) {
      BlockState targetState = level.getBlockState(targetPos);
      float relative = targetState.getDestroyProgress(fakePlayer, level, targetPos);
      //state.getPlayerRelativeBlockHardness(player, worldIn, pos);UtilItemStack.getPlayerRelativeBlockHardness(targetState.getBlock(), targetState, fakePlayer.get(), world, targetPos);
      curBlockDamage += relative;
      //
      //if hardness is relative, jus fekin break it like air eh
      if (curBlockDamage >= 1.0f || relative == 0) {
        placePlayerBehindBlock(fakePlayer, targetPos);
        boolean harvested = fakePlayer.gameMode.destroyBlock(targetPos);
        if (!harvested) {
          //removedByPlayer
          harvested = level.getBlockState(targetPos).onDestroyedByPlayer(level, targetPos, fakePlayer, true, level.getFluidState(targetPos));

        }
        if (harvested) {
          // success 
          energy.extractEnergy(cost, false);
          resetProgress();
        }
        else {
          level.destroyBlockProgress(fakePlayer.getUUID().hashCode(), targetPos, (int) (curBlockDamage * 10.0F) - 1);
        }
      }
    }
    else { //is mining is false
      level.destroyBlockProgress(fakePlayer.getUUID().hashCode(), targetPos, (int) (curBlockDamage * 10.0F) - 1);
    }
    return false;
  }

  private void placePlayerBehindBlock(FakePlayer player, BlockPos targetPos) {
    // Some items (such as Mattock) use the position of the player relative to the block to determine mining behavior
    // Place the fake player just behind the block
    Direction facing = getBlockState().getValue(BlockStateProperties.FACING);
    player.moveTo(
        targetPos.getX() - facing.getStepX() + 0.5,
        targetPos.getY() - facing.getStepY() + 0.5 - player.getEyeHeight(),
        targetPos.getZ() - facing.getStepZ() + 0.5
    );
    player.lookAt(EntityAnchorArgument.Anchor.EYES, Vec3.atCenterOf(targetPos));
  }

  /***
   * Unbreakable blocks and fluid blocks are not valid. Otherwise checks if player:canHarvestBlock using its equipped item
   */
  private boolean isTargetValid() {
    if (targetPos == null || level.isEmptyBlock(targetPos) || fakePlayer == null) {
      return false; //dont mine air or liquid. 
    }
    //is this valid
    BlockState targetState = level.getBlockState(targetPos);

    if (targetState.getDestroySpeed(level, targetPos) < 0) {
      return false; //unbreakable 
    }
    //check the tag ignore list so modpack/datapack can filter this
    if (targetState.is(DataTags.MINER_IGNORED)) {
      return false;
    }
    //water logged is 
    if (targetState.getFluidState() != null && targetState.getFluidState().isEmpty() == false) {
      //am i PURE liquid? or just a WATERLOGGED block
      if (targetState.hasProperty(BlockStateProperties.WATERLOGGED) == false) {

        //pure liquid. but this will make canHarvestBlock go true , which is a lie actually so, no. dont get stuck here
        return false;
      }
    }
    if (!this.isValidFromDatacard(targetState)) {
      return false;
    }
    //its a solid non-air, non-fluid block (but might be like waterlogged stairs or something)
    boolean canHarvest = targetState.canHarvestBlock(level, targetPos, fakePlayer.get());
    if (!canHarvest) {
      ModCyclic.LOGGER.debug(targetPos + " Mining target is not valid  ");
    }
    return canHarvest;
  }

  private boolean isValidFromDatacard(BlockState targetState) {
    ItemStack filter = inventory.getStackInSlot(SLOT_FILTER);
    if (filter.isEmpty()) {
      return true; //ya go
    }
    for (BlockStateMatcher m : BlockstateCard.getSavedStates(Item.TooltipContext.of(level), filter)) {
      if (m.doesMatch(targetState)) {
        return true; // i am allowed to mine this
      }
    }
    return false; //filter is my allow list, and you aint in it so not allowed
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
      getLevel().destroyBlockProgress(fakePlayer.get().getUUID().hashCode(), targetPos, -1); // sendBlockBreakProgress
    }
  }

  private int heightWithDirection() {
    Direction blockFacing = this.getBlockState().getValue(BlockStateProperties.FACING);
    int diff = directionIsUp ? 1 : -1;
    if (blockFacing.getAxis().isVertical()) {
      diff = (blockFacing == Direction.UP) ? 1 : -1;
    }
    return diff * height;
  }

  public List<BlockPos> getShape() {
    BlockPos center = getFacingShapeCenter(radius);
    List<BlockPos> shape = ShapeUtil.squareHorizontalFull(center, radius);
    int heightWithDirection = heightWithDirection();
    if (heightWithDirection != 0) {
      shape = ShapeUtil.repeatShapeByHeight(shape, heightWithDirection);
    }
    return shape;
  }

  public List<BlockPos> getShapeHollow() {
    BlockPos center = getFacingShapeCenter(radius);
    List<BlockPos> shape = ShapeUtil.squareHorizontalHollow(center, radius);
    int heightWithDirection = heightWithDirection();
    if (heightWithDirection != 0) {
      shape = ShapeUtil.repeatShapeByHeight(shape, heightWithDirection);
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
        this.render = value % PreviewOutlineType.values().length;
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

  @Override
  public IItemHandler getItemHandler(Direction side) {
    return inventory;
  }


  @Override
  public IEnergyStorage getEnergyHandler(Direction side) {
    return energy;
  }

}
