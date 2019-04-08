package com.lothrazar.cyclicmagic.villager.sage;

import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.villager.ListItemForEmeraldsFixed;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityVillager.EmeraldForItems;
import net.minecraft.entity.passive.EntityVillager.PriceInfo;
import net.minecraft.init.Items;
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
            new EmeraldForItems(Items.ENDER_PEARL, new PriceInfo(16, 16))
        }, {
            new EmeraldForItems(Items.NAME_TAG, new PriceInfo(1, 1)) //GROUP 6 
        }, { //GROUP 7
            // hardcoded in ItemSkull i found
            //  private static final String[] SKULL_TYPES = new String[] {"skeleton", "wither", "zombie", "char", "creeper", "dragon"};
            new ListItemForEmeraldsFixed(new ItemStack(Items.SKULL, 1, Const.skull_skeleton), new PriceInfo(28, 32)),
            new ListItemForEmeraldsFixed(new ItemStack(Items.SKULL, 1, Const.skull_creeper), new PriceInfo(28, 32)),
            new ListItemForEmeraldsFixed(new ItemStack(Items.SKULL, 1, Const.skull_zombie), new PriceInfo(28, 32)),
            new ListItemForEmeraldsFixed(new ItemStack(Items.SKULL, 1, Const.skull_player), new PriceInfo(28, 32))
        }
    };
  }
}
