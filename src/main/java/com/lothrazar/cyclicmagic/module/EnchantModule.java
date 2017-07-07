package com.lothrazar.cyclicmagic.module;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.enchantment.EnchantAutoSmelt;
import com.lothrazar.cyclicmagic.enchantment.EnchantBase;
import com.lothrazar.cyclicmagic.enchantment.EnchantBeheading;
import com.lothrazar.cyclicmagic.enchantment.EnchantLaunch;
import com.lothrazar.cyclicmagic.enchantment.EnchantLifeLeech;
import com.lothrazar.cyclicmagic.enchantment.EnchantMagnet;
import com.lothrazar.cyclicmagic.enchantment.EnchantQuickdraw;
import com.lothrazar.cyclicmagic.enchantment.EnchantReach;
import com.lothrazar.cyclicmagic.enchantment.EnchantVenom;
import com.lothrazar.cyclicmagic.enchantment.EnchantXpBoost;
import com.lothrazar.cyclicmagic.registry.EnchantRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;

public class EnchantModule extends BaseModule implements IHasConfig {
  public static List<EnchantBase> loadedChants = new ArrayList<EnchantBase>();
  public static EnchantLaunch launch;
  public static EnchantMagnet magnet;
  public static EnchantVenom venom;
  public static EnchantLifeLeech lifeleech;
  public static EnchantAutoSmelt autosmelt;
  public static EnchantXpBoost xpboost;
  public static EnchantReach reach;
  private static EnchantBeheading beheading;
  private static EnchantQuickdraw quickdraw;
  private static int reachid;
  private static int launchid;
  private static int magnetid;
  private static int venomid;
  private static int lifeleechid;
  private static int autosmeltid;
  private static int xpboostid;
  private static int beheadingid;
  private int quickdrawid;
  private static boolean enablexpboost;
  private static boolean enableLaunch;
  private static boolean enableMagnet;
  private static boolean enableVenom;
  private static boolean enableLifeleech;
  private static boolean enableautosmelt;
  private static boolean enablereach;
  private static boolean enablebeheading;
  private boolean enableQuickdraw;
  @Override
  public void onPreInit() {
    if (enablereach) {
      reach = new EnchantReach();
      EnchantRegistry.register( new ResourceLocation(Const.MODID, reach.getName()), reach);
      ModCyclic.instance.events.register(EnchantModule.reach);
    }
    if (enablexpboost) {
      xpboost = new EnchantXpBoost();
      EnchantRegistry.register( new ResourceLocation(Const.MODID, xpboost.getName()), xpboost);
      ModCyclic.instance.events.register(xpboost);
      loadedChants.add(xpboost);
    }
    if (enableautosmelt) {
      autosmelt = new EnchantAutoSmelt();
      EnchantRegistry.register( new ResourceLocation(Const.MODID, autosmelt.getName()), autosmelt);
      ModCyclic.instance.events.register(autosmelt);
      loadedChants.add(autosmelt);
    }
    if (enableLaunch) {
      launch = new EnchantLaunch();
      EnchantRegistry.register( new ResourceLocation(Const.MODID, launch.getName()), launch);
      ModCyclic.instance.events.register(launch);
      loadedChants.add(launch);
    }
    if (enableMagnet) {
      magnet = new EnchantMagnet();
      EnchantRegistry.register( new ResourceLocation(Const.MODID, magnet.getName()), magnet);
      ModCyclic.instance.events.register(magnet);
      loadedChants.add(magnet);
    }
    if (enableVenom) {
      venom = new EnchantVenom();
      EnchantRegistry.register( new ResourceLocation(Const.MODID, venom.getName()), venom);
      ModCyclic.instance.events.register(venom);
      loadedChants.add(venom);
    }
    if (enableLifeleech) {
      lifeleech = new EnchantLifeLeech();
      EnchantRegistry.register( new ResourceLocation(Const.MODID, lifeleech.getName()), lifeleech);
      ModCyclic.instance.events.register(lifeleech);
      loadedChants.add(lifeleech);
    }
    if (enablebeheading) {
      beheading = new EnchantBeheading();
      EnchantRegistry.register( new ResourceLocation(Const.MODID, beheading.getName()), beheading);
      ModCyclic.instance.events.register(beheading);
      loadedChants.add(beheading);
    }
    if (enableQuickdraw) {
      quickdraw = new EnchantQuickdraw();
      EnchantRegistry.register( new ResourceLocation(Const.MODID, quickdraw.getName()), quickdraw);
      ModCyclic.instance.events.register(quickdraw);
      loadedChants.add(quickdraw);
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
    enablebeheading = c.getBoolean("EnchantBeheading", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableQuickdraw = c.getBoolean("EnchantQuickdraw", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
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
    beheadingid = c.getInt("enchant.beheading.id", Const.ConfigCategory.modpackMisc,
        93, 71, 999, "Id of beheading.  Change this if you get id conflicts with other mods.");
    quickdrawid = c.getInt("enchant.quickdraw.id", Const.ConfigCategory.modpackMisc,
        94, 71, 999, "Id of quickdraw.  Change this if you get id conflicts with other mods.");
    for (EnchantBase b : loadedChants) {
      if (b instanceof IHasConfig) {
        ((IHasConfig) b).syncConfig(c);
      }
    }
  }
}
