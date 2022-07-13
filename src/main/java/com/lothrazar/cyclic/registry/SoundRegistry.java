package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SoundRegistry {

  public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ModCyclic.MODID);
  public static final RegistryObject<SoundEvent> BASEY = SOUND_EVENTS.register("basey", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "basey")));
  public static final RegistryObject<SoundEvent> BASS_ECHO = SOUND_EVENTS.register("bass_echo", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "bass_echo")));
  public static final RegistryObject<SoundEvent> BIP = SOUND_EVENTS.register("bip", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "bip")));
  public static final RegistryObject<SoundEvent> BUZZP = SOUND_EVENTS.register("buzzp", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "buzzp")));
  public static final RegistryObject<SoundEvent> BWEWE = SOUND_EVENTS.register("bwewe", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "bwewe")));
  public static final RegistryObject<SoundEvent> BWOAAAP = SOUND_EVENTS.register("bwoaaap", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "bwoaaap")));
  public static final RegistryObject<SoundEvent> CHAOS_REAPER = SOUND_EVENTS.register("chaos_reaper", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "chaos_reaper")));
  public static final RegistryObject<SoundEvent> COIN = SOUND_EVENTS.register("coin", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "coin")));
  public static final RegistryObject<SoundEvent> CRACK = SOUND_EVENTS.register("crack", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "crack")));
  public static final RegistryObject<SoundEvent> CRACKLE = SOUND_EVENTS.register("crackle", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "crackle")));
  public static final RegistryObject<SoundEvent> DCOIN = SOUND_EVENTS.register("dcoin", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "dcoin")));
  public static final RegistryObject<SoundEvent> DICE_MIKE_KOENIG_SHORT = SOUND_EVENTS.register("dice_mike_koenig_short", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "dice_mike_koenig_short")));
  public static final RegistryObject<SoundEvent> EXPLOSM = SOUND_EVENTS.register("explosm", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "explosm")));
  public static final RegistryObject<SoundEvent> FIREBALL_EXPLODE = SOUND_EVENTS.register("fireball_explode", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "fireball_explode")));
  public static final RegistryObject<SoundEvent> DOORBELL_MIKEKOENIG = SOUND_EVENTS.register("doorbell_mikekoenig", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "doorbell_mikekoenig")));
  public static final RegistryObject<SoundEvent> FIRELAUNCH = SOUND_EVENTS.register("firelaunch", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "firelaunch")));
  public static final RegistryObject<SoundEvent> GOODLAUNCH = SOUND_EVENTS.register("goodlaunch", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "goodlaunch")));
  public static final RegistryObject<SoundEvent> GUITAR = SOUND_EVENTS.register("guitar", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "guitar")));
  public static final RegistryObject<SoundEvent> HOVERING = SOUND_EVENTS.register("hovering", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "hovering")));
  public static final RegistryObject<SoundEvent> LASERBEANPEW = SOUND_EVENTS.register("laserbeanpew", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "laserbeanpew")));
  public static final RegistryObject<SoundEvent> MACHINE_LAUNCH = SOUND_EVENTS.register("machine_launch", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "machine_launch")));
  public static final RegistryObject<SoundEvent> MAGIC_MISSILE = SOUND_EVENTS.register("magic_missile", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "magic_missile")));
  public static final RegistryObject<SoundEvent> MONSTER_BALL_CAPTURE = SOUND_EVENTS.register("monster_ball_capture", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "monster_ball_capture")));
  public static final RegistryObject<SoundEvent> MONSTER_BALL_RELEASE = SOUND_EVENTS.register("monster_ball_release", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "monster_ball_release")));
  public static final RegistryObject<SoundEvent> PEW = SOUND_EVENTS.register("pew", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "pew")));
  public static final RegistryObject<SoundEvent> PEW_LONG = SOUND_EVENTS.register("pew_long", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "pew_long")));
  public static final RegistryObject<SoundEvent> POW = SOUND_EVENTS.register("pow", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "pow")));
  public static final RegistryObject<SoundEvent> POWERUPSCALES = SOUND_EVENTS.register("powerupscales", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "powerupscales")));
  public static final RegistryObject<SoundEvent> SPIKEMAYBE = SOUND_EVENTS.register("spikemaybe", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "spikemaybe")));
  public static final RegistryObject<SoundEvent> SPIKES_IN = SOUND_EVENTS.register("spikes_in", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "spikes_in")));
  public static final RegistryObject<SoundEvent> SPIRIT_SEEKER = SOUND_EVENTS.register("spirit_seeker", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "spirit_seeker")));
  public static final RegistryObject<SoundEvent> STEP_HEIGHT_DOWN = SOUND_EVENTS.register("step_height_down", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "step_height_down")));
  public static final RegistryObject<SoundEvent> STEP_HEIGHT_UP = SOUND_EVENTS.register("step_height_up", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "step_height_up")));
  public static final RegistryObject<SoundEvent> PSCHEW_FIRE = SOUND_EVENTS.register("pschew_fire", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "pschew_fire")));
  public static final RegistryObject<SoundEvent> FROST_STAFF_LAUNCH = SOUND_EVENTS.register("frost_staff_launch", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "frost_staff_launch")));
  public static final RegistryObject<SoundEvent> LIGHTNING_STAFF_LAUNCH = SOUND_EVENTS.register("lightning_staff_launch", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "lightning_staff_launch")));
  public static final RegistryObject<SoundEvent> FIREBALL_STAFF_LAUNCH = SOUND_EVENTS.register("fireball_staff_launch", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "fireball_staff_launch")));
  public static final RegistryObject<SoundEvent> BLOCK_SCAFFOLDING_0 = SOUND_EVENTS.register("block_scaffolding_0", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "block_scaffolding_0")));
  public static final RegistryObject<SoundEvent> BLOCK_SCAFFOLDING_1 = SOUND_EVENTS.register("block_scaffolding_1", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "block_scaffolding_1")));
  public static final RegistryObject<SoundEvent> DUNGEONFINDER = SOUND_EVENTS.register("dungeonfinder", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "dungeonfinder")));
  public static final RegistryObject<SoundEvent> SPIKES_ON = SOUND_EVENTS.register("spikes_on", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "spikes_on")));
  public static final RegistryObject<SoundEvent> SPIKES_OFF = SOUND_EVENTS.register("spikes_off", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "spikes_off")));
  public static final RegistryObject<SoundEvent> WARP_ECHO = SOUND_EVENTS.register("warp_echo", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "warp_echo")));
  public static final RegistryObject<SoundEvent> TOOL_MODE = SOUND_EVENTS.register("tool_mode", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "tool_mode")));
  public static final RegistryObject<SoundEvent> DICE_MIKE_KOENIG = SOUND_EVENTS.register("dice_mike_koenig", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "dice_mike_koenig")));
  public static final RegistryObject<SoundEvent> FILL = SOUND_EVENTS.register("fill", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "fill")));
  public static final RegistryObject<SoundEvent> THUNK = SOUND_EVENTS.register("thunk", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "thunk")));
  public static final RegistryObject<SoundEvent> FAN_LOOP = SOUND_EVENTS.register("fan_loop", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "fan_loop")));
  public static final RegistryObject<SoundEvent> FAN_OFF = SOUND_EVENTS.register("fan_off", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "fan_off")));
  public static final RegistryObject<SoundEvent> FAN_ON = SOUND_EVENTS.register("fan_on", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "fan_on")));
  public static final RegistryObject<SoundEvent> METAL_PITCH = SOUND_EVENTS.register("metal_pitch", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "metal_pitch")));
  public static final RegistryObject<SoundEvent> EQUIP_EMERALD = SOUND_EVENTS.register("equip_emerald", () -> new SoundEvent(new ResourceLocation(ModCyclic.MODID, "equip_emerald")));
  public static SoundType SCAFFOLD = new ForgeSoundType(1.0F, 1.0F,
      //suppliers
      BLOCK_SCAFFOLDING_0,
      BLOCK_SCAFFOLDING_1,
      BLOCK_SCAFFOLDING_0,
      BLOCK_SCAFFOLDING_1,
      BLOCK_SCAFFOLDING_1);
}
