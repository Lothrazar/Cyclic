package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.SoundType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SoundRegistry {

  private static final List<SoundEvent> SOUNDS = new ArrayList<>();
  public static final SoundEvent BASEY = make("basey");
  public static final SoundEvent BASS_ECHO = make("bass_echo");
  public static final SoundEvent BIP = make("bip");
  public static final SoundEvent BUZZP = make("buzzp");
  public static final SoundEvent BWEWE = make("bwewe");
  public static final SoundEvent BWOAAAP = make("bwoaaap");
  public static final SoundEvent CHAOS_REAPER = make("chaos_reaper");
  public static final SoundEvent COIN = make("coin");
  public static final SoundEvent CRACK = make("crack");
  public static final SoundEvent CRACKLE = make("crackle");
  public static final SoundEvent DCOIN = make("dcoin");
  public static final SoundEvent DICE_MIKE_KOENIG_SHORT = make("dice_mike_koenig_short");
  public static final SoundEvent DOORBELL_MIKEKOENIG = make("doorbell_mikekoenig");
  public static final SoundEvent EXPLOSM = make("explosm");
  public static final SoundEvent FIREBALL_EXPLODE = make("fireball_explode");
  public static final SoundEvent FIRELAUNCH = make("firelaunch");
  public static final SoundEvent GOODLAUNCH = make("goodlaunch");
  public static final SoundEvent GUITAR = make("guitar");
  public static final SoundEvent HOVERING = make("hovering");
  public static final SoundEvent LASERBEANPEW = make("laserbeanpew");
  public static final SoundEvent MACHINE_LAUNCH = make("machine_launch");
  public static final SoundEvent MAGIC_MISSILE = make("magic_missile");
  public static final SoundEvent MONSTER_BALL_CAPTURE = make("monster_ball_capture");
  public static final SoundEvent MONSTER_BALL_RELEASE = make("monster_ball_release");
  public static final SoundEvent PEW = make("pew");
  public static final SoundEvent PEW_LONG = make("pew_long");
  public static final SoundEvent POW = make("pow");
  public static final SoundEvent POWERUPSCALES = make("powerupscales");
  public static final SoundEvent SPIKEMAYBE = make("spikemaybe");
  public static final SoundEvent SPIKES_IN = make("spikes_in");
  public static final SoundEvent SPIRIT_SEEKER = make("spirit_seeker");
  public static final SoundEvent STEP_HEIGHT_DOWN = make("step_height_down");
  public static final SoundEvent STEP_HEIGHT_UP = make("step_height_up");
  public static final SoundEvent PSCHEW_FIRE = make("pschew_fire");
  public static final SoundEvent FROST_STAFF_LAUNCH = make("frost_staff_launch");
  public static final SoundEvent LIGHTNING_STAFF_LAUNCH = make("lightning_staff_launch");
  public static final SoundEvent FIREBALL_STAFF_LAUNCH = make("fireball_staff_launch");
  public static final SoundEvent BLOCK_SCAFFOLDING_0 = make("block_scaffolding_0");
  public static final SoundEvent BLOCK_SCAFFOLDING_1 = make("block_scaffolding_1");
  public static final SoundEvent DUNGEONFINDER = make("dungeonfinder");
  public static final SoundEvent SPIKES_ON = make("spikes_on");
  public static final SoundEvent SPIKES_OFF = make("spikes_off");
  public static final SoundEvent WARP_ECHO = make("warp_echo");
  public static final SoundEvent TOOL_MODE = make("tool_mode");
  public static final SoundEvent DICE_MIKE_KOENIG = make("dice_mike_koenig");
  public static final SoundEvent FILL = make("fill");
  public static final SoundEvent THUNK = make("thunk");
  public static final SoundEvent FAN_LOOP = make("fan_loop");
  public static final SoundEvent FAN_OFF = make("fan_off");
  public static final SoundEvent FAN_ON = make("fan_on");
  public static final SoundEvent METAL_PITCH = make("metal_pitch");
  public static final SoundEvent ITEM_ARMOR_EQUIP_EMERALD = make("equip_emerald");
  //update: forge sound type exists apparently
  public static SoundType SCAFFOLD = new ForgeSoundType(1.0F, 1.0F,
      //suppliers
      () -> BLOCK_SCAFFOLDING_0,
      () -> BLOCK_SCAFFOLDING_1,
      () -> BLOCK_SCAFFOLDING_0,
      () -> BLOCK_SCAFFOLDING_1,
      () -> BLOCK_SCAFFOLDING_1);

  private static SoundEvent make(String s) {
    SoundEvent se = new SoundEvent(new ResourceLocation(ModCyclic.MODID, s));
    se.setRegistryName(s);
    SOUNDS.add(se);
    return se;
  }

  @SubscribeEvent
  public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
    IForgeRegistry<SoundEvent> r = event.getRegistry();
    for (SoundEvent se : SOUNDS) {
      r.register(se);
    }
  }
}
