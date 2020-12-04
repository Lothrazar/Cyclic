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
import net.minecraft.world.World;

public class UnbreakableBlock extends BlockBase {

  public static final BooleanProperty ENABLED = BooleanProperty.create("enabled");

  public UnbreakableBlock(Properties properties) {
    super(properties.hardnessAndResistance(50.0F, 1200.0F));
    this.setDefaultState(this.stateContainer.getBaseState().with(ENABLED, true));
  }

  @Override
  public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
    ItemStack heldItemStack = player.getHeldItem(hand);
    Item heldItem = heldItemStack.getItem();
    if (hand == Hand.MAIN_HAND && heldItem == Items.REDSTONE ||
            heldItem == Items.REDSTONE_TORCH ||
            heldItem == Items.REDSTONE_BLOCK) {
      if (!world.isRemote && state.hasProperty(ENABLED)) {
        state.hardness = state.get(ENABLED) ? 50F : -1.0F;
        world.setBlockState(pos, state.with(ENABLED, !state.get(ENABLED)));
      }
      else if (world.isRemote)
        UtilParticle.spawnParticle(world, RedstoneParticleData.REDSTONE_DUST, pos, 5);
      return ActionResultType.SUCCESS;
    }
    return super.onBlockActivated(state, world, pos, player, hand, hit);
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(ENABLED);
  }
}
