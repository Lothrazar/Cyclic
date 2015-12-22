package com.lothrazar.cyclicmagic.gui;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.PlayerPowerups;
import com.lothrazar.cyclicmagic.net.MessageToggle;
import com.lothrazar.cyclicmagic.spell.ISpell;
import com.lothrazar.cyclicmagic.util.UtilTextureRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiButtonSpell extends GuiButton{

	private ISpell spell;
	private static final int btnSize = 16;
	public GuiButtonSpell(int x, int y, ISpell s) {
		super(s.getID(), x, y, btnSize,btnSize ,"");
		spell = s;
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
	@Override
	  public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
        	//http://www.minecraftforge.net/forum/index.php?topic=19594.0
          //  FontRenderer fontrenderer = mc.fontRendererObj;
           // mc.getTextureManager().bindTexture(spell.getIconDisplay());
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            //int i = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
          //  this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 0,16,16);
            UtilTextureRender.drawTextureSquare(spell.getIconDisplay(),this.xPosition, this.yPosition,btnSize);
            //Gui.drawModalRectWithCustomSizedTexture(this.xPosition, this.yPosition, 0F, 0F,16,16,16,16);
          //  this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
            this.mouseDragged(mc, mouseX, mouseY);
           /* int j = 14737632;

            if (packedFGColour != 0)
            {
                j = packedFGColour;
            }
            else
            if (!this.enabled)
            {
                j = 10526880;
            }
            else if (this.hovered)
            {
                j = 16777120;
            }
*/
           // this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, j);
        }
    }
    
   // TODO: override this and draw the texture here, overrides vanilla suare
    
	public List<String> getTooltipForPlayer(PlayerPowerups props) {

		List<String> tooltips = new ArrayList<String>();
		tooltips.add(spell.getName()) ;
		tooltips.add(StatCollector.translateToLocal("cost.cooldown") + EnumChatFormatting.BLUE+ spell.getCastCooldown()) ;
		tooltips.add(StatCollector.translateToLocal("cost.durability") +EnumChatFormatting.BLUE+ spell.getCostDurability()) ;
		tooltips.add(StatCollector.translateToLocal("cost.exp") +EnumChatFormatting.BLUE+ spell.getCostExp()) ;
		String ed = (props.isSpellUnlocked(this.id)) ? EnumChatFormatting.GREEN+ StatCollector.translateToLocal("spell.enabled") :EnumChatFormatting.RED+ StatCollector.translateToLocal("spell.disabled");
		tooltips.add(ed);
		return tooltips;
	}
}
