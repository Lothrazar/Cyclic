package com.lothrazar.cyclicmagic.event;

import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.gui.button.ButtonTerrariaDepositAll;
import com.lothrazar.cyclicmagic.gui.button.ButtonTerrariaLootAll;
import com.lothrazar.cyclicmagic.gui.button.ButtonTerrariaQuickStack;
import com.lothrazar.cyclicmagic.gui.button.ButtonTerrariaRestock;
import com.lothrazar.cyclicmagic.gui.player.GuiPlayerExtended;
import com.lothrazar.cyclicmagic.registry.ExtraButtonRegistry;
import com.lothrazar.cyclicmagic.util.Const;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventGuiTerrariaButtons implements IHasConfig{

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onGuiPostInit(InitGuiEvent.Post event) {
		GuiScreen gui = event.getGui();
		if (gui == null) { return; } // probably doesn't ever happen

		// all containers by default
		// but with a blacklist in config
		String self = gui.getClass().getName();
		int button_id = 256;

		// config for different locations - left right bottom top
		int x = 0, y = 0, padding = 6, yDelta = 24, xDelta = 0;

		// not GuiContainerCreative
		if (event.getGui() instanceof GuiInventory) {

		//	event.getButtonList().add(new GuiButtonCrafting( x, y));

		}
		else if (gui instanceof GuiContainer && 
				!(gui instanceof GuiCrafting ) &&	!(gui instanceof GuiPlayerExtended ) &&
				ExtraButtonRegistry.blacklistGuis.contains(self) == false && gui instanceof net.minecraft.client.gui.inventory.GuiInventory == false) {

			// align to different area depending on config
			if (ExtraButtonRegistry.position.equalsIgnoreCase(ExtraButtonRegistry.posLeft)) {
				x = padding;
				y = padding;
				// we are moving top to bottom, so
				xDelta = 0;
				yDelta = Const.btnHeight + padding;
			}
			else if (ExtraButtonRegistry.position.equalsIgnoreCase(ExtraButtonRegistry.posRight)) {

				x = Minecraft.getMinecraft().displayWidth / 2 - Const.btnWidth - padding;
				y = padding;
				// we are moving top to bottom, so
				xDelta = 0;
				yDelta = Const.btnHeight + padding;
			}
			else if (ExtraButtonRegistry.position.equalsIgnoreCase(ExtraButtonRegistry.posBottom)) {
				// test bottom
				x = padding;
				y = Minecraft.getMinecraft().displayHeight / 2 - padding - Const.btnHeight;
				xDelta = Const.btnWidth + padding;
				yDelta = 0;
			}

			event.getButtonList().add(new ButtonTerrariaLootAll(button_id++, x, y));

			x += xDelta;
			y += yDelta;

			event.getButtonList().add(new ButtonTerrariaDepositAll(button_id++, x, y));

			x += xDelta;
			y += yDelta;

			event.getButtonList().add(new ButtonTerrariaQuickStack(button_id++, x, y));

			x += xDelta;
			y += yDelta;

			event.getButtonList().add(new ButtonTerrariaRestock(button_id++, x, y));

		}
	}

	@Override
	public void syncConfig(Configuration config) {
		// TODO Auto-generated method stub
		
	}
}
