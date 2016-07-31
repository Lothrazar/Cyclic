package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class MobSpawnModule extends BaseModule {
  private final static int group = 3;
  private final static int min = 1;
  private final static int max = 4;
  private boolean blazeDesertHills;
  private boolean magmaDesert;
  private boolean caveSpiderMesaRoofed;
  private boolean ghastDeepOcean;
  private boolean guardianRiver;
  private boolean snowmanIcePlainsMount;
  private boolean horseIceExtrhillsOcean;
  private boolean ironGolemJungle;
  public void onInit() {
    if (blazeDesertHills) {
      EntityRegistry.addSpawn(EntityBlaze.class, group, min, max, EnumCreatureType.MONSTER, new Biome[] { Biomes.DESERT_HILLS });
    }
    if (magmaDesert) {
      EntityRegistry.addSpawn(EntityMagmaCube.class, group, min, max, EnumCreatureType.MONSTER, new Biome[] { Biomes.DESERT_HILLS, Biomes.DESERT });
    }
    if (caveSpiderMesaRoofed) {
      EntityRegistry.addSpawn(EntityCaveSpider.class, group, min, max, EnumCreatureType.MONSTER, new Biome[] { Biomes.MESA, Biomes.ROOFED_FOREST });
    }
    if (ghastDeepOcean) {
      EntityRegistry.addSpawn(EntityGhast.class, group, min, max, EnumCreatureType.MONSTER, new Biome[] { Biomes.DEEP_OCEAN });
    }
    if (guardianRiver) {
      EntityRegistry.addSpawn(EntityGuardian.class, group, min, max, EnumCreatureType.WATER_CREATURE, new Biome[] { Biomes.RIVER });
    }
    if (snowmanIcePlainsMount) {
      EntityRegistry.addSpawn(EntitySnowman.class, group, min, max, EnumCreatureType.CREATURE, new Biome[] { Biomes.ICE_MOUNTAINS, Biomes.ICE_PLAINS });
    }
    if (horseIceExtrhillsOcean) {
      // existing horses only spawn in plains and savanah
      EntityRegistry.addSpawn(EntityHorse.class, group, min, max, EnumCreatureType.CREATURE, new Biome[] { Biomes.EXTREME_HILLS, Biomes.ICE_PLAINS });
    }
    if (ironGolemJungle) {
      EntityRegistry.addSpawn(EntityIronGolem.class, group, min, max, EnumCreatureType.CREATURE, new Biome[] { Biomes.JUNGLE_HILLS, Biomes.JUNGLE });
    }
  }
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.mobspawns;
    config.addCustomCategoryComment(category, "Add mob spawns in more biomes");
    blazeDesertHills = config.getBoolean("Blaze DesertHills", category, true, "Adds random Blaze spawns into Desert Hills");
    magmaDesert = config.getBoolean("Magmacube Desert", category, true, "Adds random Magma Cube spawns into Desert");
    caveSpiderMesaRoofed = config.getBoolean("CaveSpider Mesa&Roofed", category, true, "Adds random Cave Spider spawns into Mesa and Roofed Forests");
    ghastDeepOcean = config.getBoolean("Ghast DeepOcean", category, true, "Adds random Ghast spawns into Deep Oceans");
    guardianRiver = config.getBoolean("Guardian River", category, true, "Adds random Guardian spawns into Rivers");
    snowmanIcePlainsMount = config.getBoolean("Snowman Ice", category, true, "Adds random Snowman spawns into Ice Plains and Ice Mountains");
    horseIceExtrhillsOcean = config.getBoolean("Horse IceHills", category, true, "Adds random Horse spawns into Extreme Hills and Ice Plains");
    ironGolemJungle = config.getBoolean("IronGolem Jungle", category, true, "Adds random IronGolem spawns into the Jungle");
  }
}
