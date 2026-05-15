package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.core.registries.Registries;

public class PotionRegistry {

  public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(Registries.POTION, ModCyclic.MODID);
  
  static final int normal = 3600;
  static final int smal = 1800;
  public static final DeferredHolder<Potion, Potion> ANTIGRAVITY = POTIONS.register("antigravity", () -> new Potion(ModCyclic.MODID + "_antigravity", new MobEffectInstance(PotionEffectRegistry.ANTIGRAVITY, normal, 3, false, false, false)));
  public static final DeferredHolder<Potion, Potion> ATTACK_RANGE = POTIONS.register("attack_range", () -> new Potion(ModCyclic.MODID + "_attack_range", new MobEffectInstance(PotionEffectRegistry.ATTACK_RANGE, normal)));
  public static final DeferredHolder<Potion, Potion> BLIND = POTIONS.register("blind", () -> new Potion(ModCyclic.MODID + "_blind", new MobEffectInstance(MobEffects.BLINDNESS, normal)));
  public static final DeferredHolder<Potion, Potion> BUTTERFINGERS = POTIONS.register("butter", () -> new Potion(ModCyclic.MODID + "_butter", new MobEffectInstance(PotionEffectRegistry.BUTTERFINGERS, normal)));
  public static final DeferredHolder<Potion, Potion> FLIGHT = POTIONS.register("flight", () -> new Potion(ModCyclic.MODID + "_flight", new MobEffectInstance(PotionEffectRegistry.FLIGHT, normal)));
  public static final DeferredHolder<Potion, Potion> FROST_WALKER = POTIONS.register("frost_walker", () -> new Potion(ModCyclic.MODID + "_frost_walker", new MobEffectInstance(PotionEffectRegistry.FROST_WALKER, normal)));
  public static final DeferredHolder<Potion, Potion> GRAVITY = POTIONS.register("gravity", () -> new Potion(ModCyclic.MODID + "_gravity", new MobEffectInstance(PotionEffectRegistry.GRAVITY, normal)));
  public static final DeferredHolder<Potion, Potion> HASTE = POTIONS.register("haste", () -> new Potion(ModCyclic.MODID + "_haste", new MobEffectInstance(MobEffects.DIG_SPEED, normal)));
  public static final DeferredHolder<Potion, Potion> HASTE_STRONG = POTIONS.register("strong_haste", () -> new Potion(ModCyclic.MODID + "_strong_haste", new MobEffectInstance(MobEffects.DIG_SPEED, smal, 1, false, false, false)));
  public static final DeferredHolder<Potion, Potion> HUNGER = POTIONS.register("hunger", () -> new Potion(ModCyclic.MODID + "_hunger", new MobEffectInstance(MobEffects.HUNGER, normal)));
  public static final DeferredHolder<Potion, Potion> LEVITATION = POTIONS.register("levitation", () -> new Potion(ModCyclic.MODID + "_levitation", new MobEffectInstance(MobEffects.LEVITATION, smal)));
  public static final DeferredHolder<Potion, Potion> MAGNETIC = POTIONS.register("magnetic", () -> new Potion(ModCyclic.MODID + "_magnetic", new MobEffectInstance(PotionEffectRegistry.MAGNETIC, normal)));
  public static final DeferredHolder<Potion, Potion> REACH_DISTANCE = POTIONS.register("reach_distance", () -> new Potion(ModCyclic.MODID + "_reach_distance", new MobEffectInstance(PotionEffectRegistry.REACH_DISTANCE, normal)));
  public static final DeferredHolder<Potion, Potion> RESISTANCE = POTIONS.register("resistance", () -> new Potion(ModCyclic.MODID + "_resistance", new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, smal)));
  public static final DeferredHolder<Potion, Potion> STUN = POTIONS.register("stun", () -> new Potion(ModCyclic.MODID + "_stun", new MobEffectInstance(PotionEffectRegistry.STUN, smal)));
  public static final DeferredHolder<Potion, Potion> SWIMSPEED = POTIONS.register("swimspeed", () -> new Potion(ModCyclic.MODID + "_swimspeed", new MobEffectInstance(PotionEffectRegistry.SWIMSPEED, normal)));
  public static final DeferredHolder<Potion, Potion> STRONG_HUNGER = POTIONS.register("strong_hunger", () -> new Potion(ModCyclic.MODID + "_strong_hunger", new MobEffectInstance(MobEffects.HUNGER, smal, 1, false, false, false)));
  public static final DeferredHolder<Potion, Potion> SNOWWALK = POTIONS.register("snowwalk", () -> new Potion(ModCyclic.MODID + "_snow", new MobEffectInstance(PotionEffectRegistry.SNOWWALK, smal)));
  public static final DeferredHolder<Potion, Potion> WATERWALK = POTIONS.register("waterwalk", () -> new Potion(ModCyclic.MODID + "_waterwalk", new MobEffectInstance(PotionEffectRegistry.WATERWALK, smal)));
  public static final DeferredHolder<Potion, Potion> WITHER = POTIONS.register("wither", () -> new Potion(ModCyclic.MODID + "_wither", new MobEffectInstance(MobEffects.WITHER, smal)));

  public static void setup() {
    // Brewings will be moved to DataPacks / Event later
  }
}
