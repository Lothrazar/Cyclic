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
package com.lothrazar.cyclicmagic.component.beaconpotion;

import com.lothrazar.cyclicmagic.component.beaconpotion.TileEntityBeaconPotion.Fields;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.gui.ProgressBar;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import com.lothrazar.cyclicmagic.gui.button.ButtonTileEntityField;
import com.lothrazar.cyclicmagic.gui.button.GuiButtonToggleSize;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiBeaconPotion extends GuiBaseContainer {

  private GuiButtonToggleSize btnSize;
  private ButtonTileEntityField btnEntityType;

  public GuiBeaconPotion(InventoryPlayer inventoryPlayer, TileEntityBeaconPotion tileEntity) {
    super(new ContainerBeaconPotion(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
    this.fieldRedstoneBtn = TileEntityBeaconPotion.Fields.REDSTONE.ordinal();
    if (TileEntityBeaconPotion.doesConsumePotions) {
      this.progressBar = new ProgressBar(this, 10, ContainerBeaconPotion.SLOTY + 20, Fields.TIMER.ordinal(), TileEntityBeaconPotion.MAX_POTION);
    }
  }

  @Override
  public void initGui() {
    super.initGui();
    int id = 1;
    int x = Const.PAD / 2, w = 70, h = 20;
    int y = Const.PAD * 3 + 2;
    TileEntityBeaconPotion.Fields f = TileEntityBeaconPotion.Fields.ENTITYTYPE;
    btnEntityType = new ButtonTileEntityField(id,
        this.guiLeft + x,
        this.guiTop + y,
        tile.getPos(),
        f.ordinal(), 1, w, h);
    btnEntityType.setTooltip("tile.beacon_potion.entity.tooltip");
    this.addButton(btnEntityType);
    x = 176 - w - Const.PAD / 2;
    id++;
    btnSize = new GuiButtonToggleSize(id,
        this.guiLeft + x,
        this.guiTop + y, this.tile.getPos());
    btnSize.width = w;
    this.buttonList.add(btnSize);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT_BOTTLE);
    for (int k = 0; k < 9; k++) {
      Gui.drawModalRectWithCustomSizedTexture(
          this.guiLeft + ContainerBeaconPotion.SLOTX_START - 1 + k * Const.SQ,
          this.guiTop + ContainerBeaconPotion.SLOTY - 1,
          u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
    if (progressBar != null) {
      progressBar.maxValue = tile.getField(Fields.FUELMAX.ordinal());
    }
  }

  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    TileEntityBeaconPotion te = ((TileEntityBeaconPotion) this.tile);
    TileEntityBeaconPotion.EntityType t = te.getEntityType();
    this.btnEntityType.displayString = UtilChat.lang("tile.beacon_potion." + t.name().toLowerCase());
    int r = tile.getField(TileEntityBeaconPotion.Fields.RANGE.ordinal());
    r = ((int) Math.pow(2, r));
    btnSize.displayString = UtilChat.lang(r + " x " + r);
    String effect = te.getFirstEffectName();
    if (effect.isEmpty() == false) {
      effect = "<" + UtilChat.lang(effect) + ">";
      this.drawStringCentered(effect, this.width / 4, 16);
    }
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
  }
}
