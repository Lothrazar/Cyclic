package com.lothrazar.cyclicmagic.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.lothrazar.cyclicmagic.util.UtilChat;

public class EventPlayerDeath{

	@SubscribeEvent
	public void onLivingDeathEvent(LivingDeathEvent event){

		Entity entity = event.getEntity();

		if(entity instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer) entity;

			// TODO: could restrict to certain damage soruces
			// event.getSource() == DamageSource.
			//ItemStack skull = UtilNBT.buildNamedPlayerSkull(player);

			//UtilEntity.dropItemStackInWorld(entity.worldObj, player.getPosition(), skull);

			String coordsStr = UtilChat.blockPosToString(player.getPosition());
			UtilChat.addChatMessage(player, player.getDisplayNameString() + " player.death.location " + coordsStr);

		}
	}
}
