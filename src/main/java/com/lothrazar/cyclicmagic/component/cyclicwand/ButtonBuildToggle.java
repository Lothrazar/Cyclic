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
package com.lothrazar.cyclicmagic.component.cyclicwand;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.gui.ITooltipButton;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilSpellCaster;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ButtonBuildToggle extends GuiButton implements ITooltipButton {

  private final EntityPlayer thePlayer;

  public ButtonBuildToggle(EntityPlayer p, int buttonId, int x, int y, int width) {
    super(buttonId, x, y, width, 20, "");
    thePlayer = p;
  }

  @SideOnly(Side.CLIENT)
  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    if (pressed) {
      ModCyclic.network.sendToServer(new PacketWandGui(PacketWandGui.WandAction.BUILDTYPE));
    }
    return pressed;
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void drawButton(Minecraft mc, int mouseX, int mouseY, float p) {
    this.displayString = UtilChat.lang(ItemCyclicWand.BuildType.getName(UtilSpellCaster.getPlayerWandIfHeld(thePlayer)));
    super.drawButton(mc, mouseX, mouseY, p);
  }

  @Override
  public List<String> getTooltips() {
    List<String> tooltips = new ArrayList<String>();
    String key = ItemCyclicWand.BuildType.getName(UtilSpellCaster.getPlayerWandIfHeld(thePlayer)) + ".tooltip";
    tooltips.add(UtilChat.lang(key));
    return tooltips;
  }
}
