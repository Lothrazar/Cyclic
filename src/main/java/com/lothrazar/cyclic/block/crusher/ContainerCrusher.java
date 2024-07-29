package com.lothrazar.cyclic.block.crusher;

import com.lothrazar.cyclic.gui.ContainerBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.MenuTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerCrusher extends ContainerBase {

  TileCrusher tile;

  public ContainerCrusher(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
    super(MenuTypeRegistry.CRUSHER.get(), windowId);
    tile = (TileCrusher) world.getBlockEntity(pos);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    addSlot(new SlotItemHandler(tile.inputSlots, 0, 53, 35) {

      @Override
      public void setChanged() {
        tile.setChanged();
      }
    });
    addSlot(new SlotItemHandler(tile.outputSlots, 0, 105 + 4, 21 + 4) {

      // do not allow player place stuff in output slot
      @Override
      public boolean mayPlace(ItemStack stack) {
        return false;
      }

      @Override
      public void setChanged() {
        tile.setChanged();
      }
    });
    addSlot(new SlotItemHandler(tile.outputSlots, 1, 109, 55) {

      // do not allow player place stuff in output-bonus slot
      @Override
      public boolean mayPlace(ItemStack stack) {
        return false;
      }

      @Override
      public void setChanged() {
        tile.setChanged();
      }
    });
    this.endInv = 3;
    layoutPlayerInventorySlots(8, 84);
    this.trackAllIntFields(tile, TileCrusher.Fields.values().length);
    trackEnergy(tile);
  }

  @Override
  public boolean stillValid(Player playerIn) {
    return stillValid(ContainerLevelAccess.create(tile.getLevel(), tile.getBlockPos()), playerEntity, BlockRegistry.CRUSHER.get());
  }
}
