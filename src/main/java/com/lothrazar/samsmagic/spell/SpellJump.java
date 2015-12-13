package com.lothrazar.samsmagic.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import com.lothrazar.samsmagic.ItemRegistry;
import com.lothrazar.samsmagic.ModSpells;
import com.lothrazar.samsmagic.SpellRegistry; 

public class SpellJump extends BaseSpellExp implements ISpell
{ 
	private static int seconds = 20 * 5;//TODO : config? reference? cost?

	@Override
	public String getSpellName()
	{
		return "jump";
	}
	@Override
	public int getSpellID() 
	{
		return 5;
	}

	@Override
	public void cast(World world, EntityPlayer player, BlockPos pos)
	{
		ModSpells.addOrMergePotionEffect(player,new PotionEffect(Potion.jump.id,seconds,4));
	}

	@Override
	public int getExpCost()
	{
		return ModSpells.cfg.jump;
	}

	@Override
	public void onCastSuccess(World world, EntityPlayer player, BlockPos pos)
	{
		ModSpells.playSoundAt(player, "random.drink");

		super.onCastSuccess(world, player, pos);
	}
	@Override
	public ItemStack getIconDisplay()
	{
		return new ItemStack(ItemRegistry.spell_jump_dummy);
	}
}
