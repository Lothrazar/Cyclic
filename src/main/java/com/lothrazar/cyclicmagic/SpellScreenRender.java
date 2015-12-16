package com.lothrazar.cyclicmagic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.lothrazar.cyclicmagic.spell.ISpell;
import com.lothrazar.cyclicmagic.util.UtilTextureRender;

public class SpellScreenRender {

	private static final int ymain = 14;
	private static final int xmain = 10;
	private static final int spellSize = 16;
	
	private static void drawSpellHeader(EntityPlayerSP player, ISpell spellCurrent) {
		int dim = 12;

		int x = 12, y = 2;

		// draw header
		if (SpellCaster.canPlayerCastAnything(player)) {
			UtilTextureRender.drawTextureSquare(spellCurrent.getIconDisplayHeaderEnabled(), x, y, dim);
		} else {
			UtilTextureRender.drawTextureSquare(spellCurrent.getIconDisplayHeaderDisabled(), x, y, dim);
		}
	}

	private static void drawCurrentSpell(EntityPlayerSP player, ISpell spellCurrent) {

		if (spellCurrent.getIconDisplay() != null) {

			UtilTextureRender.drawTextureSquare(spellCurrent.getIconDisplay(), xmain, ymain, spellSize);
		}
	}

	private static void drawPrevSpells(EntityPlayerSP player, ISpell spellCurrent) {

		ISpell spellPrev = spellCurrent.right();
		if (spellPrev != null)// && spellPrev.getIconDisplay() != null
		{
			int x = xmain + 6;
			int y = ymain + spellSize;
			int dim = spellSize / 2;
			UtilTextureRender.drawTextureSquare(spellPrev.getIconDisplay(), x, y, dim);

			ISpell sRightRight = spellPrev.right();// SpellRegistry.getSpellFromType(spellPrev.getSpellID().prev());

			if (sRightRight != null && sRightRight.getIconDisplay() != null) {
				x = xmain + 6 + 4;
				y = ymain + spellSize + 14;
				dim = spellSize / 2 - 2;
				UtilTextureRender.drawTextureSquare(sRightRight.getIconDisplay(), x, y, dim);

				ISpell another = sRightRight.right();
				if (another != null) {
					x = xmain + 6 + 7;
					y = ymain + spellSize + 14 + 10;
					dim = spellSize / 2 - 4;
					UtilTextureRender.drawTextureSquare(another.getIconDisplay(), x, y, dim);
				}
			}
		}

	}

	private static void drawNextSpells(EntityPlayerSP player, ISpell spellCurrent) {
		ISpell spellNext = spellCurrent.left();

		if (spellNext != null) {
			int x = xmain - 3;
			int y = ymain + spellSize;
			int dim = spellSize / 2;
			UtilTextureRender.drawTextureSquare(spellNext.getIconDisplay(), x, y, dim);

			ISpell sLeftLeft = spellNext.left();

			if (sLeftLeft != null && sLeftLeft.getIconDisplay() != null) {
				x = xmain - 3 - 1;
				y = ymain + spellSize + 14;
				dim = 16 / 2 - 2;
				UtilTextureRender.drawTextureSquare(sLeftLeft.getIconDisplay(), x, y, dim);

				ISpell another = sLeftLeft.left();
				if (another != null) {
					x = xmain - 3 - 3;
					y = ymain + spellSize + 14 + 10;
					dim = spellSize / 2 - 4;
					UtilTextureRender.drawTextureSquare(another.getIconDisplay(), x, y, dim);
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public static void drawSpellWheel() {
		
		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

		ISpell spellCurrent = SpellCaster.getPlayerCurrentISpell(player);

		drawSpellHeader(player, spellCurrent);

		drawCurrentSpell(player, spellCurrent);

		drawNextSpells(player, spellCurrent);

		drawPrevSpells(player, spellCurrent);
	}
}
