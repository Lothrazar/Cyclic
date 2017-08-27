package com.lothrazar.cyclicmagic.component.clock;
import com.lothrazar.cyclicmagic.component.clock.TileEntityClock.Fields;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import com.lothrazar.cyclicmagic.gui.button.ButtonIncrementField;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiClock extends GuiBaseContainer {
  boolean debugLabels = false;
  int w = 20, h = 20;
  int xCol1 = 10;
  int xCol2 = xCol1 + w;
  int xColText = xCol2 + 40;
  int xCol3 = xColText + 50;
  int xCol4 = xCol3 + w;
  int yRow1 = 16;
  int yRow2 = yRow1 + h + Const.PAD;
  int yRow3 = yRow2 + h + Const.PAD;
  public GuiClock(InventoryPlayer inventoryPlayer, TileEntityClock tileEntity) {
    super(new ContainerClock(inventoryPlayer, tileEntity), tileEntity);
  }
  @Override
  public void initGui() {
    super.initGui();
    addButton(xCol1, yRow1, Fields.TON.ordinal(), -5, "duration");
    addButton(xCol2, yRow1, Fields.TON.ordinal(), -1, "duration");
    addButton(xCol3, yRow1, Fields.TON.ordinal(), 1, "duration");
    addButton(xCol4, yRow1, Fields.TON.ordinal(), 5, "duration");
    addButton(xCol1, yRow2, Fields.TOFF.ordinal(), -5, "delay");
    addButton(xCol2, yRow2, Fields.TOFF.ordinal(), -1, "delay");
    addButton(xCol3, yRow2, Fields.TOFF.ordinal(), 1, "delay");
    addButton(xCol4, yRow2, Fields.TOFF.ordinal(), 5, "delay");
    addButton(xCol2, yRow3, Fields.POWER.ordinal(), -1, "power");
    addButton(xCol3, yRow3, Fields.POWER.ordinal(), 1, "power");
  }
  private int btnId = 0;
  private void addButton(int x, int y, int field, int value, String tooltip) {
    ButtonIncrementField btn = new ButtonIncrementField(btnId++,
        this.guiLeft + x,
        this.guiTop + y, this.tile.getPos(), field, value,
        20, 20);
    btn.setTooltip(tooltip);
    this.buttonList.add(btn);
  }
  //  @Override
  //  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
  //    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
  // 
  //  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    int rowOffset = 6;
    this.drawString("" + this.tile.getField(Fields.TON.ordinal()), xColText, yRow1+rowOffset);
    this.drawString("" + this.tile.getField(Fields.TOFF.ordinal()), xColText, yRow2+rowOffset);
    this.drawString("" + this.tile.getField(Fields.POWER.ordinal()), xColText, yRow3+rowOffset);
    this.drawString("" + this.tile.getField(Fields.TIMER.ordinal()), xColText+44, yRow3+rowOffset);
  }
}
