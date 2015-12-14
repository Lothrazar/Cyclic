package com.lothrazar.cyclicmagic.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import com.lothrazar.cyclicmagic.ItemRegistry;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.PotionRegistry;
import com.lothrazar.cyclicmagic.util.UtilSound;

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
		UtilSound.playSoundAt(player, "random.drink");

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
