package com.lothrazar.cyclic.block.screen;

import com.lothrazar.cyclic.base.ContainerBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ContainerScreentext extends ContainerBase {

  TileScreentext tile;

  public ContainerScreentext(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
    super(ContainerScreenRegistry.SCREEN, windowId);
    tile = (TileScreentext) world.getTileEntity(pos);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    this.trackAllIntFields(tile, TileScreentext.Fields.values().length);
    trackEnergy(tile);
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    return isWithinUsableDistance(IWorldPosCallable.of(tile.getWorld(), tile.getPos()), playerEntity, BlockRegistry.screen);
  }
}
