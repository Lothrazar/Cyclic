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
package com.lothrazar.cyclicmagic.block.cablewireless.energy;

import java.io.IOException;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.core.data.BlockPosDim;
import com.lothrazar.cyclicmagic.core.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.core.gui.GuiButtonTooltip;
import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.core.util.Const.ScreenSize;
import com.lothrazar.cyclicmagic.core.util.UtilChat;
import com.lothrazar.cyclicmagic.gui.EnergyBar;
import com.lothrazar.cyclicmagic.gui.GuiSliderInteger;
import com.lothrazar.cyclicmagic.item.location.ItemLocation;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class GuiCableEnergyWireless extends GuiBaseContainer {

  private GuiSliderInteger slider;

  public GuiCableEnergyWireless(InventoryPlayer inventoryPlayer, TileCableEnergyWireless te) {
    super(new ContainerCableEnergyWireless(inventoryPlayer, te), te);
    this.setScreenSize(ScreenSize.LARGE);
    this.fieldRedstoneBtn = TileCableEnergyWireless.Fields.REDSTONE.ordinal();
    this.energyBar = new EnergyBar(this);
    energyBar.setWidth(16).setY(18).setX(this.getScreenSize().width() - 24);
  }

  @Override
  public void initGui() {
    super.initGui();
    int y = 106;
    int size = Const.SQ;
    GuiButtonTooltip btnSize;
    for (int i = 0; i < TileCableEnergyWireless.SLOT_COUNT; i++) {
      btnSize = new GuiButtonTooltip(i,
          this.guiLeft + i * (size) + 8,
          this.guiTop + y, size, size, "?");
      btnSize.setTooltip("wireless.target");
      this.addButton(btnSize);
    }
    int x = this.guiLeft + 6;
    y = this.guiTop + 38;
    slider = new GuiSliderInteger(tile, 77,
        x, y, 140, 20, 1, TileCableEnergyWireless.MAX_TRANSFER,
        TileCableEnergyWireless.Fields.TRANSFER_RATE.ordinal());
    slider.setTooltip("pump.rate");
    this.addButton(slider);
  }

  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    if (button.id != redstoneBtn.id && button.id != slider.id) {
      EntityPlayer player = ModCyclic.proxy.getClientPlayer();
      BlockPosDim dim = ItemLocation.getPosition(tile.getStackInSlot(button.id));
      if (dim == null) {
        UtilChat.addChatMessage(player, "wireless.empty");
      }
      else if (dim.dimension != player.dimension) {
        UtilChat.addChatMessage(player, "wireless.dimension");
      }
      else {
        BlockPos target = dim.toBlockPos();
        if (tile.getWorld().isAreaLoaded(target, target.up())) {
          //get target
          IBlockState statehere = tile.getWorld().getBlockState(target);
          Block block = statehere.getBlock();
          //    if (block.getLocalizedName().equals(block.getUnlocalizedName())) {
          //example: thermal machiens use crazy item stack NBT + Block metadata 
          ItemStack dropped = new ItemStack(block.getItemDropped(statehere, player.world.rand, 0), 1, block.damageDropped(statehere));
          UtilChat.addChatMessage(player, dropped.getDisplayName());
          //   }
          // else
          //   UtilChat.addChatMessage(player, block.getLocalizedName());
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
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT_GPS);
    //now draw target location card slots 
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
