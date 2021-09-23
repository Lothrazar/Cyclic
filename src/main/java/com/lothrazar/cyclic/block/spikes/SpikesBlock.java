package com.lothrazar.cyclic.block.spikes;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.UtilSound;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class SpikesBlock extends BlockBase {

  public static final double CURSE_CHANCE = 0.2;
  public static final int CURSE_TIME = 8 * 20;
  public static final int FIRE_TIME = 20;
  public static final BooleanProperty ACTIVATED = BooleanProperty.create("lit");
  private static final float LARGE = 0.9375F;
  private static final float SMALL = 0.0625F;
  private static final VoxelShape NORTH_BOX = Block.makeCuboidShape(0.0F, 0.0F, 15, 15.0F, 15.0F, 16);
  private static final VoxelShape SOUTH_BOX = Block.makeCuboidShape(0.0F, 0.0F, 0.0F, 15.0F, 15.0F, SMALL * 15);
  private static final VoxelShape EAST_BOX = Block.makeCuboidShape(0.0F, 0.0F, 0.0F, SMALL * 15, 15.0F, 15.0F);
  private static final VoxelShape WEST_BOX = Block.makeCuboidShape(LARGE, 0.0F, 0.0F, 15.0F, 15.0F, 15.0F);
  private static final VoxelShape UP_BOX = Block.makeCuboidShape(0.0F, 0.0F, 0.0F, 15.0F, SMALL * 15, 15.0F);
  private static final VoxelShape DOWN_BOX = Block.makeCuboidShape(0.0F, LARGE, 0.0F, 15.0F, 15.0F, 15.0F);
  private EnumSpikeType type;

  public SpikesBlock(Properties properties, EnumSpikeType type) {
    super(properties.hardnessAndResistance(1.1F).notSolid().doesNotBlockMovement());
    this.type = type;
  }

  @Override
  public void registerClient() {
    RenderTypeLookup.setRenderLayer(this, RenderType.getCutoutMipped());
  }

  @Override
  @Deprecated
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    switch (state.get(BlockStateProperties.FACING)) {
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
    return VoxelShapes.fullCube();
  }

  @Override
  @Deprecated
  public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entity) {
    if (entity instanceof LivingEntity && state.get(ACTIVATED)) {
      //extra effects
      switch (this.type) {
        case CURSE:
          triggerCurse(worldIn, entity);
        break;
        case FIRE:
          entity.setFire(FIRE_TIME);
        break;
        case PLAIN:
          entity.attackEntityFrom(DamageSource.CACTUS, 1);
        break;
        default:
        case NONE:
        break;
      }
    }
  }

  private void triggerCurse(World worldIn, Entity entity) {
    if (worldIn.rand.nextDouble() < CURSE_CHANCE) {
      LivingEntity living = (LivingEntity) entity;
      switch (worldIn.rand.nextInt(4)) { //[0,3] if nextInt(4) given 
        case 0:
          if (!living.isPotionActive(Effects.SLOWNESS)) {
            living.addPotionEffect(new EffectInstance(Effects.SLOWNESS, CURSE_TIME, 2));
          }
        break;
        case 1:
          if (!living.isPotionActive(Effects.WEAKNESS)) {
            living.addPotionEffect(new EffectInstance(Effects.WEAKNESS, CURSE_TIME, 2));
          }
        break;
        case 2:
          if (!living.isPotionActive(Effects.UNLUCK)) {
            living.addPotionEffect(new EffectInstance(Effects.UNLUCK, CURSE_TIME, 1));
          }
        break;
        case 3:
          if (!living.isPotionActive(Effects.MINING_FATIGUE)) {
            living.addPotionEffect(new EffectInstance(Effects.MINING_FATIGUE, CURSE_TIME, 2));
          }
        break;
        case 4:
          entity.attackEntityFrom(DamageSource.MAGIC, 1);
        break;
        case 5:
          if (!living.isPotionActive(Effects.BLINDNESS)) {
            living.addPotionEffect(new EffectInstance(Effects.BLINDNESS, CURSE_TIME, 1));
          }
        break;
      }
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
    if (state.get(ACTIVATED).booleanValue() == false && world.isBlockPowered(pos)) {
      world.setBlockState(pos, state.with(ACTIVATED, true));
      if (!world.isRemote) {
        //playSoundFromServer
        UtilSound.playSoundFromServer((ServerWorld) world, pos, SoundRegistry.SPIKES_ON);
      }
    }
    else if (state.get(ACTIVATED).booleanValue() && world.isBlockPowered(pos) == false) {
      world.setBlockState(pos, state.with(ACTIVATED, false));
      if (!world.isRemote) {
        UtilSound.playSoundFromServer((ServerWorld) world, pos, SoundRegistry.SPIKES_OFF);
      }
    }
    super.neighborChanged(state, world, pos, blockIn, fromPos, isMoving);
  }

  @Override
  public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
    return true;
  }

  @Override
  public BlockState getStateForPlacement(BlockItemUseContext context) {
    World worldIn = context.getWorld();
    BlockPos pos = context.getPos();
    Direction facing = context.getFace();
    return worldIn.getBlockState(pos.offset(facing.getOpposite())).isSolid()
        ? this.getDefaultState().with(BlockStateProperties.FACING, facing).with(ACTIVATED, false)
        : this.getDefaultState().with(BlockStateProperties.FACING, Direction.DOWN).with(ACTIVATED, false);
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(BlockStateProperties.FACING).add(ACTIVATED);
  }
}
