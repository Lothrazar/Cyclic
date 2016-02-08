package com.lothrazar.cyclicmagic.block;

import java.util.Random;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * imported from https://github.com/PrinceOfAmber/SamsPowerups/blob/
 * 5083ec601e34bbe045d9a3d0ca091e3d44af562f/src/main/java/com/lothrazar/samscontent/BlockRegistry.ja
 * v a
 * 
 * @author Lothrazar
 *
 */
public class BlockFragile extends Block {
	public BlockFragile() {
		super(Material.wood);
		this.setTickRandomly(true);
		this.setHardness(0F);
		this.setResistance(0F);

		this.setStepSound(new Block.SoundType(UtilSound.Own.crackle, 1.0F, 1.0F) {
			//override so default does not prefix with "dig." and break it
			public String getBreakSound() {
				return this.soundName;
			}
			public String getStepSound() {
				return this.soundName;
			}
			public String getPlaceSound() {
				return this.soundName;
			}
		});
	}

	@Override
	public boolean isOpaqueCube() {
		return false;// transparency
	}

	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.CUTOUT;// transparency
	}

	@Override
	public void updateTick(World worldObj, BlockPos pos, IBlockState state, Random rand) {
		// TODO: maybe an extra random counter here
		// true would make it drop the 'block items' - but no recpie for them
		worldObj.destroyBlock(pos, false);
	}

	@Override
	public int quantityDropped(Random random) {
		return 0; // this makes it drop nothing at all
	}

	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		return false;
	}
}