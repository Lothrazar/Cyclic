package com.lothrazar.cyclicmagic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import com.lothrazar.cyclicmagic.spell.ISpell;
import com.lothrazar.cyclicmagic.util.UtilTextureRender;

public class SpellScreenRender{

	private static final int xoffset = 30;
	private static int xmain;
	private static int xHud;
	private static int yHud;
	private static final int ymain = 14;
	private static final int spellSize = 16;
	private static final int manaWidth = 6;
	private static final int manaHeight = 90;
	

	private static final int manaCtrWidth = 8;
	private static final int manaCtrHeight = 92;
	private static final ResourceLocation mana = new ResourceLocation(Const.MODID, "textures/hud/manabar.png");
	private static final ResourceLocation mana_container = new ResourceLocation(Const.MODID, "textures/hud/manabar_empty.png");

	@SideOnly(Side.CLIENT)
	public void drawSpellWheel(){

		if(ModMain.cfg.renderOnLeft){
			xmain = xoffset;
		}
		else{
			ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
			// NOT Minecraft.getMinecraft().displayWidth
			xmain = res.getScaledWidth() - xoffset;
		}
		xHud = xmain - 20;
		yHud = ymain - 12;

		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

		ISpell spellCurrent = SpellRegistry.caster.getPlayerCurrentISpell(player);

		drawSpellHeader(PlayerPowerups.get(player), spellCurrent);

		drawCurrentSpell(player, spellCurrent);

		drawNextSpells(player, spellCurrent);

		drawPrevSpells(player, spellCurrent);

		if(player.capabilities.isCreativeMode == false){
			drawManabar(player);
		}
	}

	private void drawSpellHeader(PlayerPowerups props, ISpell spellCurrent){

		int dim = spellSize - 4, x = xmain + 1, y = ymain - 12;

		if(SpellRegistry.caster.isBlockedBySpellTImer(props) == false){
			UtilTextureRender.drawTextureSquare(spellCurrent.getIconDisplayHeaderEnabled(), x, y, dim);
		}
		else{
			UtilTextureRender.drawTextureSquare(spellCurrent.getIconDisplayHeaderDisabled(), x, y, dim);
		}
	}

	private void drawManabar(EntityPlayer player){
 
		UtilTextureRender.drawTextureSimple(mana_container, xHud,yHud, manaCtrWidth, manaCtrHeight);

		float MAX = ItemCyclicWand.Energy.getMaximum(player.getHeldItem());
		float current = ItemCyclicWand.Energy.getCurrent(player.getHeldItem());
		float manaPercent = current / MAX;

		double h = manaHeight * manaPercent;

		UtilTextureRender.drawTextureSimple(mana, xHud+1,yHud+1, manaWidth, MathHelper.floor_double(h));
	}

	private void drawCurrentSpell(EntityPlayer player, ISpell spellCurrent){

		if(spellCurrent.getIconDisplay() != null){

			UtilTextureRender.drawTextureSquare(spellCurrent.getIconDisplay(), xmain, ymain, spellSize);
		}
	}

	private void drawPrevSpells(EntityPlayer player, ISpell spellCurrent){

		ItemStack wand = player.getHeldItem();

		ISpell prev = SpellRegistry.getSpellFromID(ItemCyclicWand.Spells.prevId(wand, spellCurrent.getID()));

		if(prev != null){
			int x = xmain + 9;
			int y = ymain + spellSize;
			int dim = spellSize / 2;
			UtilTextureRender.drawTextureSquare(prev.getIconDisplay(), x, y, dim);

			prev = SpellRegistry.getSpellFromID(ItemCyclicWand.Spells.prevId(wand, prev.getID()));

			if(prev != null){
				x += 5;
				y += 14;
				dim -= 2;
				UtilTextureRender.drawTextureSquare(prev.getIconDisplay(), x, y, dim);

				prev = SpellRegistry.getSpellFromID(ItemCyclicWand.Spells.prevId(wand, prev.getID()));

				if(prev != null){
					x += 3;
					y += 10;
					dim -= 2;
					UtilTextureRender.drawTextureSquare(prev.getIconDisplay(), x, y, dim);

					prev = SpellRegistry.getSpellFromID(ItemCyclicWand.Spells.prevId(wand, prev.getID()));

					if(prev != null){
						x += 2;
						y += 10;
						dim -= 1;
						UtilTextureRender.drawTextureSquare(prev.getIconDisplay(), x, y, dim);
					}
				}
			}
		}
	}

	private void drawNextSpells(EntityPlayer player, ISpell spellCurrent){

		ItemStack wand = player.getHeldItem();

		ISpell next = SpellRegistry.getSpellFromID(ItemCyclicWand.Spells.nextId(wand, spellCurrent.getID()));

		if(next != null){
			int x = xmain - 5;
			int y = ymain + spellSize;
			int dim = spellSize / 2;
			UtilTextureRender.drawTextureSquare(next.getIconDisplay(), x, y, dim);

			next = SpellRegistry.getSpellFromID(ItemCyclicWand.Spells.nextId(wand, next.getID()));

			if(next != null){
				x -= 2;
				y += 14;
				dim -= 2;
				UtilTextureRender.drawTextureSquare(next.getIconDisplay(), x, y, dim);

				next = SpellRegistry.getSpellFromID(ItemCyclicWand.Spells.nextId(wand, next.getID()));
				if(next != null){
					x -= 2;
					y += 10;
					dim -= 2;
					UtilTextureRender.drawTextureSquare(next.getIconDisplay(), x, y, dim);

					next = SpellRegistry.getSpellFromID(ItemCyclicWand.Spells.nextId(wand, next.getID()));
					if(next != null){
						x -= 2;
						y += 10;
						dim -= 1;
						UtilTextureRender.drawTextureSquare(next.getIconDisplay(), x, y, dim);
					}
				}
			}
		}
	}
}
