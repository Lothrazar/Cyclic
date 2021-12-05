package com.lothrazar.cyclic.block.conveyor;

import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.data.DataTags;
import com.lothrazar.cyclic.registry.TileRegistry;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockConveyor extends BlockCyclic implements SimpleWaterloggedBlock {

  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
  private static final int MAX_CONNECTED_UPDATE = 16;
  //main flat shape
  protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
  //Sub-shapes for angles
  protected static final VoxelShape AG00 = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 0.8D, 16.0D);
  protected static final VoxelShape AG01 = Block.box(1.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
  protected static final VoxelShape AG02 = Block.box(2.0D, 1.0D, 0.0D, 16.0D, 2.0D, 16.0D);
  protected static final VoxelShape AG03 = Block.box(3.0D, 2.0D, 0.0D, 16.0D, 3.0D, 16.0D);
  protected static final VoxelShape AG04 = Block.box(4.0D, 3.0D, 0.0D, 16.0D, 4.0D, 16.0D);
  protected static final VoxelShape AG05 = Block.box(5.0D, 4.0D, 0.0D, 16.0D, 5.0D, 16.0D);
  protected static final VoxelShape AG06 = Block.box(6.0D, 5.0D, 0.0D, 16.0D, 6.0D, 16.0D);
  protected static final VoxelShape AG07 = Block.box(7.0D, 6.0D, 0.0D, 16.0D, 7.0D, 16.0D);
  protected static final VoxelShape AG08 = Block.box(8.0D, 7.0D, 0.0D, 16.0D, 8.0D, 16.0D);
  protected static final VoxelShape AG09 = Block.box(9.0D, 8.0D, 0.0D, 16.0D, 9.0D, 16.0D);
  protected static final VoxelShape AG10 = Block.box(10.0D, 9.0D, 0.0D, 16.0D, 10.0D, 16.0D);
  protected static final VoxelShape AG11 = Block.box(11.0D, 10.0D, 0.0D, 16.0D, 11.0D, 16.0D);
  protected static final VoxelShape AG12 = Block.box(12.0D, 11.0D, 0.0D, 16.0D, 12.0D, 16.0D);
  protected static final VoxelShape AG13 = Block.box(13.0D, 12.0D, 0.0D, 16.0D, 13.0D, 16.0D);
  protected static final VoxelShape AG14 = Block.box(14.0D, 13.0D, 0.0D, 16.0D, 14.0D, 16.0D);
  protected static final VoxelShape AG15 = Block.box(15.0D, 14.0D, 0.0D, 16.0D, 15.0D, 16.0D);
  protected static final VoxelShape AG16 = Block.box(15.5D, 15.0D, 0.0D, 16.0D, 16.0D, 16.0D);
  //four angled shapes
  protected static final VoxelShape ANGLEEAST = Shapes.or(AG00, AG01, AG02, AG03, AG04, AG05, AG06, AG07, AG08, AG09, AG10, AG11, AG12, AG13, AG14, AG15, AG16);
  protected static final VoxelShape ANGLESOUTH = Shapes.or(rot(AG00), rot(AG01), rot(AG02), rot(AG03), rot(AG04), rot(AG05),
      rot(AG06), rot(AG07), rot(AG08), rot(AG09), rot(AG10), rot(AG11), rot(AG12), rot(AG13), rot(AG14), rot(AG15), rot(AG16));
  protected static final VoxelShape ANGLENORTH = Shapes.or(flipx(AG00), flipx(AG01), flipx(AG02), flipx(AG03), flipx(AG04), flipx(AG05),
      flipx(AG06), flipx(AG07), flipx(AG08), flipx(AG09), flipx(AG10), flipx(AG11), flipx(AG12), flipx(AG13), flipx(AG14), flipx(AG15), flipx(AG16));
  protected static final VoxelShape ANGLEWEST = Shapes.or(flipz(AG00), flipz(AG01), flipz(AG02), flipz(AG03), flipz(AG04), flipz(AG05),
      flipz(AG06), flipz(AG07), flipz(AG08), flipz(AG09), flipz(AG10), flipz(AG11), flipz(AG12), flipz(AG13), flipz(AG14), flipz(AG15), flipz(AG16));
  // PROPERTIES
  public static final EnumProperty<DyeColor> COLOUR = EnumProperty.create("colour", DyeColor.class);
  public static final EnumProperty<ConveyorType> TYPE = EnumProperty.create("type", ConveyorType.class);
  public static final EnumProperty<ConveyorSpeed> SPEED = EnumProperty.create("speed", ConveyorSpeed.class);
  //javafx.util class doesnt compile into minecraft with ./gradlew build, swapped to SimpleEntry https://www.baeldung.com/java-pairs
  public static final List<SimpleImmutableEntry<ConveyorType, Direction>> STATE_PAIRS = generateStatePairs();

  public BlockConveyor(Properties properties) {
    super(properties.strength(0.6F).noOcclusion());
    registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false));
  }
  /**
   * Utility methods for block shapes.
   *
   * @author SciWhiz12
   */
  /**
   * Rotates the given {@link VoxelShape} along the horizontal plane according to the given rotation direction.
   * <p>
   * Assumes the given shape is within the bounds of 1 unit on each axis.
   * <p>
   * https://gist.github.com/sciwhiz12/0852b629e7a3d0200ffc03ec7edab187
   *
   * @param shape       The shape to rotate
   * @param rotationDir The rotation direction
   * @return The rotated shape
   */
  public static VoxelShape rot(final VoxelShape shape) {
    double x1 = shape.min(Direction.Axis.X), x2 = shape.max(Direction.Axis.X);
    double y1 = shape.min(Direction.Axis.Y), y2 = shape.max(Direction.Axis.Y);
    double z1 = shape.min(Direction.Axis.Z), z2 = shape.max(Direction.Axis.Z);
    //    if (rotationDir == Rotation.CLOCKWISE_90 || rotationDir == Rotation.COUNTERCLOCKWISE_90) {
    double temp = z1; // ]
    z1 = x1; // ] x1 <-> z1
    x1 = temp; // ]
    temp = z2; // ]
    z2 = x2; // ] x2 <-> z2
    x2 = temp; // ]
    //    }
    //    if (rotationDir == Rotation.CLOCKWISE_90 || rotationDir == Rotation.CLOCKWISE_180) {
    x1 = 1 - x1; // clockwise
    x2 = 1 - x2;
    //    }
    //    if (rotationDir == Rotation.COUNTERCLOCKWISE_90 || rotationDir == Rotation.CLOCKWISE_180) {
    //      z1 = 1 - z1; // counterclockwise
    //      z2 = 1 - z2;
    //    }
    return safeShapeBox(x1, x2, y1, y2, z1, z2);
  }

  private static VoxelShape safeShapeBox(double x1, double x2, double y1, double y2, double z1, double z2) {
    double temp;
    if (x1 > x2) {
      temp = x1;
      x1 = x2;
      x2 = temp;
    }
    if (y1 > y2) {
      temp = y1;
      y1 = y2;
      y2 = temp;
    }
    if (z1 > z2) {
      temp = z1;
      z1 = z2;
      z2 = temp;
    }
    return Shapes.box(x1, y1, z1, x2, y2, z2);
  }

  public static VoxelShape flipx(final VoxelShape shape) {
    double x1 = shape.min(Direction.Axis.X);
    double x2 = shape.max(Direction.Axis.X);
    final double y1 = shape.min(Direction.Axis.Y);
    final double y2 = shape.max(Direction.Axis.Y);
    double z1 = shape.min(Direction.Axis.Z);
    double z2 = shape.max(Direction.Axis.Z);
    //flip
    double temp = z1; // ]
    z1 = x1; // ] x1 <-> z1
    x1 = temp; // ]
    temp = z2; // ]
    z2 = x2; // ] x2 <-> z2
    x2 = temp; // ]
    //
    //    if (rotationDir == Rotation.COUNTERCLOCKWISE_90 || rotationDir == Rotation.CLOCKWISE_180) {
    z1 = 1 - z1; // counterclockwise
    z2 = 1 - z2;
    return safeShapeBox(x1, y1, z1, x2, y2, z2);
  }

  public static VoxelShape flipz(final VoxelShape shape) {
    double x1 = shape.min(Direction.Axis.X);
    double x2 = shape.max(Direction.Axis.X);
    final double y1 = shape.min(Direction.Axis.Y);
    final double y2 = shape.max(Direction.Axis.Y);
    double z1 = shape.min(Direction.Axis.Z);
    double z2 = shape.max(Direction.Axis.Z);
    //flip
    x1 = 1 - x1; //  
    x2 = 1 - x2;
    z1 = 1 - z1; //  
    z2 = 1 - z2;
    return safeShapeBox(x1, y1, z1, x2, y2, z2);
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
    Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
    if (state.getValue(TYPE) == ConveyorType.UP) {
      //      Direction facing = state.get(BlockStateProperties.HORIZONTAL_FACING);
      switch (facing) {
        case EAST:
          return ANGLEEAST;
        case NORTH:
          return ANGLENORTH;
        case SOUTH:
          return ANGLESOUTH;
        case WEST:
          return ANGLEWEST;
        case DOWN:
        case UP:
          break;
      }
      if (state.getValue(TYPE) == ConveyorType.DOWN) {
        switch (facing) {
          case EAST:
            return ANGLEWEST;
          case NORTH:
            return ANGLESOUTH;
          case SOUTH:
            return ANGLENORTH;
          case WEST:
            return ANGLEEAST;
          case DOWN:
          case UP:
            break;
        }
      }
    }
    return SHAPE;
  }

  @Override
  public boolean useShapeForLightOcclusion(BlockState state) {
    return state.getValue(TYPE).isVertical();
  }

  @Override
  public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
    return state.getValue(TYPE).isVertical();
  }

  @Override
  public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
    ItemStack heldStack = player.getItemInHand(hand);
    Item heldItem = heldStack.getItem();
    if (heldItem instanceof DyeItem) {
      //
      DyeItem dye = (DyeItem) heldItem;
      DyeColor newc = dye.getDyeColor();
      world.setBlockAndUpdate(pos, state.setValue(COLOUR, newc));
      //
      this.setConnectedColour(world, pos, newc, 0);
      return InteractionResult.SUCCESS;
      //  }
    }
    else if (heldItem == Items.REDSTONE_TORCH) {
      //speed toggle
      ConveyorSpeed speed = state.getValue(SPEED);
      if (world.setBlockAndUpdate(pos, state.setValue(SPEED, speed.getNext()))) {
        this.setConnectedSpeed(world, pos, speed.getNext(), 0);
        return InteractionResult.SUCCESS;
      }
    }
    else if (heldStack.is(DataTags.WRENCH)) {
      SimpleImmutableEntry<ConveyorType, Direction> nextState = nextConnectedState(state.getValue(TYPE), state.getValue(BlockStateProperties.HORIZONTAL_FACING));
      boolean success = world.setBlockAndUpdate(pos, state.setValue(TYPE, nextState.getKey()).setValue(BlockStateProperties.HORIZONTAL_FACING, nextState.getValue()));
      if (success) {
        return InteractionResult.SUCCESS;
      }
    }
    return super.use(state, world, pos, player, hand, hit);
  }

  private BlockState getClosestConnected(Level world, BlockPos pos) {
    for (Direction d : Direction.values()) {
      //
      BlockPos offset = pos.relative(d);
      BlockState here = world.getBlockState(offset);
      if (here.getBlock() == this) {
        return here;
      }
    }
    return null;
  }

  private void setConnectedColour(Level world, BlockPos pos, DyeColor speedIn, int maxRecursive) {
    if (maxRecursive > MAX_CONNECTED_UPDATE) {
      return;
    }
    for (Direction d : Direction.values()) {
      //
      BlockPos offset = pos.relative(d);
      BlockState here = world.getBlockState(offset);
      if (here.getBlock() == this) {
        world.setBlockAndUpdate(offset, here.setValue(COLOUR, speedIn));
        maxRecursive++;
        this.setConnectedColour(world, offset, speedIn, maxRecursive);
        //}
      }
    }
  }

  private void setConnectedSpeed(Level world, BlockPos pos, ConveyorSpeed speedIn, int maxRecursive) {
    if (maxRecursive > MAX_CONNECTED_UPDATE) {
      return;
    }
    for (Direction d : Direction.values()) {
      //
      BlockPos offset = pos.relative(d);
      BlockState here = world.getBlockState(offset);
      if (here.getBlock() == this) {
        if (world.setBlockAndUpdate(offset, here.setValue(SPEED, speedIn))) {
          maxRecursive++;
          this.setConnectedSpeed(world, offset, speedIn, maxRecursive);
        }
      }
    }
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new TileConveyor(pos, state);
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
    return createTickerHelper(type, TileRegistry.CONVEYOR.get(), world.isClientSide ? TileConveyor::clientTick : TileConveyor::serverTick);
  }

  @Override
  public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
    //decide properties
    ConveyorSpeed speed = ConveyorSpeed.MEDIUM;
    ConveyorType type = ConveyorType.STRAIGHT;
    DyeColor col = DyeColor.GRAY;
    //
    //overwrite defaults with what is nearby
    BlockState nearby = getClosestConnected(world, pos);
    if (nearby != null) {
      speed = nearby.getValue(SPEED);
      col = nearby.getValue(COLOUR);
    }
    //now set
    Direction facing = placer != null ? placer.getDirection() : Direction.NORTH;
    world.setBlock(pos, state.setValue(BlockStateProperties.HORIZONTAL_FACING, facing).setValue(SPEED, speed).setValue(TYPE, type).setValue(COLOUR, col), 2);
    super.setPlacedBy(world, pos, state, placer, stack);
  }
  //  @Override
  //  public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
  //    if (!world.isRemote && entity instanceof ItemEntity && !(entity instanceof ConveyorItemEntity)) {
  //      ItemEntity e = (ItemEntity) entity;
  //      //I wanted to make it a custom entity that will just ride on the conveyor somewhat stationary but it's proving problematic
  //      //ConveyorItemEntity c = new ConveyorItemEntity(world, e.getPosX(), e.getPosY(), e.getPosZ(), e.getItem());
  //      //ItemEntity e2 = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Blocks.PUMPKIN, 1));
  //      //world.addEntity(e2);
  //      //world.addEntity(c);
  //      //c.setThrowerId(e.getThrowerId());
  //      //c.setMotion(e.getMotion());
  //      //c.setNoDespawn();
  //      //c.setDefaultPickupDelay();
  //      //e.setItem(ItemStack.EMPTY);
  //      //e.remove();
  //    }
  //    super.onEntityCollision(state, world, pos, entity);
  //  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(BlockStateProperties.HORIZONTAL_FACING).add(SPEED).add(TYPE).add(COLOUR).add(WATERLOGGED);
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
 
  public static SimpleImmutableEntry<ConveyorType, Direction> nextState(ConveyorType t, Direction d) {
    SimpleImmutableEntry<ConveyorType, Direction> pair = new SimpleImmutableEntry<>(t, d);
    if (STATE_PAIRS.contains(pair)) {
      int index = STATE_PAIRS.indexOf(pair) + 1;
      return nextState(STATE_PAIRS, index);
    }
    return pair;
  }

  public static SimpleImmutableEntry<ConveyorType, Direction> nextConnectedState(ConveyorType t, Direction d) {
    List<SimpleImmutableEntry<ConveyorType, Direction>> connectedStates = STATE_PAIRS.stream().filter(pair -> pair.getValue() == d).collect(Collectors.toList());
    SimpleImmutableEntry<ConveyorType, Direction> pair = new SimpleImmutableEntry<>(t, d);
    if (connectedStates.contains(pair)) {
      int index = connectedStates.indexOf(pair) + 1;
      return nextState(connectedStates, index);
    }
    return pair;
  }

  private static SimpleImmutableEntry<ConveyorType, Direction> nextState(List<SimpleImmutableEntry<ConveyorType, Direction>> list, int index) {
    return list.get(nextIndex(list, index));
  }

  private static int nextIndex(List<SimpleImmutableEntry<ConveyorType, Direction>> list, int index) {
    return index >= list.size() ? index % list.size() : index;
  }

  public static List<SimpleImmutableEntry<ConveyorType, Direction>> generateStatePairs() {
    List<SimpleImmutableEntry<ConveyorType, Direction>> pairs = new LinkedList<>();
    for (ConveyorType t : ConveyorType.values()) {
      for (Direction d : BlockStateProperties.HORIZONTAL_FACING.getPossibleValues()) {
        pairs.add(new SimpleImmutableEntry<>(t, d));
      }
    }
    return pairs;
  }
}
