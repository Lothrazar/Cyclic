package com.lothrazar.cyclicmagic.world.gen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenAmplified implements IWorldGenerator {

	//http://minecraft.gamepedia.com/Ore#Availability
	public class OreProperties{
		public OreProperties(int m, int x,int ch){
			min = m;
			max = x;
			chance = ch;
			dimension = Const.Dimension.overworld;
		}
		public int max;
		public int min;
		public int chance;
		public int dimension;
		public WorldGenerator gen;		
	}
	
	public List<OreProperties> properties = new ArrayList<OreProperties>();
	 
	
	public WorldGenAmplified(){
		//numbers are all test /subject to change. TODO: BALANCE
		OreProperties gold = new OreProperties(5,256,8);
		gold.gen = new WorldGenMinable(Blocks.gold_ore.getDefaultState(), 4, BlockMatcher.forBlock(Blocks.stone));
		properties.add(gold);
		
		OreProperties coal = new OreProperties(5,256,8);
		coal.gen = new WorldGenMinable(Blocks.coal_ore.getDefaultState(), 8, BlockMatcher.forBlock(Blocks.stone));
		properties.add(coal);
		
		OreProperties diamond = new OreProperties(5,256,8);
		diamond.gen = new WorldGenMinable(Blocks.diamond_ore.getDefaultState(), 1, BlockMatcher.forBlock(Blocks.stone));
		properties.add(diamond);

		OreProperties lapis = new OreProperties(5,256,8);
		lapis.gen = new WorldGenMinable(Blocks.lapis_ore.getDefaultState(), 4, BlockMatcher.forBlock(Blocks.stone));
		properties.add(lapis);

		OreProperties redstone = new OreProperties(5,256,8);
		redstone.gen = new WorldGenMinable(Blocks.redstone_ore.getDefaultState(), 4, BlockMatcher.forBlock(Blocks.stone));
		properties.add(redstone);
		
		OreProperties iron = new OreProperties(5,256,8);
		iron.gen = new WorldGenMinable(Blocks.iron_ore.getDefaultState(), 6, BlockMatcher.forBlock(Blocks.stone));
		properties.add(iron);
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {

		for(OreProperties prop : properties){
			if(prop.dimension == world.provider.getDimension()){
				this.run(prop.gen, world, random, chunkX * Const.CHUNK_SIZE, chunkZ * Const.CHUNK_SIZE, prop.chance, prop.min, prop.max);

			}
		}
		/*
		int chance = 45;
		if (world.provider.getDimension() == Const.Dimension.overworld) {

			this.run(this.genGold, world, random, chunkX * Const.CHUNK_SIZE, chunkZ * Const.CHUNK_SIZE, chance, MIN_HEIGHT, MAX_HEIGHT);

			chance = 25;
			this.run(this.genCoal, world, random, chunkX * Const.CHUNK_SIZE, chunkZ * Const.CHUNK_SIZE, chance, MIN_HEIGHT, MAX_HEIGHT);

			chance = 10;
			this.run(this.genDiamond, world, random, chunkX * Const.CHUNK_SIZE, chunkZ * Const.CHUNK_SIZE, chance, MIN_HEIGHT, MAX_HEIGHT);

			chance = 15;
			this.run(this.genLapis, world, random, chunkX * Const.CHUNK_SIZE, chunkZ * Const.CHUNK_SIZE, chance, MIN_HEIGHT, MAX_HEIGHT);

			chance = 15;
			this.run(this.genIron, world, random, chunkX * Const.CHUNK_SIZE, chunkZ * Const.CHUNK_SIZE, chance, MIN_HEIGHT, MAX_HEIGHT);

			chance = 15;
			this.run(this.genRedstone, world, random, chunkX * Const.CHUNK_SIZE, chunkZ * Const.CHUNK_SIZE, chance, MIN_HEIGHT, MAX_HEIGHT);
		
		}*/
	}

	private void run(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight) {

		if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
			throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");

		int heightDiff = maxHeight - minHeight;

		BlockPos pos;
		// BiomeGenBase biome;

		for (int i = 0; i < chancesToSpawn; i++) {
			int x = chunk_X + rand.nextInt(Const.CHUNK_SIZE);
			int y = minHeight + rand.nextInt(heightDiff);
			int z = chunk_Z + rand.nextInt(Const.CHUNK_SIZE);

			pos = new BlockPos(x, y, z);
			// biome = world.getBiomeGenForCoords(pos);

			// if(biome == Biomes.sky){
			generator.generate(world, rand, pos);
			// }
		}
	}
}
