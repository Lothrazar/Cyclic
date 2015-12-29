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

	private final static float healthLimit = 6;//1 heart = 2 health
	private final static int seconds = 20;

	@Override
	protected boolean trigger(World world,Entity entityIn ) {
		//apply slowfall after falling for a while
		if(entityIn instanceof EntityLivingBase){
			EntityLivingBase entity = (EntityLivingBase)entityIn;
			
			boolean didit = false;
			
			if(entity.getHealth() <= healthLimit && entity.isPotionActive(Potion.absorption.id) == false){
			
				PotionRegistry.addOrMergePotionEffect(entity, new PotionEffect(Potion.absorption.id,seconds * Const.TICKS_PER_SEC, PotionRegistry.V));
			
				PotionRegistry.addOrMergePotionEffect(entity, new PotionEffect(Potion.resistance.id,seconds * Const.TICKS_PER_SEC, PotionRegistry.I));
			
				didit = true;
			}
			if(entity.isBurning() && entity.isPotionActive(Potion.fireResistance.id) == false){

				PotionRegistry.addOrMergePotionEffect(entity, new PotionEffect(Potion.fireResistance.id,seconds * Const.TICKS_PER_SEC, PotionRegistry.V));
				
				didit = true;
			}
			
			return didit;//can trigger both fire and regular
		}
		return false;
	}

	@Override
	protected List<String> getInfo() {
		List<String> list = new ArrayList<String>();

		list.add(StatCollector.translateToLocal("rune.protection.info1"));
		list.add(StatCollector.translateToLocal("rune.protection.info2"));
		
		return list;
	}
}
