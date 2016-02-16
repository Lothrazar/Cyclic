package com.lothrazar.cyclicmagic.spell.passive;

import com.lothrazar.cyclicmagic.Const;
import com.lothrazar.cyclicmagic.PotionRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StatCollector;

public class PassiveBurn implements IPassiveSpell{
 
	private final static int SECONDS = 30;

	@Override
	public boolean canTrigger(EntityPlayer entity){

		return (entity.isBurning() && entity.isPotionActive(Potion.fireResistance.id) == false);
	}

	@Override
	public void trigger(EntityPlayer entity){

		PotionRegistry.addOrMergePotionEffect(entity, new PotionEffect(Potion.fireResistance.id, SECONDS * Const.TICKS_PER_SEC, PotionRegistry.I));

		if(entity.ridingEntity != null && entity.ridingEntity instanceof EntityLivingBase){

			PotionRegistry.addOrMergePotionEffect((EntityLivingBase) entity.ridingEntity, new PotionEffect(Potion.fireResistance.id, SECONDS * Const.TICKS_PER_SEC, PotionRegistry.I));
		}
	}

	@Override
	public String getName(){

		return StatCollector.translateToLocal("spellpassive.burn.name");
	}

	@Override
	public String getInfo(){

		return StatCollector.translateToLocal("spellpassive.burn.info");
	}
	@Override
	public int getID(){

		return 1;
	}
}
