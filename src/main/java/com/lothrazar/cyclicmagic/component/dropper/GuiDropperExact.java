package com.lothrazar.cyclicmagic.component.dropper;

import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiDropperExact extends GuiBaseContainer {
  public GuiDropperExact(InventoryPlayer inventoryPlayer, TileEntityDropperExact tileEntity) {
    super(new ContainerDropperExact(inventoryPlayer, tileEntity));
    tile = tileEntity;
   
    this.fieldRedstoneBtn = TileEntityDropperExact.Fields.REDSTONE.ordinal();
  }
  @Override
  public void initGui() {
    super.initGui();
    // buttons!  
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
 
    int xPrefix , yPrefix ;
    int rows = 3, cols = 3;
    xPrefix = ContainerDropperExact.SLOTX_START; 
    yPrefix = ContainerDropperExact.SLOTY;
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + xPrefix - 1 + j * Const.SQ,
            this.guiTop + yPrefix - 1 + i * Const.SQ, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
      }
    }
  }
}
