package com.lothrazar.cyclicmagic.itemblock;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.block.BlockScaffolding;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemBlockScaffolding extends ItemBlock {

	public ItemBlockScaffolding(Block block) {
		super(block);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World worldIn, EntityPlayer player, EnumHand hand) {
		BlockPos pos = player.getPosition().up();// at eye level

		int direction = MathHelper.floor_double((double) ((player.rotationYaw * 4F) / 360F) + 0.5D) & 3;

		//imported from my scaffolding spell https://github.com/PrinceOfAmber/CyclicMagic/blob/37ebb722378cbf940aa9cfb4fa99ce0e80127533/src/main/java/com/lothrazar/cyclicmagic/spell/SpellScaffolding.java
		
		// -45 is up
		// +45 is pitch down
		// first; is it up or down?

		boolean doHoriz = true;
		EnumFacing facing = EnumFacing.UP;

		if (player.rotationPitch < -82) {
			// really really up
			doHoriz = false;
			pos = pos.up().up();
			facing = EnumFacing.UP;
		}
		else if (player.rotationPitch > 82) {
			// really really down
			doHoriz = false;
			pos = pos.down();
			facing = EnumFacing.DOWN;
		}
		else if (player.rotationPitch < -45) {
			// angle is pretty high up. so offset up again
			pos = pos.up();
			facing = EnumFacing.UP;
			doHoriz = true;
		}
		else if (player.rotationPitch > 45) {
			// you are angled down, so bring down from eye level
			pos = pos.down();
			facing = EnumFacing.DOWN;
			doHoriz = true;
		}
		// else doHoriz = true; stays

		// if not, go by dir
		if (doHoriz) {
			switch (direction) {
			case Const.DIR_EAST:
				pos = pos.east();
				facing = EnumFacing.EAST;
			break;
			case Const.DIR_WEST:
				pos = pos.west();// .offset(EnumFacing.WEST);
				facing = EnumFacing.WEST;
			break;
			case Const.DIR_SOUTH:
				pos = pos.south();
				facing = EnumFacing.SOUTH;
			break;
			case Const.DIR_NORTH:
				pos = pos.north();
				facing = EnumFacing.NORTH;
			break;
			}
		}

		this.onItemUse(stack, player, worldIn, pos, hand, facing, 0, 0, 0);

		return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		//UtilSound.playSound(playerIn, ModMain.crackle,SoundCategory.PLAYERS);
		return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
		/*
		 * IBlockState iblockstate = worldIn.getBlockState(pos);
		 * Block block = iblockstate.getBlock();
		 * 
		 * if (!block.isReplaceable(worldIn, pos))
		 * {
		 * pos = pos.offset(facing);
		 * }
		 * 
		 * if (stack.stackSize != 0 && playerIn.canPlayerEdit(pos, facing, stack) &&
		 * worldIn.canBlockBePlaced(this.block, pos, false, facing, (Entity)null,
		 * stack))
		 * {
		 * int i = this.getMetadata(stack.getMetadata());
		 * IBlockState iblockstate1 = this.block.onBlockPlaced(worldIn, pos, facing,
		 * hitX, hitY, hitZ, i, playerIn);
		 * 
		 * if (placeBlockAt(stack, playerIn, worldIn, pos, facing, hitX, hitY, hitZ,
		 * iblockstate1))
		 * {
		 * SoundType soundtype = this.block.getStepSound();
		 * worldIn.playSound(playerIn, pos, soundtype.getPlaceSound(),
		 * SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F,
		 * soundtype.getPitch() * 0.8F);
		 * --stack.stackSize;
		 * }
		 * 
		 * return EnumActionResult.SUCCESS;
		 * }
		 * else
		 * {
		 * return EnumActionResult.FAIL;
		 * }
		 */
	}
}
