package com.lothrazar.cyclicmagic.event;

import com.lothrazar.cyclicmagic.item.ItemFoodAppleMagic;
import com.lothrazar.cyclicmagic.registry.PotionRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventAppleUse{

	@SubscribeEvent
	public void onEnderTeleportEvent(EnderTeleportEvent event){

		if(event.getEntity() instanceof EntityLivingBase == false){
			return;
		}
		EntityLivingBase entityLiving = (EntityLivingBase)event.getEntity();
		if(entityLiving != null && entityLiving.isPotionActive(PotionRegistry.ender)){
			event.setAttackDamage(0); // starts at exactly 5.0 which is 2.5hearts
	
			// do not spawn a second 'ghost' one on client side
			if(entityLiving.worldObj.isRemote == false)
			{
				EntityItem entityItem = new EntityItem(entityLiving.worldObj, 
						event.getTargetX(), event.getTargetY(), event.getTargetZ(), 
						new ItemStack(Items.ender_pearl));
				entityLiving.worldObj.spawnEntityInWorld(entityItem);
			}
		}
	}
 
	@SubscribeEvent
	public void entityInteractEvent(EntityInteract event){

		if(event.getEntity() instanceof EntityPlayer == false){
			return;
		}
		EntityPlayer entityPlayer = (EntityPlayer)event.getEntity();
		if(event.getTarget() == null || entityPlayer == null){
			return;
		}// dont think this ever happens

		if(entityPlayer.getHeldItemMainhand() != null && entityPlayer.getHeldItemMainhand().getItem() instanceof ItemFoodAppleMagic && event.getTarget() instanceof EntityLivingBase){
			ItemFoodAppleMagic item = (ItemFoodAppleMagic) entityPlayer.getHeldItemMainhand().getItem();

			EntityLivingBase mob = (EntityLivingBase) event.getTarget();

			item.addAllEffects(entityPlayer.worldObj, mob);

			if(entityPlayer.capabilities.isCreativeMode == false){
				entityPlayer.inventory.decrStackSize(entityPlayer.inventory.currentItem, 1);
			}

			entityPlayer.worldObj.playSound(mob.getPosition().getX(), mob.getPosition().getY(), mob.getPosition().getZ(), SoundEvents.entity_horse_eat, SoundCategory.PLAYERS, 1.0F, 1.0F, false);
			// event.entityLiving .setea

			// mob.setEating(true); //makes horse animate and bend down to eat
		}
	}
}
