package com.lothrazar.cyclicmagic.villager;

import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockSand;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityVillager.EmeraldForItems;
import net.minecraft.entity.passive.EntityVillager.PriceInfo;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

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
            //and SELL you this stuff for YOUR emeralds
            new ListItemForEmeraldsFixed(new ItemStack(Blocks.GRASS, 32), new PriceInfo(1, 2)), //GROUP 6
            new ListItemForEmeraldsFixed(new ItemStack(Blocks.FARMLAND, 32), new PriceInfo(1, 2)),
            new ListItemForEmeraldsFixed(new ItemStack(Blocks.DIRT, 32, BlockDirt.DirtType.PODZOL.getMetadata()), new PriceInfo(1, 2)),
            new ListItemForEmeraldsFixed(new ItemStack(Blocks.GRASS_PATH, 32), new PriceInfo(1, 2))
        }, {
            new ListItemForEmeraldsFixed(new ItemStack(Blocks.MYCELIUM, 1), new PriceInfo(12, 16)), //GROUP 7
            new ListItemForEmeraldsFixed(new ItemStack(Blocks.WATERLILY, 32), new PriceInfo(1, 2)),
            new ListItemForEmeraldsFixed(new ItemStack(Blocks.SAND, 32, BlockSand.EnumType.RED_SAND.ordinal()), new PriceInfo(1, 3)),
            new ListItemForEmeraldsFixed(new ItemStack(Items.DYE, 16, EnumDyeColor.BLACK.getDyeDamage()), new PriceInfo(1, 3))
        }, { //GROUP 8
            new ListItemForEmeraldsFixed(new ItemStack(Items.GHAST_TEAR), new PriceInfo(6, 18))
        }
    };
  }
}
