package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ConfigManager;
import com.lothrazar.cyclic.enchant.*;
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
    if (ConfigManager.ENCHANTMENTS.get()) {
      r.register(new EnchantBeheading(Enchantment.Rarity.COMMON, EnchantmentType.WEAPON, EquipmentSlotType.MAINHAND).setRegistryName("beheading"));
      r.register(new EnchantExcavation(Enchantment.Rarity.RARE, EnchantmentType.DIGGER, EquipmentSlotType.MAINHAND).setRegistryName("excavate"));
      r.register(new EnchantXp(Enchantment.Rarity.UNCOMMON, EnchantmentType.DIGGER, EquipmentSlotType.MAINHAND).setRegistryName("experience_boost"));
      r.register(new EnchantGrowth(Enchantment.Rarity.RARE, EnchantmentType.DIGGER, EquipmentSlotType.MAINHAND).setRegistryName("growth"));
      r.register(new EnchantLaunch(Enchantment.Rarity.RARE, EnchantmentType.ARMOR_FEET, EquipmentSlotType.FEET).setRegistryName("launch"));
      r.register(new EnchantLifeLeech(Enchantment.Rarity.UNCOMMON, EnchantmentType.WEAPON, EquipmentSlotType.MAINHAND).setRegistryName("life_leech"));
      r.register(new EnchantMagnet(Enchantment.Rarity.VERY_RARE, EnchantmentType.BREAKABLE, EquipmentSlotType.MAINHAND).setRegistryName("magnet"));
      r.register(new EnchantMultishot(Enchantment.Rarity.VERY_RARE, EnchantmentType.BOW, EquipmentSlotType.MAINHAND).setRegistryName("multishot"));
      r.register(new EnchantQuickdraw(Enchantment.Rarity.RARE, EnchantmentType.BOW, EquipmentSlotType.MAINHAND).setRegistryName("quickshot"));
      r.register(new EnchantReach(Enchantment.Rarity.RARE, EnchantmentType.ARMOR_CHEST, EquipmentSlotType.CHEST).setRegistryName("reach"));
      r.register(new EnchantStep(Enchantment.Rarity.RARE, EnchantmentType.ARMOR_LEGS, EquipmentSlotType.LEGS).setRegistryName("step"));
      r.register(new EnchantTraveller(Enchantment.Rarity.VERY_RARE, EnchantmentType.ARMOR_LEGS, EquipmentSlotType.LEGS).setRegistryName("traveler"));
      r.register(new EnchantVenom(Enchantment.Rarity.UNCOMMON, EnchantmentType.WEAPON, EquipmentSlotType.MAINHAND).setRegistryName("venom"));
      r.register(new EnchantAutoSmelt(Enchantment.Rarity.RARE, EnchantmentType.DIGGER, EquipmentSlotType.MAINHAND).setRegistryName("auto_smelt"));
      r.register(new EnchantDisarm(Enchantment.Rarity.UNCOMMON, EnchantmentType.WEAPON, EquipmentSlotType.MAINHAND).setRegistryName("disarm"));
      r.register(new EnchantCurse(Enchantment.Rarity.RARE, EnchantmentType.ARMOR, EquipmentSlotType.CHEST).setRegistryName("curse"));
      r.register(new EnchantPearl(Enchantment.Rarity.RARE, EnchantmentType.WEAPON, EquipmentSlotType.MAINHAND).setRegistryName("ender"));
    }
  }
}