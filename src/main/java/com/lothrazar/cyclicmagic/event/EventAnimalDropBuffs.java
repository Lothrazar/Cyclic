package com.lothrazar.cyclicmagic.event;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.lothrazar.cyclicmagic.util.UtilEntity;

public class EventAnimalDropBuffs {

	private static final int	cowExtraLeather							= 3;
	private static final int	pigExtraMeat								= 5;

	private static final int	chanceZombieVillagerEmerald	= 50;

	@SubscribeEvent
	public void onLivingDropsEvent(LivingDropsEvent event) {

		Entity entity = event.getEntity();
		World worldObj = entity.getEntityWorld();
		List<EntityItem> drops = event.getDrops();

		BlockPos pos = entity.getPosition();

		if (entity instanceof EntityZombie) // how to get this all into its own
		                                    // class
		{
			EntityZombie z = (EntityZombie) entity;

			if (z.isVillager() && chanceZombieVillagerEmerald > 0 && worldObj.rand.nextInt(100) <= chanceZombieVillagerEmerald) {
				drops.add(new EntityItem(worldObj, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.emerald)));
			}
		}

		if (cowExtraLeather > 0 && entity instanceof EntityCow) {
			UtilEntity.dropItemStackInWorld(worldObj, pos, new ItemStack(Items.leather, cowExtraLeather));
		}

		if (pigExtraMeat > 0 && entity instanceof EntityPig) {
			UtilEntity.dropItemStackInWorld(worldObj, pos, new ItemStack(Items.porkchop, pigExtraMeat));
		}
	}

}
