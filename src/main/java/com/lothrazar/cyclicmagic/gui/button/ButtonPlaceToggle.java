package com.lothrazar.cyclicmagic.gui.button;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import com.lothrazar.cyclicmagic.net.MessageTogglePlace;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ButtonPlaceToggle extends GuiButton implements ITooltipButton{

	final EntityPlayer thePlayer;

	public ButtonPlaceToggle(EntityPlayer player, int buttonId, int x, int y, int width){

		super(buttonId, x, y, width, 20, "");
		thePlayer = player;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY){

		boolean pressed = super.mousePressed(mc, mouseX, mouseY);

		if(pressed){

			ModMain.network.sendToServer(new MessageTogglePlace());
		}

		return pressed;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY){

		this.displayString = StatCollector.translateToLocal(ItemCyclicWand.PlaceType.getName(thePlayer.getHeldItem()));

		super.drawButton(mc, mouseX, mouseY);
	}

	@Override
	public List<String> getTooltips(){

		List<String> tooltips = new ArrayList<String>();
		//tooltips.add(StatCollector.translateToLocal("button.build.tooltip"));
		String key = ItemCyclicWand.PlaceType.get(thePlayer.getHeldItem())+".tooltip";
		tooltips.add(StatCollector.translateToLocal(key));
		tooltips.add(StatCollector.translateToLocal("button.build.meta"));
		
		return tooltips;
	}
}
