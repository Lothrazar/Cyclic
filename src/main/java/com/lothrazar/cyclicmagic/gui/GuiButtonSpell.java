package com.lothrazar.cyclicmagic.gui;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.net.MessageToggle;
import com.lothrazar.cyclicmagic.spell.ISpell;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiButtonSpell extends GuiButton{

	private List<String> tooltips = null;
	public GuiButtonSpell(int x, int y, ISpell spell) {
		super(spell.getID(), x, y, 20,20 ,"");
 
		tooltips = new ArrayList<String>();
		tooltips.add(spell.getName()) ;
		tooltips.add(StatCollector.translateToLocal("cost.cooldown") + spell.getCastCooldown()) ;
		tooltips.add(StatCollector.translateToLocal("cost.durability") + spell.getCostDurability()) ;
		tooltips.add(StatCollector.translateToLocal("cost.exp") + spell.getCostExp()) ;
	}
	
	@SideOnly(Side.CLIENT)
    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
    	boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    	
    	if(pressed)
    	{
    		//button id matches spell id

    		//TODO: sync client/server
    		//SpellCaster.toggleUnlock(mc.thePlayer, this.id);//do client and server
			ModMain.network.sendToServer(new MessageToggle(this.id ));
    	}
    	
    	return pressed;
    }
	
	public List<String> getTooltip() {
		return tooltips;
	}
}
