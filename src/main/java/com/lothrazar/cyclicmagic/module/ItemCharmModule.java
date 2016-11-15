package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.item.charm.*;
import com.lothrazar.cyclicmagic.item.tool.ItemAutoTorch;
import com.lothrazar.cyclicmagic.registry.AchievementRegistry;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry;
import com.lothrazar.cyclicmagic.util.Const;
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
  @Override
  public void onInit() {
    if (enableFire) {
      ItemCharmFire charm_fire = new ItemCharmFire();
      ItemRegistry.addItem(charm_fire, "charm_fire");
      LootTableRegistry.registerLoot(charm_fire);
    }
    if (enableSea) {
      ItemCharmBoat charm_boat = new ItemCharmBoat();
      ItemRegistry.addItem(charm_boat, "charm_boat");
      LootTableRegistry.registerLoot(charm_boat);
    }
    if (enableVoid) {
      ItemCharmVoid charm_void = new ItemCharmVoid();
      ItemRegistry.addItem(charm_void, "charm_void");
      LootTableRegistry.registerLoot(charm_void);
    }
    if (enableWater) {
      ItemCharmWater charm_water = new ItemCharmWater();
      ItemRegistry.addItem(charm_water, "charm_water");
      LootTableRegistry.registerLoot(charm_water);
    }
    if (antidoteCharm) {
      ItemCharmAntidote charm_antidote = new ItemCharmAntidote();
      ItemRegistry.addItem(charm_antidote, "charm_antidote");
      LootTableRegistry.registerLoot(charm_antidote);
    }
    if (slowfallCharm) {
      ItemCharmSlowfall charm_wing = new ItemCharmSlowfall();
      ItemRegistry.addItem(charm_wing, "charm_wing");
      AchievementRegistry.registerItemAchievement(charm_wing);
      LootTableRegistry.registerLoot(charm_wing);
    }
    if (autoTorch) {
      ItemAutoTorch tool_auto_torch = new ItemAutoTorch();
      ItemRegistry.addItem(tool_auto_torch, "tool_auto_torch");
      ModCyclic.instance.events.register(tool_auto_torch);
      LootTableRegistry.registerLoot(tool_auto_torch);
    }
    if (enableSpeed) {
      ItemCharmSpeed charm_speed = new ItemCharmSpeed();
      ItemRegistry.addItem(charm_speed, "charm_speed");
      LootTableRegistry.registerLoot(charm_speed);
    }
  }
  @Override
  public void syncConfig(Configuration config) {
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
