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
package com.lothrazar.cyclicmagic.block.builderpattern;

import com.lothrazar.cyclicmagic.block.builderpattern.TileEntityPatternBuilder.Fields;
import com.lothrazar.cyclicmagic.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.gui.button.ButtonTileEntityField;
import com.lothrazar.cyclicmagic.gui.component.EnergyBar;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.Const.ScreenSize;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiPattern extends GuiBaseContainer {

  static final int GUI_ROWS = 2;
  private TileEntityPatternBuilder tile;
  private ButtonTileEntityField btnRotation;
  private ButtonTileEntityField btnFlipZ;
  private ButtonTileEntityField btnFlipY;
  private ButtonTileEntityField btnFlipX;
  private ButtonTileEntityField btnRender;

  public GuiPattern(InventoryPlayer inventoryPlayer, TileEntityPatternBuilder tileEntity) {
    super(new ContainerPattern(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
    setScreenSize(ScreenSize.LARGE);
    this.xSize = getScreenSize().width();
    this.ySize = getScreenSize().height();
    this.fieldRedstoneBtn = Fields.REDSTONE.ordinal();
    this.energyBar = new EnergyBar(this);
    energyBar.setX(158).setY(4).setHeight(42);
  }

  @Override
  public void initGui() {
    super.initGui();
    int id = 2;
    int x = this.guiLeft + Const.PAD / 2;
    int y = this.guiTop + Const.PAD / 2 + 22;
    btnRender = new ButtonTileEntityField(id++,
        x,
        y, this.tile.getPos(), Fields.RENDERPARTICLES.ordinal());
    btnRender.width = btnRender.height = 18;
    this.addButton(btnRender);
    /////redstone button
    //button rotation 
    btnRotation = new ButtonTileEntityField(id++,
        this.guiLeft + 26, this.guiTop + 15, tile.getPos(),
        Fields.ROTATION.ordinal(), 1, 40, 16);
    btnRotation.setTooltip("tile.builder_pattern.rotation");
    this.addButton(btnRotation);
    // flips
    btnFlipX = new ButtonTileEntityField(id++,
        btnRotation.x + btnRotation.width + 4, this.guiTop + 15, tile.getPos(),
        Fields.FLIPX.ordinal(), 1, 20, 16);
    btnFlipX.setTooltip("tile.builder_pattern.flipaxis");
    this.addButton(btnFlipX);
    // y
    btnFlipY = new ButtonTileEntityField(id++,
        btnFlipX.x + btnFlipX.width + 4, btnFlipX.y, tile.getPos(),
        Fields.FLIPY.ordinal(), 1, 20, 16);
    btnFlipY.setTooltip("tile.builder_pattern.flipaxis");
    this.addButton(btnFlipY);
    // z
    btnFlipZ = new ButtonTileEntityField(id++,
        btnFlipY.x + btnFlipY.width + 4, btnFlipY.y, tile.getPos(),
        Fields.FLIPZ.ordinal(), 1, 20, 16);
    btnFlipZ.setTooltip("tile.builder_pattern.flipaxis");
    this.addButton(btnFlipZ);
  }

  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    btnRotation.displayString = this.tile.getRotationName();
    int render = tile.getField(Fields.RENDERPARTICLES);
    if (render == 0) {
      btnRender.setTextureIndex(-1);
    }
    else if (render == 1)
      btnRender.setTextureIndex(render + 2);
    else if (render == 2)
      btnRender.setTextureIndex(render);
    btnRender.setTooltip(tile.getName() + ".preview" + tile.getField(Fields.RENDERPARTICLES));
    btnFlipX.displayString = ((tile.getField(Fields.FLIPX) == 1) ? "^" : "") + "X";
    btnFlipY.displayString = ((tile.getField(Fields.FLIPY) == 1) ? "^" : "") + "Y";
    btnFlipZ.displayString = ((tile.getField(Fields.FLIPZ) == 1) ? "^" : "") + "Z";
    this.drawString("tile.builder_pattern.source_gps_label", 30, 40);
    this.drawString("tile.builder_pattern.target_gps_label", 110, 40);
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    int row = 0, col = 0;
    for (int i = 0; i < tile.getSizeInventory() - 3; i++) {
      row = i / GUI_ROWS;// /3 will go 000, 111, 222
      col = i % GUI_ROWS; // and %3 will go 012 012 012
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerPattern.SLOTX_START - 1 + row * Const.SQ, this.guiTop + ContainerPattern.SLOTY_START - 1 + col * Const.SQ, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT_GPS);
    int hgt = 50;
    Gui.drawModalRectWithCustomSizedTexture(
        this.guiLeft + 25, this.guiTop + hgt + 15, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    Gui.drawModalRectWithCustomSizedTexture(
        this.guiLeft + 45, this.guiTop + hgt, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    Gui.drawModalRectWithCustomSizedTexture(
        this.guiLeft + 120, this.guiTop + hgt, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
  }
}
