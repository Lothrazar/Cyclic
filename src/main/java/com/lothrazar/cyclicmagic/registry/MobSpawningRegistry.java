package com.lothrazar.cyclicmagic.registry;
 
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class MobSpawningRegistry 
{
	private final static int group = 3;
	private final static int min = 1;
	private final static int max = 4; 	
	
	public static void register()
	{  
		
		EntityRegistry.addSpawn(EntityBlaze.class, group, min, max, EnumCreatureType.MONSTER, new BiomeGenBase[]{ Biomes.desertHills} );
 
		EntityRegistry.addSpawn(EntityMagmaCube.class, group, min, max, EnumCreatureType.MONSTER, new BiomeGenBase[]{ Biomes.desert} );

		EntityRegistry.addSpawn(EntityCaveSpider.class, group, min, max, EnumCreatureType.MONSTER, new BiomeGenBase[]{ Biomes.mesa} );

		EntityRegistry.addSpawn(EntityCaveSpider.class, group, min, max, EnumCreatureType.MONSTER, new BiomeGenBase[]{ Biomes.roofedForest} );
		
		EntityRegistry.addSpawn(EntityCaveSpider.class, group, min, max, EnumCreatureType.MONSTER, new BiomeGenBase[]{ Biomes.jungle} );
 
		EntityRegistry.addSpawn(EntitySnowman.class, group, min, max, EnumCreatureType.MONSTER, new BiomeGenBase[]{ Biomes.iceMountains} );
		
		EntityRegistry.addSpawn(EntityGhast.class, group, min, max, EnumCreatureType.MONSTER, new BiomeGenBase[]{ Biomes.deepOcean} );

		//existing horses only spawn in plains and savanah
		//horses dont like trees, so biomes without them makes sense. ocean means those little islands

		EntityRegistry.addSpawn(EntityHorse.class, group, min, max, EnumCreatureType.CREATURE, new BiomeGenBase[]{ Biomes.icePlains} );

		EntityRegistry.addSpawn(EntityHorse.class, group, min, max, EnumCreatureType.CREATURE, new BiomeGenBase[]{ Biomes.deepOcean} );
	
		EntityRegistry.addSpawn(EntityHorse.class, group, min, max, EnumCreatureType.CREATURE, new BiomeGenBase[]{ Biomes.extremeHills} );
	
		EntityRegistry.addSpawn(EntityVillager.class, group, min, max, EnumCreatureType.CREATURE, new BiomeGenBase[]{ Biomes.extremeHills} );
	
		//WOLVES only spawn naturally in forest, taiga, mega taiga, cold taiga, and cold taiga M

		//irongolem - rare in jungle/?? 
	}
}
