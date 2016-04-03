package com.lothrazar.cyclicmagic.world.gen;

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

public class WorldGenNetherGold implements IWorldGenerator{


	public int chance = 45;
	public int size = 8;
	private WorldGenerator genGold;

	private final int MIN_HEIGHT = 5;
	private final int MAX_HEIGHT = 128;

	public WorldGenNetherGold(){
		//Blocks.gold_ore
		this.genGold = new WorldGenMinable(BlockRegistry.nether_gold_ore.getDefaultState(), size, BlockMatcher.forBlock(Blocks.netherrack));
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider){
 
		if(world.provider.getDimension() == Const.Dimension.nether){
			
			this.run(this.genGold, world, random, chunkX * Const.CHUNK_SIZE, chunkZ * Const.CHUNK_SIZE, chance, MIN_HEIGHT, MAX_HEIGHT);
		}
	}

	private void run(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight){

		if(minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
			throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");

		int heightDiff = maxHeight - minHeight;

		BlockPos pos;
		BiomeGenBase biome;

		for(int i = 0; i < chancesToSpawn; i++){
			int x = chunk_X + rand.nextInt(Const.CHUNK_SIZE);
			int y = minHeight + rand.nextInt(heightDiff);
			int z = chunk_Z + rand.nextInt(Const.CHUNK_SIZE);

			pos = new BlockPos(x, y, z);
			biome = world.getBiomeGenForCoords(pos);

			if(biome == Biomes.hell){
				generator.generate(world, rand, pos);
			}
		}
	}
}
