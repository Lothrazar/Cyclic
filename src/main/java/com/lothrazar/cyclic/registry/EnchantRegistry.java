package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.enchant.AutoSmeltEnchant;
import com.lothrazar.cyclic.enchant.BeekeeperEnchant;
import com.lothrazar.cyclic.enchant.BeheadingEnchant;
import com.lothrazar.cyclic.enchant.GloomCurseEnchant;
import com.lothrazar.cyclic.enchant.DisarmEnchant;
import com.lothrazar.cyclic.enchant.MultiJumpEnchant;
import com.lothrazar.cyclic.enchant.EnchantmentCyclic;
import com.lothrazar.cyclic.enchant.EnderPearlEnchant;
import com.lothrazar.cyclic.enchant.ExcavationEnchant;
import com.lothrazar.cyclic.enchant.GrowthEnchant;
import com.lothrazar.cyclic.enchant.LastStandEnchant;
import com.lothrazar.cyclic.enchant.LifeLeechEnchant;
import com.lothrazar.cyclic.enchant.MagnetEnchant;
import com.lothrazar.cyclic.enchant.MultiBowEnchant;
import com.lothrazar.cyclic.enchant.QuickdrawEnchant;
import com.lothrazar.cyclic.enchant.ReachEnchant;
import com.lothrazar.cyclic.enchant.SteadyEnchant;
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
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EnchantRegistry {

  public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, ModCyclic.MODID);
  // enchants 
  private static final EquipmentSlot[] ARMOR_SLOTS = new EquipmentSlot[] { EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET };
  public static final Enchantment TRAVELLER = new TravellerEnchant(Enchantment.Rarity.RARE, EnchantmentCategory.ARMOR_LEGS, EquipmentSlot.LEGS).setRegistryName(TravellerEnchant.ID);
  public static final EnchantmentCyclic MULTISHOT = (EnchantmentCyclic) new MultiBowEnchant(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.BOW, EquipmentSlot.MAINHAND).setRegistryName(MultiBowEnchant.ID);
  public static final EnchantmentCyclic EXCAVATE = (EnchantmentCyclic) new ExcavationEnchant(Enchantment.Rarity.RARE, EnchantmentCategory.DIGGER, EquipmentSlot.MAINHAND).setRegistryName(ExcavationEnchant.ID);
  public static final EnchantmentCyclic EXPERIENCE_BOOST = (EnchantmentCyclic) new XpEnchant(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.DIGGER, EquipmentSlot.MAINHAND).setRegistryName(XpEnchant.ID);
  public static final MultiJumpEnchant LAUNCH = (MultiJumpEnchant) new MultiJumpEnchant(Enchantment.Rarity.RARE, EnchantmentCategory.WEARABLE, new EquipmentSlot[] { EquipmentSlot.CHEST, EquipmentSlot.FEET }).setRegistryName(MultiJumpEnchant.ID);
  public static final SteadyEnchant STEADY = (SteadyEnchant) new SteadyEnchant(Enchantment.Rarity.RARE, EnchantmentCategory.WEARABLE, new EquipmentSlot[] { EquipmentSlot.CHEST, EquipmentSlot.LEGS }).setRegistryName(SteadyEnchant.ID);

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
    register(r, MULTISHOT);
    register(r, new QuickdrawEnchant(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.BOW, EquipmentSlot.MAINHAND).setRegistryName(QuickdrawEnchant.ID));
    register(r, new ReachEnchant(Enchantment.Rarity.RARE, EnchantmentCategory.WEARABLE, ARMOR_SLOTS).setRegistryName(ReachEnchant.ID));
    register(r, new StepEnchant(Enchantment.Rarity.RARE, EnchantmentCategory.ARMOR_LEGS, EquipmentSlot.LEGS).setRegistryName(StepEnchant.ID));
    register(r, TRAVELLER);
    register(r, new VenomEnchant(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND).setRegistryName(VenomEnchant.ID));
    register(r, new AutoSmeltEnchant(Enchantment.Rarity.RARE, EnchantmentCategory.DIGGER, EquipmentSlot.MAINHAND).setRegistryName(AutoSmeltEnchant.ID));
    register(r, new DisarmEnchant(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND).setRegistryName(DisarmEnchant.ID));
    register(r, new GloomCurseEnchant(Enchantment.Rarity.RARE, EnchantmentCategory.ARMOR, EquipmentSlot.CHEST).setRegistryName(GloomCurseEnchant.ID));
    register(r, new EnderPearlEnchant(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND).setRegistryName(EnderPearlEnchant.ID));
    register(r, new BeekeeperEnchant(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.ARMOR_HEAD, EquipmentSlot.HEAD).setRegistryName(BeekeeperEnchant.ID));
    register(r, new LastStandEnchant(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.ARMOR_LEGS, EquipmentSlot.LEGS).setRegistryName(LastStandEnchant.ID));
    register(r, STEADY);
  }

  private static void register(IForgeRegistry<Enchantment> r, Enchantment e) {
    EnchantmentCyclic ench = (EnchantmentCyclic) e;
    if (ench.isEnabled()) {
      r.register(ench);
    }
  }
  //TODO: 1.19 change to defferr
  //  public static final RegistryObject<TravellerEnchant> TRAVELLER = ENCHANTMENTS.register(TravellerEnchant.ID, () -> new TravellerEnchant(Enchantment.Rarity.RARE, EnchantmentCategory.ARMOR_LEGS, EquipmentSlot.LEGS));
  //  public static final RegistryObject<MultishotEnchant> MULTIBOW = ENCHANTMENTS.register(MultishotEnchant.ID, () -> new MultishotEnchant(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.BOW, EquipmentSlot.MAINHAND));
  //  public static final RegistryObject<ExcavationEnchant> EXCAVATE = ENCHANTMENTS.register(ExcavationEnchant.ID, () -> new ExcavationEnchant(Enchantment.Rarity.RARE, EnchantmentCategory.DIGGER, EquipmentSlot.MAINHAND));
  //  public static final RegistryObject<XpEnchant> EXPERIENCE_BOOST = ENCHANTMENTS.register(XpEnchant.ID, () -> new XpEnchant(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.DIGGER, EquipmentSlot.MAINHAND));
  //  public static final RegistryObject<ElytraLaunchEnchant> LAUNCH = ENCHANTMENTS.register(ElytraLaunchEnchant.ID, () -> new ElytraLaunchEnchant(Enchantment.Rarity.RARE, EnchantmentCategory.WEARABLE, new EquipmentSlot[] { EquipmentSlot.CHEST, EquipmentSlot.FEET }));
  //  public static final RegistryObject<SteadyEnchant> STEADY = ENCHANTMENTS.register(SteadyEnchant.ID, () -> new SteadyEnchant(Enchantment.Rarity.RARE, EnchantmentCategory.WEARABLE, new EquipmentSlot[] { EquipmentSlot.CHEST, EquipmentSlot.LEGS }));
  //  public static final RegistryObject<BeheadingEnchant> BEHEADING = ENCHANTMENTS.register(BeheadingEnchant.ID, () -> new BeheadingEnchant(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND));
  //  public static final RegistryObject<GrowthEnchant> GROWTH = ENCHANTMENTS.register(GrowthEnchant.ID, () -> new GrowthEnchant(Enchantment.Rarity.COMMON, EnchantmentCategory.DIGGER, EquipmentSlot.MAINHAND));
  //  public static final RegistryObject<LifeLeechEnchant> LIFELEECH = ENCHANTMENTS.register(LifeLeechEnchant.ID, () -> new LifeLeechEnchant(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND));
  //  public static final RegistryObject<MagnetEnchant> MAGNET = ENCHANTMENTS.register(MagnetEnchant.ID, () -> new MagnetEnchant(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.BREAKABLE, EquipmentSlot.MAINHAND));
  //  public static final RegistryObject<QuickdrawEnchant> QUICKDRAW = ENCHANTMENTS.register(QuickdrawEnchant.ID, () -> new QuickdrawEnchant(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.BOW, EquipmentSlot.MAINHAND));
  //  public static final RegistryObject<ReachEnchant> REACH = ENCHANTMENTS.register(ReachEnchant.ID, () -> new ReachEnchant(Enchantment.Rarity.RARE, EnchantmentCategory.WEARABLE, ARMOR_SLOTS));
  //  public static final RegistryObject<StepEnchant> STEP = ENCHANTMENTS.register(StepEnchant.ID, () -> new StepEnchant(Enchantment.Rarity.RARE, EnchantmentCategory.ARMOR_LEGS, EquipmentSlot.LEGS));
  //  public static final RegistryObject<VenomEnchant> VENOM = ENCHANTMENTS.register(VenomEnchant.ID, () -> new VenomEnchant(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND));
  //  public static final RegistryObject<AutoSmeltEnchant> AUTOSMELT = ENCHANTMENTS.register(AutoSmeltEnchant.ID, () -> new AutoSmeltEnchant(Enchantment.Rarity.RARE, EnchantmentCategory.DIGGER, EquipmentSlot.MAINHAND));
  //  public static final RegistryObject<DisarmEnchant> DISARM = ENCHANTMENTS.register(DisarmEnchant.ID, () -> new DisarmEnchant(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND));
  //  public static final RegistryObject<CurseEnchant> CURSE = ENCHANTMENTS.register(CurseEnchant.ID, () -> new CurseEnchant(Enchantment.Rarity.RARE, EnchantmentCategory.ARMOR, EquipmentSlot.CHEST));
  //  public static final RegistryObject<EnderPearlEnchant> PEARL = ENCHANTMENTS.register(PearlEnchant.ID, () -> new PearlEnchant(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND));
  //  public static final RegistryObject<BeekeeperEnchant> BEEKEEPER = ENCHANTMENTS.register(BeekeeperEnchant.ID, () -> new BeekeeperEnchant(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.ARMOR_HEAD, EquipmentSlot.HEAD));
  //  public static final RegistryObject<StandEnchant> STAND = ENCHANTMENTS.register(StandEnchant.ID, () -> new StandEnchant(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.ARMOR_LEGS, EquipmentSlot.LEGS));
}
