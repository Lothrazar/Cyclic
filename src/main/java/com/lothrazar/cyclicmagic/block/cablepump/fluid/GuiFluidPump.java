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
package com.lothrazar.cyclicmagic.block.cablepump.fluid;

import java.io.IOException;
import org.lwjgl.input.Keyboard;
import com.lothrazar.cyclicmagic.block.cable.TileEntityCableBase;
import com.lothrazar.cyclicmagic.data.FluidWrapper;
import com.lothrazar.cyclicmagic.data.ITileFluidWrapper;
import com.lothrazar.cyclicmagic.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.gui.button.ButtonTileEntityField;
import com.lothrazar.cyclicmagic.gui.component.GuiSliderInteger;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiFluidPump extends GuiBaseContainer {

  private ITileFluidWrapper te;
  private GuiSliderInteger slider;
  private ButtonTileEntityField filterBtn;

  public GuiFluidPump(InventoryPlayer inventoryPlayer, TileEntityFluidPump tileEntity) {
    super(new ContainerFluidPump(inventoryPlayer, tileEntity), tileEntity);
    te = tileEntity;
    this.fieldRedstoneBtn = TileEntityFluidPump.Fields.REDSTONE.ordinal();
  }

  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    super.actionPerformed(button);
  }

  @Override
  public void initGui() {
    super.initGui();
    Keyboard.enableRepeatEvents(true);
    int id = 1;
    int width = 164;
    int h = 20;
    int x = this.guiLeft + 6;
    int y = this.guiTop + 28;
    //not more than the cable can handle
    int fld = TileEntityFluidPump.Fields.TRANSFER_RATE.ordinal();
    slider = new GuiSliderInteger(tile, id++, x, y,
        width, h,
        1, TileEntityCableBase.TRANSFER_FLUID_PER_TICK, //min max
        fld, "pump.rate");
    this.addButton(slider);
    x = this.guiLeft + 150;
    y = this.guiTop + Const.PAD / 2;
    filterBtn = new ButtonTileEntityField(
        id++,
        x, y,
        tile.getPos(), TileEntityFluidPump.Fields.FILTERTYPE.ordinal(), 1,
        20, 20);
    this.addButton(filterBtn);
  }

  @Override
  public void onGuiClosed() {
    Keyboard.enableRepeatEvents(false);
  }

  @Override
  protected void keyTyped(char typedChar, int keyCode) throws IOException {
    super.keyTyped(typedChar, keyCode);
    slider.keyTyped(typedChar, keyCode);
  }

  @Override
  public void updateScreen() {
    super.updateScreen();
    slider.updateScreen();
  }

  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    int filterType = tile.getField(TileEntityFluidPump.Fields.FILTERTYPE.ordinal());
    filterBtn.setTooltip(UtilChat.lang("button.itemfilter.tooltip.type" + filterType));
    filterBtn.setTextureIndex(11 + filterType);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int x = this.guiLeft + Const.PAD - 1;
    int y = this.guiTop + 3 * Const.SQ + 3;

    for (int k = 0; k < te.getWrapperCount(); k++) {
      FluidWrapper wrap = te.getStackWrapper(k);
      wrap.setX(x);
      wrap.setY(y);
      x += Const.SQ;
    }
    this.renderFluidWrappers(te, true);
  }
}
