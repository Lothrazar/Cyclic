package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SoundRegistry {

  public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, ModCyclic.MODID);
  public static final DeferredHolder<SoundEvent,SoundEvent> BASEY = SOUND_EVENTS.register("basey", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "basey")));
  public static final DeferredHolder<SoundEvent,SoundEvent> BASS_ECHO = SOUND_EVENTS.register("bass_echo", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "bass_echo")));
  public static final DeferredHolder<SoundEvent,SoundEvent> BIP = SOUND_EVENTS.register("bip", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "bip")));
  public static final DeferredHolder<SoundEvent,SoundEvent> BUZZP = SOUND_EVENTS.register("buzzp", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "buzzp")));
  public static final DeferredHolder<SoundEvent,SoundEvent> BWEWE = SOUND_EVENTS.register("bwewe", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "bwewe")));
  public static final DeferredHolder<SoundEvent,SoundEvent> BWOAAAP = SOUND_EVENTS.register("bwoaaap", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "bwoaaap")));
  public static final DeferredHolder<SoundEvent,SoundEvent> CHAOS_REAPER = SOUND_EVENTS.register("chaos_reaper", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "chaos_reaper")));
  public static final DeferredHolder<SoundEvent,SoundEvent> COIN = SOUND_EVENTS.register("coin", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "coin")));
  public static final DeferredHolder<SoundEvent,SoundEvent> CRACK = SOUND_EVENTS.register("crack", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "crack")));
  public static final DeferredHolder<SoundEvent,SoundEvent> CRACKLE = SOUND_EVENTS.register("crackle", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "crackle")));
  public static final DeferredHolder<SoundEvent,SoundEvent> DCOIN = SOUND_EVENTS.register("dcoin", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "dcoin")));
  public static final DeferredHolder<SoundEvent,SoundEvent> DICE_MIKE_KOENIG_SHORT = SOUND_EVENTS.register("dice_mike_koenig_short", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "dice_mike_koenig_short")));
  public static final DeferredHolder<SoundEvent,SoundEvent> EXPLOSM = SOUND_EVENTS.register("explosm", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "explosm")));
  public static final DeferredHolder<SoundEvent,SoundEvent> FIREBALL_EXPLODE = SOUND_EVENTS.register("fireball_explode", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "fireball_explode")));
  public static final DeferredHolder<SoundEvent,SoundEvent> DOORBELL_MIKEKOENIG = SOUND_EVENTS.register("doorbell_mikekoenig", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "doorbell_mikekoenig")));
  public static final DeferredHolder<SoundEvent,SoundEvent> FIRELAUNCH = SOUND_EVENTS.register("firelaunch", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "firelaunch")));
  public static final DeferredHolder<SoundEvent,SoundEvent> GOODLAUNCH = SOUND_EVENTS.register("goodlaunch", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "goodlaunch")));
  public static final DeferredHolder<SoundEvent,SoundEvent> GUITAR = SOUND_EVENTS.register("guitar", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "guitar")));
  public static final DeferredHolder<SoundEvent,SoundEvent> HOVERING = SOUND_EVENTS.register("hovering", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "hovering")));
  public static final DeferredHolder<SoundEvent,SoundEvent> LASERBEANPEW = SOUND_EVENTS.register("laserbeanpew", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "laserbeanpew")));
  public static final DeferredHolder<SoundEvent,SoundEvent> MACHINE_LAUNCH = SOUND_EVENTS.register("machine_launch", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "machine_launch")));
  public static final DeferredHolder<SoundEvent,SoundEvent> MAGIC_MISSILE = SOUND_EVENTS.register("magic_missile", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "magic_missile")));
  public static final DeferredHolder<SoundEvent,SoundEvent> MONSTER_BALL_CAPTURE = SOUND_EVENTS.register("monster_ball_capture", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "monster_ball_capture")));
  public static final DeferredHolder<SoundEvent,SoundEvent> MONSTER_BALL_RELEASE = SOUND_EVENTS.register("monster_ball_release", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "monster_ball_release")));
  public static final DeferredHolder<SoundEvent,SoundEvent> PEW = SOUND_EVENTS.register("pew", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "pew")));
  public static final DeferredHolder<SoundEvent,SoundEvent> PEW_LONG = SOUND_EVENTS.register("pew_long", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "pew_long")));
  public static final DeferredHolder<SoundEvent,SoundEvent> POW = SOUND_EVENTS.register("pow", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "pow")));
  public static final DeferredHolder<SoundEvent,SoundEvent> POWERUPSCALES = SOUND_EVENTS.register("powerupscales", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "powerupscales")));
  public static final DeferredHolder<SoundEvent,SoundEvent> SPIKEMAYBE = SOUND_EVENTS.register("spikemaybe", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "spikemaybe")));
  public static final DeferredHolder<SoundEvent,SoundEvent> SPIKES_IN = SOUND_EVENTS.register("spikes_in", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "spikes_in")));
  public static final DeferredHolder<SoundEvent,SoundEvent> SPIRIT_SEEKER = SOUND_EVENTS.register("spirit_seeker", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "spirit_seeker")));
  public static final DeferredHolder<SoundEvent,SoundEvent> STEP_HEIGHT_DOWN = SOUND_EVENTS.register("step_height_down", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "step_height_down")));
  public static final DeferredHolder<SoundEvent,SoundEvent> STEP_HEIGHT_UP = SOUND_EVENTS.register("step_height_up", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "step_height_up")));
  public static final DeferredHolder<SoundEvent,SoundEvent> PSCHEW_FIRE = SOUND_EVENTS.register("pschew_fire", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "pschew_fire")));
  public static final DeferredHolder<SoundEvent,SoundEvent> FROST_STAFF_LAUNCH = SOUND_EVENTS.register("frost_staff_launch", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "frost_staff_launch")));
  public static final DeferredHolder<SoundEvent,SoundEvent> LIGHTNING_STAFF_LAUNCH = SOUND_EVENTS.register("lightning_staff_launch", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "lightning_staff_launch")));
  public static final DeferredHolder<SoundEvent,SoundEvent> FIREBALL_STAFF_LAUNCH = SOUND_EVENTS.register("fireball_staff_launch", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "fireball_staff_launch")));
  public static final DeferredHolder<SoundEvent,SoundEvent> BLOCK_SCAFFOLDING_0 = SOUND_EVENTS.register("block_scaffolding_0", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "block_scaffolding_0")));
  public static final DeferredHolder<SoundEvent,SoundEvent> BLOCK_SCAFFOLDING_1 = SOUND_EVENTS.register("block_scaffolding_1", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "block_scaffolding_1")));
  public static final DeferredHolder<SoundEvent,SoundEvent> DUNGEONFINDER = SOUND_EVENTS.register("dungeonfinder", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "dungeonfinder")));
  public static final DeferredHolder<SoundEvent,SoundEvent> SPIKES_ON = SOUND_EVENTS.register("spikes_on", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "spikes_on")));
  public static final DeferredHolder<SoundEvent,SoundEvent> SPIKES_OFF = SOUND_EVENTS.register("spikes_off", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "spikes_off")));
  public static final DeferredHolder<SoundEvent,SoundEvent> WARP_ECHO = SOUND_EVENTS.register("warp_echo", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "warp_echo")));
  public static final DeferredHolder<SoundEvent,SoundEvent> TOOL_MODE = SOUND_EVENTS.register("tool_mode", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "tool_mode")));
  public static final DeferredHolder<SoundEvent,SoundEvent> DICE_MIKE_KOENIG = SOUND_EVENTS.register("dice_mike_koenig", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "dice_mike_koenig")));
  public static final DeferredHolder<SoundEvent,SoundEvent> FILL = SOUND_EVENTS.register("fill", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "fill")));
  public static final DeferredHolder<SoundEvent,SoundEvent> THUNK = SOUND_EVENTS.register("thunk", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "thunk")));
  public static final DeferredHolder<SoundEvent,SoundEvent> FAN_LOOP = SOUND_EVENTS.register("fan_loop", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "fan_loop")));
  public static final DeferredHolder<SoundEvent,SoundEvent> FAN_OFF = SOUND_EVENTS.register("fan_off", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "fan_off")));
  public static final DeferredHolder<SoundEvent,SoundEvent> FAN_ON = SOUND_EVENTS.register("fan_on", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "fan_on")));
  public static final DeferredHolder<SoundEvent,SoundEvent> METAL_PITCH = SOUND_EVENTS.register("metal_pitch", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "metal_pitch")));
  public static final DeferredHolder<SoundEvent,SoundEvent> EQUIP_EMERALD = SOUND_EVENTS.register("equip_emerald", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "equip_emerald")));
  public static net.neoforged.neoforge.common.util.DeferredSoundType SCAFFOLD = new net.neoforged.neoforge.common.util.DeferredSoundType(1.0F, 1.0F,
      //suppliers
      BLOCK_SCAFFOLDING_0,
      BLOCK_SCAFFOLDING_1,
      BLOCK_SCAFFOLDING_0,
      BLOCK_SCAFFOLDING_1,
      BLOCK_SCAFFOLDING_1);
}
