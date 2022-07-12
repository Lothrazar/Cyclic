package com.lothrazar.cyclic.block.soundrecord;

import com.lothrazar.cyclic.gui.ContainerBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.MenuTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerSoundRecorder extends ContainerBase {

  TileSoundRecorder tile;

  public ContainerSoundRecorder(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
    super(MenuTypeRegistry.SOUND_RECORDER.get(), windowId);
    tile = (TileSoundRecorder) world.getBlockEntity(pos);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    addSlot(new SlotItemHandler(tile.inputSlots, 0, 9, 209) {

      @Override
      public void setChanged() {
        tile.setChanged();
      }
    });
    this.endInv = 1;
    layoutPlayerInventorySlots(53, 175);
    this.trackAllIntFields(tile, TileSoundRecorder.Fields.values().length);
  }

  @Override
  public boolean stillValid(Player playerIn) {
    return stillValid(ContainerLevelAccess.create(tile.getLevel(), tile.getBlockPos()), playerEntity, BlockRegistry.SOUND_RECORDER.get());
  }
}
