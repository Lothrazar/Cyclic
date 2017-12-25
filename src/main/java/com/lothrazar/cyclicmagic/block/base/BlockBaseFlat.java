package com.lothrazar.cyclicmagic.block.base;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockBaseFlat extends BlockBase { 
  private static final PropertyDirection PROPERTYFACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

  protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1D, 0.03125D, 1D);
  public BlockBaseFlat(Material materialIn) {
    super(materialIn);
    this.setHardness(2.0F).setResistance(2.0F);//of course can/will be overwritten in most cases, but at least have a nonzero default
  }
  /**
   * Used to determine ambient occlusion and culling when rebuilding chunks for render
   */
  @Override
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }
  @Override
  public boolean isFullCube(IBlockState state) {
    return false;
  }
  /**
   * Determines if an entity can path through this block
   */
  @Override
  public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
    return true;
  }
  @Override
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
    return this.canBePlacedOn(worldIn, pos.down());
  }
  @Override
  public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
    if (!this.canBePlacedOn(worldIn, pos.down())) {
      this.dropBlockAsItem(worldIn, pos, state, 0);
      worldIn.setBlockToAir(pos);
    }
  }
  private boolean canBePlacedOn(World worldIn, BlockPos pos) {
    return worldIn.getBlockState(pos).isTopSolid() || worldIn.getBlockState(pos).getBlock() instanceof BlockFence;
  }
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return AABB;
  }
  @SideOnly(Side.CLIENT)
  @Override
  public BlockRenderLayer getBlockLayer() {
    return BlockRenderLayer.TRANSLUCENT;
  }
  @Override
  public BlockFaceShape getBlockFaceShape(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
    return BlockFaceShape.UNDEFINED;
  }
  //below is all for facing
  @Override
  public IBlockState getStateFromMeta(int meta) {
    EnumFacing facing = EnumFacing.getHorizontal(meta);
    return this.getDefaultState().withProperty(PROPERTYFACING, facing);
  }
  public EnumFacing getFacingFromState(IBlockState state) {
    return (EnumFacing) state.getValue(PROPERTYFACING);
  }
  @Override
  public int getMetaFromState(IBlockState state) {
    EnumFacing facing = (EnumFacing) state.getValue(PROPERTYFACING);
    int facingbits = facing.getHorizontalIndex();
    return facingbits;
  }
  @Override
  public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
    return state;
  }
  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { PROPERTYFACING });
  }
  @Override
  public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing blockFaceClickedOn, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    // find the quadrant the player is facing
    EnumFacing enumfacing = (placer == null) ? EnumFacing.NORTH : EnumFacing.fromAngle(placer.rotationYaw);
    return this.getDefaultState().withProperty(PROPERTYFACING, enumfacing);
  }
  public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing blockFaceClickedOn, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    return this.getStateForPlacement(worldIn, pos, blockFaceClickedOn, hitX, hitY, hitZ, meta, placer);//110 support
  }
}
