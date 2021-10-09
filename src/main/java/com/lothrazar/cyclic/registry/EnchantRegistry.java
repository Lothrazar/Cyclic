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
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EnchantRegistry {

  public static final EnchantBase MULTIBOW = (EnchantBase) new EnchantMultishot(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.BOW, EquipmentSlot.MAINHAND).setRegistryName(EnchantMultishot.ID);
  public static final EnchantBase EXCAVATE = (EnchantBase) new EnchantExcavation(Enchantment.Rarity.RARE, EnchantmentCategory.DIGGER, EquipmentSlot.MAINHAND).setRegistryName(EnchantExcavation.ID);
  public static final EnchantBase EXPERIENCE_BOOST = (EnchantBase) new EnchantXp(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.DIGGER, EquipmentSlot.MAINHAND).setRegistryName(EnchantXp.ID);
  public static final EnchantLaunch LAUNCH = (EnchantLaunch) new EnchantLaunch(Enchantment.Rarity.RARE, EnchantmentCategory.ARMOR_FEET, EquipmentSlot.FEET).setRegistryName(EnchantLaunch.ID);

  @SubscribeEvent
  public static void onEnchantRegister(final RegistryEvent.Register<Enchantment> event) {
    IForgeRegistry<Enchantment> r = event.getRegistry();
    register(r, EXCAVATE);
    register(r, EXPERIENCE_BOOST);
    register(r, new EnchantBeheading(Enchantment.Rarity.COMMON, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND).setRegistryName(EnchantBeheading.ID));
    register(r, new EnchantGrowth(Enchantment.Rarity.COMMON, EnchantmentCategory.DIGGER, EquipmentSlot.MAINHAND).setRegistryName(EnchantGrowth.ID));
    register(r, LAUNCH);
    register(r, new EnchantLifeLeech(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND).setRegistryName(EnchantLifeLeech.ID));
    register(r, new EnchantMagnet(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.BREAKABLE, EquipmentSlot.MAINHAND).setRegistryName(EnchantMagnet.ID));
    register(r, MULTIBOW);
    register(r, new EnchantQuickdraw(Enchantment.Rarity.RARE, EnchantmentCategory.BOW, EquipmentSlot.MAINHAND).setRegistryName(EnchantQuickdraw.ID));
    register(r, new EnchantReach(Enchantment.Rarity.RARE, EnchantmentCategory.ARMOR_CHEST, EquipmentSlot.CHEST).setRegistryName(EnchantReach.ID));
    register(r, new EnchantStep(Enchantment.Rarity.RARE, EnchantmentCategory.ARMOR_LEGS, EquipmentSlot.LEGS).setRegistryName(EnchantStep.ID));
    register(r, new EnchantTraveller(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.ARMOR_LEGS, EquipmentSlot.LEGS).setRegistryName(EnchantTraveller.ID));
    register(r, new EnchantVenom(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND).setRegistryName(EnchantVenom.ID));
    register(r, new EnchantAutoSmelt(Enchantment.Rarity.RARE, EnchantmentCategory.DIGGER, EquipmentSlot.MAINHAND).setRegistryName(EnchantAutoSmelt.ID));
    register(r, new EnchantDisarm(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND).setRegistryName(EnchantDisarm.ID));
    register(r, new EnchantCurse(Enchantment.Rarity.RARE, EnchantmentCategory.ARMOR, EquipmentSlot.CHEST).setRegistryName(EnchantCurse.ID));
    register(r, new EnchantPearl(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND).setRegistryName(EnchantPearl.ID));
    register(r, new EnchantBeekeeper(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.ARMOR_HEAD, EquipmentSlot.HEAD).setRegistryName(EnchantBeekeeper.ID));
  }

  private static void register(IForgeRegistry<Enchantment> r, Enchantment e) {
    EnchantBase ench = (EnchantBase) e;
    if (ench.isEnabled()) {
      r.register(ench);
    }
  }
}
