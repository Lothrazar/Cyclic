package com.lothrazar.cyclicmagic.inventory;

import com.lothrazar.cyclicmagic.ModMain;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventGuiInventory {

	final int buttonId = 55;

	@SideOnly(value = Side.CLIENT)
	@SubscribeEvent
	public void guiPostInit(GuiScreenEvent.InitGuiEvent.Post event) {

		if (event.getGui() instanceof GuiInventory || event.getGui() instanceof GuiPlayerExtended) {
			GuiContainer gui = (GuiContainer) event.getGui();

			// TODO: reflection helper?
			// gui left and top are private, so are the sizes
			// int guiLeft = ;//gui.guiLeft
			// int guiTop = ;//gui.guiTop
			int x = 30;
			int y = 2;
			int w = 10;
			int h = 10;
			int xSize = 176;
			int ySize = 166;
			int guiLeft = (gui.width - xSize) / 2;
			int guiTop = (gui.height - ySize) / 2;
			event.getButtonList().add(new GuiButtonInventory(buttonId, guiLeft, guiTop, x, y, w, h, I18n.format((event.getGui() instanceof GuiInventory) ? "button.inventory" : "button.normal", new Object[0])));
		}

	}

	@SideOnly(value = Side.CLIENT)
	@SubscribeEvent
	public void guiPostAction(GuiScreenEvent.ActionPerformedEvent.Post event) {

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
	}
}
