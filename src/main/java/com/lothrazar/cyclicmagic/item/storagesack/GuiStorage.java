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
import net.minecraft.client.gui.GuiButton;

public class GuiStorage extends GuiBaseContainer {

  private GuiButtonTooltip buttonToggle;
  private GuiButtonTooltip buttonColour;

  public GuiStorage(ContainerStorage containerItem) {
    super(containerItem);
    this.setScreenSize(ScreenSize.SACK);
  }

  @Override
  public void initGui() {
    super.initGui();
    int id = 0;
    int y = this.guiTop;
    int x = this.guiLeft;
    buttonToggle = new GuiButtonTooltip(id++, x, y, 10, 10, "");
    buttonToggle.setTooltip("item.storage_bag.toggle");
    this.addButton(buttonToggle);
    buttonColour = new GuiButtonTooltip(id++, x, y + 20, 10, 10, "");
    buttonColour.setTooltip("item.storage_bag.color");
    this.addButton(buttonColour);
  }

  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    if (button.id == this.buttonToggle.id) {
      // packet 
      ModCyclic.network.sendToServer(new PacketStorageBag("action"));
    }
    if (button.id == this.buttonColour.id) {
      // packet 
      ModCyclic.network.sendToServer(new PacketStorageBag("colour"));
      //      ItemStack stack = player.getHeldItemMainhand();
      //      if (!stack.isEmpty() && stack.getItem() instanceof ItemStorageBag) {
      //
      //        ItemStorageBag.StorageActionType.toggleColor(stack);
      //      }
    }
  }
}
