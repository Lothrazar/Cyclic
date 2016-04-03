package com.lothrazar.cyclicmagic.block;

import java.util.Random;
import com.lothrazar.cyclicmagic.item.IHasRecipe;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.BlockRenderLayer;//EnumWorldBlockLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * imported from https://github.com/PrinceOfAmber/SamsPowerups/blob/
 * 5083ec601e34bbe045d9a3d0ca091e3d44af562f/src/main/java/com/lothrazar/samscontent/BlockRegistry.j
 * a v a
 * 
 * @author Lothrazar
 *
 */
public class BlockFragile extends Block implements IHasRecipe{

	public static final String name = "block_fragile";
	
	public BlockFragile(){

		super(Material.wood);
		this.setTickRandomly(true);
		this.setHardness(0F);
		this.setResistance(0F);

		//float volumeIn, float pitchIn, SoundEvent breakSoundIn, SoundEvent stepSoundIn, SoundEvent placeSoundIn, SoundEvent hitSoundIn, SoundEvent fallSoundIn)
	    //TODO: SOUND REGISTRY
		ResourceLocation resSound = new ResourceLocation(Const.MODID,"sounds/"+UtilSound.Own.crackle);
		SoundEvent s = new SoundEvent(resSound);
		
		this.setStepSound(new SoundType( 1.0F, 1.0F, s,s,s,s,s));
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state){
		// http://greyminecraftcoder.blogspot.ca/2014/12/transparent-blocks-18.html
		return false;
	}

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer(){
		return BlockRenderLayer.TRANSLUCENT;//CUTOUT; 
	}

	@Override
	public void updateTick(World worldObj, BlockPos pos, IBlockState state, Random rand){

		worldObj.destroyBlock(pos, false);
	}

	@Override
	public int quantityDropped(Random random){

		return 0; // this makes it drop nothing at all
	}

	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player){

		return false;
	}

	@Override
	public void addRecipe(){

		GameRegistry.addRecipe(new ItemStack(this), 
				"s s", 
				" s ", 
				"s s", 
				's', new ItemStack(Items.stick));
		
	}
}