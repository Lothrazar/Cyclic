package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.data.Const.Potions;
import com.lothrazar.cyclicmagic.item.food.ItemPotionCustom;
import com.lothrazar.cyclicmagic.registry.GuideRegistry;
import com.lothrazar.cyclicmagic.registry.GuideRegistry.GuideCategory;
import com.lothrazar.cyclicmagic.registry.GuideRegistry.GuideItem;
//import com.lothrazar.cyclicmagic.registry.GuideRegistry.GuideCategory;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry.ChestType;
import com.lothrazar.cyclicmagic.registry.PotionEffectRegistry;
import com.lothrazar.cyclicmagic.registry.PotionTypeRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPotionModule extends BaseEventModule implements IHasConfig {
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
  private boolean enableViscous;
  private boolean enableSwimspeed;
  private boolean enableBounce;
  final static int SHORT = 60 + 30;
  final static int NORMAL = 60 * 3;
  final static int LONG = 60 * 8;
  @Override
  public void onPreInit() {
    
   
    
    
    final Item UPG_LENGTH = Items.DYE;
    final Item UPG_STRONG = Items.GOLD_INGOT;
    ItemPotionCustom potion_viscous = null;
    // https://github.com/MinecraftForge/MinecraftForge/issues/3323
    // https://github.com/PrinceOfAmber/Cyclic/issues/124
    //the actual effects need to be in regardless. ex: some items/charms use these even if the potion item isdisabled
    // http://www.minecraftforge.net/forum/index.php?topic=11024.0
    //   http://www.minecraftforge.net/forum/index.php?topic=12358.0
    if (enableViscous) {
      potion_viscous = new ItemPotionCustom(false);
      ItemRegistry.register(potion_viscous, "potion_viscous", null);
      GuideItem guide = GuideRegistry.register(GuideCategory.POTION, potion_viscous);
      guide.addRecipePage(addBrewingRecipe(
          PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.AWKWARD), //   awkward,
          new ItemStack(Items.DYE, 1, EnumDyeColor.BROWN.getDyeDamage()),
          new ItemStack(potion_viscous)));
    }
    if (enableEnder) {
      ItemPotionCustom potion_ender = new ItemPotionCustom(true, PotionEffectRegistry.ENDER, NORMAL, Potions.I, "item.potion_ender.tooltip");
      ItemRegistry.register(potion_ender, "potion_ender", null);
      GuideItem page = GuideRegistry.register(GuideCategory.POTION, potion_ender);
      ItemPotionCustom potion_ender_long = new ItemPotionCustom(true, PotionEffectRegistry.ENDER, LONG, Potions.I, "item.potion_ender.tooltip");
      ItemRegistry.register(potion_ender_long, "potion_ender_long", null);
      page.addRecipePage(addBrewingRecipe(
          potion_viscous,
          Items.ENDER_PEARL,
          potion_ender));
      page.addRecipePage(addBrewingRecipe(
          potion_ender,
          UPG_LENGTH,
          potion_ender_long));
    }
    if (enableMagnet) {
      ItemPotionCustom potion_magnet = new ItemPotionCustom(false, PotionEffectRegistry.MAGNET, NORMAL, Potions.I);
      ItemRegistry.register(potion_magnet, "potion_magnet", null);
      GuideItem page = GuideRegistry.register(GuideCategory.POTION, potion_magnet);
      ItemPotionCustom potion_magnet_long = new ItemPotionCustom(false, PotionEffectRegistry.MAGNET, LONG, Potions.I);
      ItemRegistry.register(potion_magnet_long, "potion_magnet_long", null);
      page.addRecipePage(addBrewingRecipe(
          new ItemStack(potion_viscous),
          new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()),
          new ItemStack(potion_magnet)));
      page.addRecipePage(addBrewingRecipe(
          potion_magnet,
          UPG_LENGTH,
          potion_magnet_long));
    }
    if (enableWaterwalk) {
      ItemPotionCustom potion_waterwalk = new ItemPotionCustom(false, PotionEffectRegistry.WATERWALK, NORMAL, Potions.I);
      ItemRegistry.register(potion_waterwalk, "potion_waterwalk", null);
      GuideItem page = GuideRegistry.register(GuideCategory.POTION, potion_waterwalk);
      ItemPotionCustom potion_waterwalk_long = new ItemPotionCustom(false, PotionEffectRegistry.WATERWALK, LONG, Potions.I);
      ItemRegistry.register(potion_waterwalk_long, "potion_waterwalk_long", null);
      page.addRecipePage(addBrewingRecipe(
          potion_viscous,
          Items.PRISMARINE_SHARD,
          potion_waterwalk));
      page.addRecipePage(addBrewingRecipe(
          potion_viscous,
          Items.BLAZE_ROD,
          potion_waterwalk));
      page.addRecipePage(addBrewingRecipe(
          potion_waterwalk,
          UPG_LENGTH,
          potion_waterwalk_long));
    }
    if (enableSwimspeed) {
      ItemPotionCustom potion_swimspeed = new ItemPotionCustom(false, PotionEffectRegistry.SWIMSPEED, NORMAL, Potions.I);
      ItemRegistry.register(potion_swimspeed, "potion_swimspeed", null);
      GuideItem page = GuideRegistry.register(GuideCategory.POTION, potion_swimspeed);
      ItemPotionCustom potion_swimspeed_long = new ItemPotionCustom(false, PotionEffectRegistry.SWIMSPEED, LONG, Potions.I);
      ItemRegistry.register(potion_swimspeed_long, "potion_swimspeed_long", null);
      page.addRecipePage(addBrewingRecipe(
          potion_viscous,
          Items.CARROT_ON_A_STICK,
          potion_swimspeed));
      page.addRecipePage(addBrewingRecipe(
          potion_swimspeed,
          UPG_LENGTH,
          potion_swimspeed_long));
    }
    if (enableSlowfall) {
      ItemPotionCustom potion_slowfall = new ItemPotionCustom(true, PotionEffectRegistry.SLOWFALL, NORMAL, Potions.I);
      ItemRegistry.register(potion_slowfall, "potion_slowfall", null);
      GuideItem page = GuideRegistry.register(GuideCategory.POTION, potion_slowfall);
      ItemPotionCustom potion_slowfall_long = new ItemPotionCustom(true, PotionEffectRegistry.SLOWFALL, LONG, Potions.I);
      ItemRegistry.register(potion_slowfall_long, "potion_slowfall_long", null);
      page.addRecipePage(addBrewingRecipe(
          new ItemStack(potion_viscous),
          new ItemStack(Items.FISH, 1, ItemFishFood.FishType.CLOWNFISH.getMetadata()),
          new ItemStack(potion_slowfall)));
      page.addRecipePage(addBrewingRecipe(
          new ItemStack(potion_viscous),
          new ItemStack(Items.FISH, 1, ItemFishFood.FishType.SALMON.getMetadata()),
          new ItemStack(potion_slowfall)));
      page.addRecipePage(addBrewingRecipe(
          new ItemStack(potion_slowfall),
          new ItemStack(UPG_LENGTH),
          new ItemStack(potion_slowfall_long)));
    }
    if (enableSnow) {
      ItemPotionCustom potion_snow = new ItemPotionCustom(true, PotionEffectRegistry.SNOW, NORMAL, Potions.I, "item.potion_snow.tooltip");
      ItemRegistry.register(potion_snow, "potion_snow", null);
      GuideItem page = GuideRegistry.register(GuideCategory.POTION, potion_snow);
      page.addRecipePage(addBrewingRecipe(
          new ItemStack(potion_viscous),
          new ItemStack(Blocks.ICE),
          new ItemStack(potion_snow)));
      ItemPotionCustom potion_snow_long = new ItemPotionCustom(true, PotionEffectRegistry.SNOW, LONG, Potions.I, "item.potion_snow.tooltip");
      ItemRegistry.register(potion_snow_long, "potion_snow_long", null);
      page.addRecipePage(addBrewingRecipe(
          new ItemStack(potion_snow),
          new ItemStack(UPG_LENGTH),
          new ItemStack(potion_snow_long)));
      LootTableRegistry.registerLoot(potion_snow, ChestType.IGLOO);
      LootTableRegistry.registerLoot(potion_snow_long, ChestType.IGLOO);
    }
    if (enableHBoost) {
      ItemPotionCustom potion_boost = new ItemPotionCustom(true, MobEffects.HEALTH_BOOST, NORMAL, Const.Potions.V);
      ItemRegistry.register(potion_boost, "potion_boost", null);
      GuideItem page = GuideRegistry.register(GuideCategory.POTION, potion_boost);
      ItemPotionCustom potion_boost_long = new ItemPotionCustom(true, MobEffects.HEALTH_BOOST, LONG, Const.Potions.V);
      ItemRegistry.register(potion_boost_long, "potion_boost_long", null);
      page.addRecipePage(addBrewingRecipe(
          potion_viscous,
          Items.GOLDEN_APPLE,
          potion_boost));
      page.addRecipePage(addBrewingRecipe(
          potion_boost,
          UPG_LENGTH,
          potion_boost_long));
      LootTableRegistry.registerLoot(potion_boost_long);
    }
    if (enableResist) {
      ItemPotionCustom potion_resistance = new ItemPotionCustom(true, MobEffects.RESISTANCE, NORMAL);
      ItemRegistry.register(potion_resistance, "potion_resistance", null);
      GuideItem page = GuideRegistry.register(GuideCategory.POTION, potion_resistance);
      ItemPotionCustom potion_resistance_strong = new ItemPotionCustom(true, MobEffects.RESISTANCE, SHORT, Const.Potions.II);
      ItemRegistry.register(potion_resistance_strong, "potion_resistance_strong", null);
      ItemPotionCustom potion_resistance_long = new ItemPotionCustom(true, MobEffects.RESISTANCE, LONG);
      ItemRegistry.register(potion_resistance_long, "potion_resistance_long", null);
      page.addRecipePage(addBrewingRecipe(
          potion_viscous,
          Items.DIAMOND,
          potion_resistance));
      page.addRecipePage(addBrewingRecipe(
          potion_resistance,
          UPG_LENGTH,
          potion_resistance_long));
      page.addRecipePage(addBrewingRecipe(
          potion_resistance,
          UPG_STRONG,
          potion_resistance_strong));
      LootTableRegistry.registerLoot(potion_resistance);
      LootTableRegistry.registerLoot(potion_resistance_long);
    }
    if (enableHaste) {
      ItemPotionCustom potion_haste = new ItemPotionCustom(false, MobEffects.HASTE, 60 * 3);
      ItemRegistry.register(potion_haste, "potion_haste", null);
      GuideItem page = GuideRegistry.register(GuideCategory.POTION, potion_haste);
      ItemPotionCustom potion_haste_strong = new ItemPotionCustom(false, MobEffects.HASTE, 90, Const.Potions.II);
      ItemRegistry.register(potion_haste_strong, "potion_haste_strong", null);
      ItemPotionCustom potion_haste_long = new ItemPotionCustom(false, MobEffects.HASTE, LONG);
      ItemRegistry.register(potion_haste_long, "potion_haste_long", null);
      page.addRecipePage(addBrewingRecipe(
          new ItemStack(potion_viscous),
          new ItemStack(Items.EMERALD),
          new ItemStack(potion_haste)));
      page.addRecipePage(addBrewingRecipe(
          new ItemStack(potion_haste),
          new ItemStack(UPG_LENGTH),
          new ItemStack(potion_haste_long)));
      page.addRecipePage(addBrewingRecipe(
          new ItemStack(potion_haste),
          new ItemStack(UPG_STRONG),
          new ItemStack(potion_haste_strong)));
      LootTableRegistry.registerLoot(potion_haste);
      LootTableRegistry.registerLoot(potion_haste_strong);
      //https://github.com/MinecraftForge/MinecraftForge/blob/f08f3c11053d414b57d03192dd72fcbfaef100f7/src/test/java/net/minecraftforge/test/BrewingRecipeRegistryTest.java
    }
    if (enableLuck) {
      ItemPotionCustom potion_luck = new ItemPotionCustom(true, MobEffects.LUCK, NORMAL);
      ItemRegistry.register(potion_luck, "potion_luck", null);
      GuideItem page = GuideRegistry.register(GuideCategory.POTION, potion_luck);
      ItemPotionCustom potion_luck_long = new ItemPotionCustom(true, MobEffects.LUCK, LONG);
      ItemRegistry.register(potion_luck_long, "potion_luck_long", null);
      page.addRecipePage(addBrewingRecipe(
          new ItemStack(potion_viscous),
          new ItemStack(Items.DYE, 1, EnumDyeColor.GREEN.getDyeDamage()),
          new ItemStack(potion_luck)));
      page.addRecipePage(addBrewingRecipe(
          potion_luck,
          UPG_LENGTH,
          potion_luck_long));
    }
    if (enableLevit) {
      ItemPotionCustom potion_levitation = new ItemPotionCustom(true, MobEffects.LEVITATION, NORMAL);
      ItemRegistry.register(potion_levitation, "potion_levitation", null);
      GuideItem page = GuideRegistry.register(GuideCategory.POTION, potion_levitation);
      ItemPotionCustom potion_levitation_long = new ItemPotionCustom(true, MobEffects.LEVITATION, LONG);
      ItemRegistry.register(potion_levitation_long, "potion_levitation_long", null);
      page.addRecipePage(addBrewingRecipe(
          new ItemStack(potion_viscous),
          new ItemStack(Items.CHORUS_FRUIT),
          new ItemStack(potion_levitation)));
      page.addRecipePage(addBrewingRecipe(
          new ItemStack(potion_levitation),
          new ItemStack(UPG_LENGTH),
          new ItemStack(potion_levitation_long)));
      LootTableRegistry.registerLoot(potion_levitation, ChestType.ENDCITY);
      LootTableRegistry.registerLoot(potion_levitation_long, ChestType.ENDCITY);
    }
    if (enableBounce) {
      ItemPotionCustom potion_bounce = new ItemPotionCustom(false, PotionEffectRegistry.BOUNCE, NORMAL);
      ItemRegistry.register(potion_bounce, "potion_bounce", null);
      GuideItem page = GuideRegistry.register(GuideCategory.POTION, potion_bounce);
      ItemPotionCustom potion_bounce_long = new ItemPotionCustom(false, PotionEffectRegistry.BOUNCE, LONG);
      ItemRegistry.register(potion_bounce_long, "potion_bounce_long", null);
      page.addRecipePage(addBrewingRecipe(
          new ItemStack(potion_viscous),
          new ItemStack(Items.SLIME_BALL),
          new ItemStack(potion_bounce)));
      page.addRecipePage(addBrewingRecipe(
          new ItemStack(potion_bounce),
          new ItemStack(UPG_LENGTH),
          new ItemStack(potion_bounce_long)));
    }
  }
  private static BrewingRecipe addBrewingRecipe(Item input, Item ingredient, Item output) {
    if (input == null || ingredient == null || output == null) {
      return null;
    }
    return addBrewingRecipe(
        new ItemStack(input),
        new ItemStack(ingredient),
        new ItemStack(output));
  }
  private static BrewingRecipe addBrewingRecipe(ItemStack input, ItemStack ingredient, ItemStack output) {
    if (input.isEmpty() || input.getItem() == null) {
      return null;
    }
    BrewingRecipe recipe = new BrewingRecipe(
        input,
        ingredient,
        output);
    BrewingRecipeRegistry.addRecipe(recipe);
    if (ModCyclic.logger.sendInfo) {//OMG UNIT TESTING WAAT
      ItemStack output0 = BrewingRecipeRegistry.getOutput(input, ingredient);
      if (output0.getItem() == output.getItem())
        ModCyclic.logger.log("Brewing Recipe succefully registered and working: " + output.getUnlocalizedName());
      else {
        ModCyclic.logger.error("Brewing Recipe FAILED to register" + output.getUnlocalizedName());
      }
    }
    return recipe;
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
    enableBounce = config.getBoolean("PotionBounce", category, true, Const.ConfigCategory.contentDefaultText);
    enableSwimspeed = config.getBoolean("PotionSwimSpeed", category, true, Const.ConfigCategory.contentDefaultText);
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
    enableViscous = config.getBoolean("PotionViscous", category, true, Const.ConfigCategory.contentDefaultText + ".  WARNING: any recipes using this items are gone if this is disabled; its used by every other potion");
  }
}
