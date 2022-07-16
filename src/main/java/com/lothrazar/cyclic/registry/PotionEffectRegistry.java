package com.lothrazar.cyclic.registry;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.potion.TickableEffect;
import com.lothrazar.cyclic.potion.effect.AttrEffect;
import com.lothrazar.cyclic.potion.effect.SnowwalkEffect;
import com.lothrazar.cyclic.potion.effect.StunEffect;
import com.lothrazar.cyclic.potion.effect.WaterwalkEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PotionEffectRegistry {

  public static final List<TickableEffect> EFFECTS = new ArrayList<TickableEffect>();
  public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ModCyclic.MODID);
  public static final RegistryObject<StunEffect> STUN = MOB_EFFECTS.register("stun", () -> new StunEffect(MobEffectCategory.HARMFUL, 0xcccc00));
  public static final RegistryObject<MobEffect> SWIMSPEED = MOB_EFFECTS.register("swimspeed", () -> new AttrEffect(MobEffectCategory.BENEFICIAL, 0x663300)
      .addAttributeModifier(ForgeMod.SWIM_SPEED.get(), "8207DE5E-7CE8-4030-940E-514C1F160890", 2, AttributeModifier.Operation.MULTIPLY_TOTAL));
  public static final RegistryObject<WaterwalkEffect> WATERWALK = MOB_EFFECTS.register("waterwalk", () -> new WaterwalkEffect(MobEffectCategory.BENEFICIAL, 0x221061));
  public static final RegistryObject<SnowwalkEffect> SNOWWALK = MOB_EFFECTS.register("snowwalk", () -> new SnowwalkEffect(MobEffectCategory.NEUTRAL, 0xf0ecdf));
  public static final RegistryObject<MobEffect> GRAVITY = MOB_EFFECTS.register("gravity", () -> new AttrEffect(MobEffectCategory.HARMFUL, 0x730043)
      .addAttributeModifier(ForgeMod.ENTITY_GRAVITY.get(), "5207DE5E-7CE8-4030-940E-514C1F160890", 5, AttributeModifier.Operation.MULTIPLY_TOTAL));
  public static final RegistryObject<MobEffect> ANTIGRAVITY = MOB_EFFECTS.register("antigravity", () -> new AttrEffect(MobEffectCategory.NEUTRAL, 0x730043)
      .addAttributeModifier(ForgeMod.ENTITY_GRAVITY.get(), "5207DE5E-7CE8-4030-940E-514C1F160890", -0.015, AttributeModifier.Operation.ADDITION)); //default gravity is +0.08
  public static final RegistryObject<MobEffect> ATTACK_RANGE = MOB_EFFECTS.register("attack_range", () -> new AttrEffect(MobEffectCategory.BENEFICIAL, 0x35db77)
      .addAttributeModifier(ForgeMod.ATTACK_RANGE.get(), "5207DE5E-7CE8-4030-940E-514C1F160890", 2, AttributeModifier.Operation.MULTIPLY_TOTAL));
  public static final RegistryObject<MobEffect> REACH_DISTANCE = MOB_EFFECTS.register("reach_distance", () -> new AttrEffect(MobEffectCategory.BENEFICIAL, 0x500980)
      .addAttributeModifier(ForgeMod.REACH_DISTANCE.get(), "5207DE5E-7CE8-4030-940E-514C1F160890", 2, AttributeModifier.Operation.MULTIPLY_TOTAL));
}