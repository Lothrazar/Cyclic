package com.lothrazar.cyclicmagic.block;

import java.util.Random;
import net.minecraft.block.BlockOre;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class BlockNetherGold extends BlockOre {

	public static final String name = "nether_gold_ore";

	public BlockNetherGold() {

		super();
		this.setStepSound(SoundType.STONE);
		// copy what gold ore uses)
		this.setHardness(3.0F).setResistance(5.0F);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {

		return Items.gold_nugget;
	}

	public int quantityDropped(Random random) {

		// lapis uses 4 + random.nextInt(5);
		return 3 + random.nextInt(3);
	}

	@Override
	public int getExpDrop(IBlockState state, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune) {
		Random rand = world instanceof World ? ((World) world).rand : new Random();
		return MathHelper.getRandomIntegerInRange(rand, 2, 5);
	}
}
