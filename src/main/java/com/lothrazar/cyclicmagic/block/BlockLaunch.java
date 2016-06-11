package com.lothrazar.cyclicmagic.block;

import com.lothrazar.cyclicmagic.util.UtilEntity;

import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockLaunch extends BlockBasePressurePlate {

	private float angle;
	private float power;
	public BlockLaunch(float a, float p) {
		super(Material.WOOD);
		angle = a;
		power = p;
	}

	protected void playClickOnSound(World worldIn, BlockPos color) {

		worldIn.playSound((EntityPlayer) null, color, SoundEvents.BLOCK_WOOD_PRESSPLATE_CLICK_ON, SoundCategory.BLOCKS,
				0.3F, 0.8F);

	}

	protected void playClickOffSound(World worldIn, BlockPos pos) {

		worldIn.playSound((EntityPlayer) null, pos, SoundEvents.BLOCK_STONE_PRESSPLATE_CLICK_OFF, SoundCategory.BLOCKS,
				0.3F, 0.5F);

	}

	@Override
	protected int computeRedstoneStrength(World worldIn, BlockPos pos) {
		return 0;
	}

	@Override
	protected int getRedstoneStrength(IBlockState state) {
		return 0;
	}

	@Override
	protected IBlockState setRedstoneStrength(IBlockState state, int strength) {
		return null;
	}


	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entity) {

		if (entity instanceof EntityLivingBase)
			UtilEntity.launch((EntityLivingBase) entity, angle, power);
	}

}
