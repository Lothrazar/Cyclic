package com.lothrazar.cyclicmagic.registry;

import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityVillager.EmeraldForItems;
import net.minecraft.entity.passive.EntityVillager.ITradeList;
import net.minecraft.entity.passive.EntityVillager.ListItemForEmeralds;
import net.minecraft.entity.passive.EntityVillager.PriceInfo;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;

public class VillageTradeRegistry {

	public static void register(){

		//TOO TEST: /summon Villager ~ ~ ~ {Profession:5}
		//kill: /kill @e[type=Villager]
		//CONFIRMED: IT does spawn randomly when using default villager eggs
		
			/*
		//PROBLEM WITH TEXTURE
		in EntityVillager:
    public int getProfession()
    {
        return Math.max(((Integer)this.dataWatcher.get(PROFESSION)).intValue() % 5, 0);
    
    }
    //that %5 needs to be removed, it forces max of 4 profs
    */
		String sage = Const.MODRES+"textures/entity/villager/sage.png";
		final ResourceLocation sageTexture = new ResourceLocation(sage);
		
		VillagerProfession prof = new VillagerProfession(Const.MODRES+"sage", sage){
			@Override
      public ResourceLocation getSkin() {

			//	System.out.println("getSkin"+this.getSkin().getResourceDomain()+"_"+this.getSkin().getResourcePath());
				return sageTexture;//super.getSkin();
			}
		}
		;
     
		
		VillagerRegistry.instance().register(prof);
		
	//	VillagerProfession test = prof = net.minecraftforge.fml.common.registry.VillagerRegistry.instance().getRegistry().getValue(new ResourceLocation(Const.MODRES+"sage"));
		 
		//System.out.println("test isNull :"+ (test == null));
		
		 
		 final EntityVillager.ITradeList[][] sageTrades = {
				 
				 {new EmeraldForItems(Items.ender_pearl, new PriceInfo(16, 16)) 	 },// New to mod
			
				 {new EmeraldForItems(Items.beetroot, new PriceInfo(8, 12)) 	 },//New to mod
				 
				 {new EmeraldForItems(Items.wheat_seeds, new PriceInfo(64, 64))  },//New to mod
				
				 {new EmeraldForItems(Items.bone, new PriceInfo(32, 32)) }, //New to mod
				 
				 {new EmeraldForItems(Items.gunpowder, new PriceInfo(5, 5))  },//New to mod
						
				 {new EmeraldForItems(Items.feather, new PriceInfo(16, 16))  },//New to mod		
				 {new EmeraldForItems(Items.nether_wart, new PriceInfo(16, 16))  },//New to mod			
				 {new EmeraldForItems(Items.mutton, new PriceInfo(16, 16))  },//New to mod			
				 
				 {new EmeraldForItems(Items.blaze_rod, new PriceInfo(16, 16))  },//New to mod
				
				 {new EmeraldForItems(Item.getItemFromBlock(Blocks.brown_mushroom), new PriceInfo(16, 16)) },//New to mod
				 
				 {new EmeraldForItems(Item.getItemFromBlock(Blocks.red_mushroom), new PriceInfo(16, 16))  },	//New to mod
									 
				 {new EmeraldForItems(Items.blaze_rod, new PriceInfo(16, 16))  },//New to mod
								 
				 {new EmeraldForItems(Items.slime_ball, new PriceInfo(16,16)) }, //New to mod				
				 
				 {new EmeraldForItems(Items.poisonous_potato, new PriceInfo(3, 3))  },//New to mod				
				 
				 {new EmeraldForItems(Items.spider_eye, new PriceInfo(9, 9))  },//New to mod				
				 
				 {new EmeraldForItems(Items.ghast_tear, new PriceInfo(2, 2)) },//New to mod				 
				 
				 {new EmeraldForItems(Items.diamond, new PriceInfo(9, 9)) },//New to mod, 9 emeralds = 1 diamond
				 				 
				 {new EmeraldForItems(Items.cooked_fish, new PriceInfo(9, 12)) },//removed from Farmer, not in 1.9
								 
				 {new ListItemForEmeralds(Items.apple, new PriceInfo(5,5)) },//Refund at most				
				 
				 {new ListItemForEmeralds(Items.chicken, new PriceInfo(14,17))	 },//is no longer in farmer
			
				 {new EmeraldForItems(Items.beef, new PriceInfo(14, 17)) },// removed from Butcher, not in 1.9
				 {new EmeraldForItems(Items.rabbit, new PriceInfo(14, 17)) },// NEW to mod
				
				 {new EmeraldForItems(Items.experience_bottle, new PriceInfo(2, 4)) },// removed from Priest, not in 1.9 (better deal V has 3-11  emeralds for 1 bottle)
				
				 {new EmeraldForItems(Items.redstone, new PriceInfo(4,4)) },// REFUND at most
				
				 {new EmeraldForItems(Items.glowstone_dust, new PriceInfo(12,12))}// REFUND at most, since we can buy 1emerald = 3blocks = 12 dust
			
		 };
		 
		// VillagerProfession libr =	VillagerRegistry.instance().getRegistry().getValue(new ResourceLocation("minecraft:librarian"));
			
		  new VillagerCareer(prof, "sage_career"){
			 @Override
       public ITradeList[][] getTrades()
       {
           return sageTrades;
       }
		 };
		 
		 
	}
	public static void syncConfig(Configuration c){
		
	}
}
