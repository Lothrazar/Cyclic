package com.lothrazar.cyclic.block.generatorfuel;

import com.lothrazar.cyclic.base.ContainerBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerGeneratorFuel extends ContainerBase {

  TileGeneratorFuel tile;

  public ContainerGeneratorFuel(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
    super(ContainerScreenRegistry.generator_fuel, windowId);
    tile = (TileGeneratorFuel) world.getTileEntity(pos);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    addSlot(new SlotItemHandler(tile.inputSlots, 0, 75, 35));
    //    addSlot(new SlotItemHandler(tile.outputSlots, 0, 109, 35));
    this.endInv = tile.inputSlots.getSlots();
    layoutPlayerInventorySlots(8, 84);
    this.trackAllIntFields(tile, TileGeneratorFuel.Fields.values().length);
    trackEnergy(tile);
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    return isWithinUsableDistance(IWorldPosCallable.of(tile.getWorld(), tile.getPos()), playerEntity, BlockRegistry.GENERATOR_FUEL.get());
  }
}
