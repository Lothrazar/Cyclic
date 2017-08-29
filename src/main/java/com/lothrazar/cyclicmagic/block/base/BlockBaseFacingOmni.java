package com.lothrazar.cyclicmagic.block.base;
import com.lothrazar.cyclicmagic.ModCyclic;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockBaseFacingOmni extends BlockBaseHasTile {
  public static final PropertyDirection PROPERTYFACING = BlockDirectional.FACING;
  public BlockBaseFacingOmni(Material materialIn) {
    super(materialIn);
  }
  @Override
  public IBlockState getStateFromMeta(int meta) {
    return this.getDefaultState().withProperty(PROPERTYFACING, EnumFacing.getFront(meta & 7));
  }
  public EnumFacing getFacingFromState(IBlockState state) {
    EnumFacing facing = EnumFacing.NORTH;//dont want to be null to break stuff
    try { //this test should be enough, but add a trycatch for extra safety
      if (state != null && state.getBlock() instanceof BlockBaseFacing) {// i wish there was a state.hasValue()
        facing = state.getValue(PROPERTYFACING).getOpposite();
      }
    }
    catch (Exception e) {
      ModCyclic.logger.error("Could not get EnumFacing " + e.getLocalizedMessage());
    }
    return facing;
  }
  @Override
  public int getMetaFromState(IBlockState state) {
    return state.getValue(PROPERTYFACING).getIndex();
  }
  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { PROPERTYFACING });
  }
  @Override
  public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing blockFaceClickedOn, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    EnumFacing fac = EnumFacing.getDirectionFromEntityLiving(pos, placer);
    if (fac != EnumFacing.UP && fac != EnumFacing.DOWN) {
      fac = fac.getOpposite();//make consistent with non-omni blocks: faces user
    }
    return this.getDefaultState().withProperty(PROPERTYFACING,
        fac);
  }
  public static EnumFacing getCurrentFacing(World world, BlockPos pos) {
    try {
      EnumFacing f = world.getBlockState(pos).getValue(BlockBaseFacingOmni.PROPERTYFACING);
      if (f != EnumFacing.UP && f != EnumFacing.DOWN) {
        f = f.getOpposite();//backwards compatible hack
      }
      return f;
    }
    catch (Exception e) {//only BC legacy states will fail this
      return EnumFacing.NORTH;
    }
  }
}
