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
package com.lothrazar.cyclicmagic.playerupgrade;

import java.util.ArrayList;
import com.lothrazar.cyclicmagic.component.playerext.crafting.GuiPlayerExtWorkbench;
import com.lothrazar.cyclicmagic.component.playerext.storage.GuiPlayerExtended;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry.IPlayerExtendedProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.gui.inventory.GuiScreenHorseInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventGuiInvoButtons {

  @SideOnly(value = Side.CLIENT)
  @SubscribeEvent
  public void guiPostInit(GuiScreenEvent.InitGuiEvent.Post event) {
    GuiScreen gui = event.getGui();
    boolean showInvToggle = false;
    boolean showCraftToggle = false;
    if (gui instanceof GuiInventory || gui instanceof GuiPlayerExtended
        || gui instanceof GuiPlayerExtWorkbench
        || gui instanceof GuiScreenHorseInventory) {
      // gui left and top are private, so are the sizes
      int xSize = 176;
      int ySize = 166;
      int guiLeft = (gui.width - xSize) / 2;
      int guiTop = (gui.height - ySize) / 2;
      int x = 44 + guiLeft;
      int y = guiTop;
      EntityPlayer player = Minecraft.getMinecraft().player;
      final IPlayerExtendedProperties data = CapabilityRegistry.getPlayerProperties(player);
      showInvToggle = data.hasInventoryExtended();// && !(gui instanceof GuiPlayerExtWorkbench);
      showCraftToggle = data.hasInventoryCrafting();// && !(gui instanceof GuiPlayerExtended);
      if (event.getButtonList() == null) {
        event.setButtonList(new ArrayList<GuiButton>());
      }
      if (showInvToggle) {
        event.getButtonList().add(new ButtonTabToggleInventory(gui, x, y));
      }
      if (showCraftToggle) {
        event.getButtonList().add(new ButtonTabToggleCrafting(gui, x - 17, y));//the 17 is width + 2
      }
    }
  }
}
