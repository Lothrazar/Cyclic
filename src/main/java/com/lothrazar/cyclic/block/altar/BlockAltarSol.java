package com.lothrazar.cyclic.block.altar;

import java.util.Random;
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
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockAltarSol extends BlockCyclic {

  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
  public static final EnumProperty<AltarType> TYPE = EnumProperty.create("type", AltarType.class);

  public BlockAltarSol(Properties properties) {
    super(properties.strength(1.8F).noOcclusion());
    this.registerDefaultState(defaultBlockState().setValue(TYPE, AltarType.EMPTY).setValue(WATERLOGGED, false));
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
    return CandlePeaceBlock.AABB;
  }

  @Override
  public int getDirectSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
    if (blockState.getValue(TYPE).isEmpty()) {
      return 0;
    }
    return 15;
  }

  @Override
  public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
    AltarType old = state.getValue(TYPE);
    ItemStack held = player.getItemInHand(hand);
    AltarType newt = old;
    if (held.is(Items.PHANTOM_MEMBRANE)) {
      newt = AltarType.PHANTOM;
    }
    else {//toggle between empty and trader
      newt = old.isEmpty() ? AltarType.TRADER : AltarType.EMPTY;
    }
    world.setBlockAndUpdate(pos, state.setValue(TYPE, newt));
    UtilSound.playSound(world, pos, SoundEvents.FIRE_EXTINGUISH);
    UtilParticle.spawnParticle(world, ParticleTypes.SPLASH, pos.above(), 12);
    return InteractionResult.SUCCESS;
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    return super.getStateForPlacement(context)
        .setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
  }

  @Override
  @SuppressWarnings("deprecation")
  public FluidState getFluidState(BlockState state) {
    CampfireBlock y;
    return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
  }

  //copy campfire
  @Override
  public void animateTick(BlockState bs, Level world, BlockPos pos, Random rand) {
    if (!bs.getValue(TYPE).isEmpty()) {
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
    builder.add(TYPE).add(WATERLOGGED);
  }

  @Override
  public void registerClient() {
    ItemBlockRenderTypes.setRenderLayer(this, RenderType.cutoutMipped());
  }

  public static boolean isTrader(LivingEntity mob, MobSpawnType res) {
    return mob.getType() == EntityType.TRADER_LLAMA
        || mob.getType() == EntityType.WANDERING_TRADER
        || mob.getType() == EntityType.BAT;
  }

  public static boolean isPhantom(LivingEntity mob, MobSpawnType res) {
    return mob.getType() == EntityType.PHANTOM
        || mob.getType() == EntityType.ENDERMAN
        || mob.getType() == EntityType.ENDERMITE;
  }
}
