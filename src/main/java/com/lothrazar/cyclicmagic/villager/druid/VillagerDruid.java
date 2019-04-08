package com.lothrazar.cyclicmagic.villager.druid;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityVillager.EmeraldForItems;
import net.minecraft.entity.passive.EntityVillager.PriceInfo;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class VillagerDruid {

  public static final String NAME = "druid";

  public static EntityVillager.ITradeList[][] buildTrades() {
    return new EntityVillager.ITradeList[][] {
        //villager will buy this stuff for emeralds 
        {
            new EmeraldForItems(Items.COOKED_FISH, new PriceInfo(9, 12)), // GROUP 1
            new EmeraldForItems(Items.APPLE, new PriceInfo(3, 6)),
            new EmeraldForItems(Items.BEETROOT, new PriceInfo(8, 12))
        }, {
            new EmeraldForItems(Items.FEATHER, new PriceInfo(12, 13)), //GROUP 2
            new EmeraldForItems(Items.WHEAT_SEEDS, new PriceInfo(50, 64)),
            new EmeraldForItems(Items.POISONOUS_POTATO, new PriceInfo(1, 3))
        }, {
            new EmeraldForItems(Item.getItemFromBlock(Blocks.BROWN_MUSHROOM), new PriceInfo(8, 12)), //GROUP 3
            new EmeraldForItems(Item.getItemFromBlock(Blocks.RED_MUSHROOM), new PriceInfo(8, 12))
        }, {
            new EmeraldForItems(Items.BEEF, new PriceInfo(14, 17)), //GROUP 4
            new EmeraldForItems(Items.RABBIT, new PriceInfo(14, 17)),
            new EmeraldForItems(Items.CHICKEN, new PriceInfo(14, 17))
        }, {
            new EmeraldForItems(Items.WRITTEN_BOOK, new PriceInfo(1, 1)), //GROUP 5
            new EmeraldForItems(Items.FISH, new PriceInfo(9, 12)),
            new EmeraldForItems(Items.SPIDER_EYE, new PriceInfo(3, 6))
        }, {
            new EmeraldForItems(Items.SADDLE, new PriceInfo(1, 1)), //GROUP 6 
            new EmeraldForItems(Items.EXPERIENCE_BOTTLE, new PriceInfo(1, 1))
        }
    };
  }
}
