package com.lothrazar.samsmagic.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import com.lothrazar.samsmagic.ItemRegistry;
import com.lothrazar.samsmagic.ModMain;
import com.lothrazar.samsmagic.PotionRegistry;

public class SpellSlowfall extends BaseSpellExp implements ISpell
{ 
	private static int seconds = 20 * 10;
	
	@Override
	public String getSpellName()
	{
		return "slowfall";
	}
	@Override
	public int getSpellID() 
	{
		return 7;
	}

	@Override
	public void cast(World world, EntityPlayer player, BlockPos pos)
	{
		ModMain.addOrMergePotionEffect(player,new PotionEffect(PotionRegistry.slowfall.id,seconds,0));
	}

	@Override
	public void onCastSuccess(World world, EntityPlayer player, BlockPos pos)
	{
		ModMain.playSoundAt(player, "random.drink");

		super.onCastSuccess(world, player, pos);
	}
	
	@Override
	public int getExpCost()
	{
		return ModMain.cfg.slowfall;
	}
	
	@Override
	public ItemStack getIconDisplay()
	{ 
		return new ItemStack(ItemRegistry.spell_dummy_slowfall);
	}
}
