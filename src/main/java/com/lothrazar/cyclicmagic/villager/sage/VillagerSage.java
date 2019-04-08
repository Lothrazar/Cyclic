package com.lothrazar.cyclicmagic.villager.sage;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityVillager.EmeraldForItems;
import net.minecraft.entity.passive.EntityVillager.PriceInfo;
import net.minecraft.init.Items;

public class VillagerSage {

  public static final String NAME = "sage";

  public static EntityVillager.ITradeList[][] buildTrades() {
    return new EntityVillager.ITradeList[][] {
        {
            new EmeraldForItems(Items.GUNPOWDER, new PriceInfo(5, 8)), //GROUP 1
            new EmeraldForItems(Items.NETHER_WART, new PriceInfo(12, 16))
        }, {
            new EmeraldForItems(Items.BONE, new PriceInfo(8, 16)), //GROUP 2
            new EmeraldForItems(Items.MUTTON, new PriceInfo(4, 12))
        }, {
            new EmeraldForItems(Items.BLAZE_ROD, new PriceInfo(8, 16)), //GROUP 3
            new EmeraldForItems(Items.SLIME_BALL, new PriceInfo(8, 16))
        }, {
            new EmeraldForItems(Items.GHAST_TEAR, new PriceInfo(1, 2)), //GROUP 4
            new EmeraldForItems(Items.REDSTONE, new PriceInfo(4, 6))
        }, {
            new EmeraldForItems(Items.GLOWSTONE_DUST, new PriceInfo(6, 8)), //GROUP 5
            new EmeraldForItems(Items.ENDER_PEARL, new PriceInfo(16, 16))
        }, { new EmeraldForItems(Items.WRITTEN_BOOK, new PriceInfo(1, 1)), //GROUP 6
            new EmeraldForItems(Items.SADDLE, new PriceInfo(1, 1)),
            new EmeraldForItems(Items.NAME_TAG, new PriceInfo(1, 1))
        }, {
            new EmeraldForItems(Items.CAKE, new PriceInfo(1, 1)),
            new EmeraldForItems(Items.PUMPKIN_PIE, new PriceInfo(1, 1))
        }
    };
  }
}
