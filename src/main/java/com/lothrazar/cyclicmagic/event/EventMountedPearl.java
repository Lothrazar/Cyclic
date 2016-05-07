package com.lothrazar.cyclicmagic.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventMountedPearl  implements IFeatureEvent{

//	private static final String NBT_RIDING_ENTITY = "ride";

	@SubscribeEvent
	public void onEnderTeleportEvent(EnderTeleportEvent event) {

		Entity ent = event.getEntity();
		if (ent instanceof EntityLivingBase == false) { return; }
		EntityLivingBase living = (EntityLivingBase) event.getEntity();

		if (living.getRidingEntity() != null && living instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) living;
			Entity horse = player.getRidingEntity();


		//	player.setPositionAndUpdate(event.getTargetX(), event.getTargetY(), event.getTargetZ());

			//player.getEntityData().setInteger(NBT_RIDING_ENTITY, horse.getEntityId());
			//player.getEntityData().setDouble(NBT_RIDING_ENTITY + "timeout", ent.worldObj.getWorldTime() + 500);

			horse.setPositionAndUpdate(event.getTargetX(), event.getTargetY(), event.getTargetZ());
			player.startRiding(horse, true);

		}
	}
/*
	@SubscribeEvent
	public void onEntityUpdate(LivingUpdateEvent event) {

		Entity ent = event.getEntity();
		if (ent instanceof EntityLiving == false) { return; }
		EntityLivingBase living = (EntityLivingBase) event.getEntity();
		if (living == null) { return; }

		if (living instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) living;
			if (player.getEntityData() == null) { return; }
			int setride = player.getEntityData().getInteger(NBT_RIDING_ENTITY);

			if (setride > 0 && player.getRidingEntity() == null) {

				double timer = player.getEntityData().getDouble(NBT_RIDING_ENTITY + "timeout");

				if (ent.worldObj.getWorldTime() < timer) { return; }

				Entity horse = player.worldObj.getEntityByID(setride);

				if (horse != null) {
					BlockPos target = player.getPosition();
					//player.startRiding(horse, true);
					System.out.println("Cancel Riding");
					horse.setPositionAndUpdate(target.getX(), target.getY(), target.getZ());
					player.getEntityData().setInteger(NBT_RIDING_ENTITY, -1);
				}
			}
		}
	}
*/

	@Override
	public void syncConfig(Configuration config) {
		// TODO Auto-generated method stub
		
	}
}
