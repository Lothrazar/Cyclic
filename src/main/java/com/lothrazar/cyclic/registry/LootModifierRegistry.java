package com.lothrazar.cyclic.registry;

import javax.annotation.Nonnull;
import com.lothrazar.cyclic.ConfigManager;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.enchant.EnchantAutoSmelt;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class LootModifierRegistry {

  @SubscribeEvent
  public static void registerModifierSerializers(@Nonnull final RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
    if (ConfigManager.ENCHANTMENTS.get()) {
      event.getRegistry().register(new EnchantAutoSmelt.Serializer().setRegistryName(ModCyclic.MODID + ":auto_smelt"));
    }
  }
}
