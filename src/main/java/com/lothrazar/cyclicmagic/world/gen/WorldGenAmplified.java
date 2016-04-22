package com.lothrazar.cyclicmagic.world.gen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenAmplified implements IWorldGenerator {

	public class OreProperties{
		public OreProperties(int m, int x,int ch,int size){
			min = m;
			max = x;
			chance = ch;
			blockCount = size;
			dimension = Const.Dimension.overworld;
		}
		public int max;
		public int min;
		public int chance;
		public int blockCount;
		public int dimension;
		public WorldGenerator gen;
		//TODO: could be some fancy biome list, but this is enough
		public boolean extremeHillsOnly = false;
	}
	
	public List<OreProperties> properties = new ArrayList<OreProperties>();
	 
	
	public WorldGenAmplified(){
		//http://minecraft.gamepedia.com/Ore#Availability
		//use the vanilla max height as our generators MIN height
		int chancesPerRun = 55;
		
		OreProperties coal = new OreProperties(132,Const.WORLDHEIGHT,chancesPerRun,10);
		coal.gen = new WorldGenMinable(Blocks.coal_ore.getDefaultState(), coal.blockCount, BlockMatcher.forBlock(Blocks.stone));
		properties.add(coal);
		
		OreProperties iron = new OreProperties(68,Const.WORLDHEIGHT,chancesPerRun,8);
		iron.gen = new WorldGenMinable(Blocks.iron_ore.getDefaultState(), iron.blockCount, BlockMatcher.forBlock(Blocks.stone));
		properties.add(iron);

		OreProperties lapis = new OreProperties(34,Const.WORLDHEIGHT,chancesPerRun,3);
		lapis.gen = new WorldGenMinable(Blocks.lapis_ore.getDefaultState(), lapis.blockCount, BlockMatcher.forBlock(Blocks.stone));
		properties.add(lapis);
		
		OreProperties gold = new OreProperties(34,Const.WORLDHEIGHT,chancesPerRun,4);
		gold.gen = new WorldGenMinable(Blocks.gold_ore.getDefaultState(), gold.blockCount, BlockMatcher.forBlock(Blocks.stone));
		properties.add(gold);
		
		OreProperties diamond = new OreProperties(16,Const.WORLDHEIGHT,chancesPerRun,1);
		diamond.gen = new WorldGenMinable(Blocks.diamond_ore.getDefaultState(), diamond.blockCount, BlockMatcher.forBlock(Blocks.stone));
		properties.add(diamond);

		OreProperties redstone = new OreProperties(16,Const.WORLDHEIGHT,chancesPerRun,3);
		redstone.gen = new WorldGenMinable(Blocks.redstone_ore.getDefaultState(), redstone.blockCount, BlockMatcher.forBlock(Blocks.stone));
		properties.add(redstone);
		
		OreProperties emerald = new OreProperties(33,Const.WORLDHEIGHT,chancesPerRun,2);
		emerald.gen = new WorldGenMinable(Blocks.emerald_ore.getDefaultState(), emerald.blockCount, BlockMatcher.forBlock(Blocks.stone));
		emerald.extremeHillsOnly = true;
		properties.add(emerald);
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {

		for(OreProperties prop : properties){
			if(prop.dimension == world.provider.getDimension()){
				this.run(prop.gen, world, random, chunkX * Const.CHUNK_SIZE, chunkZ * Const.CHUNK_SIZE, prop.chance, prop.min, prop.max
						,prop.extremeHillsOnly);
			}
		}
	}

	private void run(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight
			,boolean extremeHillsOnly) {

		if (minHeight < 0 || maxHeight > Const.WORLDHEIGHT || minHeight > maxHeight)
			throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");

		int heightDiff = maxHeight - minHeight;

		BlockPos pos;
		BiomeGenBase biome;

		for (int i = 0; i < chancesToSpawn; i++) {
			int x = chunk_X + rand.nextInt(Const.CHUNK_SIZE);
			int y = minHeight + rand.nextInt(heightDiff);
			int z = chunk_Z + rand.nextInt(Const.CHUNK_SIZE);

			pos = new BlockPos(x, y, z);
			
			if(extremeHillsOnly){
				biome = world.getBiomeGenForCoords(pos);
				
				if(biome == Biomes.extremeHills || biome == Biomes.extremeHillsEdge || biome == Biomes.extremeHillsPlus){
					generator.generate(world, rand, pos);
				}
			}
			else{
				//just always do it
				generator.generate(world, rand, pos);
			}
		}
	}
}
