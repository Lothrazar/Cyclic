package com.lothrazar.cyclicmagic.event;

import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.lothrazar.cyclicmagic.block.BlockDimensionOre;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;

public class EventOreMined {

	@SubscribeEvent
	public void onBreakEvent(BreakEvent event){
		
	}
	
	@SubscribeEvent
	public void onHarvestDropsEvent(HarvestDropsEvent event){

		if( event.getState() != null && event.getState().getBlock() instanceof BlockDimensionOre){
		
			//then try spawning mob
			//EntityPlayer player = event.getPlayer();
			BlockPos pos = event.getPos();
			World world = event.getWorld();
			
			BlockDimensionOre block = (BlockDimensionOre)event.getState().getBlock();
			
			block.trySpawnTriggeredEntity(world, pos);
		}
	}
}
