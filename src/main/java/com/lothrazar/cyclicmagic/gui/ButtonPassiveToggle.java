package com.lothrazar.cyclicmagic.gui;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import com.lothrazar.cyclicmagic.net.MessageTogglePassive;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ButtonPassiveToggle extends GuiButton{

	final EntityPlayer thePlayer;

	public ButtonPassiveToggle(EntityPlayer player, int buttonId, int x, int y, int width){

		super(buttonId, x, y, width, 20, "passivetoggle");
		thePlayer = player;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY){

		boolean pressed = super.mousePressed(mc, mouseX, mouseY);

		if(pressed){

			ModMain.network.sendToServer(new MessageTogglePassive());
		}

		return pressed;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY){

		this.displayString = StatCollector.translateToLocal("spell.passive"+ItemCyclicWand.Spells.getPassiveCurrentID(thePlayer.getHeldItem()));
		 
		super.drawButton(mc, mouseX, mouseY);
	}
}
