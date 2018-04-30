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
package com.lothrazar.cyclicmagic.block.sorting;

import java.util.Map;
import com.google.common.collect.Maps;
import com.lothrazar.cyclicmagic.core.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.core.gui.StackWrapper;
import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.core.util.Const.ScreenSize;
import com.lothrazar.cyclicmagic.gui.button.ButtonTileEntityField;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiItemSort extends GuiBaseContainer {

  private Map<EnumFacing, ButtonTileEntityField> btnMapLock = Maps.newHashMap();
  private Map<EnumFacing, ButtonTileEntityField> btnMapDamageIgnore = Maps.newHashMap();
  TileEntityItemCableSort te;

  public GuiItemSort(InventoryPlayer inventoryPlayer, TileEntityItemCableSort tileEntity) {
    super(new ContainerItemSort(inventoryPlayer, tileEntity), tileEntity);
    setScreenSize(ScreenSize.LARGE);
    te = tileEntity;
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    this.drawDefaultBackground();//dim the background as normal
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(Const.Res.TABLEFILTER);
    int thisX = getMiddleX();
    int thisY = getMiddleY();
    int u = 0, v = 0, slotNum = 0, x, y;
    Gui.drawModalRectWithCustomSizedTexture(thisX, thisY, u, v,
        getScreenSize().width(), getScreenSize().height(),
        getScreenSize().width(), getScreenSize().height());
    //set locations
    
    /*    int fs = TileEntityItemCableSort.FILTER_SIZE;
    int slot = 1;
    int y = SLOTY;
    for (int col = 0; col < EnumFacing.values().length; col++) {
      y = col * Const.SQ;
      for (int row = 0; row < fs; row++) {
        addSlotToContainer(new SlotSingleStack(tile, slot,
            SLOTX_START + row % fs * Const.SQ + Const.SQ,
            SLOTY + y));
        slot++;
      }
    }*/
    int fs = TileEntityItemCableSort.FILTER_SIZE;

    for (int col = 0; col < EnumFacing.values().length; col++) {
      y = guiTop + col * Const.SQ + ContainerItemSort.SLOTY - 1;
      for (int row = 0; row < fs; row++) {
        StackWrapper wrap = te.getStackWrapper(slotNum);
        x = guiLeft + ContainerItemSort.SLOTX_START + row % fs * Const.SQ + Const.SQ - 1;//Const.PAD + Const.SQ * row;

        wrap.setX(x);
        wrap.setY(y);
        slotNum++;
      }
    }
    this.renderStackWrappers(te);
  }

  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    ButtonTileEntityField btn;
    for (EnumFacing f : EnumFacing.values()) {
      btn = btnMapLock.get(f);
      if (btn != null) {
        btn.setTooltip(te.getLockType(f).nameLower());
        btn.setTextureIndex(5 + te.getLockType(f).ordinal());
      }
      btn = btnMapDamageIgnore.get(f);
      if (btn != null) {
        int lockValue = te.getField(f.ordinal() + EnumFacing.values().length);
        btn.setTooltip("button.filter.ignoredamage" + lockValue + ".tooltip");
        btn.displayString = (lockValue == 1) ? "I" : "N";
      }
    }
  }

  @Override
  public void initGui() {
    super.initGui();
    int id = 2;
    ButtonTileEntityField btn;
    for (EnumFacing f : EnumFacing.values()) {
      btn = new ButtonTileEntityField(
          id++,
          this.guiLeft + Const.PAD,
          this.guiTop + f.ordinal() * Const.SQ + 17,
          tile.getPos(), f.ordinal(), 1,
          Const.SQ, Const.SQ);
      this.addButton(btn);
      btnMapLock.put(f, btn);
    }
    int offset = EnumFacing.values().length;
    for (EnumFacing f : EnumFacing.values()) {
      id++;
      btn = new ButtonTileEntityField(
          id++,
          this.guiLeft + 1,
          this.guiTop + f.ordinal() * Const.SQ + 17,
          tile.getPos(), f.ordinal() + offset, 1,
          8, Const.SQ);
      this.addButton(btn);
      btnMapDamageIgnore.put(f, btn);
    }
  }
}
