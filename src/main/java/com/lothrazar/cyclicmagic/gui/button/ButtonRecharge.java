package com.lothrazar.cyclicmagic.gui.button;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.net.MessageRecharge;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ButtonRecharge extends GuiButton implements ITooltipButton{

	public ButtonRecharge(int buttonId, int x, int y, int width){

		super(buttonId, x, y, width, 20, StatCollector.translateToLocal("button.recharge"));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY){

		boolean pressed = super.mousePressed(mc, mouseX, mouseY);

		if(pressed){

			ModMain.network.sendToServer(new MessageRecharge());
		}

		return pressed;
	}

	@Override
	public List<String> getTooltips(){

		List<String> tooltips = new ArrayList<String>();
		tooltips.add(StatCollector.translateToLocal("button.recharge.tooltip"));
		return tooltips;
	}
}
