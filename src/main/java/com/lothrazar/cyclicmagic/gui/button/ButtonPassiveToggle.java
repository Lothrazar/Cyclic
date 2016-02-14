package com.lothrazar.cyclicmagic.gui.button;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import com.lothrazar.cyclicmagic.net.MessageTogglePassive;
import com.lothrazar.cyclicmagic.spell.passive.IPassiveSpell;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ButtonPassiveToggle extends GuiButton implements ITooltipButton{

	final EntityPlayer thePlayer;

	public ButtonPassiveToggle(EntityPlayer player, int buttonId, int x, int y, int width){

		super(buttonId, x, y, width, 20, "");
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

		IPassiveSpell ps = ItemCyclicWand.Spells.getPassiveCurrent(thePlayer.getHeldItem());

		if(ps != null){
			this.displayString = ps.getName();
		}
		super.drawButton(mc, mouseX, mouseY);
	}

	public List<String> getTooltips(){

		List<String> tooltips = new ArrayList<String>();
		IPassiveSpell ps = ItemCyclicWand.Spells.getPassiveCurrent(thePlayer.getHeldItem());

		if(ps != null){
			tooltips.add(ps.getInfo());
		}

		return tooltips;
	}
}
