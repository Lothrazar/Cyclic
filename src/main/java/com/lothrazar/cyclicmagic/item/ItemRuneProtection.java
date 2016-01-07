package com.lothrazar.cyclicmagic.item;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.Const;
import com.lothrazar.cyclicmagic.PotionRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemRuneProtection  extends RuneBaseAbstract {
	public ItemRuneProtection() {
		super();
	}

	private final static float HEALTHLIMIT = 10;//1 heart = 2 health
	private final static int SECONDS = 30;
	private final static float FALLDISTANCE = 3;
	private final static float AIRLIMIT = 150;// 300 is a full bar

	@Override
	protected boolean trigger(World world,Entity entityIn ) {

		if(entityIn instanceof EntityLivingBase){
			EntityLivingBase entity = (EntityLivingBase)entityIn;

			boolean didit = false;
			
			if(entity.getHealth() <= HEALTHLIMIT && entity.isPotionActive(Potion.absorption.id) == false){
			
				PotionRegistry.addOrMergePotionEffect(entity, new PotionEffect(Potion.absorption.id,SECONDS * Const.TICKS_PER_SEC, PotionRegistry.V));
			
				PotionRegistry.addOrMergePotionEffect(entity, new PotionEffect(Potion.resistance.id,SECONDS * Const.TICKS_PER_SEC, PotionRegistry.I));
			
				didit = true;
			}
			if(entity.isBurning() && entity.isPotionActive(Potion.fireResistance.id) == false){

				PotionRegistry.addOrMergePotionEffect(entity, new PotionEffect(Potion.fireResistance.id,SECONDS * Const.TICKS_PER_SEC, PotionRegistry.V));
				
				didit = true;
			}
			if(entityIn.fallDistance >= FALLDISTANCE){
				
				if(entity.isPotionActive(PotionRegistry.slowfall) == false){
					PotionRegistry.addOrMergePotionEffect(entity, new PotionEffect(PotionRegistry.slowfall.id,SECONDS * Const.TICKS_PER_SEC));

					didit = true;
				}
			}
			if(entityIn.ridingEntity != null && entityIn.ridingEntity.fallDistance >= FALLDISTANCE 
					&& entityIn.ridingEntity instanceof EntityLivingBase){
				EntityLivingBase maybeHorse = (EntityLivingBase)entityIn.ridingEntity;
				
				if(maybeHorse.isPotionActive(PotionRegistry.slowfall) == false){

					PotionRegistry.addOrMergePotionEffect(maybeHorse, new PotionEffect(PotionRegistry.slowfall.id,SECONDS * Const.TICKS_PER_SEC));

					didit = true;
				}
			}
			if(entity.getAir() <= AIRLIMIT){
				
				if(entity.isPotionActive(Potion.waterBreathing) == false){
					PotionRegistry.addOrMergePotionEffect(entity, new PotionEffect(Potion.waterBreathing.id,SECONDS * Const.TICKS_PER_SEC));

					didit = true;
				}
			}
			if(entity.getPosition().getY() < -10){
				entity.setPositionAndUpdate(entity.getPosition().getX(), entity.getPosition().getY() + 256, entity.getPosition().getZ());;

				didit = true;
			}
			
			return didit;//can trigger both fire and regular
		}
		return false;
	}

	@Override
	protected List<String> getInfo() {
		List<String> list = new ArrayList<String>();

		list.add(StatCollector.translateToLocal("rune.protection.prot"));
		list.add(StatCollector.translateToLocal("rune.protection.fire"));
		list.add(StatCollector.translateToLocal("rune.protection.fall"));
		list.add(StatCollector.translateToLocal("rune.protection.water"));
		
		return list;
	}
}
