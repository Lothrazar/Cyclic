package com.lothrazar.cyclicmagic.block;

import java.util.Random;
import net.minecraft.block.BlockOre;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class BlockNetherOre extends BlockOre {

	private Item dropped;
	private int droppedMeta;
	private int randomMax;
	public BlockNetherOre(Item drop) {
		this(drop,0);
	}

	public BlockNetherOre(Item drop, int dmg) {
		this(drop,0,1);
	}

	public BlockNetherOre(Item drop, int dmg, int max) {

		super();
		dropped = drop;
		droppedMeta = dmg;
		randomMax = max;
		this.setStepSound(SoundType.STONE);
		this.setHardness(3.0F).setResistance(5.0F);
	}
    public int damageDropped(IBlockState state)
    {
        return droppedMeta;
    }

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return dropped;
	}

	public int quantityDropped(Random random) {
		if(randomMax == 1){
			return 1;
		}
		return 1 + random.nextInt(randomMax);
	}

	@Override
	public int getExpDrop(IBlockState state, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune) {
		Random rand = world instanceof World ? ((World) world).rand : new Random();
		return MathHelper.getRandomIntegerInRange(rand, 2, 5);
	}
}
