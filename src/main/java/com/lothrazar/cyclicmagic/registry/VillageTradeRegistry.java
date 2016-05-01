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

	final static String sage = Const.MODRES+"textures/entity/villager/elder.png";
	final static ResourceLocation sageTexture = new ResourceLocation(sage);
	private static VillagerProfession  elderProfession;
	private static boolean extraVillagersEnabled;

	public static void register(){
		
		if(!extraVillagersEnabled){
			return;
		}
		
		elderProfession = new VillagerProfession(Const.MODRES+"elder", sage){
				@Override
		    public ResourceLocation getSkin() {
					return sageTexture;
				}
			};
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

		//	System.out.println("getSkin"+this.getSkin().getResourceDomain()+"_"+this.getSkin().getResourcePath());

     
		VillagerRegistry.instance().register(elderProfession);

		//TOO TEST: /summon Villager ~ ~ ~ {Profession:5,Career:0}
		addVillagerSage();

		//TOO TEST: /summon Villager ~ ~ ~ {Profession:5,Career:1}
		addVillagerDruid();
		 
	}

	private static void addVillagerSage() {

		  new VillagerCareer(elderProfession, "sage_career"){
			 @Override
       public ITradeList[][] getTrades(){
           return sageTrades;
       }
		 };
	}
	private static void addVillagerDruid() {

	  new VillagerCareer(elderProfession, "druid_career"){
		 @Override
     public ITradeList[][] getTrades(){
         return druidTrades;
     }
	 };
		
	}

  final static EntityVillager.ITradeList[][] druidTrades = {

 		 {	new EmeraldForItems(Items.cooked_fish, new PriceInfo(9, 12))
 			 ,new EmeraldForItems(Items.apple, new PriceInfo(3,6)) 
 			 ,new EmeraldForItems(Items.beetroot, new PriceInfo(8, 12)) },
 		 
		 {	new EmeraldForItems(Items.feather, new PriceInfo(12, 13)) 
			 ,new EmeraldForItems(Items.wheat_seeds, new PriceInfo(50, 64))  },
		 
		 {	new EmeraldForItems(Item.getItemFromBlock(Blocks.brown_mushroom), new PriceInfo(8, 12))
			 ,new EmeraldForItems(Item.getItemFromBlock(Blocks.red_mushroom), new PriceInfo(8, 12)) },
		 
		 {	new EmeraldForItems(Items.beef, new PriceInfo(14, 17))
			 ,new EmeraldForItems(Items.rabbit, new PriceInfo(14, 17))
			 ,new EmeraldForItems(Items.chicken, new PriceInfo(14,17))	 },
		 
			 {	new EmeraldForItems(Items.poisonous_potato, new PriceInfo(1, 3))
				 ,new EmeraldForItems(Items.spider_eye, new PriceInfo(3, 6))}
	 };
	
  final static EntityVillager.ITradeList[][] sageTrades = {
		 
		 {	new EmeraldForItems(Items.gunpowder, new PriceInfo(5, 8))
			 ,new EmeraldForItems(Items.nether_wart, new PriceInfo(12, 16))},
		 
		 {	new EmeraldForItems(Items.bone, new PriceInfo(26, 32))
			 ,new EmeraldForItems(Items.mutton, new PriceInfo(12, 16))},
		 
		 {	new EmeraldForItems(Items.blaze_rod, new PriceInfo(12, 16))
			 ,new EmeraldForItems(Items.slime_ball, new PriceInfo(12,16))},
		 
		 {	new EmeraldForItems(Items.ghast_tear, new PriceInfo(2, 3))
			 ,new EmeraldForItems(Items.redstone, new PriceInfo(4,6)) 
			 },	
		 {	new EmeraldForItems(Items.glowstone_dust, new PriceInfo(12,16))
			 ,new ListItemForEmeralds(Items.experience_bottle, new PriceInfo(2, 4))
			 ,new EmeraldForItems(Items.diamond, new PriceInfo(1, 1))
			 ,new EmeraldForItems(Items.ender_pearl, new PriceInfo(12, 16))}
	 };
	
	public static void syncConfig(Configuration c){
		String category = Const.MODCONF + "Villagers";
		extraVillagersEnabled = c.getBoolean("More Trades", category, true, "Adds more  villager types (professions) with more trades such as gunpowder, blaze rods, beef, spider eyes, and more.  Test with the /summon command using profession 5 and careers 0,1.  Also spawn naturally. ");
	}
}
