package com.lothrazar.cyclicmagic.spell;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import com.lothrazar.cyclicmagic.PotionRegistry;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;

public class SpellPotion extends BaseSpell implements ISpell{

	private int potionId;
	private int potionDuration;
	private int potionAmp;

	public SpellPotion(int id, String name, int pcost){

		super.init(id, name);
		this.cost = pcost;
		this.cooldown = 10;
	}

	public SpellPotion setPotion(int id, int effectDuration, int effectAmplifier){

		potionId = id;
		potionDuration = effectDuration;
		potionAmp = effectAmplifier;
		return this;
	}

	@Override
	public boolean cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side){

		PotionRegistry.addOrMergePotionEffect(player, new PotionEffect(potionId, potionDuration, potionAmp));

		this.spawnParticle(world, player, player.getPosition());
		this.playSound(world, null, player.getPosition());

		return true;
	}

	@Override
	public void spawnParticle(World world, EntityPlayer player, BlockPos pos){

		UtilParticle.spawnParticle(world, EnumParticleTypes.CRIT_MAGIC, pos);

	}

	@Override
	public void playSound(World world, Block block, BlockPos pos){

		UtilSound.playSound(world, pos, UtilSound.drink);
	}
}
