package com.lothrazar.cyclic.block.dice;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.UtilBlockstates;
import com.lothrazar.cyclic.util.UtilSound;
import java.util.Random;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockDice extends BlockBase {

  private static final double BOUNDS = 1;
  private static final VoxelShape AABB = Block.box(BOUNDS, BOUNDS, BOUNDS,
      16 - BOUNDS, 16 - BOUNDS, 16 - BOUNDS);

  public BlockDice(Properties properties) {
    super(properties.strength(1.8F).noOcclusion());
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
    return AABB;
  }

  @Override
  public boolean shouldDisplayFluidOverlay(BlockState state, BlockAndTintGetter world, BlockPos pos, FluidState fluidState) {
    return true;
  }

  @Override
  public void registerClient() {
    ItemBlockRenderTypes.setRenderLayer(this, RenderType.cutoutMipped());
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos,BlockState state) {
    return new TileDice(pos,state);
  }

  @Override
  public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
    if (entity != null) {
      world.setBlock(pos, state.setValue(BlockStateProperties.FACING, UtilBlockstates.getFacingFromEntityHorizontal(pos, entity)), 2);
    }
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(BlockStateProperties.FACING);
  }

  @Override
  public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
    //
    BlockEntity tile = world.getBlockEntity(pos);
    if (hand == InteractionHand.MAIN_HAND && tile instanceof TileDice) {
      ((TileDice) tile).startSpinning();
      if (world.isClientSide) {
        UtilSound.playSound(world, pos, SoundRegistry.DICE_MIKE_KOENIG);
      }
      return InteractionResult.SUCCESS;
    }
    return super.use(state, world, pos, player, hand, result);
  }

  /**
   * TODO: util
   * 
   * @param rand
   * @return
   */
  public static Direction getRandom(Random rand) {
    int index = Mth.nextInt(rand, 0, Direction.values().length - 1);
    return Direction.values()[index];
  }
}
