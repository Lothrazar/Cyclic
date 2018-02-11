package com.lothrazar.cyclicmagic.component.miner;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiBlockMiner extends GuiBaseContainer {
  public GuiBlockMiner(InventoryPlayer inventoryPlayer, TileEntityBlockMiner tileEntity) {
    super(new ContainerBlockMiner(inventoryPlayer, tileEntity), tileEntity);
    this.fieldRedstoneBtn = TileEntityBlockMiner.Fields.REDSTONE.ordinal();
  }
}
