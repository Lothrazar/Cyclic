package com.lothrazar.cyclicmagic.registry;
import java.util.ArrayList;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.enchantment.EnchantBase;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnchantRegistry {
  public static ArrayList<EnchantBase> enchants = new ArrayList<EnchantBase>();
  public static void register(EnchantBase ench) {
    ResourceLocation resourceLocation = new ResourceLocation(Const.MODID, ench.getName());
    ench.setRegistryName(resourceLocation);
    ModCyclic.instance.events.register(ench);
    enchants.add(ench);
  }
  @SubscribeEvent
  public static void onRegistryEvent(RegistryEvent.Register<Enchantment> event) {
    for (Enchantment b : enchants) {
      event.getRegistry().register(b);
    }
  }
  public static String getStrForLevel(int lvl) {
    if (lvl == 0) {
      return "I";
    }
    return UtilChat.lang("enchantment.level." + lvl);
  }
}
