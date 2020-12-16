package com.lothrazar.cyclic.block;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.util.UtilParticle;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class UnbreakableBlock extends BlockBase {

  public static final BooleanProperty BREAKABLE = BooleanProperty.create("breakable");

  public UnbreakableBlock(Properties properties) {
    super(properties.hardnessAndResistance(50.0F, 1200.0F));
  }

  @Override
  public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
    ItemStack heldItemStack = player.getHeldItem(hand);
    Item heldItem = heldItemStack.getItem();
    if (state.hasProperty(BREAKABLE) &&
        hand == Hand.MAIN_HAND &&
        heldItem == Items.REDSTONE ||
        heldItem == Items.REDSTONE_TORCH ||
        heldItem == Items.REDSTONE_BLOCK) {
      toggle(state, world, pos);
      return ActionResultType.SUCCESS;
    }
    return super.onBlockActivated(state, world, pos, player, hand, hit);
  }

  private void toggle(BlockState state, World world, BlockPos pos) {
    setBreakable(state, world, pos, !state.get(BREAKABLE));
  }

  private void setBreakable(BlockState state, World world, BlockPos pos, boolean isBreakable) {
    boolean oldBreakable = state.get(BREAKABLE);
    world.setBlockState(pos, state.with(BREAKABLE, isBreakable));
    if (world.isRemote && oldBreakable != isBreakable)
      UtilParticle.spawnParticle(world, RedstoneParticleData.REDSTONE_DUST, pos, 5);
  }

  @Override
  public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
    BlockState neighborState = worldIn.getBlockState(fromPos);
    if (!isMoving && neighborState.hasProperty(BREAKABLE) && state.hasProperty(BREAKABLE))
      setBreakable(state, worldIn, pos, neighborState.get(BREAKABLE));
  }

  @Override
  @SuppressWarnings("deprecation")
  public float getPlayerRelativeBlockHardness(BlockState state, PlayerEntity player, IBlockReader worldIn, BlockPos pos) {
    return (state.hasProperty(BREAKABLE) && !state.get(BREAKABLE)) ? 0.0F : super.getPlayerRelativeBlockHardness(state, player, worldIn, pos);
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(BREAKABLE);
  }
}
