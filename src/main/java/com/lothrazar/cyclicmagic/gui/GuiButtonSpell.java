package com.lothrazar.cyclicmagic.gui;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.PlayerPowerups;
import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import com.lothrazar.cyclicmagic.net.MessageToggle;
import com.lothrazar.cyclicmagic.spell.ISpell;
import com.lothrazar.cyclicmagic.util.UtilTextureRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiButtonSpell extends GuiButton {

	private ISpell spell;
	private static final int btnSize = 16;

	public GuiButtonSpell(int x, int y, ISpell s) {
		super(s.getID(), x, y, btnSize, btnSize, "");
		spell = s;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		boolean pressed = super.mousePressed(mc, mouseX, mouseY);

		if (pressed) {
			// button id matches spell id

			ModMain.network.sendToServer(new MessageToggle(this.id));
		}

		return pressed;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		// override this and draw the texture here, so the vanilla grey square
		// doesnt show up
		if (this.visible) {
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

	public List<String> getTooltipForPlayer(PlayerPowerups props) {

		List<String> tooltips = new ArrayList<String>();
		tooltips.add(spell.getName());
		tooltips.add(StatCollector.translateToLocal("spell.cost") + EnumChatFormatting.BLUE + spell.getCost());
		
		boolean unlocked = ItemCyclicWand.Spells.isSpellUnlocked(props.getPlayer().getHeldItem(), spell);
		
		String ed = unlocked ? EnumChatFormatting.GREEN + StatCollector.translateToLocal("spell.enabled") : EnumChatFormatting.RED + StatCollector.translateToLocal("spell.disabled");
		tooltips.add(ed);
		return tooltips;
	}
}
