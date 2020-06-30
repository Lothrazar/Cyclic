package com.lothrazar.cyclic.block.detector;

import com.lothrazar.cyclic.base.ContainerBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.wrapper.InvWrapper;

public class ContainerDetector extends ContainerBase {

  protected TileDetector tile;

  public ContainerDetector(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
    super(BlockRegistry.ContainerScreens.detector_entity, windowId);
    tile = (TileDetector) world.getTileEntity(pos);
    this.playerEntity = player;
    this.playerInventory = new InvWrapper(playerInventory);
    layoutPlayerInventorySlots(8, 84);
    this.trackIntField(tile, TileDetector.Fields.ENTITYTYPE.ordinal());
    this.trackIntField(tile, TileDetector.Fields.GREATERTHAN.ordinal());
    this.trackIntField(tile, TileDetector.Fields.LIMIT.ordinal());
    this.trackIntField(tile, TileDetector.Fields.RANGEX.ordinal());
    this.trackIntField(tile, TileDetector.Fields.RANGEY.ordinal());
    this.trackIntField(tile, TileDetector.Fields.RANGEZ.ordinal());
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    return isWithinUsableDistance(IWorldPosCallable.of(tile.getWorld(), tile.getPos()), playerEntity, BlockRegistry.detector_entity);
  }
}