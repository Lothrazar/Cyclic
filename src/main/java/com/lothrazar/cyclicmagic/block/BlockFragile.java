package com.lothrazar.cyclicmagic.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * imported from 
 * https://github.com/PrinceOfAmber/SamsPowerups/blob/5083ec601e34bbe045d9a3d0ca091e3d44af562f/src/main/java/com/lothrazar/samscontent/BlockRegistry.java
 * @author Lothrazar
 *
 */
public class BlockFragile extends Block
{
	public BlockFragile() 
	{
		super(Material.wood);
		//this.setCreativeTab(ModScepterPowers.tabSamsContent);
		this.setTickRandomly(true);
		this.setHardness(0F);
		this.setResistance(0F); 
		this.setStepSound(soundTypeWood);
	} 
	@Override
	public boolean isOpaqueCube() 
	{
		return false;//transparency 
	}
	@SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.CUTOUT;//transparency 
    } 
	@Override
	public void updateTick(World worldObj,  BlockPos pos, IBlockState state,  Random rand)
    {   
		//TODO: maybe an extra random counter here
		//true would make it drop the 'block items' - but no recpie for them
		worldObj.destroyBlock(pos, false);  
    }
}