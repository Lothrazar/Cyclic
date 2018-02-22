package com.lothrazar.cyclicmagic.component.pump.fluid;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiFluidPump extends GuiBaseContainer {
  public GuiFluidPump(InventoryPlayer inventoryPlayer, TileEntityFluidPump tileEntity) {
    super(new ContainerFluidPump(inventoryPlayer, tileEntity), tileEntity);
    this.fieldRedstoneBtn = TileEntityFluidPump.Fields.REDSTONE.ordinal();
  }
}
