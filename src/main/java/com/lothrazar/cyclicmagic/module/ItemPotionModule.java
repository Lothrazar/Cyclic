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
  public static boolean enableEnder;
  public static boolean enableMagnet;
  public static boolean enableWaterwalk;
  public static boolean enableSlowfall;
  public static boolean enableSnow;
  public static boolean enableHaste;
  public static boolean enableResist;
  public static boolean enableLuck;
  public static boolean enableLevit;
  public static boolean enableHBoost;
  public static boolean enableSwimspeed;
  public static boolean enableBounce;
  public static boolean enableWither;
  public static boolean enableBlindness;
  public static boolean enableSaturation; 

 
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
    enableWither = config.getBoolean("PotionWither", category, true, Const.ConfigCategory.contentDefaultText);
    
    enableBlindness = config.getBoolean("PotionBlindness", category, true, Const.ConfigCategory.contentDefaultText);
    
    enableSaturation = config.getBoolean("PotionSaturation", category, true, Const.ConfigCategory.contentDefaultText);
    
    
  }
}
