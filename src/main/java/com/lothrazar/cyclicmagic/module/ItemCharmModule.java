package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.item.bauble.ItemAutoTorch;
import com.lothrazar.cyclicmagic.item.bauble.ItemCharmAir;
import com.lothrazar.cyclicmagic.item.bauble.ItemCharmAntidote;
import com.lothrazar.cyclicmagic.item.bauble.ItemCharmBoat;
import com.lothrazar.cyclicmagic.item.bauble.ItemCharmFire;
import com.lothrazar.cyclicmagic.item.bauble.ItemCharmSlowfall;
import com.lothrazar.cyclicmagic.item.bauble.ItemCharmSpeed;
import com.lothrazar.cyclicmagic.item.bauble.ItemCharmVoid;
import com.lothrazar.cyclicmagic.item.bauble.ItemCharmWater;
import com.lothrazar.cyclicmagic.registry.GuideRegistry.GuideCategory;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry;
import net.minecraftforge.common.config.Configuration;

public class ItemCharmModule extends BaseModule implements IHasConfig {
  private boolean enableFire;
  private boolean enableSea;
  private boolean enableVoid;
  private boolean enableWater;
  private boolean antidoteCharm;
  private boolean slowfallCharm;
  private boolean autoTorch;
  private boolean enableSpeed;
  private boolean enableAir;
  @Override
  public void onPreInit() {
    if (enableAir) {
      ItemCharmAir charm_air = new ItemCharmAir();
      ItemRegistry.register(charm_air, "charm_air", GuideCategory.ITEMBAUBLES);
      ModCyclic.instance.events.register(charm_air);
      LootTableRegistry.registerLoot(charm_air);
    }
    if (enableFire) {
      ItemCharmFire charm_fire = new ItemCharmFire();
      ItemRegistry.register(charm_fire, "charm_fire", GuideCategory.ITEMBAUBLES);
      LootTableRegistry.registerLoot(charm_fire); 
    }
    if (enableSea) {
      ItemCharmBoat charm_boat = new ItemCharmBoat();
      ItemRegistry.register(charm_boat, "charm_boat", GuideCategory.ITEMBAUBLES);
 
    }
    if (enableVoid) {
      ItemCharmVoid charm_void = new ItemCharmVoid();
      ItemRegistry.register(charm_void, "charm_void", GuideCategory.ITEMBAUBLES);
      LootTableRegistry.registerLoot(charm_void);
 
    }
    if (enableWater) {
      ItemCharmWater charm_water = new ItemCharmWater();
      ItemRegistry.register(charm_water, "charm_water", GuideCategory.ITEMBAUBLES);
 
    }
    if (antidoteCharm) {
      ItemCharmAntidote charm_antidote = new ItemCharmAntidote();
      ItemRegistry.register(charm_antidote, "charm_antidote", GuideCategory.ITEMBAUBLES);
      LootTableRegistry.registerLoot(charm_antidote); 
    }
    if (slowfallCharm) {
      ItemCharmSlowfall charm_wing = new ItemCharmSlowfall();
      ItemRegistry.register(charm_wing, "charm_wing", GuideCategory.ITEMBAUBLES);
      LootTableRegistry.registerLoot(charm_wing); 
    }
    if (autoTorch) {
      ItemAutoTorch tool_auto_torch = new ItemAutoTorch();
      ItemRegistry.register(tool_auto_torch, "tool_auto_torch", GuideCategory.ITEMBAUBLES);
      ModCyclic.instance.events.register(tool_auto_torch);
      LootTableRegistry.registerLoot(tool_auto_torch); 
    }
    if (enableSpeed) {
      ItemCharmSpeed charm_speed = new ItemCharmSpeed();
      ItemRegistry.register(charm_speed, "charm_speed", GuideCategory.ITEMBAUBLES);
    
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    enableAir = config.getBoolean("AirCharm", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableSpeed = config.getBoolean("SpeedCharm", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    slowfallCharm = config.getBoolean("WingCharm", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableFire = config.getBoolean("FireCharm", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableSea = config.getBoolean("SailorCharm", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableVoid = config.getBoolean("VoidCharm", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableWater = config.getBoolean("WaterCharm", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    antidoteCharm = config.getBoolean("AntidoteCharm", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    autoTorch = config.getBoolean("AutomaticTorch", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
  }
}
