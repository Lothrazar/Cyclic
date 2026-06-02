package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
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
  // NoClip is intentionally a curse: shorter duration, no paired flight. Player either brings their own flight source
  // (Flight potion, creative, elytra, etc.) or they free-fall through bedrock when they phase. The HARMFUL category
  // gives the effect a red HUD tint as a warning; the chorus_spectral item tooltip spells it out further.
  public static final DeferredHolder<Potion, Potion> NOCLIP = POTIONS.register("noclip", () -> new Potion(ModCyclic.MODID + "_noclip", new MobEffectInstance(PotionEffectRegistry.NOCLIP, smal)));
  public static final DeferredHolder<Potion, Potion> REACH_DISTANCE = POTIONS.register("reach_distance", () -> new Potion(ModCyclic.MODID + "_reach_distance", new MobEffectInstance(PotionEffectRegistry.REACH_DISTANCE, normal)));
  public static final DeferredHolder<Potion, Potion> RESISTANCE = POTIONS.register("resistance", () -> new Potion(ModCyclic.MODID + "_resistance", new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, smal)));
  public static final DeferredHolder<Potion, Potion> STUN = POTIONS.register("stun", () -> new Potion(ModCyclic.MODID + "_stun", new MobEffectInstance(PotionEffectRegistry.STUN, smal)));
  public static final DeferredHolder<Potion, Potion> SWIMSPEED = POTIONS.register("swimspeed", () -> new Potion(ModCyclic.MODID + "_swimspeed", new MobEffectInstance(PotionEffectRegistry.SWIMSPEED, normal)));
  public static final DeferredHolder<Potion, Potion> STRONG_HUNGER = POTIONS.register("strong_hunger", () -> new Potion(ModCyclic.MODID + "_strong_hunger", new MobEffectInstance(MobEffects.HUNGER, smal, 1, false, false, false)));
  public static final DeferredHolder<Potion, Potion> SNOWWALK = POTIONS.register("snowwalk", () -> new Potion(ModCyclic.MODID + "_snow", new MobEffectInstance(PotionEffectRegistry.SNOWWALK, smal)));
  public static final DeferredHolder<Potion, Potion> WATERWALK = POTIONS.register("waterwalk", () -> new Potion(ModCyclic.MODID + "_waterwalk", new MobEffectInstance(PotionEffectRegistry.WATERWALK, smal)));
  public static final DeferredHolder<Potion, Potion> WITHER = POTIONS.register("wither", () -> new Potion(ModCyclic.MODID + "_wither", new MobEffectInstance(MobEffects.WITHER, smal)));

  public static class PotionRecipeConfig {

    public static ModConfigSpec.BooleanValue ANTIGRAVITY;
    public static ModConfigSpec.BooleanValue ATTACK_RANGE;
    public static ModConfigSpec.BooleanValue BLIND;
    public static ModConfigSpec.BooleanValue BUTTERFINGERS;
    public static ModConfigSpec.BooleanValue FLIGHT;
    public static ModConfigSpec.BooleanValue FROST_WALKER;
    public static ModConfigSpec.BooleanValue GRAVITY;
    public static ModConfigSpec.BooleanValue HASTE;
    public static ModConfigSpec.BooleanValue HUNGER;
    public static ModConfigSpec.BooleanValue LEVITATION;
    public static ModConfigSpec.BooleanValue MAGNETIC;
    public static ModConfigSpec.BooleanValue NOCLIP;
    public static ModConfigSpec.BooleanValue REACH_DISTANCE;
    public static ModConfigSpec.BooleanValue RESISTANCE;
    public static ModConfigSpec.BooleanValue STUN;
    public static ModConfigSpec.BooleanValue SWIMSPEED;
    public static ModConfigSpec.BooleanValue SNOWWALK;
    public static ModConfigSpec.BooleanValue WATERWALK;
    public static ModConfigSpec.BooleanValue WITHER;
  }

  @SubscribeEvent
  public static void onRegisterBrewingRecipes(RegisterBrewingRecipesEvent event) {
    PotionBrewing.Builder builder = event.getBuilder();
    if (PotionRecipeConfig.HASTE.get()) {
      builder.addMix(Potions.AWKWARD, Items.EMERALD, PotionRegistry.HASTE);
      builder.addMix(PotionRegistry.HASTE, Items.REDSTONE, PotionRegistry.HASTE_STRONG);
    }
    if (PotionRecipeConfig.STUN.get()) {
      builder.addMix(Potions.AWKWARD, Items.CLAY, PotionRegistry.STUN);
    }
    if (PotionRecipeConfig.SWIMSPEED.get()) {
      builder.addMix(Potions.AWKWARD, Items.DRIED_KELP_BLOCK, PotionRegistry.SWIMSPEED);
    }
    if (PotionRecipeConfig.BLIND.get()) {
      builder.addMix(Potions.NIGHT_VISION, Items.BEETROOT, PotionRegistry.BLIND);
    }
    if (PotionRecipeConfig.LEVITATION.get()) {
      builder.addMix(Potions.SLOW_FALLING, Items.FERMENTED_SPIDER_EYE, PotionRegistry.LEVITATION);
    }
    if (PotionRecipeConfig.RESISTANCE.get()) {
      builder.addMix(Potions.STRENGTH, Items.IRON_INGOT, PotionRegistry.RESISTANCE);
    }
    if (PotionRecipeConfig.WITHER.get()) {
      builder.addMix(Potions.WEAKNESS, Items.NETHER_BRICK, PotionRegistry.WITHER);
    }
    if (PotionRecipeConfig.HUNGER.get()) {
      builder.addMix(Potions.THICK, Items.ROTTEN_FLESH, PotionRegistry.HUNGER);
      builder.addMix(PotionRegistry.HUNGER, Items.REDSTONE, PotionRegistry.STRONG_HUNGER);
    }
    if (PotionRecipeConfig.WATERWALK.get()) {
      builder.addMix(Potions.AWKWARD, Items.PRISMARINE_SHARD, PotionRegistry.WATERWALK);
      builder.addMix(Potions.THICK, Items.COD, PotionRegistry.WATERWALK);
    }
    if (PotionRecipeConfig.SNOWWALK.get()) {
      builder.addMix(Potions.AWKWARD, Items.SNOWBALL, PotionRegistry.SNOWWALK);
    }
    if (PotionRecipeConfig.BUTTERFINGERS.get()) {
      builder.addMix(Potions.AWKWARD, Items.GOLD_INGOT, PotionRegistry.BUTTERFINGERS);
    }
    if (PotionRecipeConfig.FROST_WALKER.get()) {
      builder.addMix(Potions.AWKWARD, Blocks.ICE.asItem(), PotionRegistry.FROST_WALKER);
    }
    if (PotionRecipeConfig.MAGNETIC.get()) {
      builder.addMix(Potions.AWKWARD, Items.LAPIS_LAZULI, PotionRegistry.MAGNETIC);
    }
    if (PotionRecipeConfig.GRAVITY.get()) {
      builder.addMix(Potions.LEAPING, Items.COPPER_INGOT, PotionRegistry.GRAVITY);
    }
    if (PotionRecipeConfig.REACH_DISTANCE.get()) {
      builder.addMix(Potions.AWKWARD, Items.AMETHYST_SHARD, PotionRegistry.REACH_DISTANCE);
    }
    if (PotionRecipeConfig.ATTACK_RANGE.get()) {
      builder.addMix(Potions.AWKWARD, Blocks.POINTED_DRIPSTONE.asItem(), PotionRegistry.ATTACK_RANGE);
    }
    if (PotionRecipeConfig.ANTIGRAVITY.get()) {
      builder.addMix(PotionRegistry.GRAVITY, Items.FERMENTED_SPIDER_EYE, PotionRegistry.ANTIGRAVITY);
    }
    if (PotionRecipeConfig.FLIGHT.get()) {
      builder.addMix(Potions.STRONG_HEALING, Items.CHORUS_FRUIT, PotionRegistry.FLIGHT);
    }
    if (PotionRecipeConfig.NOCLIP.get()) {
      builder.addMix(PotionRegistry.FLIGHT, ItemRegistry.MEMBRANE.get(), PotionRegistry.NOCLIP);
    }
  }
}
