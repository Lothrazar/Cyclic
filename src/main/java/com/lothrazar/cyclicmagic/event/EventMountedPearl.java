package com.lothrazar.cyclicmagic.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventMountedPearl{

	private static final String NBT_RIDING_ENTITY = "ride";

	@SubscribeEvent
	public void onEnderTeleportEvent(EnderTeleportEvent event){

		Entity ent = event.getEntity();
		if(ent instanceof EntityLiving == false){
			return;
		}
		EntityLivingBase living = (EntityLivingBase) event.getEntity();
		if(living == null){
			return;
		}

		if(living.worldObj.isRemote == false)// do not spawn a second 'ghost' one on client side
		{
			if(living.getRidingEntity() != null && living instanceof EntityPlayer){
				EntityPlayer player = (EntityPlayer) living;

				player.getEntityData().setInteger(NBT_RIDING_ENTITY, player.getRidingEntity().getEntityId());

				player.getRidingEntity().setPositionAndUpdate(event.getTargetX(), event.getTargetY(), event.getTargetZ());
			}
		}
	}

	@SubscribeEvent
	public void onEntityUpdate(LivingUpdateEvent event){

		Entity ent = event.getEntity();
		if(ent instanceof EntityLiving == false){
			return;
		}
		EntityLivingBase living = (EntityLivingBase) event.getEntity();
		if(living == null){
			return;
		}

		if(living instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer) living;
			if(player.getEntityData() == null){
				return;
			}
			int setride = player.getEntityData().getInteger(NBT_RIDING_ENTITY);

			if(setride > 0 && player.getRidingEntity() == null){
				Entity horse = player.worldObj.getEntityByID(setride);

				if(horse != null){
					player.startRiding(horse, true);
					player.getEntityData().setInteger(NBT_RIDING_ENTITY, -1);
				}
			}
		}
	}
}
