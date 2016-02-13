package com.lothrazar.cyclicmagic.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import com.lothrazar.cyclicmagic.PotionRegistry;
import com.lothrazar.cyclicmagic.util.UtilSound;

public class SpellExpPotion extends BaseSpell implements ISpell {

	private int potionId;
	private int potionDuration;
	private int potionAmp;
	public SpellExpPotion(int id,String name, int pcost){
		super.init(id,name);
		this.cost = pcost;
	}
	
	@Override
	public void spawnParticle(World world, EntityPlayer player, BlockPos pos) {

	}

	@Override
	public void playSound(World world, EntityPlayer player, BlockPos pos) {
		
	}

	public SpellExpPotion setPotion(int id, int effectDuration, int effectAmplifier) {
		potionId = id;
		potionDuration = effectDuration;
		potionAmp = effectAmplifier;
		return this;
	}

	@Override
	public boolean cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side ) {
		PotionRegistry.addOrMergePotionEffect(player, new PotionEffect(potionId, potionDuration, potionAmp));
		
		return true;
	}

	@Override
	public void payCost(World world, EntityPlayer player, BlockPos pos) {
		UtilSound.playSoundAt(player, UtilSound.drink);

		super.payCost(world, player, pos);
	}
}
