package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

public class EnchantRegistry {

  public static final ResourceKey<Enchantment> AUTO_SMELT = key("auto_smelt");
  public static final ResourceKey<Enchantment> BEEKEEPER = key("beekeeper");
  public static final ResourceKey<Enchantment> BEHEADING = key("beheading");
  public static final ResourceKey<Enchantment> DISARM = key("disarm");
  public static final ResourceKey<Enchantment> PEARL = key("ender");
  public static final ResourceKey<Enchantment> EXCAVATE = key("excavate");
  public static final ResourceKey<Enchantment> CURSE = key("curse");
  public static final ResourceKey<Enchantment> GROWTH = key("growth");
  public static final ResourceKey<Enchantment> STAND = key("laststand");
  public static final ResourceKey<Enchantment> LIFELEECH = key("life_leech");
  public static final ResourceKey<Enchantment> MAGNET = key("magnet");
  public static final ResourceKey<Enchantment> MULTIBOW = key("multishot");
  public static final ResourceKey<Enchantment> LAUNCH = key("launch");
  public static final ResourceKey<Enchantment> QUICKDRAW = key("quickshot");
  public static final ResourceKey<Enchantment> REACH = key("reach");
  public static final ResourceKey<Enchantment> STEADY = key("steady");
  public static final ResourceKey<Enchantment> SOULBOUND = key("soulbound");
  public static final ResourceKey<Enchantment> STEP = key("step");
  public static final ResourceKey<Enchantment> TRAVELLER = key("traveler");
  public static final ResourceKey<Enchantment> VENOM = key("venom");
  public static final ResourceKey<Enchantment> EXPERIENCE_BOOST = key("experience_boost");

  private static ResourceKey<Enchantment> key(String name) {
    return ResourceKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(ModCyclic.MODID, name));
  }

}
