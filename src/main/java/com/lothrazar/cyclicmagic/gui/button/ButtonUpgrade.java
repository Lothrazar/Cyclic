package com.lothrazar.cyclicmagic.gui.button;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import com.lothrazar.cyclicmagic.net.MessageUpgrade;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ButtonUpgrade extends GuiButton implements ITooltipButton{

	public ButtonUpgrade(EntityPlayer player, int buttonId, int x, int y, int width){

		super(buttonId, x, y, width, 20, "U");//StatCollector.translateToLocal("button.recharge")
		if(player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemCyclicWand){
			if(ItemCyclicWand.Energy.getMaximum(player.getHeldItem()) >= ItemCyclicWand.Energy.getMaximumLargest()){
				this.enabled = false;
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY){

		boolean pressed = super.mousePressed(mc, mouseX, mouseY);

		if(pressed){

			ModMain.network.sendToServer(new MessageUpgrade());
		}

		return pressed;
	}

	@Override
	public List<String> getTooltips(){

		List<String> tooltips = new ArrayList<String>();
		tooltips.add(StatCollector.translateToLocal("button.upgrade.tooltip"));
		return tooltips;
	}
}
