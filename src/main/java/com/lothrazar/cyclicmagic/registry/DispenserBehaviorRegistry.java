package com.lothrazar.cyclicmagic.registry;

import net.minecraft.block.BlockDispenser;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameData;
import com.lothrazar.cyclicmagic.BehaviorPlantSeed;


public class DispenserBehaviorRegistry{

	public static void register(){
		
		//Item.itemRegistry

		//System.out.println("DispenserBehaviorRegistry register "+ GameData.getBlockItemMap().size());
		//if I could, i would just make a list of items, and register one single BehaviorPlantSeed object
		//to all those items, but it doesnt work that way I guess
		
		for(Item item : Item.itemRegistry){ //GameData.getBlockItemMap().entrySet()){
			if( item == null){continue;}
			
			if(item instanceof ItemSeeds){

				//System.out.println("Register plant dispense for "+item.getClass().toString());
				
				BlockDispenser.dispenseBehaviorRegistry.putObject(item, new BehaviorPlantSeed());
			}
        }
	}

	public static void syncConfig(Configuration config){

		// TODO Auto-generated method stub
		
	}
}
