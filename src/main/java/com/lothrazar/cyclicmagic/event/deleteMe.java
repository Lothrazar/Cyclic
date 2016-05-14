package com.lothrazar.cyclicmagic.event;

import com.lothrazar.cyclicmagic.gui.player.ButtonTabToggleCrafting;
import com.lothrazar.cyclicmagic.gui.player.ButtonTabToggleInventory;
import com.lothrazar.cyclicmagic.gui.player.GuiPlayerExtended;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.gui.inventory.GuiScreenHorseInventory;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class deleteMe {

	/*
	@SideOnly(value = Side.CLIENT)
	@SubscribeEvent
	public void guiPostAction(GuiScreenEvent.ActionPerformedEvent.Post event) {
System.out.println("version with 51 cancelled");
		
		if (event.getGui() instanceof GuiInventory) {
			if (event.getButton().id == buttonId) {
				ModMain.network.sendToServer(new PacketOpenExtendedInventory(event.getGui().mc.thePlayer));
			}
		}

		if (event.getGui() instanceof GuiPlayerExtended) {
			if (event.getButton().id == buttonId) {
				event.getGui().mc.displayGuiScreen(new GuiInventory(event.getGui().mc.thePlayer));
				ModMain.network.sendToServer(new PacketOpenNormalInventory(event.getGui().mc.thePlayer));
			}
		}
	}*/
}
