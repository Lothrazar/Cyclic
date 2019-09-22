package com.lothrazar.cyclic.block;

import javax.annotation.Nullable;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.util.UtilStuff;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockSpikes extends BlockBase {

  public static final BooleanProperty ACTIVATED = BooleanProperty.create("lit");
  private static final float LARGE = 0.9375F;
  private static final float SMALL = 0.0625F;
  private static final VoxelShape NORTH_BOX = Block.makeCuboidShape(0.0F, 0.0F, LARGE, 1.0F, 1.0F, 1.0F);
  private static final VoxelShape EAST_BOX = Block.makeCuboidShape(0.0F, 0.0F, 0.0F, SMALL, 1.0F, 1.0F);
  private static final VoxelShape SOUTH_BOX = Block.makeCuboidShape(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, SMALL);
  private static final VoxelShape WEST_BOX = Block.makeCuboidShape(LARGE, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
  private static final VoxelShape UP_BOX = Block.makeCuboidShape(0.0F, 0.0F, 0.0F, 1.0F, SMALL, 1.0F);
  private static final VoxelShape DOWN_BOX = Block.makeCuboidShape(0.0F, LARGE, 0.0F, 1.0F, 1.0F, 1.0F);

  public BlockSpikes(Properties properties) {
    super(properties); 
    //    Blocks.LADDER?
  }

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    switch ((Direction) state.get(BlockStateProperties.FACING)) {
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
    return VoxelShapes.fullCube();//CANT BE NULL, causes crashes.   
  }

  @Override
  public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entity) {
    if (entity instanceof LivingEntity && state.get(ACTIVATED)) {
      entity.attackEntityFrom(DamageSource.CACTUS, getDamage());
    }
  }

  public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
    if (state.get(ACTIVATED).booleanValue() == false
        && world.isBlockPowered(pos)) {
      //  UtilSound.playSoundFromServer(SoundRegistry.spikes_on, SoundCategory.BLOCKS, pos, world.provider.getDimension(), 16);
      world.setBlockState(pos, state.with(ACTIVATED, true));
    }
    else if (state.get(ACTIVATED).booleanValue()
        && world.isBlockPowered(pos) == false) {
      //  UtilSound.playSoundFromServer(SoundRegistry.spikes_off, SoundCategory.BLOCKS, pos, world.provider.getDimension(), 16);
      world.setBlockState(pos, state.with(ACTIVATED, false));
    }
    super.neighborChanged(state, world, pos, blockIn, fromPos, isMoving);
  }

  private float getDamage() {
    return 1;
  }

  @Override
  public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
    return true;
  }

  @Override
  public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.CUTOUT;
  }

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
    if (entity != null) {
      Direction dir=UtilStuff.getFacingFromEntity(pos, entity);
      ModCyclic.LOGGER.info("so ",dir);// message, p0, p1, p2, p3, p4, p5, p6, p7);
      world.setBlockState(pos, state.with(BlockStateProperties.FACING, dir), 2);
    }
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(BlockStateProperties.FACING).add(ACTIVATED);
  }
}
