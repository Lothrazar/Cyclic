package com.lothrazar.cyclicmagic.block;
import com.lothrazar.cyclicmagic.block.base.BlockBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSpikesRetractable extends BlockBase {
  private static final DamageSource SOURCE = DamageSource.GENERIC;
  private static final int DAMAGE = 1;
  private static final PropertyBool ACTIVATED = PropertyBool.create("activated");
  private static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class);
  private static final float LARGE = 0.9375F;
  private static final float SMALL = 0.0625F;
  private static final AxisAlignedBB NORTH_BOX = new AxisAlignedBB(0.0F, 0.0F, LARGE, 1.0F, 1.0F, 1.0F);
  private static final AxisAlignedBB EAST_BOX = new AxisAlignedBB(0.0F, 0.0F, 0.0F, SMALL, 1.0F, 1.0F);
  private static final AxisAlignedBB SOUTH_BOX = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, SMALL);
  private static final AxisAlignedBB WEST_BOX = new AxisAlignedBB(LARGE, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
  private static final AxisAlignedBB UP_BOX = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, SMALL, 1.0F);
  private static final AxisAlignedBB DOWN_BOX = new AxisAlignedBB(0.0F, LARGE, 0.0F, 1.0F, 1.0F, 1.0F);
  private boolean redstoneControlled;
  public BlockSpikesRetractable(boolean redContr) {
    super(Material.IRON);
    setHardness(1.5F);
    setResistance(10F);
    this.setTranslucent();
    this.redstoneControlled = redContr;
  }
  //copy vanilla methods: 8 facing directions bitwise-combined with enabled or not
  public static EnumFacing getFacing(int meta) {
    return EnumFacing.getFront(meta & 7);
  }
  @Override
  public IBlockState getStateFromMeta(int meta) {
    if(this.redstoneControlled)
      return this.getDefaultState().withProperty(FACING, getFacing(meta)).withProperty(ACTIVATED, (meta & 8) > 0);
      
    else
    return this.getDefaultState().withProperty(FACING, getFacing(meta)).withProperty(ACTIVATED, true);
  }
  @Override
  public int getMetaFromState(IBlockState state) {
    byte b0 = 0;
    int i = b0 | state.getValue(FACING).getIndex();
    if (state.getValue(ACTIVATED)) {
      i |= 8;
    }
    return i;
  }
  @Override
  public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entity) {
    if (entity instanceof EntityLivingBase && worldIn.getBlockState(pos).getValue(ACTIVATED)) {
      entity.attackEntityFrom(SOURCE, DAMAGE);
    }
  }
  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { FACING, ACTIVATED });
  }
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    if (state.getValue(ACTIVATED)) { return FULL_BLOCK_AABB; }
    EnumFacing enumfacing = state.getValue(FACING);
    switch (enumfacing) {
      case NORTH:
        return NORTH_BOX;
      case EAST:
        return EAST_BOX;
      case SOUTH:
        return SOUTH_BOX;
      case WEST:
        return WEST_BOX;
      case UP:
        return UP_BOX;
      case DOWN:
        return DOWN_BOX;
    }
    return FULL_BLOCK_AABB;//CANT BE NULL, causes crashes. TODO make a small one 
  }
  @Override
  public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos) {
    return NULL_AABB;
  }
  @Override
  public boolean isFullCube(IBlockState state) {
    return false;
  }
  @Override
  public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
    if (canPlaceBlockAt(worldIn, pos) == false) { // if we are attached to somethihg dissapearedy
      dropBlockAsItem(worldIn, pos, getDefaultState(), 0);
      worldIn.setBlockToAir(pos);
    }
    if(redstoneControlled == false){
      worldIn.setBlockState(pos, state.withProperty(ACTIVATED, true));
      return;
    }// else redstone can toggle me on and off
    if (!state.getValue(ACTIVATED) && worldIn.isBlockPowered(pos)) {
      //sound
      worldIn.setBlockState(pos, state.withProperty(ACTIVATED, true));
    }
    else if (state.getValue(ACTIVATED) && !worldIn.isBlockPowered(pos)) {
      //sound
      worldIn.setBlockState(pos, state.withProperty(ACTIVATED, false));
    }
  }
  @Override
  public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase player, ItemStack stack) {
    this.neighborChanged(state, worldIn, pos, this, pos);
  }
  @Override
  public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase player, EnumHand hand) {
    return worldIn.isSideSolid(pos.offset(facing.getOpposite()), facing, true) ? this.getDefaultState().withProperty(FACING, facing).withProperty(ACTIVATED, !redstoneControlled) : this.getDefaultState().withProperty(FACING, EnumFacing.DOWN).withProperty(ACTIVATED, !redstoneControlled);
  }
  @Override
  public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
    return worldIn.isSideSolid(pos.offset(side.getOpposite()), side, true);//only place on solid placements
  }
  @Override
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
    for (EnumFacing fac : EnumFacing.values()) {
      if (worldIn.isSideSolid(pos.offset(fac), fac.getOpposite(), true)) { return true; }
    }
    return false;
  }
}
