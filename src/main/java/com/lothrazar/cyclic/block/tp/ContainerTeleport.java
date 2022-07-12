package com.lothrazar.cyclic.block.tp;

import com.lothrazar.cyclic.gui.ContainerBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.MenuTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerTeleport extends ContainerBase {

  protected TileTeleport tile;

  public ContainerTeleport(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
    super(MenuTypeRegistry.TELEPORT.get(), windowId);
    tile = (TileTeleport) world.getBlockEntity(pos);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    this.endInv = tile.gpsSlots.getSlots();
    addSlot(new SlotItemHandler(tile.gpsSlots, 0, 80, 36) {

      @Override
      public void setChanged() {
        tile.setChanged();
      }

      @Override
      public int getMaxStackSize() {
        return 1;
      }
    });
    layoutPlayerInventorySlots(8, 84);
    this.trackAllIntFields(tile, TileTeleport.Fields.values().length);
  }

  @Override
  public boolean stillValid(Player playerIn) {
    return stillValid(ContainerLevelAccess.create(tile.getLevel(), tile.getBlockPos()), playerEntity, BlockRegistry.TELEPORT.get());
  }
}
