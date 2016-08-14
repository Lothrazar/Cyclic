package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.entity.projectile.*;
import com.lothrazar.cyclicmagic.item.projectile.*;
import com.lothrazar.cyclicmagic.registry.EntityProjectileRegistry;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraftforge.common.config.Configuration;

public class ProjectileModule extends BaseModule {
  private boolean enableEnderBlaze;
  private boolean enableEnderDungeonFinder;
  private boolean enderFishing;
  private boolean enderSnow;
  private boolean enderWool;
  private boolean enderTorch;
  private boolean enderWater;
  private boolean enderLightning;
  private boolean enderBombsEnabled;
  @Override
  public void onInit() {
    if (enableEnderBlaze) {
      ItemProjectileBlaze item = new ItemProjectileBlaze();
      ItemRegistry.addItem(item, "ender_blaze");
      EntityProjectileRegistry.registerModEntity(EntityBlazeBolt.class, "blazebolt", 1008);
      EntityBlazeBolt.renderSnowball = item;
      ItemRegistry.ender_blaze = item;
    }
    if (enableEnderDungeonFinder) {
      ItemProjectileDungeon item = new ItemProjectileDungeon();
      ItemRegistry.addItem(item, "ender_dungeon");
      EntityProjectileRegistry.registerModEntity(EntityDungeonEye.class, "dungeonbolt", 1006);
      EntityDungeonEye.renderSnowball = item;
      ItemRegistry.ender_dungeon = item;
    }
    if (enderFishing) {
      ItemProjectileFishing item = new ItemProjectileFishing();
      ItemRegistry.addItem(item, "ender_fishing");
      EntityProjectileRegistry.registerModEntity(EntityFishingBolt.class, "fishingbolt", 1004);
      EntityFishingBolt.renderSnowball = item;
      ItemRegistry.ender_fishing = item;
    }
    if (enderWool) {
      ItemProjectileWool item = new ItemProjectileWool();
      ItemRegistry.addItem(item, "ender_wool");
      EntityProjectileRegistry.registerModEntity(EntityShearingBolt.class, "woolbolt", 1003);
      EntityShearingBolt.renderSnowball = item;
      ItemRegistry.ender_wool = item;
    }
    if (enderTorch) {
      ItemProjectileTorch item = new ItemProjectileTorch();
      ItemRegistry.addItem(item, "ender_torch");
      EntityProjectileRegistry.registerModEntity(EntityTorchBolt.class, "torchbolt", 1002);
      EntityTorchBolt.renderSnowball = item;
      ItemRegistry.ender_torch = item;
    }
    if (enderWater) {
      ItemProjectileWater item = new ItemProjectileWater();
      ItemRegistry.addItem(item, "ender_water");
      EntityProjectileRegistry.registerModEntity(EntityWaterBolt.class, "waterbolt", 1000);
      EntityWaterBolt.renderSnowball = item;
      ItemRegistry.ender_water = item;
    }
    if (enderSnow) {
      ItemProjectileSnow item = new ItemProjectileSnow();
      ItemRegistry.addItem(item, "ender_snow");
      EntityProjectileRegistry.registerModEntity(EntitySnowballBolt.class, "frostbolt", 1001);
      EntitySnowballBolt.renderSnowball = item;
      ItemRegistry.ender_snow = item;
    }
    if (enderLightning) {
      ItemProjectileLightning item = new ItemProjectileLightning();
      ItemRegistry.addItem(item, "ender_lightning");
      EntityProjectileRegistry.registerModEntity(EntityLightningballBolt.class, "lightningbolt", 999);
      EntityLightningballBolt.renderSnowball = item;
      ItemRegistry.ender_lightning = item;
    }
    if (enderBombsEnabled) {
      ItemRegistry.ender_tnt_1 = new ItemProjectileTNT(1);
      ItemRegistry.ender_tnt_2 = new ItemProjectileTNT(2);
      ItemRegistry.ender_tnt_3 = new ItemProjectileTNT(3);
      ItemRegistry.ender_tnt_4 = new ItemProjectileTNT(4);
      ItemRegistry.ender_tnt_5 = new ItemProjectileTNT(5);
      ItemRegistry.ender_tnt_6 = new ItemProjectileTNT(6);
      ItemRegistry.addItem(ItemRegistry.ender_tnt_1, "ender_tnt_1");
      ItemRegistry.addItem(ItemRegistry.ender_tnt_2, "ender_tnt_2");
      ItemRegistry.addItem(ItemRegistry.ender_tnt_3, "ender_tnt_3");
      ItemRegistry.addItem(ItemRegistry.ender_tnt_4, "ender_tnt_4");
      ItemRegistry.addItem(ItemRegistry.ender_tnt_5, "ender_tnt_5");
      ItemRegistry.addItem(ItemRegistry.ender_tnt_6, "ender_tnt_6");
      EntityProjectileRegistry.registerModEntity(EntityDynamite.class, "tntbolt", 1007);
      EntityDynamite.renderSnowball = ItemRegistry.ender_tnt_1;
    }
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
    enderBombsEnabled = config.getBoolean("EnderBombs", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
  }
}
