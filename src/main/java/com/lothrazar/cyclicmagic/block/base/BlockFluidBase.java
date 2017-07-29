package com.lothrazar.cyclicmagic.block.base;
import com.lothrazar.cyclicmagic.block.IBlockHasTESR;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockFluidBase extends BlockFluidClassic implements IBlockHasTESR {
  public BlockFluidBase(Fluid fluid, Material material) {
    super(fluid, material);
    this.setQuantaPerBlock(6);
  }
  @Override
  public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
    return false;
  }
  @Override
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }
  @Override
  public boolean isFullCube(IBlockState state) {
    return false;
  }
  @Override
  public EnumBlockRenderType getRenderType(IBlockState state) {
    return EnumBlockRenderType.MODEL;
  }
  @Override
  public IBlockState getStateFromMeta(int meta) {
    return getBlockState().getBaseState().withProperty(LEVEL, meta);
  }
  @SideOnly(Side.CLIENT)
  public abstract void initModel();
}
