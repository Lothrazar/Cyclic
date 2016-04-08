package com.lothrazar.cyclicmagic.event;
  
import com.lothrazar.cyclicmagic.gui.button.GuiButtonCrafting;
import com.lothrazar.cyclicmagic.gui.button.GuiButtonDepositAll;
import com.lothrazar.cyclicmagic.gui.button.GuiButtonLootAll;
import com.lothrazar.cyclicmagic.gui.button.GuiButtonQuickStack;
import com.lothrazar.cyclicmagic.gui.button.GuiButtonRestock;
import com.lothrazar.cyclicmagic.registry.ExtraButtonRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.terrariabuttons.client.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventGuiAddButtons
{
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onGuiPostInit(InitGuiEvent.Post event)
	{
		GuiScreen gui = event.getGui();
		if(gui == null){return;}//probably doesn't ever happen

		
		//all containers by default
		//but with a blacklist in config
		String self = gui.getClass().getName();
		
		if(gui instanceof GuiContainer && 
			ExtraButtonRegistry.blacklistGuis.contains(self) == false &&
			gui instanceof net.minecraft.client.gui.inventory.GuiInventory == false
				)
		{
			int button_id = 256;
			
			//  config for different locations - left right bottom top
			int x=0,y=0,padding = 6, yDelta = 24, xDelta = 0;

			if(ExtraButtonRegistry.position.equalsIgnoreCase(ExtraButtonRegistry.posLeft))
			{
				x = padding;
				y = padding;
				//we are moving top to bottom, so 
				xDelta = 0;
				yDelta = Const.btnHeight + padding;
			}
			else if(ExtraButtonRegistry.position.equalsIgnoreCase(ExtraButtonRegistry.posRight))
			{
				x = Minecraft.getMinecraft().displayWidth/2 - Const.btnWidth - padding;//align to right side
				y = padding;
				//we are moving top to bottom, so 
				xDelta = 0;
				yDelta = Const.btnHeight + padding;
			}
			else if(ExtraButtonRegistry.position.equalsIgnoreCase(ExtraButtonRegistry.posBottom))
			{
				//test bottom
				x = padding;
				y = Minecraft.getMinecraft().displayHeight/2 - padding - Const.btnHeight;
				xDelta = Const.btnWidth + padding;
				yDelta = 0;
			}
 
			event.getButtonList().add(new GuiButtonLootAll(button_id++, x,y));

			x += xDelta;
			y += yDelta;

			event.getButtonList().add(new GuiButtonDepositAll(button_id++, x,y));

			x += xDelta;
			y += yDelta;

			event.getButtonList().add(new GuiButtonQuickStack(button_id++, x,y));

			x += xDelta;
			y += yDelta;

			event.getButtonList().add(new GuiButtonRestock(button_id++, x,y));
			

			x += xDelta;
			y += yDelta;

			event.getButtonList().add(new GuiButtonCrafting(button_id++, x,y));
		}
	}
}
