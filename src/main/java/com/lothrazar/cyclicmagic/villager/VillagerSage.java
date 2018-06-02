package com.lothrazar.cyclicmagic.villager;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityVillager.EmeraldForItems;
import net.minecraft.entity.passive.EntityVillager.PriceInfo;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;

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
            new EmeraldForItems(Items.DIAMOND, new PriceInfo(8, 12)),
            new EmeraldForItems(Items.ENDER_PEARL, new PriceInfo(12, 16))
        }, {
            new ListItemForEmeraldsFixed(new ItemStack(Items.EXPERIENCE_BOTTLE, 8), new PriceInfo(1, 4)), //GROUP 6
            new ListItemForEmeraldsFixed(new ItemStack(Blocks.CLAY, 16), new PriceInfo(1, 1)),
        }, {
            new ListItemForEmeraldsFixed(new ItemStack(Blocks.QUARTZ_BLOCK, 16), new PriceInfo(2, 4)), //GROUP 7
            new ListItemForEmeraldsFixed(new ItemStack(Blocks.OBSIDIAN, 16), new PriceInfo(2, 4)),
            new ListItemForEmeraldsFixed(new ItemStack(Items.FISH, 4, ItemFishFood.FishType.PUFFERFISH.getMetadata()), new PriceInfo(1, 2))
        }
    };
  }
}
