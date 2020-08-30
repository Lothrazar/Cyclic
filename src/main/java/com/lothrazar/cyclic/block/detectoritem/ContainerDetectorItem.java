package com.lothrazar.cyclic.block.detectoritem;

import com.lothrazar.cyclic.base.ContainerBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.wrapper.InvWrapper;

public class ContainerDetectorItem extends ContainerBase {

  protected TileDetectorItem tile;

  public ContainerDetectorItem(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
    super(ContainerScreenRegistry.detector_item, windowId);
    tile = (TileDetectorItem) world.getTileEntity(pos);
    this.playerEntity = player;
    this.playerInventory = new InvWrapper(playerInventory);
    layoutPlayerInventorySlots(8, 84);
    //    this.trackIntField(tile, TileDetectorItem.Fields.ENTITYTYPE.ordinal());
    this.trackIntField(tile, TileDetectorItem.Fields.GREATERTHAN.ordinal());
    this.trackIntField(tile, TileDetectorItem.Fields.LIMIT.ordinal());
    this.trackIntField(tile, TileDetectorItem.Fields.RANGEX.ordinal());
    this.trackIntField(tile, TileDetectorItem.Fields.RANGEY.ordinal());
    this.trackIntField(tile, TileDetectorItem.Fields.RANGEZ.ordinal());
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    return isWithinUsableDistance(IWorldPosCallable.of(tile.getWorld(), tile.getPos()), playerEntity, BlockRegistry.detector_item);
  }
}