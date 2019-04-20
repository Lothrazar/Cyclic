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
package com.lothrazar.cyclicmagic.block.trash;

import com.lothrazar.cyclicmagic.gui.button.ButtonTileEntityField;
import com.lothrazar.cyclicmagic.gui.container.GuiBaseContainer;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiTrash extends GuiBaseContainer {

  private ButtonTileEntityField btnItems;
  private ButtonTileEntityField btnFluids;

  public GuiTrash(InventoryPlayer inventoryPlayer, TileEntityTrash tileEntity) {
    super(new ContainerTrash(inventoryPlayer, tileEntity), tileEntity);
    this.fieldRedstoneBtn = TileEntityTrash.Fields.REDSTONE.ordinal();
  }

  @Override
  public void initGui() {
    super.initGui();
    int id = 1;
    int w = 18, h = 18;
    int x = this.guiLeft + 4;
    int y = this.guiTop + 26;
    btnItems = new ButtonTileEntityField(id++, x, y, tile.getPos(),
        TileEntityTrash.Fields.ITEM.ordinal(), +1, w, h);
    btnItems.setTooltip("button.trash.items.tooltip");
    this.addButton(btnItems);
    y += 20;
    btnFluids = new ButtonTileEntityField(id++, x, y, tile.getPos(),
        TileEntityTrash.Fields.FLUID.ordinal(), +1, w, h);
    btnFluids.setTooltip("button.trash.fluid.tooltip");
    this.addButton(btnFluids);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    Gui.drawModalRectWithCustomSizedTexture(
        this.guiLeft + ContainerTrash.SLOTX_START - 1,
        this.guiTop + ContainerTrash.SLOTY - 1,
        u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
  }

  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    int val = tile.getField(TileEntityTrash.Fields.ITEM.ordinal());
    btnItems.setTextureIndex(16 + val);
    val = tile.getField(TileEntityTrash.Fields.FLUID.ordinal());
    btnFluids.setTextureIndex(18 + val);
    String s = UtilChat.lang("tile.trash.warning");
    int x = 30;
    int y = 26;
    this.drawString(s, x, y);
  }
}
