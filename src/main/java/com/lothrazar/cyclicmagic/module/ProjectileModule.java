package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.entity.projectile.EntityBlazeBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityDungeonEye;
import com.lothrazar.cyclicmagic.entity.projectile.EntityFishingBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityHomeBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityLightningballBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityShearingBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntitySnowballBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityTorchBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityWaterBolt;
import com.lothrazar.cyclicmagic.item.projectile.ItemProjectileBlaze;
import com.lothrazar.cyclicmagic.item.projectile.ItemProjectileDungeon;
import com.lothrazar.cyclicmagic.item.projectile.ItemProjectileFishing;
import com.lothrazar.cyclicmagic.item.projectile.ItemProjectileLightning;
import com.lothrazar.cyclicmagic.item.projectile.ItemProjectileSnow;
import com.lothrazar.cyclicmagic.item.projectile.ItemProjectileTorch;
import com.lothrazar.cyclicmagic.item.projectile.ItemProjectileWater;
import com.lothrazar.cyclicmagic.item.projectile.ItemProjectileWool;
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
      ItemRegistry.ender_blaze = new ItemProjectileBlaze();
      ItemRegistry.addItem(ItemRegistry.ender_blaze, "ender_blaze");
      EntityRegistry.registerModEntity(EntityBlazeBolt.class, "blazebolt", 1008, ModMain.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
    }
    if (enableEnderDungeonFinder) {
      ItemRegistry.ender_dungeon = new ItemProjectileDungeon();
      ItemRegistry.addItem(ItemRegistry.ender_dungeon, "ender_dungeon");
      EntityRegistry.registerModEntity(EntityDungeonEye.class, "dungeonbolt", 1006, ModMain.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
    }
    if (enderFishing) {
      ItemRegistry.ender_fishing = new ItemProjectileFishing();
      ItemRegistry.addItem(ItemRegistry.ender_fishing, "ender_fishing");
      EntityRegistry.registerModEntity(EntityFishingBolt.class, "fishingbolt", 1004, ModMain.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
    }
    if (enderWool) {
      ItemRegistry.ender_wool = new ItemProjectileWool();
      ItemRegistry.addItem(ItemRegistry.ender_wool, "ender_wool");
      EntityRegistry.registerModEntity(EntityShearingBolt.class, "woolbolt", 1003, ModMain.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
    }
    if (enderTorch) {
      ItemRegistry.ender_torch = new ItemProjectileTorch();
      ItemRegistry.addItem(ItemRegistry.ender_torch, "ender_torch");
      EntityRegistry.registerModEntity(EntityTorchBolt.class, "torchbolt", 1002, ModMain.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
    }
    if (enderWater) {
      ItemRegistry.ender_water = new ItemProjectileWater();
      ItemRegistry.addItem(ItemRegistry.ender_water, "ender_water");
      EntityRegistry.registerModEntity(EntityWaterBolt.class, "waterbolt", 1000, ModMain.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
      
    }
    if (enderSnow) {
      ItemRegistry.ender_snow = new ItemProjectileSnow();
      ItemRegistry.addItem(ItemRegistry.ender_snow, "ender_snow");
      EntityRegistry.registerModEntity(EntitySnowballBolt.class, "frostbolt", 1001, ModMain.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
    }
    if (enderLightning) {
      ItemRegistry.ender_lightning = new ItemProjectileLightning();
      ItemRegistry.addItem(ItemRegistry.ender_lightning, "ender_lightning");
      EntityRegistry.registerModEntity(EntityLightningballBolt.class, "lightningbolt", 999, ModMain.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
    }
    //from a long removed item. keep i guess, it never broke anything
    EntityRegistry.registerModEntity(EntityHomeBolt.class, "bedbolt", 1005, ModMain.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
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
