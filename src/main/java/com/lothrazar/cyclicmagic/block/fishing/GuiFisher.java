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
package com.lothrazar.cyclicmagic.block.fishing;

import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiFisher extends GuiBaseContainer {

  public static final ResourceLocation SLOTFISH = new ResourceLocation(Const.MODID, "textures/gui/inventory_slot_fish.png");
  private TileEntityFishing tile;

  public GuiFisher(InventoryPlayer inventoryPlayer, TileEntityFishing tileEntity) {
    super(new ContainerFisher(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
    this.fieldRedstoneBtn = TileEntityFishing.Fields.REDSTONE.ordinal();
  }

  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    if (tile.isEquipmentValid() && tile.isValidPosition() == false) {
      String s = UtilChat.lang("tile.block_fishing.invalidpos.gui1");
      int x = 13 + this.xSize / 3 - this.fontRenderer.getStringWidth(s);
      int y = 42;
      this.drawString(s, x, y);
      y += 14;
      s = UtilChat.lang("tile.block_fishing.invalidpos.gui2");
      s = s + TileEntityFishing.MINIMUM_WET_SIDES + "+";
      this.drawString(s, x + 12, y);
      y += 14;
      s = UtilChat.lang("tile.block_fishing.invalidpos.gui3");
      this.drawString(s, x + 14, y);
    }
    if (tile.isEquipmentValid() && tile.isValidPosition()) {
      String s = UtilChat.lang("tile.block_fishing.progress");
      int x = 4 + this.xSize / 3 - this.fontRenderer.getStringWidth(s);
      int y = 50;
      this.fontRenderer.drawString(s, x, y, 4210752);
      y += 14;
      double fs = tile.getFishSpeed() * 100.0;
      String displaySp = String.format("%.2f", fs);//split up in case crashes again.  //java.util.IllegalFormatConversionException: f != java.lang.Integer
      this.fontRenderer.drawString("(" + displaySp + " Hz)", x, y, 4210752);
    }
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(SLOTFISH);
    for (int k = 0; k < TileEntityFishing.SLOT_TOOL + 1; k++) {
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerFisher.SLOTX_START - 1 + k * Const.SQ, this.guiTop + ContainerFisher.SLOTY - 1, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    int row = 0, col = 0;
    for (int i = 0; i < TileEntityFishing.FISHSLOTS; i++) {
      row = i / 3;// /3 will go 000, 111, 222
      col = i % 3; // and %3 will go 012 012 012
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerFisher.SLOTX_FISH - 1 + row * Const.SQ, this.guiTop + ContainerFisher.SLOTY_FISH - 1 + col * Const.SQ, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
  }
}
