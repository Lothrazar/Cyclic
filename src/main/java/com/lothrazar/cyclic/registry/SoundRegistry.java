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
  public static SoundEvent frost_staff_launch = make("frost_staff_launch");
  public static SoundEvent lightning_staff_launch = make("lightning_staff_launch");
  public static SoundEvent fireball_staff_launch = make("fireball_staff_launch");
  public static SoundEvent block_scaffolding_0 = make("block_scaffolding_0");
  public static SoundEvent block_scaffolding_1 = make("block_scaffolding_1");
  public static SoundEvent dungeonfinder = make("dungeonfinder");
  public static SoundEvent spikes_on = make("spikes_on");
  public static SoundEvent spikes_off = make("spikes_off");
  public static SoundEvent warp_echo = make("warp_echo");
  public static SoundEvent tool_mode = make("tool_mode");
  public static SoundEvent dice_mike_koenig = make("dice_mike_koenig");
  public static SoundEvent fill = make("fill");
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
