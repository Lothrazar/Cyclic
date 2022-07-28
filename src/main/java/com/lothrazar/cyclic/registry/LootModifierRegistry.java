package com.lothrazar.cyclic.registry;

import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class LootModifierRegistry {
  //  @SubscribeEvent
  //  public static void registerModifierSerializers(final RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
  //    IForgeRegistry<GlobalLootModifierSerializer<?>> r = event.getRegistry();
  //    //    if (AutoSmeltEnchant.CFG.get()) {
  //    r.register(new AutoSmeltEnchant.Serializer().setRegistryName(ModCyclic.MODID + ":" + AutoSmeltEnchant.ID));
  //    //    }
  //  }
}
