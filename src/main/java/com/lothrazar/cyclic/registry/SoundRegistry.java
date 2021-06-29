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

  private static List<SoundEvent> sounds = new ArrayList<>();
  public static SoundEvent basey = make("basey");
  public static SoundEvent bass_echo = make("bass_echo");
  public static SoundEvent bip = make("bip");
  public static SoundEvent buzzp = make("buzzp");
  public static SoundEvent bwewe = make("bwewe");
  public static SoundEvent bwoaaap = make("bwoaaap");
  public static SoundEvent chaos_reaper = make("chaos_reaper");
  public static SoundEvent coin = make("coin");
  public static SoundEvent crack = make("crack");
  public static SoundEvent crackle = make("crackle");
  public static SoundEvent dcoin = make("dcoin");
  public static SoundEvent dice_mike_koenig_short = make("dice_mike_koenig_short");
  public static SoundEvent doorbell_mikekoenig = make("doorbell_mikekoenig");
  public static SoundEvent explosm = make("explosm");
  public static SoundEvent fireball_explode = make("fireball_explode");
  public static SoundEvent firelaunch = make("firelaunch");
  public static SoundEvent goodlaunch = make("goodlaunch");
  public static SoundEvent guitar = make("guitar");
  public static SoundEvent hovering = make("hovering");
  public static SoundEvent laserbeanpew = make("laserbeanpew");
  public static SoundEvent machine_launch = make("machine_launch");
  public static SoundEvent magic_missile = make("magic_missile");
  public static SoundEvent monster_ball_capture = make("monster_ball_capture");
  public static SoundEvent monster_ball_release = make("monster_ball_release");
  public static SoundEvent pew = make("pew");
  public static SoundEvent pew_long = make("pew_long");
  public static SoundEvent pow = make("pow");
  public static SoundEvent powerupscales = make("powerupscales");
  public static SoundEvent spikemaybe = make("spikemaybe");
  public static SoundEvent spikes_in = make("spikes_in");
  public static SoundEvent spirit_seeker = make("spirit_seeker");
  public static SoundEvent step_height_down = make("step_height_down");
  public static SoundEvent step_height_up = make("step_height_up");
  public static SoundEvent pschew_fire = make("pschew_fire");
  public static SoundEvent frost_staff_launch = make("frost_staff_launch");
  public static SoundEvent lightning_staff_launch = make("lightning_staff_launch");
  public static SoundEvent fireball_staff_launch = make("fireball_staff_launch");
  public static SoundEvent block_scaffolding_0 = make("block_scaffolding_0");
  public static SoundEvent block_scaffolding_1 = make("block_scaffolding_1");
  public static SoundEvent dungeonfinder = make("dungeonfinder");
  public static SoundEvent spikes_on = make("spikes_on");
  public static SoundEvent spikes_off = make("spikes_off");
  public static SoundEvent echo = make("echo");
  public static SoundEvent warp_echo = make("warp_echo");
  public static SoundEvent tool_mode = make("tool_mode");
  public static SoundEvent dice_mike_koenig = make("dice_mike_koenig");
  public static SoundEvent fill = make("fill");
  public static SoundEvent thunk = make("thunk");
  public static SoundEvent item_armor_equip_emerald = make("equip_emerald");
  //update: forge sound type exists apparently
  public static SoundType SCAFFOLD = new ForgeSoundType(1.0F, 1.0F,
      //suppliers
      () -> block_scaffolding_0,
      () -> block_scaffolding_1,
      () -> block_scaffolding_0,
      () -> block_scaffolding_1,
      () -> block_scaffolding_1);

  private static SoundEvent make(String s) {
    SoundEvent se = new SoundEvent(new ResourceLocation(ModCyclic.MODID, s));
    se.setRegistryName(s);
    sounds.add(se);
    return se;
  }

  @SubscribeEvent
  public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
    IForgeRegistry<SoundEvent> r = event.getRegistry();
    for (SoundEvent se : sounds) {
      r.register(se);
    }
  }
}
