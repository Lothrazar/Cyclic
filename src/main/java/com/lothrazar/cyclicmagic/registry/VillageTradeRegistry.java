package com.lothrazar.cyclicmagic.registry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityVillager.EmeraldForItems;
import net.minecraft.entity.passive.EntityVillager.ListItemForEmeralds;
import net.minecraft.entity.passive.EntityVillager.PriceInfo;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;

public class VillageTradeRegistry {
  private static boolean extraVillagersEnabled;
  public static void register() {
    if (!extraVillagersEnabled) { return; }
    EntityVillager.ITradeList[][] druidTrades = {
        { new EmeraldForItems(Items.COOKED_FISH, new PriceInfo(9, 12)), new EmeraldForItems(Items.APPLE, new PriceInfo(3, 6)), new EmeraldForItems(Items.BEETROOT, new PriceInfo(8, 12))
        },
        { new EmeraldForItems(Items.FEATHER, new PriceInfo(12, 13)), new EmeraldForItems(Items.WHEAT_SEEDS, new PriceInfo(50, 64))
        },
        { new EmeraldForItems(Item.getItemFromBlock(Blocks.BROWN_MUSHROOM), new PriceInfo(8, 12)), new EmeraldForItems(Item.getItemFromBlock(Blocks.RED_MUSHROOM), new PriceInfo(8, 12))
        },
        { new EmeraldForItems(Items.BEEF, new PriceInfo(14, 17)), new EmeraldForItems(Items.RABBIT, new PriceInfo(14, 17)), new EmeraldForItems(Items.CHICKEN, new PriceInfo(14, 17))
        },
        { new EmeraldForItems(Items.POISONOUS_POTATO, new PriceInfo(1, 3)), new EmeraldForItems(Items.SPIDER_EYE, new PriceInfo(3, 6)), new EmeraldForItems(Items.WRITTEN_BOOK, new PriceInfo(1, 1))
        }
    };
    //		ItemStack stack = UtilNBT.buildEnchantedBook(Enchantment.getEnchantmentByID(2), (short)1);
    //		GameRegistry.addShapelessRecipe(stack, Blocks.GRASS,Items.STICK);
    //test to make sure the book works. if it works in craft but not in trade, then ??
    EntityVillager.ITradeList[][] sageTrades = {
        { new EmeraldForItems(Items.GUNPOWDER, new PriceInfo(5, 8)), new EmeraldForItems(Items.NETHER_WART, new PriceInfo(12, 16))
        //				,new ListItemForEmeralds(stack, new PriceInfo(1, 3))
        },
        { new EmeraldForItems(Items.BONE, new PriceInfo(26, 32)), new EmeraldForItems(Items.MUTTON, new PriceInfo(12, 16))
        },
        { new EmeraldForItems(Items.BLAZE_ROD, new PriceInfo(12, 16)), new EmeraldForItems(Items.SLIME_BALL, new PriceInfo(12, 16))
        },
        { new EmeraldForItems(Items.GHAST_TEAR, new PriceInfo(2, 3)), new EmeraldForItems(Items.REDSTONE, new PriceInfo(4, 6))
        },
        { new EmeraldForItems(Items.GLOWSTONE_DUST, new PriceInfo(12, 16)), new ListItemForEmeralds(Items.EXPERIENCE_BOTTLE, new PriceInfo(2, 4)), new EmeraldForItems(Items.DIAMOND, new PriceInfo(1, 1)), new EmeraldForItems(Items.ENDER_PEARL, new PriceInfo(12, 16))
        }
    };
    //vanilla example :  new VillagerProfession("minecraft:butcher", "minecraft:textures/entity/villager/butcher.png");
    String name;
    //TO TEST: /summon Villager ~ ~ ~ {Profession:5,Career:0}
    //TODO: make my own zombie textures
    name = "druid";
    VillagerProfession druidProfession = new VillagerProfession(Const.MODRES + name,
        Const.MODRES + "textures/entity/villager/" + name + ".png",
        "minecraft:textures/entity/zombie_villager/zombie_villager.png");
    VillagerRegistry.instance().register(druidProfession);
    VillagerCareer druid = new VillagerCareer(druidProfession, name);
    for (int i = 0; i < druidTrades.length; i++) {
      druid.addTrade(i + 1, druidTrades[i]);
    }
    //TO TEST: /summon Villager ~ ~ ~ {Profession:6,Career:0}
    name = "sage";
    VillagerProfession sageProfession = new VillagerProfession(Const.MODRES + name,
        Const.MODRES + "textures/entity/villager/" + name + ".png",
        "minecraft:textures/entity/zombie_villager/zombie_villager.png");
    VillagerRegistry.instance().register(sageProfession);
    VillagerCareer sage = new VillagerCareer(sageProfession, name);
    for (int i = 0; i < sageTrades.length; i++) {
      sage.addTrade(i + 1, sageTrades[i]);
    }
  }
  public static void syncConfig(Configuration c) {
    String category = Const.ConfigCategory.villagers;
    c.addCustomCategoryComment(category, "Two new villagers with more trades");
    extraVillagersEnabled = c.getBoolean("More Trades", category, true, "Adds more  villager types (professions) with more trades such as gunpowder, blaze rods, beef, spider eyes, and more.  Spawn naturally and from mob eggs. ");
  }
}
