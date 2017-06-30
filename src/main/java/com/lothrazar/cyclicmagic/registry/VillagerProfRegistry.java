package com.lothrazar.cyclicmagic.registry;
import java.util.ArrayList;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;

public class VillagerProfRegistry {
  public static ArrayList<VillagerProfession> villagers = new ArrayList<VillagerProfession>();
  public static void register(VillagerProfession quickdraw) {
    villagers.add(quickdraw);
  }
  @SubscribeEvent
  public static void onRegistryEvent(RegistryEvent.Register<VillagerProfession> event) {
    for (VillagerProfession b : villagers) {
      event.getRegistry().register(b);
    }
  }
}
