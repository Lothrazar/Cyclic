package com.lothrazar.cyclic.block.cable;

import com.google.common.collect.Maps;
import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.data.DataTags;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.UtilSound;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public abstract class CableBase extends BlockBase {

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
  protected static final VoxelShape AABB = Block.makeCuboidShape(sm, sm, sm, lg, lg, lg);
  //Y for updown
  protected static final VoxelShape AABB_UP = Block.makeCuboidShape(sm, sm, sm, lg, top, lg);
  protected static final VoxelShape AABB_DOWN = Block.makeCuboidShape(sm, bot, sm, lg, lg, lg);
  //Z for n-s
  protected static final VoxelShape AABB_NORTH = Block.makeCuboidShape(sm, sm, bot, lg, lg, lg);
  protected static final VoxelShape AABB_SOUTH = Block.makeCuboidShape(sm, sm, sm, lg, lg, top);
  //X for e-w
  protected static final VoxelShape AABB_WEST = Block.makeCuboidShape(bot, sm, sm, lg, lg, lg);
  protected static final VoxelShape AABB_EAST = Block.makeCuboidShape(sm, sm, sm, top, lg, lg);

  static boolean shapeConnects(BlockState state, EnumProperty<EnumConnectType> dirctionProperty) {
    return state.get(dirctionProperty).isConnected();
  }

  public static VoxelShape createShape(BlockState state) {
    VoxelShape shape = AABB;
    if (shapeConnects(state, UP)) {
      shape = VoxelShapes.combine(shape, AABB_UP, IBooleanFunction.OR);
    }
    if (shapeConnects(state, DOWN)) {
      shape = VoxelShapes.combine(shape, AABB_DOWN, IBooleanFunction.OR);
    }
    if (shapeConnects(state, WEST)) {
      shape = VoxelShapes.combine(shape, AABB_WEST, IBooleanFunction.OR);
    }
    if (shapeConnects(state, EAST)) {
      shape = VoxelShapes.combine(shape, AABB_EAST, IBooleanFunction.OR);
    }
    if (shapeConnects(state, NORTH)) {
      shape = VoxelShapes.combine(shape, AABB_NORTH, IBooleanFunction.OR);
    }
    if (shapeConnects(state, SOUTH)) {
      shape = VoxelShapes.combine(shape, AABB_SOUTH, IBooleanFunction.OR);
    }
    return shape;
  }

  public CableBase(Properties properties) {
    super(properties);
    setDefaultState(getDefaultState().with(WATERLOGGED, false));
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(WATERLOGGED);
  }

  @Override
  public BlockState getStateForPlacement(BlockItemUseContext context) {
    return super.getStateForPlacement(context)
        .with(WATERLOGGED, context.getWorld().getFluidState(context.getPos()).getFluid() == Fluids.WATER);
  }

  @Override
  @SuppressWarnings("deprecation")
  public FluidState getFluidState(BlockState state) {
    return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
  }

  @Override
  @SuppressWarnings("deprecation")
  public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
    if (stateIn.get(WATERLOGGED))
      worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
    return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
  }

  @Override
  public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
    if (hit.getFace() == null) {
      return super.onBlockActivated(state, world, pos, player, handIn, hit);
    }
    if (handIn != Hand.MAIN_HAND) {
      return super.onBlockActivated(state, world, pos, player, handIn, hit);
    }
    ItemStack stack = player.getHeldItem(handIn);
    if (!stack.getItem().isIn(DataTags.WRENCH)) {
      //ex
      boolean hasExtractor = false;
      for (Direction side : Direction.values()) {
        EnumConnectType connection = state.get(CableBase.FACING_TO_PROPERTY_MAP.get(side));
        if (connection.isExtraction()) {
          hasExtractor = true;
          break;
        }
      }
      if (hasExtractor
          && (this == BlockRegistry.item_pipe || this == BlockRegistry.fluid_pipe)) {
        //if has extractor
        if (!world.isRemote) {
          TileEntity tileEntity = world.getTileEntity(pos);
          if (tileEntity instanceof INamedContainerProvider) {
            NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tileEntity, tileEntity.getPos());
          }
          else {
            throw new IllegalStateException("Our named container provider is missing!");
          }
        }
        return ActionResultType.SUCCESS;
      }
      //ex
      return super.onBlockActivated(state, world, pos, player, handIn, hit);
    }
    //now must be wrench
    final float hitLimit = 0.28F;
    Direction sideToToggle = hit.getFace();
    //hitX y and Z from old onBlockActivated 
    double hitX = hit.getHitVec().x - pos.getX();
    double hitY = hit.getHitVec().y - pos.getY();
    double hitZ = hit.getHitVec().z - pos.getZ();
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
      EnumConnectType status = state.get(prop);
      //inventory is decided not by wrench but by normal mode
      //so it rotates: 
      BlockState newState = state;
      // INVENTORY// NONE -> CABLE(extract) -> BLOCKED -> and back to none again
      boolean updatePost = false;
      switch (status) {
        case BLOCKED:
          //unblock it go back to none (dont know where connection would be if any)
          newState = state.with(prop, EnumConnectType.NONE);
          updatePost = true;
        break;
        case INVENTORY: // inventory connection or
        case NONE: // no connection
          newState = state.with(prop, EnumConnectType.CABLE);
        break;
        case CABLE: // extract
          // extract to blocked 
          newState = state.with(prop, EnumConnectType.BLOCKED);
        break;
      }
      if (world.setBlockState(pos, newState)) {
        if (updatePost) {
          newState.updatePostPlacement(sideToToggle, world.getBlockState(pos.offset(sideToToggle)), world, pos, pos.offset(sideToToggle));
        }
        player.swingArm(handIn);
        if (world.isRemote) {
          UtilSound.playSound(player, SoundRegistry.THUNK, 0.2F, 1F);
        }
      }
    }
    return super.onBlockActivated(state, world, pos, player, handIn, hit);
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
        && blockState.get(property).isUnBlocked() == false;
  }
}
