package com.lothrazar.cyclic.registry;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.potion.TickableEffect;
import com.lothrazar.cyclic.potion.effect.SnowwalkEffect;
import com.lothrazar.cyclic.potion.effect.StunEffect;
import com.lothrazar.cyclic.potion.effect.SwimEffect;
import com.lothrazar.cyclic.potion.effect.WaterwalkEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PotionEffectRegistry {

  public static final List<TickableEffect> EFFECTS = new ArrayList<TickableEffect>();
  public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ModCyclic.MODID);
  public static final RegistryObject<StunEffect> STUN = MOB_EFFECTS.register("stun", () -> new StunEffect(MobEffectCategory.HARMFUL, 0xcccc00));
  public static final RegistryObject<SwimEffect> SWIMSPEED = MOB_EFFECTS.register("swimspeed", () -> new SwimEffect(MobEffectCategory.BENEFICIAL, 0x663300));
  public static final RegistryObject<WaterwalkEffect> WATERWALK = MOB_EFFECTS.register("waterwalk", () -> new WaterwalkEffect(MobEffectCategory.BENEFICIAL, 0x221061));
  public static final RegistryObject<SnowwalkEffect> SNOWWALK = MOB_EFFECTS.register("snowwalk", () -> new SnowwalkEffect(MobEffectCategory.NEUTRAL, 0xf0ecdf));
}