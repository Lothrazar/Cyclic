package com.lothrazar.cyclicmagic.compat.fastbench;

import com.lothrazar.cyclicmagic.block.workbench.InventoryWorkbench;
import com.lothrazar.cyclicmagic.block.workbench.TileEntityWorkbench;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import shadows.fastbench.gui.ContainerFastBench;
import shadows.fastbench.gui.SlotCraftingSucks;

public class ContainerFastWorkbench extends ContainerFastBench {

  static Block workbench = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(Const.MODID, "block_workbench"));
  final TileEntityWorkbench te;

  public ContainerFastWorkbench(EntityPlayer player, World world, TileEntityWorkbench te) {
    super(player, world, te.getPos());
    this.te = te;
    this.craftMatrix = new InventoryWorkbench(this, te);
    craftMatrix.openInventory(player);
    int x = 0;
    replaceSlot(x++, new SlotCraftingSucks(this, player, this.craftMatrix, this.craftResult, 0, 124, 35));
    for (int i = 0; i < 3; ++i) {
      for (int j = 0; j < 3; ++j) {
        replaceSlot(x++, new Slot(this.craftMatrix, j + i * 3, 30 + j * 18, 17 + i * 18));
      }
    }
    onCraftMatrixChanged(te);
  }

  void replaceSlot(int num, Slot slotIn) {
    slotIn.slotNumber = num;
    this.inventorySlots.set(num, slotIn);
  }

  @Override
  public boolean canInteractWith(EntityPlayer playerIn) {
    if (te.getWorld().getBlockState(te.getPos()).getBlock() != workbench) {
      return false;
    }
    else {
      return playerIn.getDistanceSq(te.getPos().getX() + 0.5D, te.getPos().getY() + 0.5D, te.getPos().getZ() + 0.5D) <= 64.0D;
    }
  }

  @Override
  public void onContainerClosed(EntityPlayer player) {
    ((ContainerPlayer) player.inventoryContainer).craftResult.clear(); //For whatever reason the workbench causes a desync that makes the last available recipe show in the 2x2 grid.
    super.onContainerClosed(player);
    craftMatrix.closeInventory(player);
  }
}
