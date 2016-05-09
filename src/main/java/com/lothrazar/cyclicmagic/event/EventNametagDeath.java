package com.lothrazar.cyclicmagic.event;

import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventNametagDeath  implements IHasConfig{

	private boolean nameTagDeath;
	
	@SubscribeEvent
	public void onLivingDropsEvent(LivingDropsEvent event) {
		if(!nameTagDeath){return;}

		Entity entity = event.getEntity();
		World worldObj = entity.getEntityWorld();

		if (entity.getCustomNameTag() != null && entity.getCustomNameTag() != "") {

			// item stack NBT needs the name enchanted onto it

			if (entity.worldObj.isRemote == false) {
				ItemStack nameTag = UtilNBT.buildEnchantedNametag(entity.getCustomNameTag());

				UtilEntity.dropItemStackInWorld(worldObj, entity.getPosition(), nameTag);
			}
			/*
			 * //not sure why isnt working...not critical
			 * if(entity instanceof EntityLivingBase){
			 * // show message as if player, works since EntityLiving extends
			 * EntityLivingBase
			 * 
			 * UtilChat.addChatMessage(worldObj,
			 * (event.getSource().getDeathMessage((EntityLivingBase) entity)));
			 * }
			 */
		}
	}

	@Override
	public void syncConfig(Configuration config) {

		String category = Const.ConfigCategory.mobs; 
		
	 	nameTagDeath = config.getBoolean("Name Tag Death", category, true,
				"When an entity dies that is named with a tag, it drops the nametag");

		
	}
}
