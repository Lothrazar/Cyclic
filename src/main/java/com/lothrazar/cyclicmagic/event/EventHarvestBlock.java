package com.lothrazar.cyclicmagic.event;

import com.lothrazar.cyclicmagic.util.UtilEntity;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHarvestBlock {

	@SubscribeEvent
	public void onHarvestDropsEvent(HarvestDropsEvent event){
		EntityPlayer player = event.getHarvester();
		World world = event.getWorld();
		IBlockState broken = event.getState();
		if(player == null || broken == null || broken.getBlock() == null){return;}
		
		if(player.getActiveHand() == null){
			return;
		}
		
		boolean handIsEmpty = (player.getHeldItem(player.getActiveHand()) == null);
		
		if(broken.getBlock() instanceof BlockLog && handIsEmpty){
			event.setDropChance(0);
		}
		
		if(broken.getBlock() instanceof BlockLeaves && handIsEmpty && event.getDrops().size() == 0
				&& world.rand.nextDouble() < 0.30 && world.isRemote == false){
			//increasing drop change wont work, list is empty anwyay
			//if list is non empty, its a sapling so leave it
			
			UtilEntity.dropItemStackInWorld(world, event.getPos(), Items.STICK);
		}
	}
}
