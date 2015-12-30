package com.lothrazar.cyclicmagic.item;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.Const;
import com.lothrazar.cyclicmagic.PotionRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemRuneSlowfall  extends RuneBaseAbstract {
	public ItemRuneSlowfall() {
		super();
	}

	private final static int seconds = 20;
	private final static float falldistance = 3;

	@Override
	protected boolean trigger(World world,Entity entityIn ) {
		//apply slowfall after falling for a while
		if(entityIn instanceof EntityLivingBase && entityIn.fallDistance >= falldistance){
			EntityLivingBase entity = (EntityLivingBase)entityIn;
			
			if(entity.isPotionActive(PotionRegistry.slowfall) == false){
				PotionRegistry.addOrMergePotionEffect(entity, new PotionEffect(PotionRegistry.slowfall.id,seconds * Const.TICKS_PER_SEC));
			
				return true;
			}
		}
		return false;
	}

	@Override
	protected List<String> getInfo() {
		List<String> list = new ArrayList<String>();

		list.add(StatCollector.translateToLocal("rune.slowfall.info"));
		
		return list;
	}
}
