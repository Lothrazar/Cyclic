package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.registry.PotionRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilSound;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpellPotion extends BaseSpell {
	private Potion potionId;
	private int potionDuration;
	private int potionAmp;
	
	public SpellPotion(int id, String name) {
		super.init(id, name);
	}
	
	public void setPotion(Potion id, int effectDuration, int effectAmplifier) {
		potionId = id;
		potionDuration = effectDuration * Const.TICKS_PER_SEC;
		potionAmp = effectAmplifier;
		//return this;
	}

	@Override
	public boolean cast(World world, EntityPlayer player, ItemStack wand, BlockPos pos, EnumFacing side) {
		
		PotionRegistry.addOrMergePotionEffect(player, new PotionEffect(potionId, potionDuration, potionAmp));
		
		this.playSound(world, player, null, pos);
		
		//https://github.com/PrinceOfAmber/Cyclic/blob/2c947d4a91c28f8f853c6d1171ad019002892972/src/main/java/com/lothrazar/cyclicmagic/spell/SpellExpPotion.java
		return true;
	}

	@Override
	public void spawnParticle(World world, EntityPlayer player, BlockPos pos) {
		  
	}

	@Override
	public void playSound(World world, EntityPlayer player, Block block, BlockPos pos) { 
		UtilSound.playSound(player, pos, SoundEvents.ENTITY_GENERIC_DRINK);

	}
}
