package com.lothrazar.cyclic.block.cable;

import com.google.common.collect.Maps;
import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.data.DataTags;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.UtilSound;
import java.util.Map;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public abstract class CableBase extends BlockBase implements SimpleWaterloggedBlock {

  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
  //regular connections
  public static final EnumProperty<EnumConnectType> DOWN = EnumProperty.create("down", EnumConnectType.class);
  public static final EnumProperty<EnumConnectType> UP = EnumProperty.create("up", EnumConnectType.class);
  public static final EnumProperty<EnumConnectType> NORTH = EnumProperty.create("north", EnumConnectType.class);
  public static final EnumProperty<EnumConnectType> SOUTH = EnumProperty.create("south", EnumConnectType.class);
  public static final EnumProperty<EnumConnectType> WEST = EnumProperty.create("west", EnumConnectType.class);
  public static final EnumProperty<EnumConnectType> EAST = EnumProperty.create("east", EnumConnectType.class);
  public static final Map<Direction, EnumProperty<EnumConnectType>> FACING_TO_PROPERTY_MAP = Util.make(Maps.newEnumMap(Direction.class), (p) -> {
    p.put(Direction.NORTH, NORTH);
    p.put(Direction.EAST, EAST);
    p.put(Direction.SOUTH, SOUTH);
    p.put(Direction.WEST, WEST);
    p.put(Direction.UP, UP);
    p.put(Direction.DOWN, DOWN);
  });
  private static final double top = 16;
  private static final double bot = 0;
  private static final double C = 8;
  private static final double w = 2;
  private static final double sm = C - w;
  private static final double lg = C + w;
  protected static final VoxelShape AABB = Block.box(sm, sm, sm, lg, lg, lg);
  //Y for updown
  protected static final VoxelShape AABB_UP = Block.box(sm, sm, sm, lg, top, lg);
  protected static final VoxelShape AABB_DOWN = Block.box(sm, bot, sm, lg, lg, lg);
  //Z for n-s
  protected static final VoxelShape AABB_NORTH = Block.box(sm, sm, bot, lg, lg, lg);
  protected static final VoxelShape AABB_SOUTH = Block.box(sm, sm, sm, lg, lg, top);
  //X for e-w
  protected static final VoxelShape AABB_WEST = Block.box(bot, sm, sm, lg, lg, lg);
  protected static final VoxelShape AABB_EAST = Block.box(sm, sm, sm, top, lg, lg);

  static boolean shapeConnects(BlockState state, EnumProperty<EnumConnectType> dirctionProperty) {
    return state.getValue(dirctionProperty).isConnected();
  }

  public static VoxelShape createShape(BlockState state) {
    VoxelShape shape = AABB;
    if (shapeConnects(state, UP)) {
      shape = Shapes.joinUnoptimized(shape, AABB_UP, BooleanOp.OR);
    }
    if (shapeConnects(state, DOWN)) {
      shape = Shapes.joinUnoptimized(shape, AABB_DOWN, BooleanOp.OR);
    }
    if (shapeConnects(state, WEST)) {
      shape = Shapes.joinUnoptimized(shape, AABB_WEST, BooleanOp.OR);
    }
    if (shapeConnects(state, EAST)) {
      shape = Shapes.joinUnoptimized(shape, AABB_EAST, BooleanOp.OR);
    }
    if (shapeConnects(state, NORTH)) {
      shape = Shapes.joinUnoptimized(shape, AABB_NORTH, BooleanOp.OR);
    }
    if (shapeConnects(state, SOUTH)) {
      shape = Shapes.joinUnoptimized(shape, AABB_SOUTH, BooleanOp.OR);
    }
    return shape;
  }

  public CableBase(Properties properties) {
    super(properties);
    registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(WATERLOGGED);
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
  @SuppressWarnings("deprecation")
  public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
    if (stateIn.getValue(WATERLOGGED)) {
      worldIn.getLiquidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
    }
    return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
  }

  @Override
  public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
    if (hit.getDirection() == null) {
      return super.use(state, world, pos, player, handIn, hit);
    }
    if (handIn != InteractionHand.MAIN_HAND) {
      return super.use(state, world, pos, player, handIn, hit);
    }
    ItemStack stack = player.getItemInHand(handIn);
    if (!stack.is(DataTags.WRENCH)) {
      //ex
      boolean hasExtractor = false;
      for (Direction side : Direction.values()) {
        EnumConnectType connection = state.getValue(CableBase.FACING_TO_PROPERTY_MAP.get(side));
        if (connection.isExtraction()) {
          hasExtractor = true;
          break;
        }
      }
      if (hasExtractor
          && (this == BlockRegistry.item_pipe || this == BlockRegistry.fluid_pipe)) {
        //if has extractor
        if (!world.isClientSide) {
          BlockEntity tileEntity = world.getBlockEntity(pos);
          if (tileEntity instanceof MenuProvider) {
            NetworkHooks.openGui((ServerPlayer) player, (MenuProvider) tileEntity, tileEntity.getBlockPos());
          }
          else {
            throw new IllegalStateException("Our named container provider is missing!");
          }
        }
        return InteractionResult.SUCCESS;
      }
      //ex
      return super.use(state, world, pos, player, handIn, hit);
    }
    //now must be wrench
    final float hitLimit = 0.28F;
    Direction sideToToggle = hit.getDirection();
    //hitX y and Z from old onBlockActivated 
    double hitX = hit.getLocation().x - pos.getX();
    double hitY = hit.getLocation().y - pos.getY();
    double hitZ = hit.getLocation().z - pos.getZ();
    if (hitX < hitLimit) {
      sideToToggle = Direction.WEST;
    }
    else if (hitX > 1 - hitLimit) {
      sideToToggle = Direction.EAST;
    }
    else if (hitY < hitLimit) {
      sideToToggle = Direction.DOWN;
    }
    else if (hitY > 1 - hitLimit) {
      sideToToggle = Direction.UP;
    }
    else if (hitZ < hitLimit) {
      sideToToggle = Direction.NORTH;
    }
    else if (hitZ > 1 - hitLimit) {
      sideToToggle = Direction.SOUTH;
    }
    EnumProperty<EnumConnectType> prop = CableBase.FACING_TO_PROPERTY_MAP.get(sideToToggle);
    if (state.hasProperty(prop)) {
      EnumConnectType status = state.getValue(prop);
      //inventory is decided not by wrench but by normal mode
      //so it rotates: 
      BlockState newState = state;
      // INVENTORY// NONE -> CABLE(extract) -> BLOCKED -> and back to none again
      boolean updatePost = false;
      switch (status) {
        case BLOCKED:
          //unblock it go back to none (dont know where connection would be if any)
          newState = state.setValue(prop, EnumConnectType.NONE);
          updatePost = true;
        break;
        case INVENTORY: // inventory connection or
        case NONE: // no connection
          newState = state.setValue(prop, EnumConnectType.CABLE);
        break;
        case CABLE: // extract
          // extract to blocked 
          newState = state.setValue(prop, EnumConnectType.BLOCKED);
        break;
      }
      if (world.setBlockAndUpdate(pos, newState)) {
        if (updatePost) {
          newState.updateShape(sideToToggle, world.getBlockState(pos.relative(sideToToggle)), world, pos, pos.relative(sideToToggle));
        }
        player.swing(handIn);
        if (world.isClientSide) {
          UtilSound.playSound(player, SoundRegistry.THUNK, 0.2F, 1F);
        }
      }
    }
    return super.use(state, world, pos, player, handIn, hit);
  }

  /**
   * True means cable is blocked. false means its not a cable at all, or its unblocked
   * 
   * @param blockState
   * @param side
   * @return
   */
  public static boolean isCableBlocked(BlockState blockState, Direction side) {
    if (side == null) {
      return false;
    }
    EnumProperty<EnumConnectType> property = CableBase.FACING_TO_PROPERTY_MAP.get(side);
    return blockState.getBlock() instanceof CableBase
        && blockState.hasProperty(property)
        && blockState.getValue(property).isUnBlocked() == false;
  }
}
