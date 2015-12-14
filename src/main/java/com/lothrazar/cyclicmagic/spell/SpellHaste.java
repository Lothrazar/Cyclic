 package com.lothrazar.cyclicmagic.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import com.lothrazar.cyclicmagic.Const;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.PotionRegistry;
import com.lothrazar.cyclicmagic.util.UtilSound;

public class SpellHaste extends BaseSpellExp implements ISpell
{
	private static int seconds = 20* 10; 
 
	@Override
	public void cast(World world, EntityPlayer player, BlockPos pos)
	{ 
		ModMain.addOrMergePotionEffect(player,new PotionEffect(Potion.digSpeed.id,seconds,PotionRegistry.II));
	}

	@Override
	public void onCastSuccess(World world, EntityPlayer player, BlockPos pos)
	{
		UtilSound.playSoundAt(player, "random.drink");

		super.onCastSuccess(world, player, pos);
	}
	
	private final ResourceLocation icon = new ResourceLocation(Const.MODID,"textures/spells/spell_haste_dummy.png");
	
	@Override
	public ResourceLocation getIconDisplay()
	{
		return icon;
	}
}
