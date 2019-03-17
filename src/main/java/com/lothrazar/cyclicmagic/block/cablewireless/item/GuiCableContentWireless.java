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
package com.lothrazar.cyclicmagic.block.cablewireless.item;

import java.io.IOException;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.cablewireless.energy.TileCableEnergyWireless;
import com.lothrazar.cyclicmagic.data.BlockPosDim;
import com.lothrazar.cyclicmagic.gui.core.GuiBaseContainer;
import com.lothrazar.cyclicmagic.gui.core.GuiButtonTooltip;
import com.lothrazar.cyclicmagic.item.location.ItemLocation;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.Const.ScreenSize;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.block.Block;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.math.BlockPos;

public class GuiCableContentWireless extends GuiBaseContainer {

  public GuiCableContentWireless(InventoryPlayer inventoryPlayer, TileCableContentWireless te) {
    super(new ContainerCableContentWireless(inventoryPlayer, te), te);
    this.setScreenSize(ScreenSize.LARGE);
    this.fieldRedstoneBtn = TileCableContentWireless.Fields.REDSTONE.ordinal();
    int xCenter = this.getScreenSize().width() / 2;
  }

  @Override
  public void initGui() {
    super.initGui();
    int x;
    int y = 106;
    int size = Const.SQ;
    GuiButtonTooltip btnSize;
    for (int i = 1; i < TileCableContentWireless.SLOT_COUNT; i++) {
      btnSize = new GuiButtonTooltip(i,
          this.guiLeft + (i - 1) * (size) + 8,
          this.guiTop + y, size, size, "?");
      btnSize.setTooltip("wireless.target");
      //      btnSize.height = 14;
      this.addButton(btnSize);
    }
  }

  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    if (button.id != redstoneBtn.id) {
      //TODO: DIMENSION 
      EntityPlayer player = ModCyclic.proxy.getClientPlayer();
      BlockPosDim dim = ItemLocation.getPosition(tile.getStackInSlot(button.id));
      if (dim == null) {
        UtilChat.addChatMessage(player, "wireless.empty");
      }
      else if (dim.getDimension() != player.dimension) {
        UtilChat.addChatMessage(player, "wireless.dimension");
      }
      else {
        BlockPos target = dim.toBlockPos();
        if (tile.getWorld().isAreaLoaded(target, target.up())) {
          //get target
          Block block = tile.getWorld().getBlockState(target).getBlock();
          UtilChat.addChatMessage(player, block.getLocalizedName());
        }
        else {
          UtilChat.addChatMessage(player, "wireless.unloaded");
        }
      }
    }
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0, x, y;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT_LARGE);
    // item transfer slot   
    x = this.guiLeft + 142;
    y = this.guiTop + 38;
    int s = Const.SQ + 4;
    Gui.drawModalRectWithCustomSizedTexture(
        x, y,
        u, v, s, s, s, s);
    //now draw target location card slots 
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT_GPS);
    x = this.guiLeft + 8;
    y = this.guiTop + 86;
    for (int i = 0; i < TileCableEnergyWireless.SLOT_COUNT; i++) {
      Gui.drawModalRectWithCustomSizedTexture(// this is for item transfer
          x, y,
          u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
      x += Const.SQ;
    }
  }
}
