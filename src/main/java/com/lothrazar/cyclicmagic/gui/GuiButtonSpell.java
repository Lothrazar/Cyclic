package com.lothrazar.cyclicmagic.gui;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.net.MessageToggle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiButtonSpell extends GuiButton{

	public GuiButtonSpell(int buttonId, int x, int y, String buttonText) {
		super(buttonId, x, y, 20,20 ,buttonText);
	}
	
	@SideOnly(Side.CLIENT)
    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
    	boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    	
    	if(pressed)
    	{
    		//button id matches spell id
    		System.out.println("clicked button "+this.id);

			ModMain.network.sendToServer(new MessageToggle(this.id ));
    	}
    	
    	return pressed;
    }
}
