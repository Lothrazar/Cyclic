package com.lothrazar.cyclicmagic.registry;
import java.util.ArrayList;
import com.lothrazar.cyclicmagic.enchantment.EnchantQuickdraw;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnchantRegistry {
  public static ArrayList<Enchantment> enchants = new ArrayList<Enchantment>();
  public static void register(ResourceLocation resourceLocation, Enchantment quickdraw) {
    quickdraw.setRegistryName(resourceLocation);
    enchants.add(quickdraw);
  }
  @SubscribeEvent
  public static void onRegistryEvent(RegistryEvent.Register<Enchantment> event) {
    for (Enchantment b : enchants) {
      event.getRegistry().register(b);
    }
  }
}
