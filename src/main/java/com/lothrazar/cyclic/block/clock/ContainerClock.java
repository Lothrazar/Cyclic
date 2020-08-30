package com.lothrazar.cyclic.block.clock;

import com.lothrazar.cyclic.base.ContainerBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.wrapper.InvWrapper;

public class ContainerClock extends ContainerBase {

  protected TileRedstoneClock tile;

  public ContainerClock(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
    super(ContainerScreenRegistry.clock, windowId);
    tile = (TileRedstoneClock) world.getTileEntity(pos);
    this.playerEntity = player;
    this.playerInventory = new InvWrapper(playerInventory);
    layoutPlayerInventorySlots(8, 84);
    this.trackIntField(tile, TileRedstoneClock.Fields.TIMER.ordinal());
    this.trackIntField(tile, TileRedstoneClock.Fields.DELAY.ordinal());
    this.trackIntField(tile, TileRedstoneClock.Fields.DURATION.ordinal());
    this.trackIntField(tile, TileRedstoneClock.Fields.POWER.ordinal());
    //    this.trackIntField(tile, TileRedstoneClock.Fields.REDSTONE.ordinal());
    //    this.trackIntField(tile, TileRedstoneClock.Fields.N.ordinal());
    //    this.trackIntField(tile, TileRedstoneClock.Fields.E.ordinal());
    //    this.trackIntField(tile, TileRedstoneClock.Fields.S.ordinal());
    //    this.trackIntField(tile, TileRedstoneClock.Fields.W.ordinal());
    //    this.trackIntField(tile, TileRedstoneClock.Fields.U.ordinal());
    //    this.trackIntField(tile, TileRedstoneClock.Fields.D.ordinal());
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    return isWithinUsableDistance(IWorldPosCallable.of(tile.getWorld(), tile.getPos()), playerEntity, BlockRegistry.clock);
  }
}