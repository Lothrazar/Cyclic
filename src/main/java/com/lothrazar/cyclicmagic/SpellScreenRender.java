package com.lothrazar.cyclicmagic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.lothrazar.cyclicmagic.spell.ISpell;
import com.lothrazar.cyclicmagic.util.UtilTextureRender;

public class SpellScreenRender {

	private static final int xmain = 30;
	private static final int ymain = 14;
	private static final int spellSize = 16;
	private static final int manaWidth = 8;
	private static final int manaHeight = 90;
	private static final ResourceLocation manabar = new ResourceLocation(Const.MODID, "textures/spells/manabar.png");
	
	private void drawSpellHeader(PlayerPowerups props, ISpell spellCurrent) {
		int dim = spellSize - 4, x = xmain+1, y = ymain-12;
  
		if (SpellCaster.isBlockedBySpellTImer(props) == false) {
			UtilTextureRender.drawTextureSquare(spellCurrent.getIconDisplayHeaderEnabled(), x,y, dim);
		}
		else {
			UtilTextureRender.drawTextureSquare(spellCurrent.getIconDisplayHeaderDisabled(), x,y, dim);
		}
	}

	private void drawCurrentSpell(PlayerPowerups props, ISpell spellCurrent) {

		if (spellCurrent.getIconDisplay() != null) {

			UtilTextureRender.drawTextureSquare(spellCurrent.getIconDisplay(), xmain, ymain, spellSize);
		}
	}

	private void drawPrevSpells(PlayerPowerups props, ISpell spellCurrent) {

		ISpell prev = SpellRegistry.getSpellFromID(props.prevId(spellCurrent.getID()));
  
		if (prev != null) {
			int x = xmain + 9;
			int y = ymain + spellSize;
			int dim = spellSize / 2;
			UtilTextureRender.drawTextureSquare(prev.getIconDisplay(), x, y, dim);

			prev = SpellRegistry.getSpellFromID(props.prevId(prev.getID()));
		
			if (prev != null ) {
				x += 5;
				y += 14;
				dim -= 2;
				UtilTextureRender.drawTextureSquare(prev.getIconDisplay(), x, y, dim);

				prev = SpellRegistry.getSpellFromID(props.prevId(prev.getID()));
	
				if (prev != null) {
					x += 3;
					y += 10;
					dim -= 2;
					UtilTextureRender.drawTextureSquare(prev.getIconDisplay(), x, y, dim);
					
					prev = SpellRegistry.getSpellFromID(props.prevId(prev.getID()));
					
					if (prev != null) {
						x += 2;
						y += 10;
						dim -= 1;
						UtilTextureRender.drawTextureSquare(prev.getIconDisplay(), x, y, dim);
					}
				}
			}
		}
	}

	private void drawNextSpells(PlayerPowerups props, ISpell spellCurrent) {

		ISpell next = SpellRegistry.getSpellFromID(props.nextId(spellCurrent.getID()));

		if (next != null) {
			int x = xmain - 5;
			int y = ymain + spellSize;
			int dim = spellSize / 2;
			UtilTextureRender.drawTextureSquare(next.getIconDisplay(), x, y, dim);

			next = SpellRegistry.getSpellFromID(props.nextId(next.getID())); 

			if (next != null ) {
				x -= 2;
				y += 14;
				dim -= 2;
				UtilTextureRender.drawTextureSquare(next.getIconDisplay(), x, y, dim);

				next = SpellRegistry.getSpellFromID(props.nextId(next.getID())); 
				if (next != null) {
					x -= 2;
					y += 10;
					dim -= 2;
					UtilTextureRender.drawTextureSquare(next.getIconDisplay(), x, y, dim);
					
					next = SpellRegistry.getSpellFromID(props.nextId(next.getID())); 
					if (next != null) {
						x -= 2;
						y += 10;
						dim -= 1;
						UtilTextureRender.drawTextureSquare(next.getIconDisplay(), x, y, dim);
					}
				}
			}
		}
	}
	
	private void drawManabar(PlayerPowerups props){
		
		int x = xmain-20, y = ymain-12 , w=manaWidth, h=manaHeight;
		  
		UtilTextureRender.drawTextureSimple(manabar, x,y, w,h);
	}

	@SideOnly(Side.CLIENT)
	public void drawSpellWheel() {

		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

		ISpell spellCurrent = SpellCaster.getPlayerCurrentISpell(player);
		PlayerPowerups props = PlayerPowerups.get(player);

		drawSpellHeader(props, spellCurrent);

		drawCurrentSpell(props, spellCurrent);

		drawNextSpells(props, spellCurrent);

		drawPrevSpells(props, spellCurrent);
		
		drawManabar(props);
	}
}
