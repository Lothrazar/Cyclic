/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic.registry.content;

import com.lothrazar.cyclicmagic.IContent;
import com.lothrazar.cyclicmagic.potion.PotionEffectRegistry;
import com.lothrazar.cyclicmagic.potion.PotionTypeCyclic;
import com.lothrazar.cyclicmagic.potion.PotionTypeRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.config.Configuration;

public class ItemPotionContent implements IContent {

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
  public static boolean enableFrostw;
  public static boolean enableButter;
  private PotionTypeCyclic potionTypeSlowfall;
  private PotionTypeCyclic potionTypeBounce;
  private PotionTypeCyclic potionTypeWaterwalk;
  private PotionTypeCyclic potionTypeSnow;
  private PotionTypeCyclic potionTypeSwim;
  private PotionTypeCyclic potionTypeMagnet;
  private PotionTypeCyclic potionTypeLevitation;
  private PotionTypeCyclic potionTypeHaste;
  private PotionTypeCyclic potionTypeResistance;
  private PotionTypeCyclic potionTypeResistanceII;
  private PotionTypeCyclic potionHealth;
  private PotionTypeCyclic potionEnder;
  private PotionTypeCyclic potionTypeLuck;
  private PotionTypeCyclic potionTypeWither;
  private PotionTypeCyclic potionTypeBlindness;
  private PotionTypeCyclic potionTypeSat;
  private PotionTypeCyclic potionTypeIce;
  private PotionTypeCyclic potionTypeButter;
  private PotionTypeCyclic potionTypeButterII;
  private final int SHORT = 1800;
  private final int NORMAL = 3600;

  //  private static final int LONG = 9600;
  @Override
  public void register() {
    if (ItemPotionContent.enableButter) {
      potionTypeButter = PotionTypeRegistry.addPotionType(new PotionEffect(PotionEffectRegistry.DROPS, NORMAL), "butter", new ItemStack(Items.GOLD_INGOT));
      //      potions.add(potionTypeButter);
      potionTypeButterII = PotionTypeRegistry.addPotionType(new PotionEffect(PotionEffectRegistry.DROPS, SHORT, Const.Potions.II), "butter2", Item.getItemFromBlock(Blocks.HARDENED_CLAY));
      potionTypeButterII.base = potionTypeButter;
      //      potions.add(potionTypeButterII);
    }
    if (ItemPotionContent.enableSlowfall) {
      potionTypeSlowfall = PotionTypeRegistry.addPotionType(new PotionEffect(PotionEffectRegistry.SLOWFALL, NORMAL), "slowfall", new ItemStack(Items.FISH, 1, ItemFishFood.FishType.CLOWNFISH.getMetadata()));
    }
    if (ItemPotionContent.enableWaterwalk) {
      potionTypeWaterwalk = PotionTypeRegistry.addPotionType(new PotionEffect(PotionEffectRegistry.WATERWALK, NORMAL), "waterwalk", new ItemStack(Items.FISH, 1, ItemFishFood.FishType.COD.getMetadata()));
    }
    if (ItemPotionContent.enableSnow) {
      potionTypeSnow = PotionTypeRegistry.addPotionType(new PotionEffect(PotionEffectRegistry.SNOW, NORMAL), "snow", Items.SNOWBALL);
    }
    if (ItemPotionContent.enableSwimspeed) {
      potionTypeSwim = PotionTypeRegistry.addPotionType(new PotionEffect(PotionEffectRegistry.SWIMSPEED, NORMAL), "swim", Items.CARROT_ON_A_STICK);
    }
    if (ItemPotionContent.enableBounce) {
      potionTypeBounce = PotionTypeRegistry.addPotionType(new PotionEffect(PotionEffectRegistry.BOUNCE, NORMAL), "bounce", Items.SLIME_BALL);
    }
    if (ItemPotionContent.enableFrostw) {
      potionTypeIce = PotionTypeRegistry.addPotionType(new PotionEffect(PotionEffectRegistry.FROSTW, NORMAL), "frostwalker", new ItemStack(Blocks.ICE));
    }
    if (ItemPotionContent.enableMagnet) {
      potionTypeMagnet = PotionTypeRegistry.addPotionType(new PotionEffect(PotionEffectRegistry.MAGNET, NORMAL), "magnet", new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()));
    }
    if (ItemPotionContent.enableLevit) {
      potionTypeLevitation = PotionTypeRegistry.addPotionType(new PotionEffect(MobEffects.LEVITATION, NORMAL), "levitation", Items.CHORUS_FRUIT);
    }
    if (ItemPotionContent.enableHaste) {
      potionTypeHaste = PotionTypeRegistry.addPotionType(new PotionEffect(MobEffects.HASTE, NORMAL), "haste", Items.COOKIE);
      PotionTypeCyclic potionTypeHasteII = PotionTypeRegistry.addPotionType(new PotionEffect(MobEffects.HASTE, SHORT, Const.Potions.II), "haste2", Items.GLOWSTONE_DUST);
      potionTypeHasteII.base = potionTypeHaste;
    }
    if (ItemPotionContent.enableResist) {
      potionTypeResistance = PotionTypeRegistry.addPotionType(new PotionEffect(MobEffects.RESISTANCE, NORMAL), "resistance", new ItemStack(Blocks.OBSIDIAN));
      potionTypeResistanceII = PotionTypeRegistry.addPotionType(new PotionEffect(MobEffects.RESISTANCE, SHORT, Const.Potions.II), "resistance2", Items.GLOWSTONE_DUST);
      potionTypeResistanceII.base = potionTypeResistance;
    }
    if (ItemPotionContent.enableHBoost) {
      potionHealth = PotionTypeRegistry.addPotionType(new PotionEffect(MobEffects.HEALTH_BOOST, NORMAL, Const.Potions.V), "healthboost", Items.GOLDEN_APPLE);
    }
    if (ItemPotionContent.enableEnder) {
      potionEnder = PotionTypeRegistry.addPotionType(new PotionEffect(PotionEffectRegistry.ENDER, NORMAL), "ender", Items.ENDER_PEARL);
    }
    if (ItemPotionContent.enableLuck) {
      potionTypeLuck = PotionTypeRegistry.addPotionType(new PotionEffect(MobEffects.LUCK, NORMAL), "luck", new ItemStack(Items.DYE, 1, EnumDyeColor.GREEN.getDyeDamage()));
    }
    if (ItemPotionContent.enableWither) {
      potionTypeWither = PotionTypeRegistry.addPotionType(new PotionEffect(MobEffects.WITHER, NORMAL), "wither", Items.FERMENTED_SPIDER_EYE);
      potionTypeWither.base = PotionTypes.WEAKNESS;
    }
    if (ItemPotionContent.enableBlindness) {
      potionTypeBlindness = PotionTypeRegistry.addPotionType(new PotionEffect(MobEffects.BLINDNESS, NORMAL), "blindness", Items.FERMENTED_SPIDER_EYE);
      potionTypeBlindness.base = PotionTypes.INVISIBILITY;
    }
    if (ItemPotionContent.enableSaturation) {
      potionTypeSat = PotionTypeRegistry.addPotionType(new PotionEffect(MobEffects.SATURATION, NORMAL), "saturation", Items.CAKE);
      potionTypeSat.base = PotionTypes.HEALING;
    }
    // wither
    // blindness
    // saturation
    // glowing
    // ! nausea nah
    // ! absorp ! skipping bc golden apples fill this role
    // ! hunger, pointless
    // ! unluck?? waste of time, skip this.
  }

  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.content;
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
    enableFrostw = config.getBoolean("PotionFrostWalker", category, true, Const.ConfigCategory.contentDefaultText);
    enableButter = config.getBoolean("PotionButterDropItems", category, true, Const.ConfigCategory.contentDefaultText);
  }

  @Override
  public boolean enabled() {
    return true;
  }
}
