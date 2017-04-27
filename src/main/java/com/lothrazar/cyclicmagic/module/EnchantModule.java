package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.CyclicGuideBook;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.enchantment.EnchantAutoSmelt;
import com.lothrazar.cyclicmagic.enchantment.EnchantLaunch;
import com.lothrazar.cyclicmagic.enchantment.EnchantLifeLeech;
import com.lothrazar.cyclicmagic.enchantment.EnchantMagnet;
import com.lothrazar.cyclicmagic.enchantment.EnchantReach;
import com.lothrazar.cyclicmagic.enchantment.EnchantVenom;
import com.lothrazar.cyclicmagic.enchantment.EnchantXpBoost;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;

public class EnchantModule extends BaseModule implements IHasConfig {
  public static EnchantLaunch launch;
  public static EnchantMagnet magnet;
  public static EnchantVenom venom;
  public static EnchantLifeLeech lifeleech;
  public static EnchantAutoSmelt autosmelt;
  public static EnchantXpBoost xpboost;
  public static EnchantReach reach;
  private static int reachid;
  private static int launchid;
  private static int magnetid;
  private static int venomid;
  private static int lifeleechid;
  private static int autosmeltid;
  private static int xpboostid;
  private static boolean enablexpboost;
  private static boolean enableLaunch;
  private static boolean enableMagnet;
  private static boolean enableVenom;
  private static boolean enableLifeleech;
  private boolean enableautosmelt;
  private boolean enablereach;
  @Override
  public void onPreInit() {
    if (enablereach) {
      reach = new EnchantReach();
      Enchantment.REGISTRY.register(reachid, new ResourceLocation(reach.getName()), reach);
      ModCyclic.instance.events.register(EnchantModule.reach);
    }
    if (enablexpboost) {
      xpboost = new EnchantXpBoost();
      Enchantment.REGISTRY.register(xpboostid, new ResourceLocation(xpboost.getName()), xpboost);
      ModCyclic.instance.events.register(EnchantModule.xpboost);
    }
    if (enableautosmelt) {
      autosmelt = new EnchantAutoSmelt();
      Enchantment.REGISTRY.register(autosmeltid, new ResourceLocation(autosmelt.getName()), autosmelt);
      ModCyclic.instance.events.register(EnchantModule.autosmelt);
    }
    if (enableLaunch) {
      launch = new EnchantLaunch();
      Enchantment.REGISTRY.register(launchid, new ResourceLocation(launch.getName()), launch);
      ModCyclic.instance.events.register(EnchantModule.launch);
    }
    if (enableMagnet) {
      magnet = new EnchantMagnet();
      Enchantment.REGISTRY.register(magnetid, new ResourceLocation(magnet.getName()), magnet);
      ModCyclic.instance.events.register(EnchantModule.magnet);
    }
    if (enableVenom) {
      venom = new EnchantVenom();
      Enchantment.REGISTRY.register(venomid, new ResourceLocation(venom.getName()), venom);
      ModCyclic.instance.events.register(EnchantModule.venom);
    }
    if (enableLifeleech) {
      lifeleech = new EnchantLifeLeech();
      Enchantment.REGISTRY.register(lifeleechid, new ResourceLocation(lifeleech.getName()), lifeleech);
      ModCyclic.instance.events.register(EnchantModule.lifeleech);
    }
  }
  @Override
  public void syncConfig(Configuration c) {
    enablereach = c.getBoolean("EnchantReach", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enablexpboost = c.getBoolean("EnchantExpBoost", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableautosmelt = c.getBoolean("EnchantAutoSmelt", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableLaunch = c.getBoolean("EnchantLaunch", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableMagnet = c.getBoolean("EnchantMagnet", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableVenom = c.getBoolean("EnchantVenom", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableLifeleech = c.getBoolean("EnchantLifeLeech", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    launchid = c.getInt("enchant.launch.id", Const.ConfigCategory.modpackMisc,
        86, 71, 999, "Id of the launch enchantment (double jump on boots).  Change this if you get id conflicts with other mods.");
    magnetid = c.getInt("enchant.magnet.id", Const.ConfigCategory.modpackMisc,
        87, 71, 999, "Id of the magnet enchantment.  Change this if you get id conflicts with other mods.");
    venomid = c.getInt("enchant.venom.id", Const.ConfigCategory.modpackMisc,
        88, 71, 999, "Id of the venom enchantment.  Change this if you get id conflicts with other mods.");
    lifeleechid = c.getInt("enchant.lifeleech.id", Const.ConfigCategory.modpackMisc,
        89, 71, 999, "Id of the lifeleech enchantment.  Change this if you get id conflicts with other mods.");
    autosmeltid = c.getInt("enchant.autosmelt.id", Const.ConfigCategory.modpackMisc,
        90, 71, 999, "Id of the autosmelt enchantment.  Change this if you get id conflicts with other mods.");
    xpboostid = c.getInt("enchant.xpboostid.id", Const.ConfigCategory.modpackMisc,
        91, 71, 999, "Id of the xpboost enchantment.  Change this if you get id conflicts with other mods.");
    reachid = c.getInt("enchant.reach.id", Const.ConfigCategory.modpackMisc,
        92, 71, 999, "Id of the reach enchantment.  Change this if you get id conflicts with other mods.");
  }
}
