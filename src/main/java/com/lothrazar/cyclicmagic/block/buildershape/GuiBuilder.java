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
package com.lothrazar.cyclicmagic.block.buildershape;

import java.io.IOException;
import org.lwjgl.input.Keyboard;
import com.lothrazar.cyclicmagic.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.gui.button.ButtonTileEntityField;
import com.lothrazar.cyclicmagic.gui.button.ButtonTriggerWrapper.ButtonTriggerType;
import com.lothrazar.cyclicmagic.gui.component.EnergyBar;
import com.lothrazar.cyclicmagic.gui.component.GuiSliderInteger;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.Const.ScreenSize;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiBuilder extends GuiBaseContainer {

  private TileEntityStructureBuilder tile;
  private int yOffset = 10 + Const.PAD;
  private GuiSliderInteger sliderHeight;
  private GuiSliderInteger sliderSize;

  public GuiBuilder(InventoryPlayer inventoryPlayer, TileEntityStructureBuilder tileEntity) {
    super(new ContainerBuilder(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
    setScreenSize(ScreenSize.LARGE);
    this.fieldRedstoneBtn = TileEntityStructureBuilder.Fields.REDSTONE.ordinal();
    this.fieldPreviewBtn = TileEntityStructureBuilder.Fields.RENDERPARTICLES.ordinal();
    this.energyBar = new EnergyBar(this);
    energyBar.setWidth(10).setY(4).setX(160).setHeight(42);
  }

  @Override
  public void initGui() {
    super.initGui();
    Keyboard.enableRepeatEvents(true);
    //first the main top left type button
    TileEntityStructureBuilder.Fields fld;
    int id = 1;
    int width = 102;
    int h = 10;
    int x = this.guiLeft + 52;
    int y = this.guiTop + 15;
    id++;
    sliderHeight = new GuiSliderInteger(tile, id, x, y, width, h,
        1, TileEntityStructureBuilder.maxHeight, TileEntityStructureBuilder.Fields.HEIGHT.ordinal());
    sliderHeight.setTooltip("slider.height.tooltip");
    this.addButton(sliderHeight);
    id++;
    y += 16;
    sliderSize = new GuiSliderInteger(tile, id, x, y, width, h,
        1, TileEntityStructureBuilder.maxSize, TileEntityStructureBuilder.Fields.SIZE.ordinal());
    sliderSize.setTooltip("slider.size.tooltip");
    this.addButton(sliderSize);
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
    for (StructureBuilderType shape : StructureBuilderType.values()) {
      numInRow++;
      if (numInRow == 9) {
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
  }

  @Override
  public void onGuiClosed() {
    Keyboard.enableRepeatEvents(false);
  }

  @Override
  protected void keyTyped(char typedChar, int keyCode) throws IOException {
    super.keyTyped(typedChar, keyCode);
    sliderHeight.keyTyped(typedChar, keyCode);
    sliderSize.keyTyped(typedChar, keyCode);
  }

  @Override
  public void updateScreen() {
    super.updateScreen();
    sliderHeight.updateScreen();
    sliderSize.updateScreen();
  }

  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    String label = UtilChat.lang("buildertype." + this.tile.getBuildTypeEnum().name().toLowerCase() + ".name");
    this.drawString(label, 66, 76);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    for (int k = 0; k < this.tile.getSizeInventory() - 1; k++) {
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerBuilder.SLOTX_START - 1 + k * Const.SQ, this.guiTop + ContainerBuilder.SLOTY - 1, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
    // the GPS slot
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT_GPS);
    Gui.drawModalRectWithCustomSizedTexture(
        this.guiLeft + 25, this.guiTop + 28, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
  }
}
