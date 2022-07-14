package com.lothrazar.cyclic.block.magnet;

import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.ParticleUtil;
import com.lothrazar.cyclic.util.SoundUtil;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class BlockMagnetPanel extends BlockCyclic {

  public static IntValue RADIUS;
  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
  protected static final VoxelShape AABB = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 0.5D, 15.0D);

  public BlockMagnetPanel(Properties properties) {
    super(properties.strength(1.8F).sound(SoundType.METAL).noOcclusion().noCollission());
    registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false).setValue(LIT, true));
  }

  @Override
  public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
    if (hand == InteractionHand.MAIN_HAND) {
      world.setBlockAndUpdate(pos, state.setValue(LIT, !state.getValue(LIT)));
      SoundUtil.playSound(world, pos, SoundEvents.FIRE_EXTINGUISH);
      ParticleUtil.spawnParticle(world, ParticleTypes.SPLASH, pos.above(), 12);
      return InteractionResult.SUCCESS;
    }
    return super.use(state, world, pos, player, hand, result);
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
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new TileInsertingMagnet(pos, state);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(WATERLOGGED);
    builder.add(LIT);
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
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
    return createTickerHelper(type, TileRegistry.MAGNET.get(), world.isClientSide ? TileInsertingMagnet::clientTick : TileInsertingMagnet::serverTick);
  }
}
