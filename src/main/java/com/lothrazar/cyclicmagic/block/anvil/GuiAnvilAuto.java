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
package com.lothrazar.cyclicmagic.block.anvil;

import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiAnvilAuto extends GuiBaseContainer {

  public GuiAnvilAuto(InventoryPlayer inventoryPlayer, TileEntityAnvilAuto tileEntity) {
    super(new ContainerAnvilAuto(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
    this.fieldRedstoneBtn = TileEntityAnvilAuto.Fields.REDSTONE.ordinal();
    this.setUsesEnergy();
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    Gui.drawModalRectWithCustomSizedTexture(
        this.guiLeft + 50 - 1,
        this.guiTop + ContainerAnvilAuto.SLOTY - 1,
        u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    Gui.drawModalRectWithCustomSizedTexture(
        this.guiLeft + 110 - 1,
        this.guiTop + ContainerAnvilAuto.SLOTY - 1,
        u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    this.drawFluidBar();
  }

  private void drawFluidBar() {
    int u = 0, v = 0;
    int currentFluid = ((TileEntityAnvilAuto) tile).getCurrentFluidStackAmount();
    this.mc.getTextureManager().bindTexture(Const.Res.FLUID);
    int pngWidth = 36, pngHeight = 124, f = 2, h = pngHeight / f;//f is scale factor. original is too big
    int fuelWidth = 16;
    int x = this.guiLeft + this.xSize / 2 - fuelWidth / 2 - 1, y = this.guiTop + fuelWidth;
    Gui.drawModalRectWithCustomSizedTexture(
        x, y, u, v,
        pngWidth / f, h,
        pngWidth / f, h);
    h -= 2;// inner texture is 2 smaller, one for each border
    this.mc.getTextureManager().bindTexture(Const.Res.FLUID_LAVA);
    float percent = ((float) currentFluid / ((float) TileEntityAnvilAuto.TANK_FULL));
    int hpct = (int) (h * percent);
    Gui.drawModalRectWithCustomSizedTexture(
        x + 1, y + 1 + h - hpct,
        u, v,
        fuelWidth, hpct,
        fuelWidth, h);
  }
}
