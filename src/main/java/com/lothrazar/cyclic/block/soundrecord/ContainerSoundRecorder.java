package com.lothrazar.cyclic.block.soundrecord;

import com.lothrazar.cyclic.base.ContainerBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerSoundRecorder extends ContainerBase {

  TileSoundRecorder tile;

  public ContainerSoundRecorder(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
    super(ContainerScreenRegistry.SOUND_RECORDER, windowId);
    tile = (TileSoundRecorder) world.getTileEntity(pos);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    addSlot(new SlotItemHandler(tile.inputSlots, 0, 55, 35));
    //    addSlot(new SlotItemHandler(tile.outputSlots, 0, 109, 35));
    this.endInv = 1;
    layoutPlayerInventorySlots(52, 168);
    this.trackAllIntFields(tile, TileSoundRecorder.Fields.values().length);
    //    trackEnergy(tile);
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    return isWithinUsableDistance(IWorldPosCallable.of(tile.getWorld(), tile.getPos()), playerEntity, BlockRegistry.SOUND_RECORDER.get());
  }
}
