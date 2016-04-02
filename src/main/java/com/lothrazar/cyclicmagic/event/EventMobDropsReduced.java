package com.lothrazar.cyclicmagic.event;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.lothrazar.cyclicmagic.util.UtilEntity;

public class EventMobDropsReduced{

	public boolean removeZombieCarrotPotato = true;

	@SubscribeEvent
	public void onLivingDropsEvent(LivingDropsEvent event){

		Entity entity = event.getEntity();
		//World worldObj = entity.getEntityWorld();
		List<EntityItem> drops = event.getDrops();

		//BlockPos pos = entity.getPosition();

		if(entity instanceof EntityZombie) // how to get this all into its own class
		{
			//EntityZombie z = (EntityZombie) entity;

			for(int i = 0; i < drops.size(); i++){
				EntityItem item = drops.get(i);

				if(item.getEntityItem().getItem() == Items.carrot || item.getEntityItem().getItem() == Items.potato){
					drops.remove(i);
				}
			}
		}
	}
}
