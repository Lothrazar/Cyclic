package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.entity.projectile.*;
import com.lothrazar.cyclicmagic.item.projectile.*;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ProjectileModule extends BaseModule {
  private boolean enableEnderBlaze;
  private boolean enableEnderDungeonFinder;
  private boolean enderFishing;
  private boolean enderSnow;
  private boolean enderWool;
  private boolean enderTorch;
  private boolean enderWater;
  private boolean enderLightning;
  int trackingRange = 64;
  int updateFrequency = 1;
  boolean sendsVelocityUpdates = true;
  @Override
  public void onInit() {
    if (enableEnderBlaze) {
      ItemProjectileBlaze item = new ItemProjectileBlaze();
      ItemRegistry.addItem(item, "ender_blaze");
      EntityRegistry.registerModEntity(EntityBlazeBolt.class, "blazebolt", 1008, ModMain.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
      EntityBlazeBolt.renderSnowball = item;
      ItemRegistry.ender_blaze = item;
    }
    if (enableEnderDungeonFinder) {
      ItemProjectileDungeon item = new ItemProjectileDungeon();
      ItemRegistry.addItem(item, "ender_dungeon");
      EntityRegistry.registerModEntity(EntityDungeonEye.class, "dungeonbolt", 1006, ModMain.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
      EntityDungeonEye.renderSnowball = item;
      ItemRegistry.ender_dungeon = item;
    }
    if (enderFishing) {
      ItemProjectileFishing item = new ItemProjectileFishing();
      ItemRegistry.addItem(item, "ender_fishing");
      EntityRegistry.registerModEntity(EntityFishingBolt.class, "fishingbolt", 1004, ModMain.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
      EntityFishingBolt.renderSnowball = item;
      ItemRegistry.ender_fishing = item;
    }
    if (enderWool) {
      ItemProjectileWool item = new ItemProjectileWool();
      ItemRegistry.addItem(item, "ender_wool");
      EntityRegistry.registerModEntity(EntityShearingBolt.class, "woolbolt", 1003, ModMain.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
      EntityShearingBolt.renderSnowball = item;
      ItemRegistry.ender_wool = item;
    }
    if (enderTorch) {
      ItemProjectileTorch item = new ItemProjectileTorch();
      ItemRegistry.addItem(item, "ender_torch");
      EntityRegistry.registerModEntity(EntityTorchBolt.class, "torchbolt", 1002, ModMain.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
      EntityTorchBolt.renderSnowball = item;
      ItemRegistry.ender_torch = item;
    }
    if (enderWater) {
      ItemProjectileWater item = new ItemProjectileWater();
      ItemRegistry.addItem(item, "ender_water");
      EntityRegistry.registerModEntity(EntityWaterBolt.class, "waterbolt", 1000, ModMain.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
      EntityWaterBolt.renderSnowball = item;
      ItemRegistry.ender_water = item;
    }
    if (enderSnow) {
      ItemProjectileSnow item = new ItemProjectileSnow();
      ItemRegistry.addItem(item, "ender_snow");
      EntityRegistry.registerModEntity(EntitySnowballBolt.class, "frostbolt", 1001, ModMain.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
      EntitySnowballBolt.renderSnowball = item;
      ItemRegistry.ender_snow = item;
    }
    if (enderLightning) {
      ItemProjectileLightning item = new ItemProjectileLightning();
      ItemRegistry.addItem(item, "ender_lightning");
      EntityRegistry.registerModEntity(EntityLightningballBolt.class, "lightningbolt", 999, ModMain.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
      EntityLightningballBolt.renderSnowball = item;
      ItemRegistry.ender_lightning = item;
    }
    //TODO: ender bomb module/ registry is sprate?
  }
  @Override
  public void syncConfig(Configuration config) {
    enableEnderBlaze = config.getBoolean("EnderBlaze", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableEnderDungeonFinder = config.getBoolean("EnderDungeonFinder", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enderFishing = config.getBoolean("EnderFishing", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enderSnow = config.getBoolean("EnderSnow", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enderWool = config.getBoolean("EnderWool", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enderTorch = config.getBoolean("EnderTorch", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enderLightning = config.getBoolean("EnderLightning", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enderWater = config.getBoolean("EnderWater", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
  }
}
