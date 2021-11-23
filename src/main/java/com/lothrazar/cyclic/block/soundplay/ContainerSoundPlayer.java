package com.lothrazar.cyclic.block.soundplay;

import com.lothrazar.cyclic.gui.ContainerBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerSoundPlayer extends ContainerBase {

  protected TileSoundPlayer tile;

  public ContainerSoundPlayer(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
    super(ContainerScreenRegistry.SOUND_PLAYER, windowId);
    tile = (TileSoundPlayer) world.getBlockEntity(pos);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    addSlot(new SlotItemHandler(tile.inventory, 0, 60, 60));
    this.endInv = 1;
    layoutPlayerInventorySlots(8, 84);
    trackEnergy(tile);
    //        this.trackAllIntFields(tile, TileSoundPlayer.Fields.values().length);
  }

  @Override
  public boolean stillValid(Player playerIn) {
    return stillValid(ContainerLevelAccess.create(tile.getLevel(), tile.getBlockPos()), playerEntity, BlockRegistry.SOUND_PLAYER.get());
  }
}
