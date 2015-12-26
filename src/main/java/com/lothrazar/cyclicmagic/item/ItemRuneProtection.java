package com.lothrazar.cyclicmagic.item;

import com.lothrazar.cyclicmagic.Const;
import com.lothrazar.cyclicmagic.PotionRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemRuneProtection  extends ItemRuneBase {
	public ItemRuneProtection() {
		super();
	}

	private final static int seconds = 20;
	private final static float healthLimit = 10;//1 heart = 2 health

	@Override
	protected void trigger(World world,Entity entityIn ) {
		//apply slowfall after falling for a while
		if(entityIn instanceof EntityLivingBase){
			EntityLivingBase entity = (EntityLivingBase)entityIn;
			
			if(entity.getHealth() < healthLimit && entity.isPotionActive(Potion.absorption.id) == false){

				PotionRegistry.addOrMergePotionEffect(entity, new PotionEffect(Potion.absorption.id,seconds * Const.TICKS_PER_SEC, PotionRegistry.V));
				PotionRegistry.addOrMergePotionEffect(entity, new PotionEffect(Potion.resistance.id,seconds * Const.TICKS_PER_SEC, PotionRegistry.II));
			}
		}
	}
}
