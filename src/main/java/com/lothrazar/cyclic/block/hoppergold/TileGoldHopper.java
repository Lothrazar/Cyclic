package com.lothrazar.cyclic.block.hoppergold;

import com.lothrazar.cyclic.block.hopper.TileSimpleHopper;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.items.ItemStackHandler;

public class TileGoldHopper extends TileSimpleHopper implements MenuProvider {

  private static final String NBT_FILTER = "filter";

  final ItemStackHandler filter = new ItemStackHandler(1) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      if (stack.isEmpty()) {
        return true;
      }
      return stack.getItem() == ItemRegistry.FILTER_DATA.get();
    }

    @Override
    public int getSlotLimit(int slot) {
      return 1;
    }
  };

  public TileGoldHopper(BlockPos pos, BlockState state) {
    super(TileRegistry.HOPPER_GOLD.get(), pos, state);
    this.needsRedstone = 0;
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileGoldHopper e) {
    e.tick();
  }

  @Override
  public int getFlow() {
    return 64;
  }

  @Override
  protected ItemStackHandler getFilter() {
    return filter;
  }

  public ItemStackHandler getFilterSlot() {
    return filter;
  }

  @Override
  public Component getDisplayName() {
    return BlockRegistry.HOPPER_GOLD.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerGoldHopper(i, level, worldPosition, playerInventory, playerEntity);
  }

  @Override
  public void loadAdditional(ValueInput input) {
    super.loadAdditional(input);
    filter.deserialize(input.childOrEmpty(NBT_FILTER));
  }

  @Override
  public void saveAdditional(ValueOutput output) {
    super.saveAdditional(output);
    filter.serialize(output.child(NBT_FILTER));
  }
}
