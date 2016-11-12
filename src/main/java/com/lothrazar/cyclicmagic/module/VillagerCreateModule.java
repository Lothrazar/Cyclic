package com.lothrazar.cyclicmagic.module;
import java.util.Random;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.block.BlockSand;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityVillager.EmeraldForItems;
import net.minecraft.entity.passive.EntityVillager.ListItemForEmeralds;
import net.minecraft.entity.passive.EntityVillager.PriceInfo;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;

public class VillagerCreateModule extends BaseModule implements IHasConfig {
  private boolean extraVillagersEnabled;
  private void initDruid() {
    //vanilla example :  new VillagerProfession("minecraft:butcher", "minecraft:textures/entity/villager/butcher.png");
    String name;
    //TO TEST: /summon Villager ~ ~ ~ {Profession:6,Career:0}
    EntityVillager.ITradeList[][] druidTrades = {
        {
            new EmeraldForItems(Items.COOKED_FISH, new PriceInfo(9, 12)), // GROUP 1
            new EmeraldForItems(Items.APPLE, new PriceInfo(3, 6)),
            //TODO FIX IT IGNORING MY 33 quantity of stack, its hardcoded to stacksize 1
            new ListItemForEmeraldsFixed(new ItemStack(Blocks.SAND,33, BlockSand.EnumType.RED_SAND.ordinal()), new PriceInfo(2, 3)),//HACK
            new EmeraldForItems(Items.BEETROOT, new PriceInfo(8, 12))
        },
        {
            new EmeraldForItems(Items.FEATHER, new PriceInfo(12, 13)), //GROUP 2
            new EmeraldForItems(Items.WHEAT_SEEDS, new PriceInfo(50, 64)),
            new EmeraldForItems(Items.POISONOUS_POTATO, new PriceInfo(1, 3))
        },
        {
            new EmeraldForItems(Item.getItemFromBlock(Blocks.BROWN_MUSHROOM), new PriceInfo(8, 12)), //GROUP 3
            new EmeraldForItems(Item.getItemFromBlock(Blocks.RED_MUSHROOM), new PriceInfo(8, 12))
        },
        {
            new EmeraldForItems(Items.BEEF, new PriceInfo(14, 17)), //GROUP 4
            new EmeraldForItems(Items.RABBIT, new PriceInfo(14, 17)),
            new EmeraldForItems(Items.CHICKEN, new PriceInfo(14, 17))
        },
        {
            new EmeraldForItems(Items.WRITTEN_BOOK, new PriceInfo(1, 1)), //GROUP 5
            new EmeraldForItems(Items.FISH, new PriceInfo(9, 12)),
            new EmeraldForItems(Items.SPIDER_EYE, new PriceInfo(3, 6))
        },
        {
          new ListItemForEmeralds(new ItemStack(Blocks.GRASS,32), new PriceInfo(1, 1)), //GROUP 6
          new ListItemForEmeralds(new ItemStack(Blocks.GRASS_PATH,32), new PriceInfo(1, 1)),
          new ListItemForEmeralds(new ItemStack(Blocks.MYCELIUM,1), new PriceInfo(1, 3)),
        }
    };
    name = "druid";
    VillagerProfession druidProfession = new VillagerProfession(Const.MODRES + name,
        Const.MODRES + "textures/entity/villager/" + name + ".png",
        "minecraft:textures/entity/zombie_villager/zombie_villager.png");
    VillagerRegistry.instance().register(druidProfession);
    VillagerCareer druid = new VillagerCareer(druidProfession, name);
    for (int i = 0; i < druidTrades.length; i++) {
      druid.addTrade(i + 1, druidTrades[i]);
    }
  }
  private void initSage() {
    EntityVillager.ITradeList[][] sageTrades = {
        {
            new EmeraldForItems(Items.GUNPOWDER, new PriceInfo(5, 8)), //GROUP 1
            new EmeraldForItems(Items.NETHER_WART, new PriceInfo(12, 16))
        },
        {
            new EmeraldForItems(Items.BONE, new PriceInfo(8, 16)), //GROUP 2
            new EmeraldForItems(Items.MUTTON, new PriceInfo(4, 12))
        },
        {
            new EmeraldForItems(Items.BLAZE_ROD, new PriceInfo(8, 16)), //GROUP 3
            new EmeraldForItems(Items.SLIME_BALL, new PriceInfo(8, 16))
        },
        {
            new EmeraldForItems(Items.GHAST_TEAR, new PriceInfo(1, 2)), //GROUP 4
            new EmeraldForItems(Items.REDSTONE, new PriceInfo(4, 6))
        },
        {
            new EmeraldForItems(Items.GLOWSTONE_DUST, new PriceInfo(6, 8)), //GROUP 5
            new EmeraldForItems(Items.DIAMOND, new PriceInfo(1, 1)),
            new EmeraldForItems(Items.ENDER_PEARL, new PriceInfo(12, 16))
        },
        { //ListItemForEmeralds MEANS price for a SINGLE ITEM.. hwo to sell multiple?
            new ListItemForEmeralds(Items.EXPERIENCE_BOTTLE, new PriceInfo(1, 4)),
            new ListItemForEmeralds(new ItemStack(Blocks.OBSIDIAN,16), new PriceInfo(2, 4)),//GROUP 6
            new ListItemForEmeralds(new ItemStack(Blocks.CLAY,16), new PriceInfo(1, 1)),
            new ListItemForEmeralds(new ItemStack(Items.FISH, 4, ItemFishFood.FishType.PUFFERFISH.getMetadata()), new PriceInfo(1, 2)),
        }
    };
    //TO TEST: /summon Villager ~ ~ ~ {Profession:5,Career:0}
    String name = "sage";
    VillagerProfession sageProfession = new VillagerProfession(Const.MODRES + name,
        Const.MODRES + "textures/entity/villager/" + name + ".png",
        "minecraft:textures/entity/zombie_villager/zombie_villager.png");
    VillagerRegistry.instance().register(sageProfession);
    VillagerCareer sage = new VillagerCareer(sageProfession, name);
    for (int i = 0; i < sageTrades.length; i++) {
      sage.addTrade(i + 1, sageTrades[i]);
    }
  }
  @Override
  public void onInit() {
    if (extraVillagersEnabled) {
      initSage();
      initDruid();
    }
  }
  public void syncConfig(Configuration c) {
    String category = Const.ConfigCategory.villagers;
    c.addCustomCategoryComment(category, "Two new villagers with more trades");
    extraVillagersEnabled = c.getBoolean("More Trades", category, true, "Adds more  villager types (professions) with more trades such as gunpowder, blaze rods, beef, spider eyes, and more.  Spawn naturally and from mob eggs. ");
  }
  public static class ListItemForEmeraldsFixed extends ListItemForEmeralds implements EntityVillager.ITradeList
  {
      /** The item that is being bought for emeralds */
      public ItemStack itemToBuy;
      /**
       * The price info for the amount of emeralds to sell for, or if negative, the amount of the item to buy for
       * an emerald.
       */
      public EntityVillager.PriceInfo priceInfo;

      public ListItemForEmeraldsFixed(Item par1Item, EntityVillager.PriceInfo priceInfo)
      {
        super(par1Item,priceInfo);
          this.itemToBuy = new ItemStack(par1Item);
          this.priceInfo = priceInfo;
      }

      public ListItemForEmeraldsFixed(ItemStack stack, EntityVillager.PriceInfo priceInfo)
      {
        super(stack,priceInfo);
          this.itemToBuy = stack;
          this.priceInfo = priceInfo;
      }

      /**
       * Affects the given MerchantRecipeList to possibly add or remove MerchantRecipes.
       */
      public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random)
      {
          int i = 1;

          if (this.priceInfo != null)
          {
              i = this.priceInfo.getPrice(random);
          }

          ItemStack itemstack;
          ItemStack itemstack1;

          if (i < 0)
          {
              itemstack = new ItemStack(Items.EMERALD);
              itemstack1 = new ItemStack(this.itemToBuy.getItem(), -i, this.itemToBuy.getMetadata());
          }
          else
          {
              itemstack = new ItemStack(Items.EMERALD, i, 0);
              itemstack1 = this.itemToBuy.copy();

              //copy does same thing but yeah. the stackSize is hardcodeed 1 in vanilla
              //THIS IS THE FIX!!!! vanilla is hardcoded to 1 but we just do stacksize
              ////new ItemStack(this.itemToBuy.getItem(), this.itemToBuy.stackSize, this.itemToBuy.getMetadata());
          }

          recipeList.add(new MerchantRecipe(itemstack, itemstack1));
      }
  }
}
