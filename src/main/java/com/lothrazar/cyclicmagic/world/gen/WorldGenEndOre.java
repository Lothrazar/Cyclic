package com.lothrazar.cyclicmagic.world.gen;

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

public class WorldGenEndOre implements IWorldGenerator{

	private final int MIN_HEIGHT = 5;
	private final int MAX_HEIGHT = 128;
	
	private WorldGenerator genGold;
	private WorldGenerator genCoal;
	private WorldGenerator genEmerald;
	private WorldGenerator genLapis;

	public WorldGenEndOre(){
		int blockCount = 8;
		this.genGold = new WorldGenMinable(BlockRegistry.end_redstone_ore.getDefaultState(), blockCount, BlockMatcher.forBlock(Blocks.end_stone));

		blockCount = 8;
		this.genCoal = new WorldGenMinable(BlockRegistry.end_coal_ore.getDefaultState(), blockCount, BlockMatcher.forBlock(Blocks.end_stone));

		blockCount = 4;
		this.genEmerald = new WorldGenMinable(BlockRegistry.end_emerald_ore.getDefaultState(), blockCount, BlockMatcher.forBlock(Blocks.end_stone));

		blockCount = 8;
		this.genLapis = new WorldGenMinable(BlockRegistry.end_lapis_ore.getDefaultState(), blockCount, BlockMatcher.forBlock(Blocks.end_stone));
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider){

		int chance = 45;
		if(world.provider.getDimension() == Const.Dimension.end){
			
			this.run(this.genGold, world, random, chunkX * Const.CHUNK_SIZE, chunkZ * Const.CHUNK_SIZE, chance, MIN_HEIGHT, MAX_HEIGHT);

			chance = 25;
			this.run(this.genCoal, world, random, chunkX * Const.CHUNK_SIZE, chunkZ * Const.CHUNK_SIZE, chance, MIN_HEIGHT, MAX_HEIGHT);

			chance = 10;
			this.run(this.genEmerald, world, random, chunkX * Const.CHUNK_SIZE, chunkZ * Const.CHUNK_SIZE, chance, MIN_HEIGHT, MAX_HEIGHT);

			chance = 15;
			this.run(this.genLapis, world, random, chunkX * Const.CHUNK_SIZE, chunkZ * Const.CHUNK_SIZE, chance, MIN_HEIGHT, MAX_HEIGHT);
		}
	}

	private void run(WorldGenerator generator, World world, Random rand, int chunk_X, int chunk_Z, int chancesToSpawn, int minHeight, int maxHeight){

		if(minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
			throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");

		int heightDiff = maxHeight - minHeight;

		BlockPos pos;
		//BiomeGenBase biome;

		for(int i = 0; i < chancesToSpawn; i++){
			int x = chunk_X + rand.nextInt(Const.CHUNK_SIZE);
			int y = minHeight + rand.nextInt(heightDiff);
			int z = chunk_Z + rand.nextInt(Const.CHUNK_SIZE);

			pos = new BlockPos(x, y, z);
			//biome = world.getBiomeGenForCoords(pos);

			//if(biome == Biomes.sky){
				generator.generate(world, rand, pos);
			//}
		}
	}
}
