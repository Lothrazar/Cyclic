package com.lothrazar.cyclicmagic.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiButtonClose extends GuiButton{
 
	public GuiButtonClose(int id,int x, int y) {
		super(id, x, y, 30,20 ,StatCollector.translateToLocal("button.close"));
	}
	
	@SideOnly(Side.CLIENT)
    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
    	boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    	
    	if(pressed)
    	{
    		mc.thePlayer.closeScreen();
    	}
    	
    	return pressed;
    }
}
