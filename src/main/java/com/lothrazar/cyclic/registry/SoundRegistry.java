package com.lothrazar.cyclic.registry;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import net.minecraft.block.SoundType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SoundRegistry {

  private static List<SoundEvent> sounds = new ArrayList<>();
  public static SoundEvent block_scaffolding_0 = make("block_scaffolding_0");
  public static SoundEvent block_scaffolding_1 = make("block_scaffolding_1");
  public static SoundEvent dungeonfinder = make("dungeonfinder");
  public static SoundEvent spikes_on = make("spikes_on");
  public static SoundEvent spikes_off = make("spikes_off");
  public static SoundType SCAFFOLD = new SoundType(1.0F, 1.0F, block_scaffolding_0, block_scaffolding_1, block_scaffolding_0, block_scaffolding_1, block_scaffolding_1);

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
