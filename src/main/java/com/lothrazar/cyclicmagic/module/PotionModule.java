package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.item.ItemPotionCustom;
import com.lothrazar.cyclicmagic.registry.AchievementRegistry;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry;
import com.lothrazar.cyclicmagic.registry.PotionEffectRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry.ChestType;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.Const.Potions;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionModule extends BaseEventModule {
  public static final ItemPotionCustom potion_viscous = new ItemPotionCustom(false);
  public boolean cancelPotionInventoryShift;
  private boolean enableMagnet;
  private boolean enableWaterwalk;
  private boolean enableSlowfall;
  private boolean enableSnow;
  private boolean enableEnder;
  private boolean enableHaste;
  private boolean enableResist;
  private boolean enableLuck;
  private boolean enableLevit;
  private boolean enableHBoost;
  final static int SHORT = 60 + 30;
  final static int NORMAL = 60 * 3;
  final static int LONG = 60 * 8;
  @Override
  public void onInit() {
    //the actual effects need to be in regardless. ex: some items/charms use these even if the potion item isdisabled
    // http://www.minecraftforge.net/forum/index.php?topic=11024.0
    // ??? http://www.minecraftforge.net/forum/index.php?topic=12358.0
    //CORE/BASE POTION
    ItemStack awkward = BrewingRecipeRegistry.getOutput(new ItemStack(Items.POTIONITEM), new ItemStack(Items.NETHER_WART));
    ItemRegistry.addItem(potion_viscous, "potion_viscous");
    AchievementRegistry.registerItemAchievement(potion_viscous);
    addBrewingRecipe(
        awkward,
        new ItemStack(Items.DYE, 1, EnumDyeColor.BROWN.getDyeDamage()),
        new ItemStack(potion_viscous));
    if (enableEnder) {
      ItemPotionCustom potion_ender = new ItemPotionCustom(true, PotionEffectRegistry.enderEffect, NORMAL, Potions.I, "item.potion_ender.tooltip");
      ItemRegistry.addItem(potion_ender, "potion_ender");
      ItemPotionCustom potion_ender_long = new ItemPotionCustom(true, PotionEffectRegistry.enderEffect, LONG, Potions.I, "item.potion_ender.tooltip");
      ItemRegistry.addItem(potion_ender_long, "potion_ender_long");
      addBrewingRecipe(
          potion_viscous,
          Items.ENDER_PEARL,
          potion_ender);
      addBrewingRecipe(
          potion_ender,
          Items.REDSTONE,
          potion_ender_long);
    }
    if (enableMagnet) {
      ItemPotionCustom potion_magnet = new ItemPotionCustom(false, PotionEffectRegistry.magnetEffect, NORMAL, Potions.I);
      ItemPotionCustom potion_magnet_long = new ItemPotionCustom(false, PotionEffectRegistry.magnetEffect, LONG, Potions.I);
      ItemRegistry.addItem(potion_magnet, "potion_magnet");
      ItemRegistry.addItem(potion_magnet_long, "potion_magnet_long");
      addBrewingRecipe(
          new ItemStack(potion_viscous),
          new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()),
          new ItemStack(potion_magnet));
      addBrewingRecipe(
          potion_magnet,
          Items.REDSTONE,
          potion_magnet_long);
    }
    if (enableWaterwalk) {
      ItemPotionCustom potion_waterwalk = new ItemPotionCustom(false, PotionEffectRegistry.waterwalkEffect, NORMAL, Potions.I);
      ItemPotionCustom potion_waterwalk_long = new ItemPotionCustom(false, PotionEffectRegistry.waterwalkEffect, LONG, Potions.I);
      ItemRegistry.addItem(potion_waterwalk, "potion_waterwalk");
      ItemRegistry.addItem(potion_waterwalk_long, "potion_waterwalk_long");
      addBrewingRecipe(
          potion_viscous,
          Items.PRISMARINE_SHARD,
          potion_waterwalk);
      addBrewingRecipe(
          potion_viscous,
          Items.BLAZE_POWDER,
          potion_waterwalk);
      addBrewingRecipe(
          potion_waterwalk,
          Items.REDSTONE,
          potion_waterwalk_long);
    }
    if (enableSlowfall) {
      ItemPotionCustom potion_slowfall = new ItemPotionCustom(true, PotionEffectRegistry.slowfallEffect, NORMAL, Potions.I);
      ItemPotionCustom potion_slowfall_long = new ItemPotionCustom(true, PotionEffectRegistry.slowfallEffect, LONG, Potions.I);
      ItemRegistry.addItem(potion_slowfall, "potion_slowfall");
      ItemRegistry.addItem(potion_slowfall_long, "potion_slowfall_long");
      addBrewingRecipe(
          new ItemStack(potion_viscous),
          new ItemStack(Items.FISH, 1, ItemFishFood.FishType.CLOWNFISH.getMetadata()),
          new ItemStack(potion_slowfall));
      addBrewingRecipe(
          new ItemStack(potion_viscous),
          new ItemStack(Items.FISH, 1, ItemFishFood.FishType.SALMON.getMetadata()),
          new ItemStack(potion_slowfall));
      addBrewingRecipe(
          new ItemStack(potion_slowfall),
          new ItemStack(Items.REDSTONE),
          new ItemStack(potion_slowfall_long));
    }
    if (enableSnow) {
      ItemPotionCustom potion_snow = new ItemPotionCustom(true, PotionEffectRegistry.snowEffect, NORMAL, Potions.I, "item.potion_snow.tooltip");
      ItemRegistry.addItem(potion_snow, "potion_snow");
      addBrewingRecipe(
          new ItemStack(potion_viscous),
          new ItemStack(Blocks.ICE),
          new ItemStack(potion_snow));
      ItemPotionCustom potion_snow_long = new ItemPotionCustom(true, PotionEffectRegistry.snowEffect, LONG, Potions.I, "item.potion_snow.tooltip");
      ItemRegistry.addItem(potion_snow_long, "potion_snow_long");
      addBrewingRecipe(
          new ItemStack(potion_snow),
          new ItemStack(Items.REDSTONE),
          new ItemStack(potion_snow_long));
      LootTableRegistry.registerLoot(potion_snow, ChestType.IGLOO);
      LootTableRegistry.registerLoot(potion_snow_long, ChestType.IGLOO);
    }
    if (enableHBoost) {
      ItemPotionCustom potion_boost = new ItemPotionCustom(true, MobEffects.HEALTH_BOOST, NORMAL, Const.Potions.V);
      ItemPotionCustom potion_boost_long = new ItemPotionCustom(true, MobEffects.HEALTH_BOOST, LONG, Const.Potions.V);
      ItemRegistry.addItem(potion_boost, "potion_boost");
      ItemRegistry.addItem(potion_boost_long, "potion_boost_long");
      addBrewingRecipe(
          potion_viscous,
          Items.GOLDEN_APPLE,
          potion_boost);
      addBrewingRecipe(
          potion_boost,
          Items.REDSTONE,
          potion_boost_long);
      LootTableRegistry.registerLoot(potion_boost_long);
    }
    if (enableResist) {
      ItemPotionCustom potion_resistance = new ItemPotionCustom(true, MobEffects.RESISTANCE, NORMAL);
      ItemPotionCustom potion_resistance_strong = new ItemPotionCustom(true, MobEffects.RESISTANCE, SHORT, Const.Potions.II);
      ItemPotionCustom potion_resistance_long = new ItemPotionCustom(true, MobEffects.RESISTANCE, LONG);
      ItemRegistry.addItem(potion_resistance, "potion_resistance");
      ItemRegistry.addItem(potion_resistance_long, "potion_resistance_long");
      ItemRegistry.addItem(potion_resistance_strong, "potion_resistance_strong");
      addBrewingRecipe(
          potion_viscous,
          Items.DIAMOND,
          potion_resistance);
      addBrewingRecipe(
          potion_resistance,
          Items.REDSTONE,
          potion_resistance_long);
      addBrewingRecipe(
          potion_resistance,
          Items.GLOWSTONE_DUST,
          potion_resistance_strong);
      LootTableRegistry.registerLoot(potion_resistance_long);
    }
    if (enableHaste) {
      ItemPotionCustom potion_haste = new ItemPotionCustom(false, MobEffects.HASTE, 60 * 3);
      ItemPotionCustom potion_haste_strong = new ItemPotionCustom(false, MobEffects.HASTE, 90, Const.Potions.II);
      ItemPotionCustom potion_haste_long = new ItemPotionCustom(false, MobEffects.HASTE, LONG);
      ItemRegistry.addItem(potion_haste, "potion_haste");
      ItemRegistry.addItem(potion_haste_long, "potion_haste_long");
      ItemRegistry.addItem(potion_haste_strong, "potion_haste_strong");
      addBrewingRecipe(
          new ItemStack(potion_viscous),
          new ItemStack(Items.EMERALD),
          new ItemStack(potion_haste));
      addBrewingRecipe(
          new ItemStack(potion_haste),
          new ItemStack(Items.REDSTONE),
          new ItemStack(potion_haste_long));
      addBrewingRecipe(
          new ItemStack(potion_haste),
          new ItemStack(Items.GLOWSTONE_DUST),
          new ItemStack(potion_haste_strong));
      LootTableRegistry.registerLoot(potion_haste_strong);
//      System.out.println("testPotionStrong= "+testPotionStrong);
//      ItemStack output1 = BrewingRecipeRegistry.getOutput(new ItemStack(potion_haste), new ItemStack(Items.GLOWSTONE_DUST));
//      if (output1.getItem() == potion_haste_strong)
//        System.out.println("RECIPE succefully registered and working. potion_haste_strong obtained.");
//      else {
//        System.out.println("RECIPE FAILED potion_haste_strong : "+output1.getUnlocalizedName());
//      }
//      if (output1.getItem() == potion_haste)
//        System.out.println("RECIPE output matches input? WHY.");
      
//      RECIPE FAILED potion_haste_strong : item.potion_haste
//     RECIPE output matches input? WHY.
      
      //https://github.com/MinecraftForge/MinecraftForge/blob/f08f3c11053d414b57d03192dd72fcbfaef100f7/src/test/java/net/minecraftforge/test/BrewingRecipeRegistryTest.java
      addBrewingRecipe(new ItemStack(Items.DIAMOND_SWORD), new ItemStack(Items.ROTTEN_FLESH), new ItemStack(Items.DIAMOND_HOE));

 
    }
    if (enableLuck) {
      ItemPotionCustom potion_luck = new ItemPotionCustom(true, MobEffects.LUCK, NORMAL);
      ItemPotionCustom potion_luck_long = new ItemPotionCustom(true, MobEffects.LUCK, LONG);
      ItemRegistry.addItem(potion_luck, "potion_luck");
      ItemRegistry.addItem(potion_luck_long, "potion_luck_long");
      addBrewingRecipe(
          new ItemStack(potion_viscous),
          new ItemStack(Items.SLIME_BALL),
          new ItemStack(potion_luck));
      addBrewingRecipe(
          potion_luck,
          Items.REDSTONE,
          potion_luck_long);
    }
    if (enableLevit) {
      ItemPotionCustom potion_levitation = new ItemPotionCustom(true, MobEffects.LEVITATION, NORMAL);
      ItemPotionCustom potion_levitation_long = new ItemPotionCustom(true, MobEffects.LEVITATION, LONG);
      ItemRegistry.addItem(potion_levitation, "potion_levitation");
      ItemRegistry.addItem(potion_levitation_long, "potion_levitation_long");
      addBrewingRecipe(
          new ItemStack(potion_viscous),
          new ItemStack(Items.CHORUS_FRUIT),
          new ItemStack(potion_levitation));
      addBrewingRecipe(
          new ItemStack(potion_levitation),
          new ItemStack(Items.REDSTONE),
          new ItemStack(potion_levitation_long));
    }
  }
  private static void addBrewingRecipe(Item input, Item ingredient, Item output) {
    addBrewingRecipe(
        new ItemStack(input),
        new ItemStack(ingredient),
        new ItemStack(output));
  }
  private static void addBrewingRecipe(ItemStack input, ItemStack ingredient, ItemStack output) {
    BrewingRecipeRegistry.addRecipe(
        input,
        ingredient,
        output);
    ItemStack output0 = BrewingRecipeRegistry.getOutput(input, ingredient);
    if(output0.getItem() == output.getItem())
        ModMain.logger.info("Recipe succefully registered and working: "+output.getUnlocalizedName());
    else{
      ModMain.logger.info("Recipe FAILED"+output.getUnlocalizedName());
    }
  }
  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onPotionShiftEvent(GuiScreenEvent.PotionShiftEvent event) {
    event.setCanceled(cancelPotionInventoryShift);
  }
  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.inventory;
    cancelPotionInventoryShift = config.getBoolean("Potion Inventory Shift", category, true,
        "When true, this blocks the potions moving the inventory over");
    category = Const.ConfigCategory.content;
    enableMagnet = config.getBoolean("PotionMagnet", category, true, Const.ConfigCategory.contentDefaultText);
    enableWaterwalk = config.getBoolean("PotionWaterwalk", category, true, Const.ConfigCategory.contentDefaultText);
    enableSlowfall = config.getBoolean("PotionSlowfall", category, true, Const.ConfigCategory.contentDefaultText);
    enableSnow = config.getBoolean("PotionSnow", category, true, Const.ConfigCategory.contentDefaultText);
    enableEnder = config.getBoolean("PotionEnder", category, true, Const.ConfigCategory.contentDefaultText);
    enableHaste = config.getBoolean("Potionhaste", category, true, Const.ConfigCategory.contentDefaultText);
    enableResist = config.getBoolean("PotionResistance", category, true, Const.ConfigCategory.contentDefaultText);
    enableLuck = config.getBoolean("PotionLuck", category, true, Const.ConfigCategory.contentDefaultText);
    enableLevit = config.getBoolean("PotionLevitation", category, true, Const.ConfigCategory.contentDefaultText);
    enableHBoost = config.getBoolean("PotionHealthBoost", category, true, Const.ConfigCategory.contentDefaultText);
  }
}
