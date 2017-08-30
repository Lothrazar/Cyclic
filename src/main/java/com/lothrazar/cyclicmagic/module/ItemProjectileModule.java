package com.lothrazar.cyclicmagic.module;
import java.util.ArrayList;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.dispenser.BehaviorProjectileThrowable;
import com.lothrazar.cyclicmagic.entity.projectile.EntityBlazeBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityDungeonEye;
import com.lothrazar.cyclicmagic.entity.projectile.EntityDynamite;
import com.lothrazar.cyclicmagic.entity.projectile.EntityDynamiteBlockSafe;
import com.lothrazar.cyclicmagic.entity.projectile.EntityDynamiteMining;
import com.lothrazar.cyclicmagic.entity.projectile.EntityFishingBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityLightningballBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityMagicNetEmpty;
import com.lothrazar.cyclicmagic.entity.projectile.EntityMagicNetFull;
import com.lothrazar.cyclicmagic.entity.projectile.EntityShearingBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntitySnowballBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityTorchBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityWaterBolt;
import com.lothrazar.cyclicmagic.item.projectile.BaseItemProjectile;
import com.lothrazar.cyclicmagic.item.projectile.ItemProjectileBlaze;
import com.lothrazar.cyclicmagic.item.projectile.ItemProjectileDungeon;
import com.lothrazar.cyclicmagic.item.projectile.ItemProjectileFishing;
import com.lothrazar.cyclicmagic.item.projectile.ItemProjectileLightning;
import com.lothrazar.cyclicmagic.item.projectile.ItemProjectileMagicNet;
import com.lothrazar.cyclicmagic.item.projectile.ItemProjectileSnow;
import com.lothrazar.cyclicmagic.item.projectile.ItemProjectileTNT;
import com.lothrazar.cyclicmagic.item.projectile.ItemProjectileTNT.ExplosionType;
import com.lothrazar.cyclicmagic.item.projectile.ItemProjectileTorch;
import com.lothrazar.cyclicmagic.item.projectile.ItemProjectileWater;
import com.lothrazar.cyclicmagic.item.projectile.ItemProjectileWool;
import com.lothrazar.cyclicmagic.registry.EntityProjectileRegistry;
import com.lothrazar.cyclicmagic.registry.GuideRegistry;
import com.lothrazar.cyclicmagic.registry.GuideRegistry.GuideCategory;
import com.lothrazar.cyclicmagic.registry.GuideRegistry.GuideItem;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.block.BlockDispenser;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

public class ItemProjectileModule extends BaseModule implements IHasConfig {
  private boolean enableEnderBlaze;
  private boolean enableEnderDungeonFinder;
  private boolean enderFishing;
  private boolean enderSnow;
  private boolean enderWool;
  private boolean enderTorch;
  private boolean enderWater;
  private boolean enderLightning;
  private boolean enderBombsEnabled;
  ArrayList<BaseItemProjectile> projectiles = new ArrayList<BaseItemProjectile>();
  private boolean dynamiteSafe;
  private boolean dynamiteMining;
  private boolean magicNet;
  @Override
  public void onPreInit() {
    if (enableEnderBlaze) {
      ItemProjectileBlaze ender_blaze = new ItemProjectileBlaze();
      ItemRegistry.register(ender_blaze, "ender_blaze", GuideCategory.ITEMTHROW);
      EntityProjectileRegistry.registerModEntity(EntityBlazeBolt.class, "blazebolt", 1008);
  
      projectiles.add(ender_blaze);
    }
    if (enableEnderDungeonFinder) {
      ItemProjectileDungeon ender_dungeon = new ItemProjectileDungeon();
      ItemRegistry.register(ender_dungeon, "ender_dungeon", GuideCategory.ITEMTHROW);
      EntityProjectileRegistry.registerModEntity(EntityDungeonEye.class, "dungeonbolt", 1006);
 
      LootTableRegistry.registerLoot(ender_dungeon);
     //ItemRegistry.registerWithJeiDescription(ender_dungeon);
      projectiles.add(ender_dungeon);
    }
    if (enderFishing) {
      ItemProjectileFishing ender_fishing = new ItemProjectileFishing();
      ItemRegistry.register(ender_fishing, "ender_fishing", GuideCategory.ITEMTHROW);
      EntityProjectileRegistry.registerModEntity(EntityFishingBolt.class, "fishingbolt", 1004);
 
     // ItemRegistry.registerWithJeiDescription(ender_fishing);
      projectiles.add(ender_fishing);
    }
    if (enderWool) {
      ItemProjectileWool ender_wool = new ItemProjectileWool();
      ItemRegistry.register(ender_wool, "ender_wool", GuideCategory.ITEMTHROW);
      EntityProjectileRegistry.registerModEntity(EntityShearingBolt.class, "woolbolt", 1003);
 
      projectiles.add(ender_wool);
    }
    if (enderTorch) {
      ItemProjectileTorch ender_torch = new ItemProjectileTorch();
      ItemRegistry.register(ender_torch, "ender_torch", GuideCategory.ITEMTHROW);
      EntityProjectileRegistry.registerModEntity(EntityTorchBolt.class, "torchbolt", 1002);
 
      projectiles.add(ender_torch);
    }
    if (enderWater) {
      ItemProjectileWater ender_water = new ItemProjectileWater();
      ItemRegistry.register(ender_water, "ender_water", GuideCategory.ITEMTHROW);
      EntityProjectileRegistry.registerModEntity(EntityWaterBolt.class, "waterbolt", 1000);
      EntityWaterBolt.renderSnowball = ender_water;
 
      projectiles.add(ender_water);
    }
    if (enderSnow) {
      ItemProjectileSnow ender_snow = new ItemProjectileSnow();
      ItemRegistry.register(ender_snow, "ender_snow", GuideCategory.ITEMTHROW);
      EntityProjectileRegistry.registerModEntity(EntitySnowballBolt.class, "frostbolt", 1001);
 
      projectiles.add(ender_snow);
    }
    if (enderLightning) {
      ItemProjectileLightning ender_lightning = new ItemProjectileLightning();
      ItemRegistry.register(ender_lightning, "ender_lightning", GuideCategory.ITEMTHROW);
      EntityProjectileRegistry.registerModEntity(EntityLightningballBolt.class, "lightningbolt", 999);
 
      LootTableRegistry.registerLoot(ender_lightning);
 
      projectiles.add(ender_lightning);
    }
    if (dynamiteSafe) {
      ItemProjectileTNT dynamite_safe = new ItemProjectileTNT(6, ExplosionType.BLOCKSAFE);
      ItemRegistry.register(dynamite_safe, "dynamite_safe", GuideCategory.ITEMTHROW);
      GuideItem page = GuideRegistry.register(GuideCategory.ITEMTHROW, dynamite_safe);
      EntityProjectileRegistry.registerModEntity(EntityDynamiteBlockSafe.class, "tntblocksafebolt", 1009);
      EntityDynamiteBlockSafe.renderSnowball = dynamite_safe;
      projectiles.add(dynamite_safe);
      page.addRecipePage(RecipeRegistry.addShapelessRecipe(new ItemStack(dynamite_safe, 6),
          "gunpowder",
          new ItemStack(Items.SUGAR),
          "gunpowder",
          "paper",
          new ItemStack(Items.CLAY_BALL),
          new ItemStack(Blocks.BROWN_MUSHROOM),
          "feather",
          new ItemStack(Items.WHEAT_SEEDS),
          "cobblestone"));
    }
    if (magicNet) {
      ItemProjectileMagicNet magic_net = new ItemProjectileMagicNet();
      ItemRegistry.register(magic_net, "magic_net", GuideCategory.ITEMTHROW);
      EntityMagicNetEmpty.renderSnowball = magic_net;
      
      EntityProjectileRegistry.registerModEntity(EntityMagicNetFull.class, "magicnetfull", 1011);
      EntityProjectileRegistry.registerModEntity(EntityMagicNetEmpty.class, "magicnetempty", 1012);
      projectiles.add(magic_net);
    }
    if (dynamiteMining) {
      ItemProjectileTNT dynamite_mining = new ItemProjectileTNT(6, ExplosionType.MINING);
      ItemRegistry.register(dynamite_mining, "dynamite_mining", GuideCategory.ITEMTHROW);
      GuideItem page = GuideRegistry.register(GuideCategory.ITEMTHROW, dynamite_mining);
      EntityProjectileRegistry.registerModEntity(EntityDynamiteMining.class, "tntminingbolt", 1010);
      EntityDynamiteMining.renderSnowball = dynamite_mining;
      projectiles.add(dynamite_mining);
      page.addRecipePage(RecipeRegistry.addShapelessRecipe(new ItemStack(dynamite_mining, 6),
          "gunpowder",
          "ingotIron",
          "gunpowder",
          "paper",
          new ItemStack(Items.CLAY_BALL),
          new ItemStack(Blocks.RED_MUSHROOM),
          "feather",
          new ItemStack(Items.WHEAT_SEEDS),
          "ingotBrickNether"));
    }
    if (enderBombsEnabled) {
      ItemProjectileTNT ender_tnt_1 = new ItemProjectileTNT(1, ExplosionType.NORMAL);
      ItemProjectileTNT ender_tnt_2 = new ItemProjectileTNT(2, ExplosionType.NORMAL);
      ItemProjectileTNT ender_tnt_3 = new ItemProjectileTNT(3, ExplosionType.NORMAL);
      ItemProjectileTNT ender_tnt_4 = new ItemProjectileTNT(4, ExplosionType.NORMAL);
      ItemProjectileTNT ender_tnt_5 = new ItemProjectileTNT(5, ExplosionType.NORMAL);
      ItemProjectileTNT ender_tnt_6 = new ItemProjectileTNT(6, ExplosionType.NORMAL);
      ItemRegistry.register(ender_tnt_1, "ender_tnt_1", null);
      ItemRegistry.register(ender_tnt_2, "ender_tnt_2", null);
      ItemRegistry.register(ender_tnt_3, "ender_tnt_3", null);
      ItemRegistry.register(ender_tnt_4, "ender_tnt_4", null);
      ItemRegistry.register(ender_tnt_5, "ender_tnt_5", null);
      ItemRegistry.register(ender_tnt_6, "ender_tnt_6", null);
      GuideItem page = GuideRegistry.register(GuideCategory.ITEMTHROW, ender_tnt_1);
      EntityProjectileRegistry.registerModEntity(EntityDynamite.class, "tntbolt", 1007);
 
      projectiles.add(ender_tnt_1);
      projectiles.add(ender_tnt_2);
      projectiles.add(ender_tnt_3);
      projectiles.add(ender_tnt_4);
      projectiles.add(ender_tnt_5);
      projectiles.add(ender_tnt_6);
      //first the basic recipes
      page.addRecipePage(RecipeRegistry.addShapelessRecipe(new ItemStack(ender_tnt_1, 12), new ItemStack(Blocks.TNT), "paper", new ItemStack(Items.CLAY_BALL), "enderpearl"));
      page.addRecipePage(RecipeRegistry.addShapelessRecipe(new ItemStack(ender_tnt_2), new ItemStack(ender_tnt_1), new ItemStack(ender_tnt_1), new ItemStack(Items.CLAY_BALL)));
      page.addRecipePage(RecipeRegistry.addShapelessRecipe(new ItemStack(ender_tnt_3), new ItemStack(ender_tnt_2), new ItemStack(ender_tnt_2), new ItemStack(Items.CLAY_BALL)));
      page.addRecipePage(RecipeRegistry.addShapelessRecipe(new ItemStack(ender_tnt_4), new ItemStack(ender_tnt_3), new ItemStack(ender_tnt_3), new ItemStack(Items.CLAY_BALL)));
      page.addRecipePage(RecipeRegistry.addShapelessRecipe(new ItemStack(ender_tnt_5), new ItemStack(ender_tnt_4), new ItemStack(ender_tnt_4), new ItemStack(Items.CLAY_BALL)));
      page.addRecipePage(RecipeRegistry.addShapelessRecipe(new ItemStack(ender_tnt_6), new ItemStack(ender_tnt_5), new ItemStack(ender_tnt_5), new ItemStack(Items.CLAY_BALL)));
      //default recipes are added already insice the IRecipe
      page.addRecipePage(RecipeRegistry.addShapelessRecipe(new ItemStack(ender_tnt_3), new ItemStack(ender_tnt_1), new ItemStack(ender_tnt_1), new ItemStack(ender_tnt_1), new ItemStack(ender_tnt_1), new ItemStack(Items.CLAY_BALL)));
      //two 3s is four 2s
      page.addRecipePage(RecipeRegistry.addShapelessRecipe(new ItemStack(ender_tnt_4), new ItemStack(ender_tnt_2), new ItemStack(ender_tnt_2), new ItemStack(ender_tnt_2), new ItemStack(ender_tnt_2), new ItemStack(Items.CLAY_BALL)));
      //four 3s is two 4s is one 5
      page.addRecipePage(RecipeRegistry.addShapelessRecipe(new ItemStack(ender_tnt_5), new ItemStack(ender_tnt_3), new ItemStack(ender_tnt_3), new ItemStack(ender_tnt_3), new ItemStack(ender_tnt_3), new ItemStack(Items.CLAY_BALL)));
      page.addRecipePage(RecipeRegistry.addShapelessRecipe(new ItemStack(ender_tnt_6), new ItemStack(ender_tnt_4), new ItemStack(ender_tnt_4), new ItemStack(ender_tnt_4), new ItemStack(ender_tnt_4), new ItemStack(Items.CLAY_BALL)));
      LootTableRegistry.registerLoot(ender_tnt_6);
    }
  }
  @Override
  public void onPostInit() {
    for (BaseItemProjectile item : projectiles) {
      BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(item, new BehaviorProjectileThrowable(item));
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    magicNet = config.getBoolean("MonsterBall", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    dynamiteSafe = config.getBoolean("DynamiteSafe", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    dynamiteMining = config.getBoolean("DynamiteMining", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
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
