package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.item.ItemPotionCustom;
import com.lothrazar.cyclicmagic.potion.PotionCustom;
import com.lothrazar.cyclicmagic.potion.PotionEnder;
import com.lothrazar.cyclicmagic.potion.PotionMagnet;
import com.lothrazar.cyclicmagic.potion.PotionSlowfall;
import com.lothrazar.cyclicmagic.potion.PotionSnow;
import com.lothrazar.cyclicmagic.potion.PotionWaterwalk;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.Const.Potions;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionModule extends BaseEventModule {
  public static PotionCustom slowfallEffect;
  public static PotionCustom magnetEffect;
  public static PotionCustom enderEffect;
  public static PotionCustom waterwalkEffect;
  public static PotionCustom snowEffect;
  public static final ItemPotionCustom potion_viscous = new ItemPotionCustom(false);
  public static ItemPotionCustom potion_snow;
  public static ItemPotionCustom potion_ender;
  public static ItemPotionCustom potion_ender_long;
  public static ItemPotionCustom potion_magnet;
  public static ItemPotionCustom potion_magnet_long;
  public static ItemPotionCustom potion_waterwalk;
  public static ItemPotionCustom potion_waterwalk_long;
  public static ItemPotionCustom potion_slowfall;
  public static ItemPotionCustom potion_slowfall_long;
  public static ItemPotionCustom potion_levitation;
  public static ItemPotionCustom potion_levitation_long;
  public static ItemPotionCustom potion_luck;
  public static ItemPotionCustom potion_luck_long;
  //  public static final ItemPotionCustom potion_glowing = new ItemPotionCustom(MobEffects.GLOWING, 60*3);
  //  public static final ItemPotionCustom potion_glowing_long = new ItemPotionCustom(MobEffects.GLOWING, 60*8);
  public static ItemPotionCustom potion_resistance;
  public static ItemPotionCustom potion_resistance_strong;
  public static ItemPotionCustom potion_resistance_long;
  public static ItemPotionCustom potion_boost;
  public static ItemPotionCustom potion_boost_long;
  public static ItemPotionCustom potion_haste;
  public static ItemPotionCustom potion_haste_strong;
  public static ItemPotionCustom potion_haste_long;
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
  @Override
  public void onInit() {
    // http://www.minecraftforge.net/forum/index.php?topic=11024.0
    // ??? http://www.minecraftforge.net/forum/index.php?topic=12358.0
    //CORE/BASE POTION
    ItemStack awkward = BrewingRecipeRegistry.getOutput(new ItemStack(Items.POTIONITEM), new ItemStack(Items.NETHER_WART));
    ItemRegistry.addItem(potion_viscous, "potion_viscous");
    BrewingRecipeRegistry.addRecipe(
        awkward,
        new ItemStack(Items.DYE, 1, EnumDyeColor.BROWN.getDyeDamage()),
        new ItemStack(potion_viscous));
    if (enableEnder) {
      enderEffect = new PotionEnder("ender", true, 0);
      GameRegistry.register(enderEffect, enderEffect.getIcon());//was geticon
      MinecraftForge.EVENT_BUS.register(enderEffect);
      potion_ender = new ItemPotionCustom(true);
      ItemRegistry.addItem(potion_ender, "potion_ender");
      potion_ender.addEffect(enderEffect, 60 * 3, Potions.I);
      potion_ender_long = new ItemPotionCustom(true);
      ItemRegistry.addItem(potion_ender_long, "potion_ender_long");
      potion_ender_long.addEffect(enderEffect, 60 * 8, Potions.I);
      addBrewingRecipe(
          potion_viscous,
          Items.ENDER_EYE,
          potion_ender);
      addBrewingRecipe(
          potion_ender,
          Items.REDSTONE,
          potion_ender_long);
    }
    if (enableMagnet) {
      magnetEffect = new PotionMagnet("magnet", true, 0);
      GameRegistry.register(magnetEffect, magnetEffect.getIcon());
      MinecraftForge.EVENT_BUS.register(magnetEffect);
      potion_magnet = new ItemPotionCustom(false);
      potion_magnet_long = new ItemPotionCustom(false);
      ItemRegistry.addItem(potion_magnet, "potion_magnet");
      ItemRegistry.addItem(potion_magnet_long, "potion_magnet_long");
      potion_magnet.addEffect(magnetEffect, 60 * 3, Potions.I);
      potion_magnet_long.addEffect(magnetEffect, 60 * 8, Potions.I);
      BrewingRecipeRegistry.addRecipe(
          new ItemStack(potion_viscous),
          new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()),
          new ItemStack(potion_magnet));
      addBrewingRecipe(
          potion_magnet,
          Items.REDSTONE,
          potion_magnet_long);
    }
    if (enableWaterwalk) {
      waterwalkEffect = new PotionWaterwalk("waterwalk", true, 0);
      GameRegistry.register(waterwalkEffect, waterwalkEffect.getIcon());
      MinecraftForge.EVENT_BUS.register(waterwalkEffect);
      potion_waterwalk = new ItemPotionCustom(false);
      potion_waterwalk_long = new ItemPotionCustom(false);
      ItemRegistry.addItem(potion_waterwalk, "potion_waterwalk");
      ItemRegistry.addItem(potion_waterwalk_long, "potion_waterwalk_long");
      potion_waterwalk.addEffect(waterwalkEffect, 60 * 3, Potions.I);
      potion_waterwalk_long.addEffect(waterwalkEffect, 60 * 8, Potions.I);
      addBrewingRecipe(
          potion_viscous,
          Items.PRISMARINE_CRYSTALS,
          potion_waterwalk);
      addBrewingRecipe(
          potion_waterwalk,
          Items.REDSTONE,
          potion_waterwalk_long);
    }
    if (enableSlowfall) {
      slowfallEffect = new PotionSlowfall("slowfall", true, 0);
      GameRegistry.register(slowfallEffect, slowfallEffect.getIcon());
      MinecraftForge.EVENT_BUS.register(slowfallEffect);
      potion_slowfall = new ItemPotionCustom(true);
      potion_slowfall_long = new ItemPotionCustom(true);
      ItemRegistry.addItem(potion_slowfall, "potion_slowfall");
      ItemRegistry.addItem(potion_slowfall_long, "potion_slowfall_long");
      potion_slowfall.addEffect(slowfallEffect, 60 * 3, Potions.I);
      potion_slowfall_long.addEffect(slowfallEffect, 60 * 8, Potions.I);
      BrewingRecipeRegistry.addRecipe(
          new ItemStack(potion_viscous),
          new ItemStack(Items.FISH, 1, ItemFishFood.FishType.CLOWNFISH.getMetadata()),
          new ItemStack(potion_slowfall));
      BrewingRecipeRegistry.addRecipe(
          new ItemStack(potion_slowfall),
          new ItemStack(Items.REDSTONE),
          new ItemStack(potion_slowfall_long));
    }
    if (enableSnow) {
      snowEffect = new PotionSnow("snow", true, 0);
      GameRegistry.register(snowEffect, snowEffect.getIcon());
      MinecraftForge.EVENT_BUS.register(snowEffect);
      potion_snow = new ItemPotionCustom(true);
      potion_snow.addEffect(snowEffect, 60 * 3, Potions.I);
      ItemRegistry.addItem(potion_snow, "potion_snow");
      BrewingRecipeRegistry.addRecipe(
          new ItemStack(potion_viscous),
          new ItemStack(Blocks.ICE),
          new ItemStack(potion_snow));
    }
    if (enableHBoost) {
      potion_boost = new ItemPotionCustom(true, MobEffects.HEALTH_BOOST, 60 * 3, Const.Potions.V);
      potion_boost_long = new ItemPotionCustom(true, MobEffects.HEALTH_BOOST, 60 * 8, Const.Potions.V);
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
    }
    if (enableResist) {
      potion_resistance = new ItemPotionCustom(true, MobEffects.RESISTANCE, 60 * 3);
      potion_resistance_strong = new ItemPotionCustom(true, MobEffects.RESISTANCE, 90, Const.Potions.II);
      potion_resistance_long = new ItemPotionCustom(true, MobEffects.RESISTANCE, 60 * 8);
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
    }
    if (enableHaste) {
      potion_haste = new ItemPotionCustom(false, MobEffects.HASTE, 60 * 3);
      potion_haste_strong = new ItemPotionCustom(false, MobEffects.HASTE, 90, Const.Potions.II);
      potion_haste_long = new ItemPotionCustom(false, MobEffects.HASTE, 60 * 8);
      ItemRegistry.addItem(potion_haste, "potion_haste");
      ItemRegistry.addItem(potion_haste_long, "potion_haste_long");
      ItemRegistry.addItem(potion_haste_strong, "potion_haste_strong");
      addBrewingRecipe(
          potion_viscous,
          Items.EMERALD,
          potion_haste);
      addBrewingRecipe(
          potion_haste,
          Items.REDSTONE,
          potion_haste_long);
      addBrewingRecipe(
          potion_haste,
          Items.GLOWSTONE_DUST,
          potion_haste_strong);
    }
    if (enableLuck) {
      potion_luck = new ItemPotionCustom(true, MobEffects.LUCK, 60 * 3);
      potion_luck_long = new ItemPotionCustom(true, MobEffects.LUCK, 60 * 8);
      ItemRegistry.addItem(potion_luck, "potion_luck");
      ItemRegistry.addItem(potion_luck_long, "potion_luck_long");
      BrewingRecipeRegistry.addRecipe(
          new ItemStack(potion_viscous),
          new ItemStack(Items.SLIME_BALL),
          new ItemStack(potion_luck));
    }
    if (enableLevit) {
      potion_levitation = new ItemPotionCustom(true, MobEffects.LEVITATION, 60 * 3);
      potion_levitation_long = new ItemPotionCustom(true, MobEffects.LEVITATION, 60 * 8);
      ItemRegistry.addItem(potion_levitation, "potion_levitation");
      ItemRegistry.addItem(potion_levitation_long, "potion_levitation_long");
      BrewingRecipeRegistry.addRecipe(
          new ItemStack(potion_viscous),
          new ItemStack(Items.CHORUS_FRUIT),
          new ItemStack(potion_levitation));
      BrewingRecipeRegistry.addRecipe(
          new ItemStack(potion_levitation),
          new ItemStack(Items.REDSTONE),
          new ItemStack(potion_levitation_long));
    }
  }
  private static void addBrewingRecipe(Item input, Item ingredient, Item output) {
    BrewingRecipeRegistry.addRecipe(
        new ItemStack(input),
        new ItemStack(ingredient),
        new ItemStack(output));
  }
  @SubscribeEvent
  public void onEntityUpdate(LivingUpdateEvent event) {
    EntityLivingBase entity = event.getEntityLiving();
    if (entity == null) { return; }
    if (slowfallEffect != null && entity.isPotionActive(slowfallEffect)) {
      slowfallEffect.tick(entity);
    }
    if (magnetEffect != null && entity.isPotionActive(magnetEffect)) {
      magnetEffect.tick(entity);
    }
    if (waterwalkEffect != null && entity.isPotionActive(waterwalkEffect)) {
      waterwalkEffect.tick(entity);
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
