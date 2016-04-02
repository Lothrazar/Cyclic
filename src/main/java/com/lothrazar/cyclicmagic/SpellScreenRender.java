package com.lothrazar.cyclicmagic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import com.lothrazar.cyclicmagic.spell.ISpell;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilTextureRender;

public class SpellScreenRender{

	private static final int xoffset = 30;
	private static int xmain;
	private static int xHud;
	private static int yHud;
	private static final int ymain = 14;
	private static final int spellSize = 16;
	

	private static final int manaCtrWidth = 8;
	private static final int manaCtrHeight = 92;
	private static final int manaWidth = manaCtrWidth - 2;
	//private static final int manaHeight = manaCtrHeight - 2;
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

		drawSpellHeader(player, spellCurrent);

		drawCurrentSpell(player, spellCurrent);

		drawNextSpells(player, spellCurrent);

		drawPrevSpells(player, spellCurrent);

		if(player.capabilities.isCreativeMode == false){
			drawManabar(player);
		}
	}

	private void drawSpellHeader(EntityPlayer player, ISpell spellCurrent){

		int dim = spellSize - 4, x = xmain + 1, y = ymain - 12;

		if(ItemCyclicWand.Timer.isBlockedBySpellTimer(SpellCaster.getPlayerWandIfHeld(player)) == false){
			UtilTextureRender.drawTextureSquare(spellCurrent.getIconDisplayHeaderEnabled(), x, y, dim);
		}
		else{
			UtilTextureRender.drawTextureSquare(spellCurrent.getIconDisplayHeaderDisabled(), x, y, dim);
		}
	}

	private void drawManabar(EntityPlayer player){
		ItemStack wand = SpellCaster.getPlayerWandIfHeld(player);

		double MAX = ItemCyclicWand.Energy.getMaximum(wand);
		double largest = ItemCyclicWand.Energy.getMaximumLargest();
		
		double ratio = MAX / largest;
		
		double hFull = manaCtrHeight * ratio;
		
		//draw the outer container
		UtilTextureRender.drawTextureSimple(mana_container, xHud,yHud, manaCtrWidth, MathHelper.floor_double(hFull));

		double current = ItemCyclicWand.Energy.getCurrent(wand);
		double manaPercent = current / MAX;//not using MAX anymore!!!

		double hEmpty = (hFull - 2) * manaPercent;
		
		//draw the filling inside
		UtilTextureRender.drawTextureSimple(mana, xHud+1,yHud+1, manaWidth, MathHelper.floor_double(hEmpty));
	}

	private void drawCurrentSpell(EntityPlayer player, ISpell spellCurrent){

		if(spellCurrent.getIconDisplay() != null){

			UtilTextureRender.drawTextureSquare(spellCurrent.getIconDisplay(), xmain, ymain, spellSize);
		}
	}

	private void drawPrevSpells(EntityPlayer player, ISpell spellCurrent){

		ItemStack wand = SpellCaster.getPlayerWandIfHeld(player);

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

		ItemStack wand = SpellCaster.getPlayerWandIfHeld(player);

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
