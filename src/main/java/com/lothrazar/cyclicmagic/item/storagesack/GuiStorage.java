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
package com.lothrazar.cyclicmagic.item.storagesack;

import java.io.IOException;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.core.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.core.gui.GuiButtonTexture;
import com.lothrazar.cyclicmagic.core.gui.GuiButtonTooltip;
import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.core.util.Const.ScreenSize;
import com.lothrazar.cyclicmagic.core.util.UtilChat;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiStorage extends GuiBaseContainer {

  private GuiButtonTexture buttonToggle;
  private EntityPlayer player;
  private GuiButtonTexture buttonTogglePickup;

  public GuiStorage(ContainerStorage containerItem, EntityPlayer player) {
    super(containerItem);
    this.player = player;
    this.setScreenSize(ScreenSize.SACK);
  }

  @Override
  public void initGui() {
    super.initGui();
    int y = this.guiTop + 138;
    int x = this.guiLeft + 194;
    int id = 75, size = Const.SQ;
    buttonToggle = new GuiButtonTexture(id++, x, y, size, size);
    buttonToggle.setTooltip("item.storage_bag.toggle");
    this.addButton(buttonToggle);
    y += 20;
    buttonTogglePickup = new GuiButtonTexture(id++, x, y, size, size);
    buttonTogglePickup.setTooltip("item.storage_bag.togglepickup");
    this.addButton(buttonTogglePickup);
    int i = 0;
    size = 12;
    y = this.guiTop;
    x = this.guiLeft;
    for (EnumDyeColor color : EnumDyeColor.values()) {
      GuiButtonTooltip buttonColour = new GuiButtonTooltip(color.getColorValue(), x - size, y + size * i,
          size, size, color.name().substring(0, 1));
      buttonColour.setTooltip(UtilChat.lang("colour." + color.getUnlocalizedName() + ".name"));
      buttonColour.packedFGColour = color.getColorValue();
      this.addButton(buttonColour);
      i++;
    }
  }

  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    buttonToggle.setTextureIndex(11 + ItemStorageBag.StorageActionType.get(player.getHeldItemMainhand()));
    buttonTogglePickup.setTextureIndex(11 + ItemStorageBag.StoragePickupType.get(player.getHeldItemMainhand()));
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
  }
  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    if (button.id == this.buttonToggle.id) {
      ModCyclic.network.sendToServer(new PacketStorageBag(0));
    }
    else if (button.id == this.buttonTogglePickup.id) {
      ModCyclic.network.sendToServer(new PacketStorageBag(1));
    }
    else {
      ItemStorageBag.StorageActionType.setColour(player.getHeldItemMainhand(), button.id);
      ModCyclic.network.sendToServer(new PacketColorStack(button.id));
    }
  }
}
