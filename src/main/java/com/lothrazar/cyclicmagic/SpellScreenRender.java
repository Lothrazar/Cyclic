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

	private static void drawSpellHeader(PlayerPowerups props, ISpell spellCurrent) {
		int dim = 12;

		int x = 12, y = 2;

		// draw header
		if (SpellCaster.isBlockedBySpellTImer(props) == false) {
			UtilTextureRender.drawTextureSquare(spellCurrent.getIconDisplayHeaderEnabled(), x, y, dim);
		}
		else {
			UtilTextureRender.drawTextureSquare(spellCurrent.getIconDisplayHeaderDisabled(), x, y, dim);
		}
	}

	private static void drawCurrentSpell(PlayerPowerups props, ISpell spellCurrent) {

		if (spellCurrent.getIconDisplay() != null) {

			UtilTextureRender.drawTextureSquare(spellCurrent.getIconDisplay(), xmain, ymain, spellSize);
		}
	}

	private static void drawPrevSpells(PlayerPowerups props, ISpell spellCurrent) {

		// PlayerPowerups props = PlayerPowerups.get(player);

		ISpell spellPrev = SpellRegistry.getSpellFromID(props.prevId(spellCurrent.getID()));
		// SpellRegistry.right(spellCurrent);
		if (spellPrev == null) {
			System.out.println("spellPrev is null, prevId from " + spellCurrent.getID() + " -> " + props.prevId(spellCurrent.getID()));
		}
		if (spellPrev != null) {
			int x = xmain + 6;
			int y = ymain + spellSize;
			int dim = spellSize / 2;
			UtilTextureRender.drawTextureSquare(spellPrev.getIconDisplay(), x, y, dim);

			ISpell sRightRight = SpellRegistry.getSpellFromID(props.prevId(spellPrev.getID()));
			// SpellRegistry.right(spellPrev);//
			// SpellRegistry.getSpellFromType(spellPrev.getSpellID().prev());

			if (sRightRight != null && sRightRight.getIconDisplay() != null) {
				x = xmain + 6 + 4;
				y = ymain + spellSize + 14;
				dim = spellSize / 2 - 2;
				UtilTextureRender.drawTextureSquare(sRightRight.getIconDisplay(), x, y, dim);

				ISpell another = SpellRegistry.getSpellFromID(props.prevId(sRightRight.getID()));
				// SpellRegistry.right(sRightRight);
				if (another != null) {
					x = xmain + 6 + 7;
					y = ymain + spellSize + 14 + 10;
					dim = spellSize / 2 - 4;
					UtilTextureRender.drawTextureSquare(another.getIconDisplay(), x, y, dim);
				}
			}
		}
	}

	private static void drawNextSpells(PlayerPowerups props, ISpell spellCurrent) {

		// PlayerPowerups props = PlayerPowerups.get(player);
		ISpell spellNext = SpellRegistry.getSpellFromID(props.nextId(spellCurrent.getID()));
		// SpellRegistry.left(spellCurrent);

		if (spellNext != null) {
			int x = xmain - 3;
			int y = ymain + spellSize;
			int dim = spellSize / 2;
			UtilTextureRender.drawTextureSquare(spellNext.getIconDisplay(), x, y, dim);

			ISpell sLeftLeft = SpellRegistry.getSpellFromID(props.nextId(spellNext.getID()));// SpellRegistry.left(spellNext);

			if (sLeftLeft != null && sLeftLeft.getIconDisplay() != null) {
				x = xmain - 3 - 1;
				y = ymain + spellSize + 14;
				dim = 16 / 2 - 2;
				UtilTextureRender.drawTextureSquare(sLeftLeft.getIconDisplay(), x, y, dim);

				ISpell another = SpellRegistry.getSpellFromID(props.nextId(sLeftLeft.getID()));// SpellRegistry.left(sLeftLeft);
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
		PlayerPowerups props = PlayerPowerups.get(player);

		drawSpellHeader(props, spellCurrent);

		drawCurrentSpell(props, spellCurrent);

		drawNextSpells(props, spellCurrent);

		drawPrevSpells(props, spellCurrent);
	}
}
