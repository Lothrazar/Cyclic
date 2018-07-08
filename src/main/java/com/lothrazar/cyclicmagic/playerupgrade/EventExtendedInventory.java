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
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.capability.IPlayerExtendedProperties;
import com.lothrazar.cyclicmagic.core.util.UtilPlayerInventoryFilestorage;
import com.lothrazar.cyclicmagic.playerupgrade.crafting.ButtonTabToggleCrafting;
import com.lothrazar.cyclicmagic.playerupgrade.crafting.GuiPlayerExtWorkbench;
import com.lothrazar.cyclicmagic.playerupgrade.storage.ButtonTabToggleInventory;
import com.lothrazar.cyclicmagic.playerupgrade.storage.GuiPlayerExtended;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.gui.inventory.GuiScreenHorseInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventExtendedInventory {

  public static boolean keepOnDeath;

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
      EntityPlayer player = ModCyclic.proxy.getClientPlayer();
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

  @SubscribeEvent
  public void playerLoggedInEvent(PlayerLoggedInEvent event) {
    Side side = FMLCommonHandler.instance().getEffectiveSide();
    if (side == Side.SERVER) {
      UtilPlayerInventoryFilestorage.playerEntityIds.add(event.player.getEntityId());
    }
  }

  @SubscribeEvent
  public void playerTick(PlayerEvent.LivingUpdateEvent event) {
    // player events
    if (event.getEntity() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) event.getEntity();
      if (!UtilPlayerInventoryFilestorage.playerEntityIds.isEmpty() && UtilPlayerInventoryFilestorage.playerEntityIds.contains(player.getEntityId())) {
        UtilPlayerInventoryFilestorage.syncItems(player);
        UtilPlayerInventoryFilestorage.playerEntityIds.remove(player.getEntityId());
      }
    }
  }

  @SubscribeEvent
  public void playerLoad(PlayerEvent.LoadFromFile event) {
    UtilPlayerInventoryFilestorage.playerSetupOnLoad(event);
  }

  @SubscribeEvent
  public void playerSave(PlayerEvent.SaveToFile event) {
    UtilPlayerInventoryFilestorage.savePlayerItems(event.getEntityPlayer(), event.getPlayerDirectory());
  }

  @SubscribeEvent
  public void playerDeath(PlayerDropsEvent event) {
    if (keepOnDeath == false) {
      World world = event.getEntityPlayer().getEntityWorld();
      if (!world.isRemote && world.getGameRules().getBoolean("keepInventory") == false) {
        //so config says dont keep it on death. AND the gamerule says dont keep as well
        UtilPlayerInventoryFilestorage.getPlayerInventory(event.getEntityPlayer()).dropItems(event.getDrops(), event.getEntityPlayer().getPosition());
      }
    }
  }
}
