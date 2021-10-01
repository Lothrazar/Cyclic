package com.lothrazar.cyclic.block.soundplay;

import com.lothrazar.cyclic.base.ContainerBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerSoundPlayer extends ContainerBase {

  protected TileSoundPlayer tile;

  public ContainerSoundPlayer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
    super(ContainerScreenRegistry.SOUND_PLAYER, windowId);
    tile = (TileSoundPlayer) world.getTileEntity(pos);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    addSlot(new SlotItemHandler(tile.inventory, 0, 60, 60));
    this.endInv = 1;
    layoutPlayerInventorySlots(8, 84);
    trackEnergy(tile);
    //        this.trackAllIntFields(tile, TileSoundPlayer.Fields.values().length);
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    return isWithinUsableDistance(IWorldPosCallable.of(tile.getWorld(), tile.getPos()), playerEntity, BlockRegistry.SOUND_PLAYER.get());
  }
}
