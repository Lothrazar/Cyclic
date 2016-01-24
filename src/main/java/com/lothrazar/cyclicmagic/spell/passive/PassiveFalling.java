package com.lothrazar.cyclicmagic.spell.passive;

import java.util.ArrayList;
import java.util.Arrays;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import com.lothrazar.cyclicmagic.Const;
import com.lothrazar.cyclicmagic.PotionRegistry;

public class PassiveFalling implements IPassiveSpell{
	private static final ArrayList<String> info = new ArrayList<String>(Arrays.asList("passive.falling"));

	private final static float FALLDISTANCE = 3;
	private final static int SECONDS = 30;
	
	@Override
	public boolean canTrigger(EntityPlayer entity) {
		return true;//there are multiple tests
	}

	@Override
	public void trigger(EntityPlayer entity) {

		if(entity.fallDistance >= FALLDISTANCE){
			
			if(entity.isPotionActive(PotionRegistry.slowfall) == false){
				PotionRegistry.addOrMergePotionEffect(entity, new PotionEffect(PotionRegistry.slowfall.id,SECONDS * Const.TICKS_PER_SEC));

			}
		}
		
		if(entity.getPosition().getY() < -10){
			entity.setPositionAndUpdate(entity.getPosition().getX(), entity.getPosition().getY() + 256, entity.getPosition().getZ());;
		}
 
		if(entity.ridingEntity != null && entity.ridingEntity.fallDistance >= FALLDISTANCE 
				&& entity.ridingEntity instanceof EntityLivingBase){
			EntityLivingBase maybeHorse = (EntityLivingBase)entity.ridingEntity;
			
			if(maybeHorse.isPotionActive(PotionRegistry.slowfall) == false){

				PotionRegistry.addOrMergePotionEffect(maybeHorse, new PotionEffect(PotionRegistry.slowfall.id,SECONDS * Const.TICKS_PER_SEC));
			}
		}		
	}
	

	@Override
	public ArrayList<String> info() {
		// TODO Auto-generated method stub
		return null;
	}

}
