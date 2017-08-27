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
  int w = 18, h = 15;
  int xCol1 = (this.width + this.xSize) / 2 - 54;
  int xCol2 = xCol1 + w+Const.PAD/4;
  int xColText = xCol2 + 24;
  int xCol3 = xColText + 18;
  int xCol4 = xCol3 + w+Const.PAD/4;
  int yRow1 = Const.PAD*2+ Const.PAD/2;
  int yRow2 = yRow1 + h + Const.PAD/4;
  int yRow3 = yRow2 + h + Const.PAD/4;
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
        w, h);
    btn.displayString = ""+value;
    btn.setTooltip("tile.clock."+tooltip);
    this.buttonList.add(btn);
  }
 
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    int rowOffset = 4;
    this.drawString("" + this.tile.getField(Fields.TON.ordinal()), xColText, yRow1+rowOffset);
    this.drawString("" + this.tile.getField(Fields.TOFF.ordinal()), xColText, yRow2+rowOffset);
    this.drawString("" + this.tile.getField(Fields.POWER.ordinal()), xColText, yRow3+rowOffset);
    //FOR debug only
//    this.drawString("" + this.tile.getField(Fields.TIMER.ordinal()), xColText+44, yRow3+rowOffset);
  }
}
