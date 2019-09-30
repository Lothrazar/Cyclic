package com.lothrazar.cyclic.block;

import java.util.Locale;
import com.lothrazar.cyclic.base.BlockBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockSpikes extends BlockBase {

  private static final double CURSE_CHANCE = 0.2;
  private static final int CURSE_TIME = 30;
  private static final int FIRE_TIME = 30;

  public static enum EnumSpikeType implements IStringSerializable {
    PLAIN, FIRE, CURSE;

    @Override
    public String getName() {
      return this.name().toLowerCase(Locale.ENGLISH);
    }
  }

  public static final BooleanProperty ACTIVATED = BooleanProperty.create("lit");
  private static final float LARGE = 0.9375F;
  private static final float SMALL = 0.0625F;
  private static final VoxelShape NORTH_BOX = Block.makeCuboidShape(0.0F, 0.0F, LARGE, 15.0F, 15.0F, 1.0F);
  private static final VoxelShape EAST_BOX = Block.makeCuboidShape(0.0F, 0.0F, 0.0F, SMALL * 15, 15.0F, 15.0F);
  private static final VoxelShape SOUTH_BOX = Block.makeCuboidShape(0.0F, 0.0F, 0.0F, 15.0F, 15.0F, SMALL * 15);
  private static final VoxelShape WEST_BOX = Block.makeCuboidShape(LARGE, 0.0F, 0.0F, 15.0F, 15.0F, 15.0F);
  private static final VoxelShape UP_BOX = Block.makeCuboidShape(0.0F, 0.0F, 0.0F, 15.0F, SMALL * 15, 15.0F);
  private static final VoxelShape DOWN_BOX = Block.makeCuboidShape(0.0F, LARGE, 0.0F, 15.0F, 15.0F, 15.0F);
  protected static final VoxelShape UNPRESSED_AABB = Block.makeCuboidShape(
      1.0D, 0.0D, 1.0D,
      15.0D, 1.0D, 15.0D);
  private EnumSpikeType type;

  public BlockSpikes(Properties properties, EnumSpikeType type) {
    super(properties.hardnessAndResistance(1.1F));
    this.type = type;
  }

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    switch ((Direction) state.get(BlockStateProperties.FACING)) {
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
    return VoxelShapes.fullCube();//CANT BE NULL, causes crashes.   
  }

  @Override
  public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entity) {
    if (entity instanceof LivingEntity && state.get(ACTIVATED)) {
      //extra effects
      switch (this.type) {
        case CURSE:
          if (worldIn.rand.nextDouble() < CURSE_CHANCE) {
            entity.attackEntityFrom(DamageSource.MAGIC, 1);
          }
          LivingEntity living = (LivingEntity) entity;
          switch (worldIn.rand.nextInt(4)) {//[0,3] if nextInt(4) given 
            case 0:
              living.addPotionEffect(new EffectInstance(Effects.SLOWNESS, CURSE_TIME, 2));
            break;
            case 1:
              living.addPotionEffect(new EffectInstance(Effects.WEAKNESS, CURSE_TIME, 2));
            break;
            case 2:
              living.addPotionEffect(new EffectInstance(Effects.UNLUCK, CURSE_TIME, 1));
            break;
            case 3:
              living.addPotionEffect(new EffectInstance(Effects.MINING_FATIGUE, CURSE_TIME, 1));
            break;
          }
        break;
        case FIRE:
          if (!entity.isBurning()) {
            entity.setFire(FIRE_TIME);
          }
        break;
        case PLAIN:
          entity.attackEntityFrom(DamageSource.CACTUS, 1);
        break;
        default:
        break;
      }
    }
  }

  public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
    if (state.get(ACTIVATED).booleanValue() == false
        && world.isBlockPowered(pos)) {
      //  UtilSound.playSoundFromServer(SoundRegistry.spikes_on, SoundCategory.BLOCKS, pos, world.provider.getDimension(), 16);
      world.setBlockState(pos, state.with(ACTIVATED, true));
    }
    else if (state.get(ACTIVATED).booleanValue()
        && world.isBlockPowered(pos) == false) {
      //  UtilSound.playSoundFromServer(SoundRegistry.spikes_off, SoundCategory.BLOCKS, pos, world.provider.getDimension(), 16);
      world.setBlockState(pos, state.with(ACTIVATED, false));
    }
    super.neighborChanged(state, world, pos, blockIn, fromPos, isMoving);
  }

  @Override
  public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
    return true;
  }

  @Override
  public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.CUTOUT;
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
