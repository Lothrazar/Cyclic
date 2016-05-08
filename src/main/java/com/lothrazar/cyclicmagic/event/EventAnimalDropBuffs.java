package com.lothrazar.cyclicmagic.event;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteractSpecific;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilEntity;

public class EventAnimalDropBuffs  implements IFeatureEvent{

	private static boolean farmDropBuffs;
	
	private static final int	cowExtraLeather							= 4;
	private static final int	pigExtraMeat								= 5;
	private static final int	chanceZombieVillagerEmerald	= 50;
	public static final int sheepExtraWool = 5;
 
	
	@SubscribeEvent
	public void onEntityInteractSpecific(EntityInteractSpecific event) {
		if(!farmDropBuffs){return;}
		
		if(event.getEntityPlayer() != null && event.getTarget() instanceof EntitySheep){
			EntityPlayer p = event.getEntityPlayer();
			EntitySheep s = (EntitySheep)event.getTarget();
			
			if(event.getHand() != null && p.getHeldItem(event.getHand()) != null && 
					p.getHeldItem(event.getHand()).getItem() == Items.shears){
				
				int meta = s.getFleeceColor().getMetadata();
				
				UtilEntity.dropItemStackInWorld(event.getWorld(), event.getPos(), new ItemStack(Blocks.wool, sheepExtraWool ,meta));
				
			}	
		}
	}
	
	@SubscribeEvent
	public void onLivingDropsEvent(LivingDropsEvent event) {
		if(!farmDropBuffs){return;}

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

	@Override
	public void syncConfig(Configuration config) {
		String category = Const.ConfigCategory.mobs;
		
		farmDropBuffs = config.getBoolean("Farm Drops Buffed", category, true,
				"Increase drops of farm animals: more leather, more wool from shearing, pigs drop a bit more pork");
		
	}

}
