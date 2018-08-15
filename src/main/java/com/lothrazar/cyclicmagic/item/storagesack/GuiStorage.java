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
import com.lothrazar.cyclicmagic.core.gui.GuiButtonTooltip;
import com.lothrazar.cyclicmagic.core.util.Const.ScreenSize;
import com.lothrazar.cyclicmagic.core.util.UtilChat;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;

public class GuiStorage extends GuiBaseContainer {

  private GuiButtonTooltip buttonToggle;
  private EntityPlayer player;

  public GuiStorage(ContainerStorage containerItem, EntityPlayer player) {
    super(containerItem);
    this.player = player;
    this.setScreenSize(ScreenSize.SACK);
  }

  @Override
  public void initGui() {
    super.initGui();
    int y = this.guiTop;
    int x = this.guiLeft;
    buttonToggle = new GuiButtonTooltip(75, x, y, 10, 10, "");
    buttonToggle.setTooltip("item.storage_bag.toggle");
    this.addButton(buttonToggle);
    int i = 0;
    int size = 12;
    for (EnumDyeColor color : EnumDyeColor.values()) {
      GuiButtonTooltip buttonColour = new GuiButtonTooltip(color.getColorValue(), x - size, y + size * i,
          size, size, color.name().substring(0, 1));
      buttonColour.setTooltip(UtilChat.lang("colour." + color.getUnlocalizedName() + ".name"));
      buttonColour.packedFGColour = color.getColorValue();
      this.addButton(buttonColour);
      i++;
    }
  }

  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    if (button.id == this.buttonToggle.id) {
      ModCyclic.network.sendToServer(new PacketStorageBag());
    }
    else {
      ItemStorageBag.StorageActionType.setColour(player.getHeldItemMainhand(), button.id);
      ModCyclic.network.sendToServer(new PacketColorStack(button.id));
    }
  }
}
