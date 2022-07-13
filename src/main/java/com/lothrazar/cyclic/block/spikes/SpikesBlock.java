package com.lothrazar.cyclic.block.spikes;

import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.UtilSound;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SpikesBlock extends BlockCyclic implements SimpleWaterloggedBlock {

  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
  public static final double CURSE_CHANCE = 0.2;
  public static final int CURSE_TIME = 8 * 20;
  public static final int FIRE_TIME = 20;
  public static final BooleanProperty ACTIVATED = BooleanProperty.create("lit");
  private static final float LARGE = 0.9375F;
  private static final float SMALL = 0.0625F;
  private static final VoxelShape NORTH_BOX = Block.box(0.0F, 0.0F, 15, 15.0F, 15.0F, 16);
  private static final VoxelShape SOUTH_BOX = Block.box(0.0F, 0.0F, 0.0F, 15.0F, 15.0F, SMALL * 15);
  private static final VoxelShape EAST_BOX = Block.box(0.0F, 0.0F, 0.0F, SMALL * 15, 15.0F, 15.0F);
  private static final VoxelShape WEST_BOX = Block.box(LARGE, 0.0F, 0.0F, 15.0F, 15.0F, 15.0F);
  private static final VoxelShape UP_BOX = Block.box(0.0F, 0.0F, 0.0F, 15.0F, SMALL * 15, 15.0F);
  private static final VoxelShape DOWN_BOX = Block.box(0.0F, LARGE, 0.0F, 15.0F, 15.0F, 15.0F);
  private EnumSpikeType type;

  public SpikesBlock(Properties properties, EnumSpikeType type) {
    super(properties.strength(1.1F).noOcclusion().noCollission());
    registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false));
    this.type = type;
  }

  @Override
  public void registerClient() {
    ItemBlockRenderTypes.setRenderLayer(this, RenderType.cutoutMipped());
  }

  @Override
  @Deprecated
  public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
    switch (state.getValue(BlockStateProperties.FACING)) {
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
    return Shapes.block();
  }

  @Override
  @Deprecated
  public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entity) {
    if (entity instanceof LivingEntity && state.getValue(ACTIVATED)) {
      //extra effects
      switch (this.type) {
        case CURSE:
          triggerCurse(worldIn, entity);
        break;
        case FIRE:
          entity.setSecondsOnFire(FIRE_TIME);
        break;
        case PLAIN:
          entity.hurt(DamageSource.CACTUS, 1);
        break;
        default:
        case NONE:
        break;
      }
    }
  }

  private void triggerCurse(Level worldIn, Entity entity) {
    if (worldIn.random.nextDouble() < CURSE_CHANCE) {
      LivingEntity living = (LivingEntity) entity;
      switch (worldIn.random.nextInt(4)) { //[0,3] if nextInt(4) given 
        case 0:
          if (!living.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
            living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, CURSE_TIME, 2));
          }
        break;
        case 1:
          if (!living.hasEffect(MobEffects.WEAKNESS)) {
            living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, CURSE_TIME, 2));
          }
        break;
        case 2:
          if (!living.hasEffect(MobEffects.UNLUCK)) {
            living.addEffect(new MobEffectInstance(MobEffects.UNLUCK, CURSE_TIME, 1));
          }
        break;
        case 3:
          if (!living.hasEffect(MobEffects.DIG_SLOWDOWN)) {
            living.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, CURSE_TIME, 2));
          }
        break;
        case 4:
          entity.hurt(DamageSource.MAGIC, 1);
        break;
        case 5:
          if (!living.hasEffect(MobEffects.BLINDNESS)) {
            living.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, CURSE_TIME, 1));
          }
        break;
      }
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public void neighborChanged(BlockState state, Level world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
    if (state.getValue(ACTIVATED).booleanValue() == false && world.hasNeighborSignal(pos)) {
      world.setBlockAndUpdate(pos, state.setValue(ACTIVATED, true));
      if (!world.isClientSide) {
        //playSoundFromServer
        UtilSound.playSoundFromServer((ServerLevel) world, pos, SoundRegistry.SPIKES_ON.get());
      }
    }
    else if (state.getValue(ACTIVATED).booleanValue() && world.hasNeighborSignal(pos) == false) {
      world.setBlockAndUpdate(pos, state.setValue(ACTIVATED, false));
      if (!world.isClientSide) {
        UtilSound.playSoundFromServer((ServerLevel) world, pos, SoundRegistry.SPIKES_OFF.get());
      }
    }
    super.neighborChanged(state, world, pos, blockIn, fromPos, isMoving);
  }

  @Override
  public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
    return true;
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    Level worldIn = context.getLevel();
    BlockPos pos = context.getClickedPos();
    Direction facing = context.getClickedFace();
    boolean isSolid = worldIn.getBlockState(pos.relative(facing.getOpposite())).canOcclude();
    BlockState state = this.defaultBlockState().setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
    if (isSolid) {
      return state.setValue(BlockStateProperties.FACING, facing).setValue(ACTIVATED, false);
    }
    else {
      return state.setValue(BlockStateProperties.FACING, Direction.DOWN).setValue(ACTIVATED, false);
    }
  }

  @Override
  @SuppressWarnings("deprecation")
  public FluidState getFluidState(BlockState state) {
    return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(BlockStateProperties.FACING).add(ACTIVATED).add(WATERLOGGED);
  }
}
