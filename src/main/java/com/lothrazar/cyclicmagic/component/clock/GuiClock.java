package com.lothrazar.cyclicmagic.component.clock;
import java.util.HashMap;
import java.util.Map;
import com.lothrazar.cyclicmagic.component.builder.TileEntityStructureBuilder;
import com.lothrazar.cyclicmagic.component.clock.TileEntityClock.Fields;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer.ButtonTriggerWrapper.ButtonTriggerType;
import com.lothrazar.cyclicmagic.gui.button.ButtonTileEntityField;
import com.lothrazar.cyclicmagic.gui.button.ButtonToggleFacing;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiClock extends GuiBaseContainer {
  private Map<EnumFacing, ButtonToggleFacing> poweredButtons = new HashMap<EnumFacing, ButtonToggleFacing>();
  boolean debugLabels = false;
  private int btnId = 0;
  int w = 18, h = 15;
  int rowOffset = Const.PAD / 2;
  int colOffset = Const.PAD / 4;
  int xCol1 = (this.width + this.xSize) / 2 - 78;
  int xCol2 = xCol1 + w + colOffset;
  int xColText = xCol2 + 24;
  int xCol3 = xColText + 18;
  int xCol4 = xCol3 + w + colOffset;
  int yRow1 = Const.PAD * 2 + rowOffset;
  int yRow2 = yRow1 + h + colOffset;
  int yRow3 = yRow2 + h + colOffset;
  int xColFacing = xCol4 + w + Const.PAD;
  TileEntityClock tileClock;
  public GuiClock(InventoryPlayer inventoryPlayer, TileEntityClock tileEntity) {
    super(new ContainerClock(inventoryPlayer, tileEntity), tileEntity);
    tileClock = (TileEntityClock) this.tile;
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
    for (EnumFacing f : EnumFacing.values()) {
      addButtonFacing(f);
    }
  }
  private void addButtonFacing(EnumFacing side) {
    int x = 0, y = 0;
    int xCenter = 140, yCenter = Const.PAD * 4;
    int spacing = 14;
    switch (side) {
      case EAST:
        x = xCenter + spacing;
        y = yCenter;
      break;
      case NORTH:
        x = xCenter;
        y = yCenter - spacing;
      break;
      case SOUTH:
        x = xCenter;
        y = yCenter + spacing;
      break;
      case WEST:
        x = xCenter - spacing;
        y = yCenter;
      break;
      case UP:
        x = xCenter + spacing;
        y = Const.PAD * 8;
      break;
      case DOWN:
        x = xCenter - spacing;
        y = Const.PAD * 8;
      break;
    }
    ButtonToggleFacing btn = new ButtonToggleFacing(btnId++,
        this.guiLeft + x,
        this.guiTop + y, this.tile.getPos(), side, w, h);
    btn.setIsChecked(tileClock.getSideHasPower(side));
    btn.setTooltip("tile.clock.facing." + side.name().toLowerCase());
    this.buttonList.add(btn);
    poweredButtons.put(side, btn);
  }
  private void addButton(int x, int y, int field, int value, String tooltip) {
    ButtonTileEntityField btn = new ButtonTileEntityField(btnId++,
        this.guiLeft + x,
        this.guiTop + y, this.tile.getPos(), field, value,
        w, h);
    if (value > 0) {
      btn.displayString = "+" + value;
      if (field == Fields.POWER.ordinal()) {
        //TODO: setup/find magic numbers for redstone, 15 is max
        this.registerButtonDisableTrigger(btn, ButtonTriggerType.EQUAL, field, 15);
      }
    }
    else {
      btn.displayString = "" + value;
      int min = (field == Fields.POWER.ordinal()) ? 0 : 1;
      this.registerButtonDisableTrigger(btn, ButtonTriggerType.EQUAL, field, min);
    }
    btn.setTooltip("tile.clock." + tooltip);
    this.buttonList.add(btn);
  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    this.drawString("" + this.tile.getField(Fields.TON.ordinal()), xColText, yRow1 + rowOffset);
    this.drawString("" + this.tile.getField(Fields.TOFF.ordinal()), xColText, yRow2 + rowOffset);
    this.drawString("" + this.tile.getField(Fields.POWER.ordinal()), xColText, yRow3 + rowOffset);
  }
}
