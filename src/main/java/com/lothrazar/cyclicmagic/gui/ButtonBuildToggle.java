package com.lothrazar.cyclicmagic.gui;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import com.lothrazar.cyclicmagic.net.MessageToggleBuild;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ButtonBuildToggle extends GuiButton{

	final EntityPlayer thePlayer;
	public ButtonBuildToggle(EntityPlayer player,int buttonId, int x, int y) {
		super(buttonId, x, y,50,20,"");
		thePlayer = player;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		boolean pressed = super.mousePressed(mc, mouseX, mouseY);

		if (pressed) {
	

			ModMain.network.sendToServer(new MessageToggleBuild());
		}

		return pressed;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
		this.displayString = StatCollector.translateToLocal(ItemCyclicWand.BuildType.getBuildTypeName(thePlayer.getHeldItem()));
		
		super.drawButton(mc, mouseX, mouseY);
    }
}
