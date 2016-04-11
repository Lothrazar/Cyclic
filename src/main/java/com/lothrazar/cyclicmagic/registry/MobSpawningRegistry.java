package com.lothrazar.cyclicmagic.registry;

import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class MobSpawningRegistry {

	private final static int	group		= 3;
	private final static int	min			= 1;
	private final static int	max			= 4;
	public static boolean			enabled	= true;

	public static void register() {

		if(!enabled){
			return;
		}
		EntityRegistry.addSpawn(EntityEnderman.class, group, min, max, EnumCreatureType.MONSTER, new BiomeGenBase[] { Biomes.hell });

		EntityRegistry.addSpawn(EntityBlaze.class, group, min, max, EnumCreatureType.MONSTER, new BiomeGenBase[] { Biomes.desertHills });

		EntityRegistry.addSpawn(EntityMagmaCube.class, group, min, max, EnumCreatureType.MONSTER, new BiomeGenBase[] { Biomes.desertHills, Biomes.desert });

		EntityRegistry.addSpawn(EntityCaveSpider.class, group, min, max, EnumCreatureType.MONSTER, new BiomeGenBase[] { Biomes.mesa, Biomes.roofedForest, Biomes.jungle });

		EntityRegistry.addSpawn(EntityGhast.class, group, min, max, EnumCreatureType.MONSTER, new BiomeGenBase[] { Biomes.deepOcean });

		// existing horses only spawn in plains and savanah
		// horses dont like trees, so biomes without them makes sense. ocean means
		// those little
		// islands

		EntityRegistry.addSpawn(EntityGuardian.class, group, min, max, EnumCreatureType.WATER_CREATURE, new BiomeGenBase[] { Biomes.river });

		EntityRegistry.addSpawn(EntitySnowman.class, group, min, max, EnumCreatureType.CREATURE, new BiomeGenBase[] { Biomes.iceMountains, Biomes.icePlains });

		EntityRegistry.addSpawn(EntityHorse.class, group, min, max, EnumCreatureType.CREATURE, new BiomeGenBase[] { Biomes.iceMountains, Biomes.extremeHills, Biomes.icePlains, Biomes.deepOcean });

//TODO: wolves and villagers?
		// WOLVES only spawn naturally in forest, taiga, mega taiga, cold taiga, and
		// cold taiga M

		EntityRegistry.addSpawn(EntityIronGolem.class, group, min, max, EnumCreatureType.CREATURE, new BiomeGenBase[] { Biomes.jungleHills, Biomes.jungle });

	}

	public static void syncConfig(Configuration config) {

		String category = Const.MODCONF + "Mobs";

		String msg = "Allow tons of mobs to spawn in more biomes.  Horses in more places; Cave spiders in mesa and roofed forests; some nether mobs in the desert; enderman in the nether; snowmen in winter biomes;  ghasts in deep ocean; Iron Golems in the jungle; Guardians in rivers.";

		config.setCategoryComment(category, msg);

		Property prop = config.get(category, "Extra Spawns Enabled", true, msg);
	
		enabled = prop.getBoolean();
	}
}
