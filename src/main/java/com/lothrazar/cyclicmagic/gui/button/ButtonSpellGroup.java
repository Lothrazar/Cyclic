package com.lothrazar.cyclicmagic.gui.button;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.net.MessageToggleSpellGroup;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ButtonSpellGroup extends GuiButton{

	private String group;

	public ButtonSpellGroup(String g,int id,int x, int y, int width){

		super(id, x, y, width, 20 , StatCollector.translateToLocal("button.group."+g.toLowerCase()));
		group = g;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY){

		boolean pressed = super.mousePressed(mc, mouseX, mouseY);

		if(pressed){
	
			ModMain.network.sendToServer(new MessageToggleSpellGroup(this.group));
		}

		return pressed;
	}
}
