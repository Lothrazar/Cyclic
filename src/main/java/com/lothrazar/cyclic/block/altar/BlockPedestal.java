package com.lothrazar.cyclic.block.altar;

import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.block.CandlePeaceBlock;
import com.lothrazar.cyclic.util.UtilParticle;
import com.lothrazar.cyclic.util.UtilSound;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockPedestal extends BlockCyclic {

  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

  public BlockPedestal(Properties properties) {
    super(properties.strength(1.8F).noOcclusion());
    this.registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false));
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
    return CandlePeaceBlock.AABB;
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new TilePedestal(pos, state);
  }

  @Override
  public int getDirectSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
    if (blockAccess.getBlockEntity(pos) instanceof TilePedestal p) {
      if (p.inventory.getStackInSlot(0).isEmpty()) {
        return 15;
      }
    }
    return 0;
  }

  @Override
  public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
    if (world.getBlockEntity(pos) instanceof TilePedestal p) {
      if (p.inventory.getStackInSlot(0).isEmpty()) {
        ItemStack held = player.getItemInHand(hand);
        p.inventory.insertItem(0, held, false);
      }
      else {
        ItemStack remove = p.inventory.extractItem(0, 64, false);
        player.drop(remove, false);
      }
      //INSERT
      UtilSound.playSound(world, pos, SoundEvents.FIRE_EXTINGUISH);
      UtilParticle.spawnParticle(world, ParticleTypes.SPLASH, pos.above(), 12);
      return InteractionResult.SUCCESS;
    }
    return super.use(state, world, pos, player, hand, result);
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    return super.getStateForPlacement(context)
        .setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
  }

  @Override
  @SuppressWarnings("deprecation")
  public FluidState getFluidState(BlockState state) {
    return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(WATERLOGGED);
  }

  @Override
  public void registerClient() {
    ItemBlockRenderTypes.setRenderLayer(this, RenderType.cutoutMipped());
  }
}
