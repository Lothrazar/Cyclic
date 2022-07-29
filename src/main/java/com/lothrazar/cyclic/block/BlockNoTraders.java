package com.lothrazar.cyclic.block;

import com.lothrazar.cyclic.util.ParticleUtil;
import com.lothrazar.cyclic.util.SoundUtil;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
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

public class BlockNoTraders extends BlockCyclic implements SimpleWaterloggedBlock {

  public static IntValue HEIGHT;
  public static IntValue RADIUS;
  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
  private static final double BOUNDS = 3;
  public static final VoxelShape AABB = Block.box(BOUNDS, 0, BOUNDS,
      16 - BOUNDS, 16 - BOUNDS, 16 - BOUNDS);

  public BlockNoTraders(Properties properties) {
    super(properties.strength(1.2F).noOcclusion());
    this.registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false));
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
    return AABB;
  }

  @Override
  public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
    if (hand == InteractionHand.MAIN_HAND) { //  && player.getItemInHand(hand).isEmpty()
      world.setBlockAndUpdate(pos, state.setValue(LIT, !state.getValue(LIT)));
      SoundUtil.playSound(world, pos, SoundEvents.FIRE_EXTINGUISH);
      ParticleUtil.spawnParticle(world, ParticleTypes.SPLASH, pos.above(), 12);
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

  //copy campfire
  @Override
  public void animateTick(BlockState bs, Level world, BlockPos pos, RandomSource rand) {
    if (bs.getValue(LIT)) {
      if (rand.nextInt(10) == 0) {
        world.playLocalSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS, 0.5F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.6F, false);
      }
      if (rand.nextInt(5) == 0) {
        for (int i = 0; i < rand.nextInt(1) + 1; ++i) {
          world.addParticle(ParticleTypes.LAVA, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, rand.nextFloat() / 2.0F, 5.0E-5D, rand.nextFloat() / 2.0F);
        }
      }
    }
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(LIT).add(WATERLOGGED);
  }

  @Override
  public void registerClient() {
    ItemBlockRenderTypes.setRenderLayer(this, RenderType.cutoutMipped());
  }

  public static boolean isSpawnDenied(LivingEntity mob, MobSpawnType res) {
    return mob.getType() == EntityType.TRADER_LLAMA
        || mob.getType() == EntityType.WANDERING_TRADER
        || mob.getType() == EntityType.BAT;
  }
  //  public static boolean isExplosive(LivingEntity mob, MobSpawnType res) {
  //    return mob.getType() == EntityType.CREEPER
  //        || mob instanceof Creeper;
  //  }
  //
  //  public static boolean isFlight(LivingEntity mob, MobSpawnType res) {
  //    return mob.getType() == EntityType.PHANTOM
  //        || mob.getType() == EntityType.BAT;
  //  }
}
