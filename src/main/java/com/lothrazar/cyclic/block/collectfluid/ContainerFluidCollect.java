package com.lothrazar.cyclic.block.collectfluid;

import com.lothrazar.cyclic.gui.ContainerBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerFluidCollect extends ContainerBase {

  protected TileFluidCollect tile;

  public ContainerFluidCollect(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
    super(ContainerScreenRegistry.COLLECTOR_FLUID, windowId);
    tile = (TileFluidCollect) world.getBlockEntity(pos);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
      this.endInv = h.getSlots();
      addSlot(new SlotItemHandler(h, 0, 10, 51) {

        @Override
        public void setChanged() {
          tile.setChanged();
        }
      });
    });
    layoutPlayerInventorySlots(8, 84);
    this.trackEnergy(tile);
    this.trackAllIntFields(tile, TileFluidCollect.Fields.values().length);
  }

  @Override
  public boolean stillValid(Player playerIn) {
    return stillValid(ContainerLevelAccess.create(tile.getLevel(), tile.getBlockPos()), playerEntity, BlockRegistry.COLLECTOR_FLUID.get());
  }
}
