package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.config.IHasConfig;
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
import com.lothrazar.cyclicmagic.enchantment.EnchantWaterwalking;
import com.lothrazar.cyclicmagic.enchantment.EnchantXpBoost;
import com.lothrazar.cyclicmagic.registry.EnchantRegistry;
import net.minecraftforge.common.config.Configuration;

public class EnchantModule extends BaseModule implements IHasConfig {
  public static EnchantLaunch launch;
  public static EnchantMagnet magnet;
  public static EnchantVenom venom;
  public static EnchantLifeLeech lifeleech;
  public static EnchantAutoSmelt autosmelt;
  public static EnchantXpBoost xpboost;
  public static EnchantReach reach;
  public static EnchantBeheading beheading;
  public static EnchantQuickdraw quickdraw;
  public static EnchantWaterwalking waterwalk;
  private boolean enablexpboost;
  private boolean enableLaunch;
  private boolean enableMagnet;
  private boolean enableVenom;
  private boolean enableLifeleech;
  private boolean enableautosmelt;
  private boolean enablereach;
  private boolean enablebeheading;
  private boolean enableQuickdraw;
  private boolean enablewaterwalk;
  @Override
  public void onPreInit() {
    if (enablewaterwalk) {
      waterwalk = new EnchantWaterwalking();
      EnchantRegistry.register(waterwalk);
    }
    if (enablereach) {
      reach = new EnchantReach();
      EnchantRegistry.register(reach);
    }
    if (enablexpboost) {
      xpboost = new EnchantXpBoost();
      EnchantRegistry.register(xpboost);
    }
    if (enableautosmelt) {
      autosmelt = new EnchantAutoSmelt();
      EnchantRegistry.register(autosmelt);
    }
    if (enableLaunch) {
      launch = new EnchantLaunch();
      EnchantRegistry.register(launch);
    }
    if (enableMagnet) {
      magnet = new EnchantMagnet();
      EnchantRegistry.register(magnet);
    }
    if (enableVenom) {
      venom = new EnchantVenom();
      EnchantRegistry.register(venom);
    }
    if (enableLifeleech) {
      lifeleech = new EnchantLifeLeech();
      EnchantRegistry.register(lifeleech);
  
    }
    if (enablebeheading) {
      beheading = new EnchantBeheading();
      EnchantRegistry.register(beheading);
 
    }
    if (enableQuickdraw) {
      quickdraw = new EnchantQuickdraw();
      EnchantRegistry.register(quickdraw);
 
    }
  }
  @Override
  public void syncConfig(Configuration c) {
    enablewaterwalk = c.getBoolean("EnchantWaterwalk", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enablereach = c.getBoolean("EnchantReach", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enablexpboost = c.getBoolean("EnchantExpBoost", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableautosmelt = c.getBoolean("EnchantAutoSmelt", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableLaunch = c.getBoolean("EnchantLaunch", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableMagnet = c.getBoolean("EnchantMagnet", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableVenom = c.getBoolean("EnchantVenom", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableLifeleech = c.getBoolean("EnchantLifeLeech", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enablebeheading = c.getBoolean("EnchantBeheading", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableQuickdraw = c.getBoolean("EnchantQuickdraw", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    for (EnchantBase b : EnchantRegistry.enchants) {
      if (b instanceof IHasConfig) {
        ((IHasConfig) b).syncConfig(c);
      }
    }
  }
}
