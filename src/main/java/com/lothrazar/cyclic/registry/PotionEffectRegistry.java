package com.lothrazar.cyclic.registry;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.potion.CyclicMobEffect;
import com.lothrazar.cyclic.potion.effect.ButterEffect;
import com.lothrazar.cyclic.potion.effect.FlightMayflyEffect;
import com.lothrazar.cyclic.potion.effect.FrostEffect;
import com.lothrazar.cyclic.potion.effect.MagneticEffect;
import com.lothrazar.cyclic.potion.effect.NoClipEffect;
import com.lothrazar.cyclic.potion.effect.SnowwalkEffect;
import com.lothrazar.cyclic.potion.effect.StunEffect;
import com.lothrazar.cyclic.potion.effect.WaterwalkEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.registries.DeferredHolder;

public class PotionEffectRegistry {

  /**
   * This is used by PotionEvents::onEntityUpdate which Subscribes to EntityTickEvent.Pre
   *
   */
  public static final List<CyclicMobEffect> EFFECTS = new ArrayList<>();
  public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, ModCyclic.MODID);
  public static final DeferredHolder<MobEffect, StunEffect> STUN = MOB_EFFECTS.register("stun", () -> new StunEffect(MobEffectCategory.HARMFUL, 0xcccc00));
  public static final DeferredHolder<MobEffect, ? extends MobEffect> SWIMSPEED = MOB_EFFECTS.register("swimspeed", () -> new CyclicMobEffect(MobEffectCategory.BENEFICIAL, 0x339900).addAttributeModifier(NeoForgeMod.SWIM_SPEED, Identifier.fromNamespaceAndPath(ModCyclic.MODID, "swimspeed"), 0.5F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
  public static final DeferredHolder<MobEffect, WaterwalkEffect> WATERWALK = MOB_EFFECTS.register("waterwalk", () -> new WaterwalkEffect(MobEffectCategory.NEUTRAL, 0x221061));
  public static final DeferredHolder<MobEffect, SnowwalkEffect> SNOWWALK = MOB_EFFECTS.register("snowwalk", () -> new SnowwalkEffect(MobEffectCategory.NEUTRAL, 0xf0ecdf));
  public static final DeferredHolder<MobEffect, ? extends MobEffect> GRAVITY = MOB_EFFECTS.register("gravity", () -> new CyclicMobEffect(MobEffectCategory.HARMFUL, 0x730043)
      .addAttributeModifier(Attributes.GRAVITY, Identifier.fromNamespaceAndPath(ModCyclic.MODID, "gravity"), 5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
  public static final DeferredHolder<MobEffect, ? extends MobEffect> ANTIGRAVITY = MOB_EFFECTS.register("antigravity", () -> new CyclicMobEffect(MobEffectCategory.BENEFICIAL, 0x730043)
      .addAttributeModifier(Attributes.GRAVITY, Identifier.fromNamespaceAndPath(ModCyclic.MODID, "antigravity"), -0.015, AttributeModifier.Operation.ADD_VALUE)); //default gravity is +0.08
  public static final DeferredHolder<MobEffect, ? extends MobEffect> ATTACK_RANGE = MOB_EFFECTS.register("attack_range", () -> new CyclicMobEffect(MobEffectCategory.BENEFICIAL, 0x35db77)
      .addAttributeModifier(Attributes.ENTITY_INTERACTION_RANGE, Identifier.fromNamespaceAndPath(ModCyclic.MODID, "attack_range"), 2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
  public static final DeferredHolder<MobEffect, ? extends MobEffect> REACH_DISTANCE = MOB_EFFECTS.register("reach_distance", () -> new CyclicMobEffect(MobEffectCategory.BENEFICIAL, 0x500980)
      .addAttributeModifier(Attributes.BLOCK_INTERACTION_RANGE, Identifier.fromNamespaceAndPath(ModCyclic.MODID, "reach_distance"), 2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
  public static final DeferredHolder<MobEffect, ? extends MobEffect> BUTTERFINGERS = MOB_EFFECTS.register("butter", () -> new ButterEffect(MobEffectCategory.HARMFUL, 0xe5e500));
  public static final DeferredHolder<MobEffect, ? extends MobEffect> FROST_WALKER = MOB_EFFECTS.register("frost_walker", () -> new FrostEffect(MobEffectCategory.BENEFICIAL, 0x42f4d7));
  public static final DeferredHolder<MobEffect, ? extends MobEffect> MAGNETIC = MOB_EFFECTS.register("magnetic", () -> new MagneticEffect(MobEffectCategory.NEUTRAL, 0x224BAF));
  public static final DeferredHolder<MobEffect, ? extends MobEffect> FLIGHT = MOB_EFFECTS.register("flight", () -> new FlightMayflyEffect(MobEffectCategory.BENEFICIAL, 0xF24BAF));
  // HARMFUL category gives the effect a red HUD tint as a "this is risky" cue (player can fall through bedrock if Flight isn't also active).
  // Still vanilla-removable by milk; Flight is protected against milk via PotionEvents snapshot/restore.
  public static final DeferredHolder<MobEffect, NoClipEffect> NOCLIP = MOB_EFFECTS.register("noclip", () -> new NoClipEffect(MobEffectCategory.HARMFUL, 0xa0a0c0));
}