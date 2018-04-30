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
package com.lothrazar.cyclicmagic.playerupgrade.storage;

import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.core.gui.GuiButtonTooltip;
import com.lothrazar.cyclicmagic.playerupgrade.PacketOpenExtendedInventory;
import com.lothrazar.cyclicmagic.playerupgrade.PacketOpenNormalInventory;
import com.lothrazar.cyclicmagic.playerupgrade.crafting.GuiPlayerExtWorkbench;
import com.lothrazar.cyclicmagic.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ButtonTabToggleInventory extends GuiButtonTooltip {

  private GuiScreen gui;

  public ButtonTabToggleInventory(GuiScreen g, int x, int y) {
    super(51, x, y, 15, 10, "");
    gui = g;
    if (ClientProxy.keyExtraInvo != null && ClientProxy.keyExtraInvo.getDisplayName() != null &&
        ClientProxy.keyExtraInvo.getDisplayName().equals("NONE") == false) {
      this.displayString = ClientProxy.keyExtraInvo.getDisplayName();
    }
    else {
      this.displayString = "I";//the legacy one. in case someone is just running with the key unbound
    }
  }

  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    if (pressed) {
      if (this.gui instanceof GuiInventory || this.gui instanceof GuiPlayerExtWorkbench) {
        ModCyclic.network.sendToServer(new PacketOpenExtendedInventory());
      }
      else {//if (this.gui instanceof GuiPlayerExtended || this.gui instanceof GuiCrafting) {
        this.gui.mc.displayGuiScreen(new GuiInventory(gui.mc.player));
        ModCyclic.network.sendToServer(new PacketOpenNormalInventory(this.gui.mc.player));
      }
    }
    return pressed;
  }
}
