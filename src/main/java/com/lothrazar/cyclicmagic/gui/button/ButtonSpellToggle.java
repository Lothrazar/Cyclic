package com.lothrazar.cyclicmagic.gui.button;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.spell.ISpell;
import com.lothrazar.cyclicmagic.util.UtilString;
import com.lothrazar.cyclicmagic.util.UtilTextureRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ButtonSpellToggle extends GuiButton implements ITooltipButton{

	final EntityPlayer thePlayer;
	private ISpell spell;
	private static final int btnSize = 16;

	public ButtonSpellToggle(EntityPlayer player,int x, int y, ISpell s){

		super(s.getID(), x, y, btnSize, btnSize, "");
		spell = s;
		thePlayer = player;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY){

		
		boolean pressed = super.mousePressed(mc, mouseX, mouseY);
		return pressed;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY){

		// override this and draw the texture here, so the vanilla grey square
		// doesnt show up
		if(this.visible){
			// http://www.minecraftforge.net/forum/index.php?topic=19594.0

			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;

			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.blendFunc(770, 771);

			UtilTextureRender.drawTextureSquare(spell.getIconDisplay(), this.xPosition, this.yPosition, btnSize);

			this.mouseDragged(mc, mouseX, mouseY);
		}
	}

	public List<String> getTooltips(){

		//PlayerPowerups props = PlayerPowerups.get(thePlayer);
		List<String> tooltips = new ArrayList<String>();
		tooltips.add(TextFormatting.LIGHT_PURPLE + spell.getName());
		tooltips.add(TextFormatting.DARK_GRAY + I18n.translateToLocal("spell.meta.cost") + TextFormatting.BLUE + spell.getCost());

		// tooltips.add(EnumChatFormatting.LIGHT_PURPLE + spell.getInfo());

		tooltips.addAll(UtilString.splitIntoLine(spell.getInfo(), 28));

		//boolean unlocked = ItemCyclicWand.Spells.isSpellUnlocked(props.getPlayer().getHeldItem(), spell);

		//String ed = unlocked ? EnumChatFormatting.GREEN + StatCollector.translateToLocal("spell.meta.enabled") : EnumChatFormatting.RED + StatCollector.translateToLocal("spell.meta.disabled");
		//tooltips.add(ed);
		return tooltips;
	}
}
