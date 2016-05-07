package com.lothrazar.cyclicmagic.event;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.lothrazar.cyclicmagic.util.UtilEntity;

public class EventEndermanDropBlock  implements IFeatureEvent{

	@SubscribeEvent
	public void onLivingDeathEvent(LivingDeathEvent event) {

		Entity entity = event.getEntity();

		if (entity instanceof EntityEnderman) {
			EntityEnderman mob = (EntityEnderman) entity;

			IBlockState bs = mob.getHeldBlockState();// mob.func_175489_ck();

			if (bs != null && bs.getBlock() != null && entity.worldObj.isRemote == false) {
				UtilEntity.dropItemStackInWorld(entity.worldObj, mob.getPosition(), bs.getBlock());
			}
		}
	}

	@Override
	public void syncConfig(Configuration config) {
		// TODO Auto-generated method stub
		
	}
}
