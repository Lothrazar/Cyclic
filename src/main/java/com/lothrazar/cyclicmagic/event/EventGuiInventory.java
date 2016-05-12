package com.lothrazar.cyclicmagic.event;

import com.lothrazar.cyclicmagic.gui.player.ButtonCrafting;
import com.lothrazar.cyclicmagic.gui.player.ButtonInventory;
import com.lothrazar.cyclicmagic.gui.player.GuiPlayerExtended;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.gui.inventory.GuiScreenHorseInventory;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventGuiInventory {

	@SideOnly(value = Side.CLIENT)
	@SubscribeEvent
	public void guiPostInit(GuiScreenEvent.InitGuiEvent.Post event) {
		GuiScreen gui = event.getGui();
		
		if (gui instanceof GuiInventory || gui instanceof GuiPlayerExtended
				|| gui instanceof GuiCrafting 
				|| gui instanceof GuiScreenHorseInventory) {
		

			// TODO: reflection helper?
			// gui left and top are private, so are the sizes
			// int guiLeft = ;//gui.guiLeft
			// int guiTop = ;//gui.guiTop

			int xSize = 176;
			int ySize = 166;
			int guiLeft = (gui.width - xSize) / 2;
			int guiTop = (gui.height - ySize) / 2;
			int x = 30 + guiLeft;
			int y = guiTop + 2;
			event.getButtonList().add(new ButtonInventory(gui, x, y));
		

			event.getButtonList().add(new ButtonCrafting(gui, x - 12, y));
		}
	}
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
