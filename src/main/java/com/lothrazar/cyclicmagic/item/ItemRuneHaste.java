package com.lothrazar.cyclicmagic.item;

import com.lothrazar.cyclicmagic.Const;
import com.lothrazar.cyclicmagic.PotionRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemTool;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemRuneHaste  extends RuneBaseAbstract {
	public ItemRuneHaste() {
		super();
	}

	private final static int seconds = 20;

	@Override
	protected boolean trigger(World world,Entity entityIn ) {
		//apply slowfall after falling for a while
		if(entityIn instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer)entityIn;
			
			if(player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemTool
					&& player.isPotionActive(Potion.digSpeed) == false){
				PotionRegistry.addOrMergePotionEffect(player, new PotionEffect(Potion.digSpeed.id,seconds * Const.TICKS_PER_SEC,PotionRegistry.II));
			
				return true;
			}
		}
		
		return false;
	}
}
