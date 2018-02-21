package com.lothrazar.cyclicmagic.component.dropper;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.gui.GuiSliderInteger;
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
    int id = 1;
    int width = 90;
    int h = 10;
    int x = this.guiLeft + 6;
    int y = this.guiTop + 28;
    GuiSliderInteger sliderDelay = new GuiSliderInteger(tile, id, x, y, width, h, 0, 64,
        TileEntityDropperExact.Fields.DELAY.ordinal(), true);
    sliderDelay.setTooltip("dropper.delay");
    this.addButton(sliderDelay);
    id++;
    y += 18;
    //offset
    GuiSliderInteger sliderOffset = new GuiSliderInteger(tile, id, x, y, width, h, 0, 16,
        TileEntityDropperExact.Fields.OFFSET.ordinal(), true);
    sliderOffset.setTooltip("dropper.offset");
    this.addButton(sliderOffset);
    id++;
    y += 18;
    //stack size
    GuiSliderInteger sliderCount = new GuiSliderInteger(tile, id, x, y, width, h, 1, 64,
        TileEntityDropperExact.Fields.DROPCOUNT.ordinal(), true);
    sliderCount.setTooltip("dropper.count");
    this.addButton(sliderCount);
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    int xPrefix, yPrefix;
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
