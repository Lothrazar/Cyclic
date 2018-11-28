package com.lothrazar.cyclicmagic.compat.fastbench;

import com.lothrazar.cyclicmagic.block.workbench.TileEntityWorkbench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.world.World;

public class ClientContainerFastWorkbench extends ContainerFastWorkbench {

  public ClientContainerFastWorkbench(EntityPlayer player, World world, TileEntityWorkbench te) {
    super(player, world, te);
  }

  @Override
  protected void slotChangedCraftingGrid(World world, EntityPlayer player, InventoryCrafting inv, InventoryCraftResult result) {}
}
