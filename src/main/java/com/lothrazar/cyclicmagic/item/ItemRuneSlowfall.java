package com.lothrazar.cyclicmagic.item;

import com.lothrazar.cyclicmagic.Const;
import com.lothrazar.cyclicmagic.PotionRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemRuneSlowfall  extends ItemRuneBase {
	public ItemRuneSlowfall() {
		super();
		this.setMaxStackSize(1);
	}


	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {

		//apply slowfall after falling for a while
		//TODO: toggle off and on rune thing
		if(entityIn instanceof EntityLivingBase && entityIn.fallDistance > 4F){
			EntityLivingBase entity = (EntityLivingBase)entityIn;
			
			if(entity.isPotionActive(PotionRegistry.slowfall) == false){
				PotionRegistry.addOrMergePotionEffect(entity, new PotionEffect(PotionRegistry.slowfall.id,20,20*Const.TICKS_PER_SEC));
			}
		}
	}
}
