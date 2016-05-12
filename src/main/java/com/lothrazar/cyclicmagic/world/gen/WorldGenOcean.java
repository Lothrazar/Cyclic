package com.lothrazar.cyclicmagic.world.gen;

import java.util.Random;

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

public class WorldGenOcean implements IWorldGenerator {

	public int							clayChance		= 30;
	public int							clayNumBlocks	= 50;
	public int							dirtChance		= 20;
	public int							dirtNumBlocks	= 20;
	public int							sandChance		= 45;
	public int							sandNumBlocks	= 25;
	// Thanks to ref :
	// http://bedrockminer.jimdo.com/modding-tutorials/basic-modding/world-generation/

	private WorldGenerator	genClay;
	private WorldGenerator	genSand;
	private WorldGenerator	genDirt;

	private final int				MIN_HEIGHT		= 20;
	private final int				MAX_HEIGHT		= 128;

	public WorldGenOcean() {

		this.genClay = new WorldGenMinable(Blocks.clay.getDefaultState(), clayNumBlocks, BlockMatcher.forBlock(Blocks.gravel));
		this.genSand = new WorldGenMinable(Blocks.dirt.getDefaultState(), dirtNumBlocks, BlockMatcher.forBlock(Blocks.gravel));
		this.genDirt = new WorldGenMinable(Blocks.sand.getDefaultState(), sandNumBlocks, BlockMatcher.forBlock(Blocks.gravel));
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {

		if (world.provider.getDimension() == Const.Dimension.overworld) {
			this.run(this.genClay, world, random, chunkX * Const.CHUNK_SIZE, chunkZ * Const.CHUNK_SIZE, clayChance, MIN_HEIGHT, MAX_HEIGHT);
			this.run(this.genSand, world, random, chunkX * Const.CHUNK_SIZE, chunkZ * Const.CHUNK_SIZE, sandChance, MIN_HEIGHT, MAX_HEIGHT);
			this.run(this.genDirt, world, random, chunkX * Const.CHUNK_SIZE, chunkZ * Const.CHUNK_SIZE, dirtChance, MIN_HEIGHT, MAX_HEIGHT);
		}
	}

	private void run(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight) {

		if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
			throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");

		int heightDiff = maxHeight - minHeight;

		BlockPos pos;
		BiomeGenBase biome;

		for (int i = 0; i < chancesToSpawn; i++) {
			int x = chunk_X + rand.nextInt(Const.CHUNK_SIZE);
			int y = minHeight + rand.nextInt(heightDiff);
			int z = chunk_Z + rand.nextInt(Const.CHUNK_SIZE);

			pos = new BlockPos(x, y, z);
			biome = world.getBiomeGenForCoords(pos);

			if (biome == Biomes.ocean || biome == Biomes.deepOcean || biome == Biomes.frozenOcean) {
				generator.generate(world, rand, pos);
			}
		}
	}
}
