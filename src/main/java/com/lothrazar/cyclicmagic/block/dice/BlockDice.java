package com.lothrazar.cyclicmagic.block.dice;

import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.core.block.BlockBaseFacingOmni;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class BlockDice extends BlockBaseFacingOmni implements IHasRecipe {

  public BlockDice() {
    super(Material.ROCK);
    this.setTranslucent();
  }

  @Override
  public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing blockFaceClickedOn, float hitX, float hitY, float hitZ, int meta, @Nullable EntityLivingBase placer) {
    int index = MathHelper.getInt(world.rand, 0, EnumFacing.values().length - 1);
    //randomize  
    return this.getDefaultState().withProperty(PROPERTYFACING, EnumFacing.values()[index]);
  }

  @Override
  public IRecipe addRecipe() {
    return null;
  }
}
