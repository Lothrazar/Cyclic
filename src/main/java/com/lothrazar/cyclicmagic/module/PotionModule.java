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
  public static PotionCustom slowfall;
  public static PotionCustom magnet;
  public static PotionCustom ender;
  public static PotionCustom waterwalk;
  public static PotionCustom snow;
  public static final ItemPotionCustom potion_snow = new ItemPotionCustom(true);
  public static final ItemPotionCustom potion_ender = new ItemPotionCustom(true);
  public static final ItemPotionCustom potion_ender_long = new ItemPotionCustom(true);
  public static final ItemPotionCustom potion_magnet = new ItemPotionCustom(false);
  public static final ItemPotionCustom potion_magnet_long = new ItemPotionCustom(false);
  public static final ItemPotionCustom potion_waterwalk = new ItemPotionCustom(false);
  public static final ItemPotionCustom potion_waterwalk_long = new ItemPotionCustom(false);
  public static final ItemPotionCustom potion_slowfall = new ItemPotionCustom(true);
  public static final ItemPotionCustom potion_slowfall_long = new ItemPotionCustom(true);
  public static final ItemPotionCustom potion_viscous = new ItemPotionCustom(false);
  public static final ItemPotionCustom potion_levitation = new ItemPotionCustom(true, MobEffects.LEVITATION, 60 * 3);
  public static final ItemPotionCustom potion_levitation_long = new ItemPotionCustom(true, MobEffects.LUCK, 60 * 8);
  public static final ItemPotionCustom potion_luck = new ItemPotionCustom(true, MobEffects.LUCK, 60 * 3);
  public static final ItemPotionCustom potion_luck_long = new ItemPotionCustom(true, MobEffects.LEVITATION, 60 * 8);
  //  public static final ItemPotionCustom potion_glowing = new ItemPotionCustom(MobEffects.GLOWING, 60*3);
  //  public static final ItemPotionCustom potion_glowing_long = new ItemPotionCustom(MobEffects.GLOWING, 60*8);
  public static final ItemPotionCustom potion_resistance = new ItemPotionCustom(true, MobEffects.RESISTANCE, 60 * 3);
  public static final ItemPotionCustom potion_resistance_strong = new ItemPotionCustom(true, MobEffects.RESISTANCE, 90, Const.Potions.II);
  public static final ItemPotionCustom potion_resistance_long = new ItemPotionCustom(true, MobEffects.RESISTANCE, 60 * 8);
  public static final ItemPotionCustom potion_boost = new ItemPotionCustom(true, MobEffects.HEALTH_BOOST, 60 * 3, Const.Potions.V);
  public static final ItemPotionCustom potion_boost_long = new ItemPotionCustom(true, MobEffects.HEALTH_BOOST, 60 * 8, Const.Potions.V);
  public static final ItemPotionCustom potion_haste = new ItemPotionCustom(false, MobEffects.HASTE, 60 * 3);
  public static final ItemPotionCustom potion_haste_strong = new ItemPotionCustom(false, MobEffects.HASTE, 90, Const.Potions.II);
  public static final ItemPotionCustom potion_haste_long = new ItemPotionCustom(false, MobEffects.HASTE, 60 * 8);
  @Override
  public void onInit() {
    // http://www.minecraftforge.net/forum/index.php?topic=11024.0
    // ??? http://www.minecraftforge.net/forum/index.php?topic=12358.0
    ender = new PotionEnder("ender", true, 0);
    waterwalk = new PotionWaterwalk("waterwalk", true, 0);
    slowfall = new PotionSlowfall("slowfall", true, 0);
    magnet = new PotionMagnet("magnet", true, 0);
    snow = new PotionSnow("snow", true, 0);
    GameRegistry.register(ender, ender.getIcon());//was geticon
    GameRegistry.register(waterwalk, waterwalk.getIcon());
    GameRegistry.register(slowfall, slowfall.getIcon());
    GameRegistry.register(magnet, magnet.getIcon());
    GameRegistry.register(snow, snow.getIcon());
    ItemRegistry.addItem(potion_viscous, "potion_viscous");
    ItemRegistry.addItem(potion_boost, "potion_boost");
    ItemRegistry.addItem(potion_boost_long, "potion_boost_long");
    ItemRegistry.addItem(potion_resistance, "potion_resistance");
    ItemRegistry.addItem(potion_resistance_long, "potion_resistance_long");
    ItemRegistry.addItem(potion_resistance_strong, "potion_resistance_strong");
    ItemRegistry.addItem(potion_waterwalk, "potion_waterwalk");
    ItemRegistry.addItem(potion_waterwalk_long, "potion_waterwalk_long");
    ItemRegistry.addItem(potion_slowfall, "potion_slowfall");
    ItemRegistry.addItem(potion_slowfall_long, "potion_slowfall_long");
    ItemRegistry.addItem(potion_magnet, "potion_magnet");
    ItemRegistry.addItem(potion_magnet_long, "potion_magnet_long");
    ItemRegistry.addItem(potion_haste, "potion_haste");
    ItemRegistry.addItem(potion_haste_long, "potion_haste_long");
    ItemRegistry.addItem(potion_haste_strong, "potion_haste_strong");
    ItemRegistry.addItem(potion_ender, "potion_ender");
    ItemRegistry.addItem(potion_ender_long, "potion_ender_long");
    ItemRegistry.addItem(potion_snow, "potion_snow");
    ItemRegistry.addItem(potion_luck, "potion_luck");
    ItemRegistry.addItem(potion_luck_long, "potion_luck_long");
    ItemRegistry.addItem(potion_levitation, "potion_levitation");
    ItemRegistry.addItem(potion_levitation_long, "potion_levitation_long");
    MinecraftForge.EVENT_BUS.register(slowfall);
    MinecraftForge.EVENT_BUS.register(magnet);
    MinecraftForge.EVENT_BUS.register(waterwalk);
    MinecraftForge.EVENT_BUS.register(ender);
    MinecraftForge.EVENT_BUS.register(snow);
    potion_snow.addEffect(snow, 60 * 3, Potions.I);
    potion_ender.addEffect(ender, 60 * 3, Potions.I);
    potion_magnet.addEffect(magnet, 60 * 3, Potions.I);
    potion_waterwalk.addEffect(waterwalk, 60 * 3, Potions.I);
    potion_slowfall.addEffect(slowfall, 60 * 3, Potions.I);
    potion_ender_long.addEffect(ender, 60 * 8, Potions.I);
    potion_magnet_long.addEffect(magnet, 60 * 8, Potions.I);
    potion_waterwalk_long.addEffect(waterwalk, 60 * 8, Potions.I);
    potion_slowfall_long.addEffect(slowfall, 60 * 8, Potions.I);
    registerBrewing();
  }
  private static void registerBrewing() {
    ItemStack awkward = BrewingRecipeRegistry.getOutput(new ItemStack(Items.POTIONITEM), new ItemStack(Items.NETHER_WART));
    BrewingRecipeRegistry.addRecipe(
        awkward,
        new ItemStack(Items.DYE, 1, EnumDyeColor.BROWN.getDyeDamage()),
        new ItemStack(potion_viscous));
    BrewingRecipeRegistry.addRecipe(
        new ItemStack(potion_viscous),
        new ItemStack(Items.CHORUS_FRUIT),
        new ItemStack(potion_levitation));
    BrewingRecipeRegistry.addRecipe(
        new ItemStack(potion_levitation),
        new ItemStack(Items.REDSTONE),
        new ItemStack(potion_levitation_long));
    BrewingRecipeRegistry.addRecipe(
        new ItemStack(potion_viscous),
        new ItemStack(Items.SLIME_BALL),
        new ItemStack(potion_luck));
    addBrewingRecipe(
        potion_viscous,
        Items.ENDER_EYE,
        potion_ender);
    addBrewingRecipe(
        potion_ender,
        Items.REDSTONE,
        potion_ender_long);
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
    addBrewingRecipe(
        potion_viscous,
        Items.PRISMARINE_CRYSTALS,
        potion_waterwalk);
    addBrewingRecipe(
        potion_waterwalk,
        Items.REDSTONE,
        potion_waterwalk_long);
    addBrewingRecipe(
        potion_viscous,
        Items.GOLDEN_APPLE,
        potion_boost);
    addBrewingRecipe(
        potion_boost,
        Items.REDSTONE,
        potion_boost_long);
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
    BrewingRecipeRegistry.addRecipe(
        new ItemStack(potion_viscous),
        new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()),
        new ItemStack(potion_magnet));
    addBrewingRecipe(
        potion_magnet,
        Items.REDSTONE,
        potion_magnet_long);
    BrewingRecipeRegistry.addRecipe(
        new ItemStack(potion_viscous),
        new ItemStack(Items.FISH, 1, ItemFishFood.FishType.CLOWNFISH.getMetadata()),
        new ItemStack(potion_slowfall));
    BrewingRecipeRegistry.addRecipe(
        new ItemStack(potion_slowfall),
        new ItemStack(Items.REDSTONE),
        new ItemStack(potion_slowfall_long));
    BrewingRecipeRegistry.addRecipe(
        new ItemStack(potion_viscous),
        new ItemStack(Blocks.ICE),
        new ItemStack(potion_snow));
  }
  private static void addBrewingRecipe(Item input, Item ingredient, Item output) {
    BrewingRecipeRegistry.addRecipe(
        new ItemStack(input),
        new ItemStack(ingredient),
        new ItemStack(output));
  }
  public boolean cancelPotionInventoryShift;
  @SubscribeEvent
  public void onEntityUpdate(LivingUpdateEvent event) {
    EntityLivingBase entity = event.getEntityLiving();
    if (entity == null) { return; }
    if (slowfall != null && entity.isPotionActive(slowfall)) {
      slowfall.tick(entity);
    }
    if (magnet != null && entity.isPotionActive(magnet)) {
      magnet.tick(entity);
    }
    if (waterwalk != null && entity.isPotionActive(waterwalk)) {
      waterwalk.tick(entity);
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
  }
}
