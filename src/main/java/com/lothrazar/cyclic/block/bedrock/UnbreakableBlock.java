package com.lothrazar.cyclic.block.bedrock;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.util.UtilParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class UnbreakableBlock extends BlockBase {

  public static final BooleanProperty BREAKABLE = BooleanProperty.create("breakable");

  public UnbreakableBlock(Properties properties) {
    super(properties.strength(50.0F, 1200.0F));
  }

  @Override
  public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
    ItemStack heldItemStack = player.getItemInHand(hand);
    Item heldItem = heldItemStack.getItem();
    if (state.hasProperty(BREAKABLE) &&
        hand == InteractionHand.MAIN_HAND &&
        heldItem == Items.REDSTONE ||
        heldItem == Items.REDSTONE_TORCH ||
        heldItem == Items.REDSTONE_BLOCK) {
      toggle(state, world, pos);
      return InteractionResult.SUCCESS;
    }
    return super.use(state, world, pos, player, hand, hit);
  }

  private void toggle(BlockState state, Level world, BlockPos pos) {
    setBreakable(state, world, pos, !state.getValue(BREAKABLE));
  }

  private void setBreakable(BlockState state, Level world, BlockPos pos, boolean isBreakable) {
    boolean oldBreakable = state.getValue(BREAKABLE);
    world.setBlockAndUpdate(pos, state.setValue(BREAKABLE, isBreakable));
    if (world.isClientSide && oldBreakable != isBreakable) {
      UtilParticle.spawnParticle(world, DustParticleOptions.REDSTONE, pos, 5);
    }
  }

  @Override
  public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
    BlockState neighborState = worldIn.getBlockState(fromPos);
    if (!isMoving && neighborState.hasProperty(BREAKABLE) && state.hasProperty(BREAKABLE)) {
      setBreakable(state, worldIn, pos, neighborState.getValue(BREAKABLE));
    }
  }

  @Override
  @SuppressWarnings("deprecation")
  public float getDestroyProgress(BlockState state, Player player, BlockGetter worldIn, BlockPos pos) {
    return (state.hasProperty(BREAKABLE) && !state.getValue(BREAKABLE)) ? 0.0F : super.getDestroyProgress(state, player, worldIn, pos);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(BREAKABLE);
  }
}
