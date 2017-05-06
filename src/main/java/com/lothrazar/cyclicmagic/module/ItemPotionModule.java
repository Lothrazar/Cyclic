package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.item.ItemPotionCustom;
import com.lothrazar.cyclicmagic.registry.AchievementRegistry;
import com.lothrazar.cyclicmagic.registry.GuideRegistry;
import com.lothrazar.cyclicmagic.registry.GuideRegistry.GuideCategory;
import com.lothrazar.cyclicmagic.registry.GuideRegistry.GuideItem;
//import com.lothrazar.cyclicmagic.registry.GuideRegistry.GuideCategory;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.JeiDescriptionRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry;
import com.lothrazar.cyclicmagic.registry.PotionEffectRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry.ChestType;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.Const.Potions;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPotionModule extends BaseEventModule implements IHasConfig {
  public static ItemPotionCustom potion_viscous = null;
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
    final Item UPG_LENGTH = Items.DYE;//long fails
    final Item UPG_STRONG = Items.GOLD_INGOT;//strong works?
    // https://github.com/MinecraftForge/MinecraftForge/issues/3323
    // https://github.com/PrinceOfAmber/Cyclic/issues/124
    //the actual effects need to be in regardless. ex: some items/charms use these even if the potion item isdisabled
    // http://www.minecraftforge.net/forum/index.php?topic=11024.0
    // ??? http://www.minecraftforge.net/forum/index.php?topic=12358.0
    //CORE/BASE POTION
    //    GuideRegistry.register(cat,item, recipe,null);
    //
    if (enableViscous) {
      potion_viscous = new ItemPotionCustom(false);
      //      ItemStack awkward = BrewingRecipeRegistry.getOutput(new ItemStack(Items.POTIONITEM), new ItemStack(Items.NETHER_WART));
      ItemRegistry.addItem(potion_viscous, "potion_viscous", null);
      GuideItem guide = GuideRegistry.register(GuideCategory.POTION, potion_viscous);
      AchievementRegistry.registerItemAchievement(potion_viscous);
      guide.addRecipePage(  addBrewingRecipe(
          PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.AWKWARD), //   awkward,
          new ItemStack(Items.DYE, 1, EnumDyeColor.BROWN.getDyeDamage()),
          new ItemStack(potion_viscous)));
      JeiDescriptionRegistry.registerWithJeiDescription(potion_viscous);
      guide.addTextPage("item.potion_viscous.guide");
    }
    if (enableEnder) {
      ItemPotionCustom potion_ender = new ItemPotionCustom(true, PotionEffectRegistry.ENDER, NORMAL, Potions.I, "item.potion_ender.tooltip");
      ItemRegistry.addItem(potion_ender, "potion_ender");
      GuideItem page = GuideRegistry.register(GuideCategory.POTION, potion_ender);
      ItemPotionCustom potion_ender_long = new ItemPotionCustom(true, PotionEffectRegistry.ENDER, LONG, Potions.I, "item.potion_ender.tooltip");
//=======
//      ItemPotionCustom potion_ender_long = new ItemPotionCustom(true, PotionEffectRegistry.ENDER, LONG, Potions.I, "item.potion_ender.tooltip");
//>>>>>>> bbeb49c0fa1ca7b0defc4c391c93d873df9406e6
      ItemRegistry.addItem(potion_ender_long, "potion_ender_long");
      if (potion_viscous != null)
        addBrewingRecipe(
            potion_viscous,
            Items.ENDER_PEARL,
            potion_ender);
      addBrewingRecipe(
          potion_ender,
          UPG_LENGTH,
          potion_ender_long);
      page.addTextPage("item.potion_ender_long.guide");
      JeiDescriptionRegistry.registerWithJeiDescription(potion_ender);
    }
    if (enableMagnet) {
      ItemPotionCustom potion_magnet = new ItemPotionCustom(false, PotionEffectRegistry.MAGNET, NORMAL, Potions.I);
      ItemPotionCustom potion_magnet_long = new ItemPotionCustom(false, PotionEffectRegistry.MAGNET, LONG, Potions.I);
      ItemRegistry.addItem(potion_magnet, "potion_magnet");
      ItemRegistry.addItem(potion_magnet_long, "potion_magnet_long");
      if (potion_viscous != null)
        addBrewingRecipe(
            new ItemStack(potion_viscous),
            new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()),
            new ItemStack(potion_magnet));
      addBrewingRecipe(
          potion_magnet,
          UPG_LENGTH,
          potion_magnet_long);
      JeiDescriptionRegistry.registerWithJeiDescription(potion_magnet);
    }
    if (enableWaterwalk) {
      ItemPotionCustom potion_waterwalk = new ItemPotionCustom(false, PotionEffectRegistry.WATERWALK, NORMAL, Potions.I);
      ItemPotionCustom potion_waterwalk_long = new ItemPotionCustom(false, PotionEffectRegistry.WATERWALK, LONG, Potions.I);
      ItemRegistry.addItem(potion_waterwalk, "potion_waterwalk");
      ItemRegistry.addItem(potion_waterwalk_long, "potion_waterwalk_long");
      if (potion_viscous != null)
        addBrewingRecipe(
            potion_viscous,
            Items.PRISMARINE_SHARD,
            potion_waterwalk);
      if (potion_viscous != null)
        addBrewingRecipe(
            potion_viscous,
            Items.BLAZE_ROD,
            potion_waterwalk);
      addBrewingRecipe(
          potion_waterwalk,
          UPG_LENGTH,
          potion_waterwalk_long);
      JeiDescriptionRegistry.registerWithJeiDescription(potion_waterwalk);
    }
    if (enableSwimspeed) {
      ItemPotionCustom potion_swimspeed = new ItemPotionCustom(false, PotionEffectRegistry.SWIMSPEED, NORMAL, Potions.I);
      ItemPotionCustom potion_swimspeed_long = new ItemPotionCustom(false, PotionEffectRegistry.SWIMSPEED, LONG, Potions.I);
      ItemRegistry.addItem(potion_swimspeed, "potion_swimspeed");
      ItemRegistry.addItem(potion_swimspeed_long, "potion_swimspeed_long");
      if (potion_viscous != null)
        addBrewingRecipe(
            potion_viscous,
            Items.CARROT_ON_A_STICK,
            potion_swimspeed);
      addBrewingRecipe(
          potion_swimspeed,
          UPG_LENGTH,
          potion_swimspeed_long);
      JeiDescriptionRegistry.registerWithJeiDescription(potion_swimspeed);
    }
    if (enableSlowfall) {
      ItemPotionCustom potion_slowfall = new ItemPotionCustom(true, PotionEffectRegistry.SLOWFALL, NORMAL, Potions.I);
      ItemPotionCustom potion_slowfall_long = new ItemPotionCustom(true, PotionEffectRegistry.SLOWFALL, LONG, Potions.I);
      ItemRegistry.addItem(potion_slowfall, "potion_slowfall");
      ItemRegistry.addItem(potion_slowfall_long, "potion_slowfall_long");
      if (potion_viscous != null)
        addBrewingRecipe(
            new ItemStack(potion_viscous),
            new ItemStack(Items.FISH, 1, ItemFishFood.FishType.CLOWNFISH.getMetadata()),
            new ItemStack(potion_slowfall));
      if (potion_viscous != null)
        addBrewingRecipe(
            new ItemStack(potion_viscous),
            new ItemStack(Items.FISH, 1, ItemFishFood.FishType.SALMON.getMetadata()),
            new ItemStack(potion_slowfall));
      addBrewingRecipe(
          new ItemStack(potion_slowfall),
          new ItemStack(UPG_LENGTH),
          new ItemStack(potion_slowfall_long));
      JeiDescriptionRegistry.registerWithJeiDescription(potion_slowfall);
    }
    if (enableSnow) {
      ItemPotionCustom potion_snow = new ItemPotionCustom(true, PotionEffectRegistry.SNOW, NORMAL, Potions.I, "item.potion_snow.tooltip");
      ItemRegistry.addItem(potion_snow, "potion_snow");
      if (potion_viscous != null)
        addBrewingRecipe(
            new ItemStack(potion_viscous),
            new ItemStack(Blocks.ICE),
            new ItemStack(potion_snow));
      ItemPotionCustom potion_snow_long = new ItemPotionCustom(true, PotionEffectRegistry.SNOW, LONG, Potions.I, "item.potion_snow.tooltip");
      ItemRegistry.addItem(potion_snow_long, "potion_snow_long");
      addBrewingRecipe(
          new ItemStack(potion_snow),
          new ItemStack(UPG_LENGTH),
          new ItemStack(potion_snow_long));
      LootTableRegistry.registerLoot(potion_snow, ChestType.IGLOO);
      LootTableRegistry.registerLoot(potion_snow_long, ChestType.IGLOO);
      JeiDescriptionRegistry.registerWithJeiDescription(potion_snow);
    }
    if (enableHBoost) {
      ItemPotionCustom potion_boost = new ItemPotionCustom(true, MobEffects.HEALTH_BOOST, NORMAL, Const.Potions.V);
      ItemPotionCustom potion_boost_long = new ItemPotionCustom(true, MobEffects.HEALTH_BOOST, LONG, Const.Potions.V);
      ItemRegistry.addItem(potion_boost, "potion_boost");
      ItemRegistry.addItem(potion_boost_long, "potion_boost_long");
      if (potion_viscous != null)
        addBrewingRecipe(
            potion_viscous,
            Items.GOLDEN_APPLE,
            potion_boost);
      addBrewingRecipe(
          potion_boost,
          UPG_LENGTH,
          potion_boost_long);
      LootTableRegistry.registerLoot(potion_boost);
      LootTableRegistry.registerLoot(potion_boost_long);
      JeiDescriptionRegistry.registerWithJeiDescription(potion_boost);
    }
    if (enableResist) {
      ItemPotionCustom potion_resistance = new ItemPotionCustom(true, MobEffects.RESISTANCE, NORMAL);
      ItemPotionCustom potion_resistance_strong = new ItemPotionCustom(true, MobEffects.RESISTANCE, SHORT, Const.Potions.II);
      ItemPotionCustom potion_resistance_long = new ItemPotionCustom(true, MobEffects.RESISTANCE, LONG);
      ItemRegistry.addItem(potion_resistance, "potion_resistance");
      ItemRegistry.addItem(potion_resistance_long, "potion_resistance_long");
      ItemRegistry.addItem(potion_resistance_strong, "potion_resistance_strong");
      if (potion_viscous != null)
        addBrewingRecipe(
            potion_viscous,
            Items.DIAMOND,
            potion_resistance);
      addBrewingRecipe(
          potion_resistance,
          UPG_LENGTH,
          potion_resistance_long);
      addBrewingRecipe(
          potion_resistance,
          UPG_STRONG,
          potion_resistance_strong);
      LootTableRegistry.registerLoot(potion_resistance);
      LootTableRegistry.registerLoot(potion_resistance_long);
    }
    if (enableHaste) {
      ItemPotionCustom potion_haste = new ItemPotionCustom(false, MobEffects.HASTE, 60 * 3);
      ItemPotionCustom potion_haste_strong = new ItemPotionCustom(false, MobEffects.HASTE, 90, Const.Potions.II);
      ItemPotionCustom potion_haste_long = new ItemPotionCustom(false, MobEffects.HASTE, LONG);
      ItemRegistry.addItem(potion_haste, "potion_haste");
      ItemRegistry.addItem(potion_haste_long, "potion_haste_long");
      ItemRegistry.addItem(potion_haste_strong, "potion_haste_strong");
      if (potion_viscous != null)
        addBrewingRecipe(
            new ItemStack(potion_viscous),
            new ItemStack(Items.EMERALD),
            new ItemStack(potion_haste));
      addBrewingRecipe(
          new ItemStack(potion_haste),
          new ItemStack(UPG_LENGTH),
          new ItemStack(potion_haste_long));
      addBrewingRecipe(
          new ItemStack(potion_haste),
          new ItemStack(UPG_STRONG),
          new ItemStack(potion_haste_strong));
      LootTableRegistry.registerLoot(potion_haste);
      LootTableRegistry.registerLoot(potion_haste_strong);
      //https://github.com/MinecraftForge/MinecraftForge/blob/f08f3c11053d414b57d03192dd72fcbfaef100f7/src/test/java/net/minecraftforge/test/BrewingRecipeRegistryTest.java
    }
    if (enableLuck) {
      ItemPotionCustom potion_luck = new ItemPotionCustom(true, MobEffects.LUCK, NORMAL);
      ItemPotionCustom potion_luck_long = new ItemPotionCustom(true, MobEffects.LUCK, LONG);
      ItemRegistry.addItem(potion_luck, "potion_luck");
      ItemRegistry.addItem(potion_luck_long, "potion_luck_long");
      if (potion_viscous != null)
        addBrewingRecipe(
            new ItemStack(potion_viscous),
            new ItemStack(Items.DYE, 1, EnumDyeColor.GREEN.getDyeDamage()),
            new ItemStack(potion_luck));
      addBrewingRecipe(
          potion_luck,
          UPG_LENGTH,
          potion_luck_long);
    }
    if (enableLevit) {
      ItemPotionCustom potion_levitation = new ItemPotionCustom(true, MobEffects.LEVITATION, NORMAL);
      ItemPotionCustom potion_levitation_long = new ItemPotionCustom(true, MobEffects.LEVITATION, LONG);
      ItemRegistry.addItem(potion_levitation, "potion_levitation");
      ItemRegistry.addItem(potion_levitation_long, "potion_levitation_long");
      if (potion_viscous != null)
        addBrewingRecipe(
            new ItemStack(potion_viscous),
            new ItemStack(Items.CHORUS_FRUIT),
            new ItemStack(potion_levitation));
      addBrewingRecipe(
          new ItemStack(potion_levitation),
          new ItemStack(UPG_LENGTH),
          new ItemStack(potion_levitation_long));
      LootTableRegistry.registerLoot(potion_levitation, ChestType.ENDCITY);
      LootTableRegistry.registerLoot(potion_levitation_long, ChestType.ENDCITY);
    }
    if (enableBounce) {
      ItemPotionCustom potion_bounce = new ItemPotionCustom(false, PotionEffectRegistry.BOUNCE, NORMAL);
      ItemPotionCustom potion_bounce_long = new ItemPotionCustom(false, PotionEffectRegistry.BOUNCE, LONG);
      ItemRegistry.addItem(potion_bounce, "potion_bounce");
      ItemRegistry.addItem(potion_bounce_long, "potion_bounce_long");
      if (potion_viscous != null)
        addBrewingRecipe(
            new ItemStack(potion_viscous),
            new ItemStack(Items.SLIME_BALL),
            new ItemStack(potion_bounce));
      addBrewingRecipe(
          new ItemStack(potion_bounce),
          new ItemStack(UPG_LENGTH),
          new ItemStack(potion_bounce_long));
    }
  }
  private static void addBrewingRecipe(Item input, Item ingredient, Item output) {
    addBrewingRecipe(
        new ItemStack(input),
        new ItemStack(ingredient),
        new ItemStack(output));
  }
  private static BrewingRecipe addBrewingRecipe(ItemStack input, ItemStack ingredient, ItemStack output) {
    BrewingRecipe recipe = new BrewingRecipe(
        input,
        ingredient,
        output);
    BrewingRecipeRegistry.addRecipe(recipe);
    if (ModCyclic.logger.sendInfo) {//OMG UNIT TESTING WAAT
      ItemStack output0 = BrewingRecipeRegistry.getOutput(input, ingredient);
      if (output0.getItem() == output.getItem())
        ModCyclic.logger.info("Brewing Recipe succefully registered and working: " + output.getUnlocalizedName());
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
