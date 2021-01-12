package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.base.EnchantBase;
import com.lothrazar.cyclic.enchant.EnchantAutoSmelt;
import com.lothrazar.cyclic.enchant.EnchantBeekeeper;
import com.lothrazar.cyclic.enchant.EnchantBeheading;
import com.lothrazar.cyclic.enchant.EnchantCurse;
import com.lothrazar.cyclic.enchant.EnchantDisarm;
import com.lothrazar.cyclic.enchant.EnchantExcavation;
import com.lothrazar.cyclic.enchant.EnchantGrowth;
import com.lothrazar.cyclic.enchant.EnchantLaunch;
import com.lothrazar.cyclic.enchant.EnchantLifeLeech;
import com.lothrazar.cyclic.enchant.EnchantMagnet;
import com.lothrazar.cyclic.enchant.EnchantMultishot;
import com.lothrazar.cyclic.enchant.EnchantPearl;
import com.lothrazar.cyclic.enchant.EnchantQuickdraw;
import com.lothrazar.cyclic.enchant.EnchantReach;
import com.lothrazar.cyclic.enchant.EnchantStep;
import com.lothrazar.cyclic.enchant.EnchantTraveller;
import com.lothrazar.cyclic.enchant.EnchantVenom;
import com.lothrazar.cyclic.enchant.EnchantXp;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EnchantRegistry {

  @SubscribeEvent
  public static void onEnchantRegister(final RegistryEvent.Register<Enchantment> event) {
    IForgeRegistry<Enchantment> r = event.getRegistry();
    register(r, new EnchantBeheading(Enchantment.Rarity.COMMON, EnchantmentType.WEAPON, EquipmentSlotType.MAINHAND).setRegistryName(EnchantBeheading.id));
    register(r, new EnchantExcavation(Enchantment.Rarity.RARE, EnchantmentType.DIGGER, EquipmentSlotType.MAINHAND).setRegistryName(EnchantExcavation.id));
    register(r, new EnchantXp(Enchantment.Rarity.UNCOMMON, EnchantmentType.DIGGER, EquipmentSlotType.MAINHAND).setRegistryName(EnchantXp.id));
    register(r, new EnchantGrowth(Enchantment.Rarity.RARE, EnchantmentType.DIGGER, EquipmentSlotType.MAINHAND).setRegistryName(EnchantGrowth.id));
    register(r, new EnchantLaunch(Enchantment.Rarity.RARE, EnchantmentType.ARMOR_FEET, EquipmentSlotType.FEET).setRegistryName(EnchantLaunch.id));
    register(r, new EnchantLifeLeech(Enchantment.Rarity.UNCOMMON, EnchantmentType.WEAPON, EquipmentSlotType.MAINHAND).setRegistryName(EnchantLifeLeech.id));
    register(r, new EnchantMagnet(Enchantment.Rarity.VERY_RARE, EnchantmentType.BREAKABLE, EquipmentSlotType.MAINHAND).setRegistryName(EnchantMagnet.id));
    register(r, new EnchantMultishot(Enchantment.Rarity.VERY_RARE, EnchantmentType.BOW, EquipmentSlotType.MAINHAND).setRegistryName(EnchantMultishot.id));
    register(r, new EnchantQuickdraw(Enchantment.Rarity.RARE, EnchantmentType.BOW, EquipmentSlotType.MAINHAND).setRegistryName(EnchantQuickdraw.id));
    register(r, new EnchantReach(Enchantment.Rarity.RARE, EnchantmentType.ARMOR_CHEST, EquipmentSlotType.CHEST).setRegistryName(EnchantReach.id));
    register(r, new EnchantStep(Enchantment.Rarity.RARE, EnchantmentType.ARMOR_LEGS, EquipmentSlotType.LEGS).setRegistryName(EnchantStep.id));
    register(r, new EnchantTraveller(Enchantment.Rarity.VERY_RARE, EnchantmentType.ARMOR_LEGS, EquipmentSlotType.LEGS).setRegistryName(EnchantTraveller.id));
    register(r, new EnchantVenom(Enchantment.Rarity.UNCOMMON, EnchantmentType.WEAPON, EquipmentSlotType.MAINHAND).setRegistryName(EnchantVenom.id));
    register(r, new EnchantAutoSmelt(Enchantment.Rarity.RARE, EnchantmentType.DIGGER, EquipmentSlotType.MAINHAND).setRegistryName(EnchantAutoSmelt.id));
    register(r, new EnchantDisarm(Enchantment.Rarity.UNCOMMON, EnchantmentType.WEAPON, EquipmentSlotType.MAINHAND).setRegistryName(EnchantDisarm.id));
    register(r, new EnchantCurse(Enchantment.Rarity.RARE, EnchantmentType.ARMOR, EquipmentSlotType.CHEST).setRegistryName(EnchantCurse.id));
    register(r, new EnchantPearl(Enchantment.Rarity.RARE, EnchantmentType.WEAPON, EquipmentSlotType.MAINHAND).setRegistryName(EnchantPearl.id));
    register(r, new EnchantBeekeeper(Enchantment.Rarity.VERY_RARE, EnchantmentType.ARMOR_HEAD, EquipmentSlotType.HEAD).setRegistryName(EnchantBeekeeper.id));
  }

  private static void register(IForgeRegistry<Enchantment> r, Enchantment e) {
    EnchantBase ench = (EnchantBase) e;
    if (ench.isEnabled())
      r.register(ench);
  }
}
