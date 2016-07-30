package com.lothrazar.cyclicmagic.module;
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

public class ProjectileModule extends BaseModule {
  private boolean enableEnderBlaze;
  private boolean enableEnderDungeonFinder;
  private boolean enderFishing;
  private boolean enderSnow;
  private boolean enderWool;
  private boolean enderTorch;
  private boolean enderWater;
  private boolean enderLightning;
  @Override
  public void register() {
    if (enableEnderBlaze) {
      ItemRegistry.ender_blaze = new ItemProjectileBlaze();
      ItemRegistry.addItem(ItemRegistry.ender_blaze, "ender_blaze");
    }
    if (enableEnderDungeonFinder) {
      ItemRegistry.ender_dungeon = new ItemProjectileDungeon();
      ItemRegistry.addItem(ItemRegistry.ender_dungeon, "ender_dungeon");
    }
    if (enderFishing) {
      ItemRegistry.ender_fishing = new ItemProjectileFishing();
      ItemRegistry.addItem(ItemRegistry.ender_fishing, "ender_fishing");
    }
    if (enderWool) {
      ItemRegistry.ender_wool = new ItemProjectileWool();
      ItemRegistry.addItem(ItemRegistry.ender_wool, "ender_wool");
    }
    if (enderTorch) {
      ItemRegistry.ender_torch = new ItemProjectileTorch();
      ItemRegistry.addItem(ItemRegistry.ender_torch, "ender_torch");
    }
    if (enderWater) {
      ItemRegistry.ender_water = new ItemProjectileWater();
      ItemRegistry.addItem(ItemRegistry.ender_water, "ender_water");
    }
    if (enderSnow) {
      ItemRegistry.ender_snow = new ItemProjectileSnow();
      ItemRegistry.addItem(ItemRegistry.ender_snow, "ender_snow");
    }
    if (enderLightning) {
      ItemRegistry.ender_lightning = new ItemProjectileLightning();
      ItemRegistry.addItem(ItemRegistry.ender_lightning, "ender_lightning");
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
  }
}
