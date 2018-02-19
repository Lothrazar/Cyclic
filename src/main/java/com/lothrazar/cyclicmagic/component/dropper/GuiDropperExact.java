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
    //    Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + 136 - 1,
    //        this.guiTop + 35 - 1, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    int xPrefix = Const.PAD, yPrefix = 27;
    int rows = 3, cols = 3;
    xPrefix = (screenSize.width() / 2 - (Const.SQ * 3) / 2);//calculate exact center
    yPrefix = 20;
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + xPrefix - 1 + j * Const.SQ,
            this.guiTop + yPrefix - 1 + i * Const.SQ, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
      }
    }
  }
}
