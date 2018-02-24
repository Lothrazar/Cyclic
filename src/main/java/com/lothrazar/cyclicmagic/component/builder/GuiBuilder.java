/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic.component.builder;
import com.lothrazar.cyclicmagic.component.builder.TileEntityStructureBuilder.Fields;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.data.Const.ScreenSize;
import com.lothrazar.cyclicmagic.gui.GuiSliderInteger;
import com.lothrazar.cyclicmagic.gui.ProgressBar;
import com.lothrazar.cyclicmagic.gui.base.ContainerBaseMachine;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer.ButtonTriggerWrapper.ButtonTriggerType;
import com.lothrazar.cyclicmagic.gui.button.ButtonTileEntityField;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiPageButtonList.GuiResponder;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiBuilder extends GuiBaseContainer implements GuiResponder {
  private TileEntityStructureBuilder tile;
  private ButtonTileEntityField btnSizeUp;
  private ButtonTileEntityField btnSizeDown;
  private ButtonTileEntityField btnHeightUp;
  private ButtonTileEntityField btnHeightDown;
  private final static int yRowTextbox = 30;
  private int xControlsStart = 158;
  private final static int xControlsSpacing = 14;
  private int yOffset = 10 + Const.PAD;
  public GuiBuilder(InventoryPlayer inventoryPlayer, TileEntityStructureBuilder tileEntity) {
    super(new ContainerBuilder(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
    setScreenSize(ScreenSize.LARGE);
    this.fieldRedstoneBtn = TileEntityStructureBuilder.Fields.REDSTONE.ordinal();
    this.progressBar = new ProgressBar(this, 10, ContainerBuilder.SLOTY + 22, TileEntityStructureBuilder.Fields.TIMER.ordinal(), TileEntityStructureBuilder.TIMER_FULL);
    this.fieldPreviewBtn = TileEntityStructureBuilder.Fields.RENDERPARTICLES.ordinal();
    this.setFieldFuel(TileEntityStructureBuilder.Fields.FUEL.ordinal());
  }
  @Override
  public void initGui() {
    super.initGui();
    //first the main top left type button
    TileEntityStructureBuilder.Fields fld;
    int id = 1;
    float maxOffset = 16F;
    int width = 102;
    int h = 10;
    int x = this.guiLeft + 24;
    int y = this.guiTop + 15;
    GuiSliderInteger sliderX = new GuiSliderInteger(tile, id, x, y, width, h, -1 * maxOffset, maxOffset, Fields.OX.ordinal(), true);
    sliderX.setTooltip("X");
    this.addButton(sliderX);
    id++;
    y += h + 1;
    GuiSliderInteger sliderY = new GuiSliderInteger(tile, id, x, y, width, h, -1 * maxOffset, maxOffset, Fields.OY.ordinal(), true);
    sliderY.setTooltip("Y");
    this.addButton(sliderY);
    id++;
    y += h + 1;
    GuiSliderInteger sliderZ = new GuiSliderInteger(tile, id, x, y, width, h, -1 * maxOffset, maxOffset, Fields.OZ.ordinal(), true);
    sliderZ.setTooltip("Z");
    this.addButton(sliderZ);
    id++;
    x = this.guiLeft + Const.PAD + h;
    y = this.guiTop + yOffset + Const.PAD;
    //shape btns in loop
    ButtonTileEntityField btnShape;
    width = 18;
    h = width;
    x = this.guiLeft + Const.PAD / 2;
    y = this.guiTop + 50;
    fld = TileEntityStructureBuilder.Fields.BUILDTYPE;
    int numInRow = 0;
    for (TileEntityStructureBuilder.BuildType shape : TileEntityStructureBuilder.BuildType.values()) {
      numInRow++;
      if (numInRow == 7) {//only 6 per row fit on screen
        //so just reset x back to left side and bump up the y
        x = this.guiLeft + Const.PAD / 2;
        y += h + Const.PAD / 2;
      }
      btnShape = new ButtonTileEntityField(id++,
          x,
          y,
          tile.getPos(),
          fld.ordinal(),
          shape.ordinal(), width, h);
      String n = UtilChat.lang("buildertype." + shape.name().toLowerCase() + ".name");
      this.addButton(btnShape).setTooltip(n).displayString = shape.shortcode();
      btnShape.buttonMode = ButtonTileEntityField.ButtonMode.SET;
      x += width + 2;
      this.registerButtonDisableTrigger(btnShape, ButtonTriggerType.EQUAL, fld.ordinal(), shape.ordinal());
    }
    //////// all the control groups
    width = xControlsSpacing - 2;
    h = width;
    int yTopRow = this.guiTop + yRowTextbox;
    int yBottomRow = this.guiTop + yRowTextbox + yOffset + Const.PAD;
    fld = TileEntityStructureBuilder.Fields.SIZE;
    ////////// SIZE 
    x = this.guiLeft + xControlsStart;
    btnSizeUp = new ButtonTileEntityField(id++,
        x,
        yTopRow,
        tile.getPos(),
        fld.ordinal(),
        1, width, h);
    btnSizeUp.setTooltip("button." + fld.name().toLowerCase() + "." + "up");
    btnSizeUp.displayString = "+";
    this.addButton(btnSizeUp);
    this.registerButtonDisableTrigger(btnSizeUp, ButtonTriggerType.EQUAL, fld.ordinal(), TileEntityStructureBuilder.maxSize);
    btnSizeDown = new ButtonTileEntityField(id++,
        x,
        yBottomRow,
        tile.getPos(),
        fld.ordinal(),
        -1, width, h);
    btnSizeDown.setTooltip("button." + fld.name().toLowerCase() + "." + "down");
    btnSizeDown.displayString = "-";
    this.addButton(btnSizeDown);
    this.registerButtonDisableTrigger(btnSizeDown, ButtonTriggerType.EQUAL, fld.ordinal(), 1);
    //////////////HEIGHT BUTTONS
    fld = TileEntityStructureBuilder.Fields.HEIGHT;
    x = this.guiLeft + xControlsStart - xControlsSpacing;
    btnHeightUp = new ButtonTileEntityField(id++,
        x,
        yTopRow,
        tile.getPos(),
        fld.ordinal(),
        1, width, h);
    btnHeightUp.setTooltip("button." + fld.name().toLowerCase() + "." + "up");
    btnHeightUp.displayString = "+";
    this.addButton(btnHeightUp);
    this.registerButtonDisableTrigger(btnHeightUp, ButtonTriggerType.EQUAL, fld.ordinal(), TileEntityStructureBuilder.maxHeight);
    btnHeightDown = new ButtonTileEntityField(id++,
        x,
        yBottomRow,
        tile.getPos(),
        fld.ordinal(),
        -1, width, h);
    btnHeightDown.setTooltip("button." + fld.name().toLowerCase() + "." + "down");
    btnHeightDown.displayString = "-";
    this.addButton(btnHeightDown);
    this.registerButtonDisableTrigger(btnHeightDown, ButtonTriggerType.EQUAL, fld.ordinal(), 1);
    //////////////////ROTATION BUTTONS
    fld = TileEntityStructureBuilder.Fields.ROTATIONS;
    x = this.guiLeft + xControlsStart - 2 * xControlsSpacing;
    ButtonTileEntityField btnRotUp = new ButtonTileEntityField(id++,
        x,
        yTopRow,
        tile.getPos(),
        fld.ordinal(),
        1, width, h);
    btnRotUp.setTooltip("button." + fld.name().toLowerCase() + "." + "up");
    btnRotUp.displayString = "+";
    this.addButton(btnRotUp);
    ButtonTileEntityField btnRotDown = new ButtonTileEntityField(id++,
        x,
        yBottomRow,
        tile.getPos(),
        fld.ordinal(),
        -1, width, h);
    btnRotDown.setTooltip("button." + fld.name().toLowerCase() + "." + "down");
    btnRotDown.displayString = "-";
    this.addButton(btnRotDown);
    this.registerButtonDisableTrigger(btnRotDown, ButtonTriggerType.EQUAL, fld.ordinal(), 0);
  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    String label = UtilChat.lang("buildertype." + this.tile.getBuildTypeEnum().name().toLowerCase() + ".name");
    this.drawString(label, 112, 76);
    int sp = Const.PAD / 2;
    int x = xControlsStart + sp;
    int y = yRowTextbox + yOffset - sp;
    if (this.tile.getSize() > 0) {
      String display = "" + this.tile.getSize();
      //move it over if more than 1 digit 
      this.drawStringCenteredCheckLength(display, x, y);
    }
    x = xControlsStart - xControlsSpacing + sp;
    if (this.tile.getHeight() > 0 && this.tile.getBuildTypeEnum().hasHeight()) {
      String display = "" + this.tile.getHeight();
      //move it over if more than 1 digit 
      this.drawStringCenteredCheckLength(display, x, y);
    }
    x = xControlsStart - 2 * xControlsSpacing + sp;
    String display = "" + this.tile.getField(Fields.ROTATIONS.ordinal());
    //move it over if more than 1 digit 
    this.drawStringCenteredCheckLength(display, x, y);
    updateDisabledButtons();
  }
  private void updateDisabledButtons() {
    //a semi hack to hide btns
    if (btnHeightDown != null)
      this.btnHeightDown.visible = this.tile.getBuildTypeEnum().hasHeight();
    if (btnHeightUp != null)
      this.btnHeightUp.visible = this.tile.getBuildTypeEnum().hasHeight();
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    for (int k = 0; k < this.tile.getSizeInventory() - 1; k++) {
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerBuilder.SLOTX_START - 1 + k * Const.SQ, this.guiTop + ContainerBuilder.SLOTY - 1, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
    super.tryDrawFuelSlot(ContainerBaseMachine.SLOTX_FUEL - 1, ContainerBaseMachine.SLOTY_FUEL - 1);
  }
  @Override
  public void setEntryValue(int id, boolean value) {}
  @Override
  public void setEntryValue(int id, float value) {
    System.out.println("Set Slider Valuue" + id + "   " + value);
  }
  @Override
  public void setEntryValue(int id, String value) {}
}
