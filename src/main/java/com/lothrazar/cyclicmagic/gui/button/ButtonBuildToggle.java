package com.lothrazar.cyclicmagic.gui.button;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.SpellCaster;
import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import com.lothrazar.cyclicmagic.net.MessageToggleBuild;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ButtonBuildToggle extends GuiButton implements ITooltipButton{

	final EntityPlayer thePlayer;

	public ButtonBuildToggle(EntityPlayer player, int buttonId, int x, int y, int width){

		super(buttonId, x, y, width, 20, "");
		thePlayer = player;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY){

		boolean pressed = super.mousePressed(mc, mouseX, mouseY);

		if(pressed){

			ModMain.network.sendToServer(new MessageToggleBuild());
		}

		return pressed;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY){

		ItemStack wand = SpellCaster.getPlayerWandIfHeld(thePlayer);
		this.displayString = I18n.translateToLocal(ItemCyclicWand.BuildType.getName(wand));

		super.drawButton(mc, mouseX, mouseY);
	}

	@Override
	public List<String> getTooltips(){

		List<String> tooltips = new ArrayList<String>();
		tooltips.add(I18n.translateToLocal("button.build.tooltip"));
		//String key = ItemCyclicWand.BuildType.getName(thePlayer.getHeldItem())+".tooltip";
		//tooltips.add(I18n.translateToLocal(key));
		tooltips.add(I18n.translateToLocal("button.build.meta"));
		
		return tooltips;
	}
}
