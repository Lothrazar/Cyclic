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
package com.lothrazar.cyclicmagic.block.exppylon;

import com.lothrazar.cyclicmagic.block.exppylon.TileEntityXpPylon.Fields;
import com.lothrazar.cyclicmagic.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.gui.button.ButtonTileEntityField;
import com.lothrazar.cyclicmagic.gui.component.FluidBar;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.Const.ScreenSize;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiPylon extends GuiBaseContainer {

  public static final ResourceLocation PROGEXP = new ResourceLocation(Const.MODID, "textures/gui/progress_exp.png");
  public static final ResourceLocation SLOT_BOTTLE = new ResourceLocation(Const.MODID, "textures/gui/inventory_slot_bottle.png");
  public static final ResourceLocation SLOT_EBOTTLE = new ResourceLocation(Const.MODID, "textures/gui/inventory_slot_ebottle.png");
  private TileEntityXpPylon tile;
  boolean debugLabels = false;
  private ButtonTileEntityField btnCollect;

  public GuiPylon(InventoryPlayer inventoryPlayer, TileEntityXpPylon tileEntity) {
    super(new ContainerPylon(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
    this.setScreenSize(ScreenSize.STANDARD);
    this.fieldRedstoneBtn = Fields.REDSTONE.ordinal();
    this.fluidBar = new FluidBar(this, 128, 17);
    fluidBar.setCapacity(TileEntityXpPylon.TANK_FULL);
  }

  @Override
  public void initGui() {
    super.initGui();
    int btnId = 0;
    int x = this.guiLeft + Const.PAD * 2;
    int y = this.guiTop + Const.PAD * 3 + Const.PAD / 2;
    btnCollect = new ButtonTileEntityField(btnId++, x, y, tile.getPos(), TileEntityXpPylon.Fields.COLLECT.ordinal());
    btnCollect.width = 70;
    btnCollect.height = 20;
    this.addButton(btnCollect.setTooltip("button.exp_pylon.collect.tooltip"));
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(SLOT_BOTTLE);
    for (int k = 0; k < this.tile.getSizeInventory(); k++) {
      if (k == 0)
        this.mc.getTextureManager().bindTexture(SLOT_BOTTLE);
      else
        this.mc.getTextureManager().bindTexture(SLOT_EBOTTLE);
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerPylon.SLOTX - 1, this.guiTop + ContainerPylon.SLOTY - 1 + k * (8 + Const.SQ), u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
    fluidBar.draw(tile.getCurrentFluidStack());
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    //moveQty
    btnCollect.displayString = UtilChat.lang("button.exp_pylon.collect" + tile.getField(TileEntityXpPylon.Fields.COLLECT.ordinal()));
    int fluidHas = this.tile.getField(TileEntityXpPylon.Fields.EXP.ordinal());
    //    this.drawString(fluidHas + " / " + TileEntityXpPylon.TANK_FULL, this.xSize / 2 - 8, 108);
    int expHas = fluidHas / TileEntityXpPylon.FLUID_PER_EXP;
    int expFull = TileEntityXpPylon.TANK_FULL / TileEntityXpPylon.FLUID_PER_EXP;
    this.drawString("EXP: " + expHas + " / " + expFull, Const.PAD, 72);
  }
}
