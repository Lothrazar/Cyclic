package com.lothrazar.cyclic.block.breaker;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.data.DataTags;
import com.lothrazar.cyclic.item.datacard.BlockStateMatcher;
import com.lothrazar.cyclic.item.datacard.BlockstateCard;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.items.IItemHandler;

public class TileBreaker extends TileBlockEntityCyclic implements MenuProvider {

  static enum Fields {
    REDSTONE, TIMER;
  }

  static final int MAX = 64000;
  public static final int TIMER_FULL = 500;
  ItemStackHandler inventory = new ItemStackHandler() {

    @Override
    public int getSlotLimit(int slot) {
      return 1;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return stack.getItem() == ItemRegistry.BLOCKSTATE_DATA.get();
    }
  };

  public TileBreaker(BlockPos pos, BlockState state) {
    super(TileRegistry.BREAKER.get(), pos, state);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileBreaker e) {
    e.tick();
  }

  public void tick() {
    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      return;
    }
    setLitProperty(true);
    if (level.isClientSide) {
      return;
    }
    BlockPos target = worldPosition.relative(this.getCurrentFacing());
    if (this.isTargetValid(target)) {
      //old way would pass thru here and try to mine minecraft:water 
      this.level.destroyBlock(target, true);
    }
  }

  /**
   * Avoid mining source liquid blocks and unbreakable
   */
  private boolean isTargetValid(BlockPos targetPos) { //rename to isTargetValid
    BlockState targetState = level.getBlockState(targetPos);
    if (level.isEmptyBlock(targetPos)) {
      return false;
    }
    if (targetState.getDestroySpeed(level, targetPos) < 0) {
      return false;
    }
    //check the tag ignore list so modpack/datapack can filter this
    if (targetState.is(DataTags.BREAKER_IGNORED)) {
      ModCyclic.LOGGER.debug("breaker/ignored tag skips " + targetPos);
      return false;
    }
    if (targetState.getFluidState() != null && targetState.getFluidState().isEmpty() == false) {
      //am i a solid waterlogged state block? 
      if (targetState.hasProperty(BlockStateProperties.WATERLOGGED) == false) {
        //pure liquid. but this will make canHarvestBlock go true  
        return false;
      }
    }
    if (!this.isValidFromDatacard(targetState)) {
      return false;
    }
    // else filter is empty
    return true;
  }

  private boolean isValidFromDatacard(BlockState targetState) {
    ItemStack filter = inventory.getStackInSlot(0);
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

  @Override
  public Component getDisplayName() {
    return BlockRegistry.BREAKER.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerBreaker(i, level, worldPosition, playerInventory, playerEntity);
  }

  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    inventory.deserializeNBT(registries,tag.getCompound(NBTINV));
    super.loadAdditional(tag, registries);
  }

  @Override
  public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    tag.put(NBTINV, inventory.serializeNBT(registries));
    super.saveAdditional(tag, registries);
  }

  @Override
  public void setField(int field, int value) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        this.needsRedstone = value % 2;
      break;
      case TIMER:
        timer = value;
      break;
    }
  }

  @Override
  public int getField(int field) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        return this.needsRedstone;
      case TIMER:
        return timer;
    }
    return 0;
  }

  public int getEnergyMax() {
    return MAX;
  }

  @Override
  public IItemHandler getItemHandler(Direction side) {
    return inventory;
  }

}
