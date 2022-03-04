package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.enchant.AutoSmeltEnchant;
import com.lothrazar.cyclic.enchant.BeekeeperEnchant;
import com.lothrazar.cyclic.enchant.BeheadingEnchant;
import com.lothrazar.cyclic.enchant.CurseEnchant;
import com.lothrazar.cyclic.enchant.DisarmEnchant;
import com.lothrazar.cyclic.enchant.ElytraLaunchEnchant;
import com.lothrazar.cyclic.enchant.EnchantmentCyclic;
import com.lothrazar.cyclic.enchant.ExcavationEnchant;
import com.lothrazar.cyclic.enchant.GrowthEnchant;
import com.lothrazar.cyclic.enchant.LifeLeechEnchant;
import com.lothrazar.cyclic.enchant.MagnetEnchant;
import com.lothrazar.cyclic.enchant.Multishot;
import com.lothrazar.cyclic.enchant.PearlEnchant;
import com.lothrazar.cyclic.enchant.QuickdrawEnchant;
import com.lothrazar.cyclic.enchant.ReachEnchant;
import com.lothrazar.cyclic.enchant.StandEnchant;
import com.lothrazar.cyclic.enchant.StepEnchant;
import com.lothrazar.cyclic.enchant.TravellerEnchant;
import com.lothrazar.cyclic.enchant.VenomEnchant;
import com.lothrazar.cyclic.enchant.XpEnchant;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EnchantRegistry {

  private static final EquipmentSlot[] ARMOR_SLOTS = new EquipmentSlot[] { EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET };
  public static final Enchantment TRAVELLER = new TravellerEnchant(Enchantment.Rarity.RARE, EnchantmentCategory.ARMOR_LEGS, EquipmentSlot.LEGS).setRegistryName(TravellerEnchant.ID);
  public static final EnchantmentCyclic MULTIBOW = (EnchantmentCyclic) new Multishot(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.BOW, EquipmentSlot.MAINHAND).setRegistryName(Multishot.ID);
  public static final EnchantmentCyclic EXCAVATE = (EnchantmentCyclic) new ExcavationEnchant(Enchantment.Rarity.RARE, EnchantmentCategory.DIGGER, EquipmentSlot.MAINHAND).setRegistryName(ExcavationEnchant.ID);
  public static final EnchantmentCyclic EXPERIENCE_BOOST = (EnchantmentCyclic) new XpEnchant(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.DIGGER, EquipmentSlot.MAINHAND).setRegistryName(XpEnchant.ID);
  public static final ElytraLaunchEnchant LAUNCH = (ElytraLaunchEnchant) new ElytraLaunchEnchant(Enchantment.Rarity.RARE, EnchantmentCategory.WEARABLE, new EquipmentSlot[] { EquipmentSlot.CHEST, EquipmentSlot.FEET }).setRegistryName(ElytraLaunchEnchant.ID);

  @SubscribeEvent
  public static void onEnchantRegister(final RegistryEvent.Register<Enchantment> event) {
    IForgeRegistry<Enchantment> r = event.getRegistry();
    register(r, EXCAVATE);
    register(r, EXPERIENCE_BOOST);
    register(r, new BeheadingEnchant(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND).setRegistryName(BeheadingEnchant.ID));
    register(r, new GrowthEnchant(Enchantment.Rarity.COMMON, EnchantmentCategory.DIGGER, EquipmentSlot.MAINHAND).setRegistryName(GrowthEnchant.ID));
    register(r, LAUNCH);
    register(r, new LifeLeechEnchant(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND).setRegistryName(LifeLeechEnchant.ID));
    register(r, new MagnetEnchant(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.BREAKABLE, EquipmentSlot.MAINHAND).setRegistryName(MagnetEnchant.ID));
    register(r, MULTIBOW);
    register(r, new QuickdrawEnchant(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.BOW, EquipmentSlot.MAINHAND).setRegistryName(QuickdrawEnchant.ID));
    register(r, new ReachEnchant(Enchantment.Rarity.RARE, EnchantmentCategory.WEARABLE, ARMOR_SLOTS).setRegistryName(ReachEnchant.ID));
    register(r, new StepEnchant(Enchantment.Rarity.RARE, EnchantmentCategory.ARMOR_LEGS, EquipmentSlot.LEGS).setRegistryName(StepEnchant.ID));
    register(r, TRAVELLER);
    register(r, new VenomEnchant(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND).setRegistryName(VenomEnchant.ID));
    register(r, new AutoSmeltEnchant(Enchantment.Rarity.RARE, EnchantmentCategory.DIGGER, EquipmentSlot.MAINHAND).setRegistryName(AutoSmeltEnchant.ID));
    register(r, new DisarmEnchant(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND).setRegistryName(DisarmEnchant.ID));
    register(r, new CurseEnchant(Enchantment.Rarity.RARE, EnchantmentCategory.ARMOR, EquipmentSlot.CHEST).setRegistryName(CurseEnchant.ID));
    register(r, new PearlEnchant(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND).setRegistryName(PearlEnchant.ID));
    register(r, new BeekeeperEnchant(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.ARMOR_HEAD, EquipmentSlot.HEAD).setRegistryName(BeekeeperEnchant.ID));
    register(r, new StandEnchant(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.ARMOR_LEGS, EquipmentSlot.LEGS).setRegistryName(StandEnchant.ID));
  }

  private static void register(IForgeRegistry<Enchantment> r, Enchantment e) {
    EnchantmentCyclic ench = (EnchantmentCyclic) e;
    if (ench.isEnabled()) {
      r.register(ench);
    }
  }
}
