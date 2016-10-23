package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.entity.projectile.*;
import com.lothrazar.cyclicmagic.item.projectile.*;
import com.lothrazar.cyclicmagic.registry.EntityProjectileRegistry;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

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
      ItemProjectileBlaze ender_blaze = new ItemProjectileBlaze();
      ItemRegistry.addItem(ender_blaze, "ender_blaze");
      EntityProjectileRegistry.registerModEntity(EntityBlazeBolt.class, "blazebolt", 1008);
      EntityBlazeBolt.renderSnowball = ender_blaze;
    }
    if (enableEnderDungeonFinder) {
      ItemProjectileDungeon ender_dungeon = new ItemProjectileDungeon();
      ItemRegistry.addItem(ender_dungeon, "ender_dungeon");
      EntityProjectileRegistry.registerModEntity(EntityDungeonEye.class, "dungeonbolt", 1006);
      EntityDungeonEye.renderSnowball = ender_dungeon;
      LootTableRegistry.registerLoot(ender_dungeon);
    }
    if (enderFishing) {
      ItemProjectileFishing ender_fishing = new ItemProjectileFishing();
      ItemRegistry.addItem(ender_fishing, "ender_fishing");
      EntityProjectileRegistry.registerModEntity(EntityFishingBolt.class, "fishingbolt", 1004);
      EntityFishingBolt.renderSnowball = ender_fishing;
    }
    if (enderWool) {
      ItemProjectileWool ender_wool = new ItemProjectileWool();
      ItemRegistry.addItem(ender_wool, "ender_wool");
      EntityProjectileRegistry.registerModEntity(EntityShearingBolt.class, "woolbolt", 1003);
      EntityShearingBolt.renderSnowball = ender_wool;
    }
    if (enderTorch) {
      ItemProjectileTorch ender_torch = new ItemProjectileTorch();
      ItemRegistry.addItem(ender_torch, "ender_torch");
      EntityProjectileRegistry.registerModEntity(EntityTorchBolt.class, "torchbolt", 1002);
      EntityTorchBolt.renderSnowball = ender_torch;
    }
    if (enderWater) {
      ItemProjectileWater ender_water = new ItemProjectileWater();
      ItemRegistry.addItem(ender_water, "ender_water");
      EntityProjectileRegistry.registerModEntity(EntityWaterBolt.class, "waterbolt", 1000);
      EntityWaterBolt.renderSnowball = ender_water;
    }
    if (enderSnow) {
      ItemProjectileSnow ender_snow = new ItemProjectileSnow();
      ItemRegistry.addItem(ender_snow, "ender_snow");
      EntityProjectileRegistry.registerModEntity(EntitySnowballBolt.class, "frostbolt", 1001);
      EntitySnowballBolt.renderSnowball = ender_snow;
    }
    if (enderLightning) {
      ItemProjectileLightning ender_lightning = new ItemProjectileLightning();
      ItemRegistry.addItem(ender_lightning, "ender_lightning");
      EntityProjectileRegistry.registerModEntity(EntityLightningballBolt.class, "lightningbolt", 999);
      EntityLightningballBolt.renderSnowball = ender_lightning;
      LootTableRegistry.registerLoot(ender_lightning);
    }
    if (enderBombsEnabled) {
      ItemProjectileTNT ender_tnt_1 = new ItemProjectileTNT(1);
      ItemProjectileTNT ender_tnt_2 = new ItemProjectileTNT(2);
      ItemProjectileTNT ender_tnt_3 = new ItemProjectileTNT(3);
      ItemProjectileTNT ender_tnt_4 = new ItemProjectileTNT(4);
      ItemProjectileTNT ender_tnt_5 = new ItemProjectileTNT(5);
      ItemProjectileTNT ender_tnt_6 = new ItemProjectileTNT(6);
      ItemRegistry.addItem(ender_tnt_1, "ender_tnt_1");
      ItemRegistry.addItem(ender_tnt_2, "ender_tnt_2");
      ItemRegistry.addItem(ender_tnt_3, "ender_tnt_3");
      ItemRegistry.addItem(ender_tnt_4, "ender_tnt_4");
      ItemRegistry.addItem(ender_tnt_5, "ender_tnt_5");
      ItemRegistry.addItem(ender_tnt_6, "ender_tnt_6");
      EntityProjectileRegistry.registerModEntity(EntityDynamite.class, "tntbolt", 1007);
      EntityDynamite.renderSnowball = ender_tnt_1;
      //first the basic recipes
      GameRegistry.addShapelessRecipe(new ItemStack(ender_tnt_1, 12), new ItemStack(Blocks.TNT), new ItemStack(Items.PAPER), new ItemStack(Items.CLAY_BALL), new ItemStack(Items.ENDER_PEARL));
      GameRegistry.addShapelessRecipe(new ItemStack(ender_tnt_2), new ItemStack(ender_tnt_1), new ItemStack(ender_tnt_1), new ItemStack(Items.CLAY_BALL));
      GameRegistry.addShapelessRecipe(new ItemStack(ender_tnt_3), new ItemStack(ender_tnt_2), new ItemStack(ender_tnt_2), new ItemStack(Items.CLAY_BALL));
      GameRegistry.addShapelessRecipe(new ItemStack(ender_tnt_4), new ItemStack(ender_tnt_3), new ItemStack(ender_tnt_3), new ItemStack(Items.CLAY_BALL));
      GameRegistry.addShapelessRecipe(new ItemStack(ender_tnt_5), new ItemStack(ender_tnt_4), new ItemStack(ender_tnt_4), new ItemStack(Items.CLAY_BALL));
      GameRegistry.addShapelessRecipe(new ItemStack(ender_tnt_6), new ItemStack(ender_tnt_5), new ItemStack(ender_tnt_5), new ItemStack(Items.CLAY_BALL));
      //default recipes are added already insice the IRecipe
      GameRegistry.addShapelessRecipe(new ItemStack(ender_tnt_3), new ItemStack(ender_tnt_1), new ItemStack(ender_tnt_1), new ItemStack(ender_tnt_1), new ItemStack(ender_tnt_1), new ItemStack(Items.CLAY_BALL));
      //two 3s is four 2s
      GameRegistry.addShapelessRecipe(new ItemStack(ender_tnt_4), new ItemStack(ender_tnt_2), new ItemStack(ender_tnt_2), new ItemStack(ender_tnt_2), new ItemStack(ender_tnt_2), new ItemStack(Items.CLAY_BALL));
      //four 3s is two 4s is one 5
      GameRegistry.addShapelessRecipe(new ItemStack(ender_tnt_5), new ItemStack(ender_tnt_3), new ItemStack(ender_tnt_3), new ItemStack(ender_tnt_3), new ItemStack(ender_tnt_3), new ItemStack(Items.CLAY_BALL));
      GameRegistry.addShapelessRecipe(new ItemStack(ender_tnt_6), new ItemStack(ender_tnt_4), new ItemStack(ender_tnt_4), new ItemStack(ender_tnt_4), new ItemStack(ender_tnt_4), new ItemStack(Items.CLAY_BALL));
      LootTableRegistry.registerLoot(ender_tnt_6);
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
