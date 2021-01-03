package com.lothrazar.cyclic.block.conveyor;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.base.BlockBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockConveyor extends BlockBase {

  protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
  protected static final VoxelShape AG00 = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 0.1D, 16.0D);
  protected static final VoxelShape AG01 = Block.makeCuboidShape(1.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
  protected static final VoxelShape AG02 = Block.makeCuboidShape(2.0D, 1.0D, 0.0D, 16.0D, 2.0D, 16.0D);
  protected static final VoxelShape AG03 = Block.makeCuboidShape(3.0D, 2.0D, 0.0D, 16.0D, 3.0D, 16.0D);
  protected static final VoxelShape AG04 = Block.makeCuboidShape(4.0D, 3.0D, 0.0D, 16.0D, 4.0D, 16.0D);
  protected static final VoxelShape AG05 = Block.makeCuboidShape(5.0D, 4.0D, 0.0D, 16.0D, 5.0D, 16.0D);
  protected static final VoxelShape AG06 = Block.makeCuboidShape(6.0D, 5.0D, 0.0D, 16.0D, 6.0D, 16.0D);
  protected static final VoxelShape AG07 = Block.makeCuboidShape(7.0D, 6.0D, 0.0D, 16.0D, 7.0D, 16.0D);
  protected static final VoxelShape AG08 = Block.makeCuboidShape(8.0D, 7.0D, 0.0D, 16.0D, 8.0D, 16.0D);
  protected static final VoxelShape AG09 = Block.makeCuboidShape(9.0D, 8.0D, 0.0D, 16.0D, 9.0D, 16.0D);
  protected static final VoxelShape AG10 = Block.makeCuboidShape(10.0D, 9.0D, 0.0D, 16.0D, 10.0D, 16.0D);
  protected static final VoxelShape AG11 = Block.makeCuboidShape(11.0D, 10.0D, 0.0D, 16.0D, 11.0D, 16.0D);
  protected static final VoxelShape AG12 = Block.makeCuboidShape(12.0D, 11.0D, 0.0D, 16.0D, 12.0D, 16.0D);
  protected static final VoxelShape AG13 = Block.makeCuboidShape(13.0D, 12.0D, 0.0D, 16.0D, 13.0D, 16.0D);
  protected static final VoxelShape AG14 = Block.makeCuboidShape(14.0D, 13.0D, 0.0D, 16.0D, 14.0D, 16.0D);
  protected static final VoxelShape AG15 = Block.makeCuboidShape(15.0D, 14.0D, 0.0D, 16.0D, 15.0D, 16.0D);
  protected static final VoxelShape ANGLEEAST = VoxelShapes.or(AG00, AG01, AG02, AG03, AG04, AG05, AG06, AG07, AG08, AG09, AG10, AG11, AG12, AG13, AG14, AG15);
  protected static final VoxelShape ANGLESOUTH = VoxelShapes.or(rot(AG00), rot(AG01), rot(AG02), rot(AG03), rot(AG04), rot(AG05), rot(AG06), rot(AG07), rot(AG08), rot(AG09), rot(AG10), rot(AG11), rot(AG12), rot(AG13), rot(AG14), rot(AG15));
  //
  protected static final VoxelShape ANGLENORTH = VoxelShapes.or(flipx(AG00), flipx(AG01), flipx(AG02), flipx(AG03), flipx(AG04), flipx(AG05), flipx(AG06), flipx(AG07), flipx(AG08), flipx(AG09), flipx(AG10), flipx(AG11), flipx(AG12), flipx(AG13), flipx(AG14), flipx(AG15));
  //
  protected static final VoxelShape ANGLEWEST = VoxelShapes.or(flipz(AG00), flipz(AG01), flipz(AG02), flipz(AG03), flipz(AG04), flipz(AG05), flipz(AG06), flipz(AG07), flipz(AG08), flipz(AG09), flipz(AG10), flipz(AG11), flipz(AG12), flipz(AG13), flipz(AG14), flipz(AG15));
  protected static final VoxelShape BOTTOM = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 0.0D, 8.0D, 16.0D);
  protected static final VoxelShape TOP = Block.makeCuboidShape(0.0D, 8.0D, 0.0D, 0.0D, 16.0D, 8.0D);
  protected static final VoxelShape STAIR = VoxelShapes.combine(BOTTOM, TOP, IBooleanFunction.OR);
  public static final EnumProperty<ConveyorType> TYPE = EnumProperty.create("type", ConveyorType.class);
  public static final EnumProperty<ConveyorSpeed> SPEED = EnumProperty.create("speed", ConveyorSpeed.class);
  //javafx.util class doesnt compile into minecraft with ./gradlew build, swapped to SimpleEntry https://www.baeldung.com/java-pairs
  public static final List<SimpleImmutableEntry<ConveyorType, Direction>> STATE_PAIRS = generateStatePairs();

  public enum ConveyorType implements IStringSerializable {

    STRAIGHT, UP, DOWN, CORNER_LEFT, CORNER_RIGHT;

    @Override
    public String getString() {
      return this.name().toLowerCase();
    }

    public boolean isCorner() {
      return this == CORNER_LEFT || this == CORNER_RIGHT;
    }

    public boolean isVertical() {
      return this == UP || this == DOWN;
    }
  }

  public enum ConveyorSpeed implements IStringSerializable {

    SLOWEST, SLOW, MEDIUM, FAST;

    @Override
    public String getString() {
      return this.name().toLowerCase();
    }

    public double getSpeed() {
      switch (this) {
        case SLOWEST:
          return 0.08D;
        case SLOW:
          return 0.12D;
        case MEDIUM:
          return 0.16D;
        case FAST:
          return 0.21D;
      }
      return 0;
    }
  }

  public BlockConveyor(Properties properties) {
    super(properties.notSolid());
  }

  /**
   * Utility methods for block shapes.
   *
   * @author SciWhiz12
   */
  /**
   * Rotates the given {@link VoxelShape} along the horizontal plane according to the given rotation direction.
   *
   * Assumes the given shape is within the bounds of 1 unit on each axis.
   * 
   * https://gist.github.com/sciwhiz12/0852b629e7a3d0200ffc03ec7edab187
   * 
   * @param shape
   *          The shape to rotate
   * @param rotationDir
   *          The rotation direction
   * @return The rotated shape
   */
  public static VoxelShape rot(final VoxelShape shape) {
    double x1 = shape.getStart(Direction.Axis.X), x2 = shape.getEnd(Direction.Axis.X);
    final double y1 = shape.getStart(Direction.Axis.Y), y2 = shape.getEnd(Direction.Axis.Y);
    double z1 = shape.getStart(Direction.Axis.Z), z2 = shape.getEnd(Direction.Axis.Z);
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
    return VoxelShapes.create(x1, y1, z1, x2, y2, z2);
  }

  public static VoxelShape flipx(final VoxelShape shape) {
    double x1 = shape.getStart(Direction.Axis.X);
    double x2 = shape.getEnd(Direction.Axis.X);
    final double y1 = shape.getStart(Direction.Axis.Y);
    final double y2 = shape.getEnd(Direction.Axis.Y);
    double z1 = shape.getStart(Direction.Axis.Z);
    double z2 = shape.getEnd(Direction.Axis.Z);
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
    return VoxelShapes.create(x1, y1, z1, x2, y2, z2);
  }

  public static VoxelShape flipz(final VoxelShape shape) {
    double x1 = shape.getStart(Direction.Axis.X);
    double x2 = shape.getEnd(Direction.Axis.X);
    final double y1 = shape.getStart(Direction.Axis.Y);
    final double y2 = shape.getEnd(Direction.Axis.Y);
    double z1 = shape.getStart(Direction.Axis.Z);
    double z2 = shape.getEnd(Direction.Axis.Z);
    //flip
    //    double temp = z1; // ]
    //    z1 = x1; // ] x1 <-> z1
    //    x1 = temp; // ]
    //    temp = z2; // ]
    //    z2 = x2; // ] x2 <-> z2
    //    x2 = temp; // ]
    //
    x1 = 1 - x1; //  
    x2 = 1 - x2;
    z1 = 1 - z1; //  
    z2 = 1 - z2;
    return VoxelShapes.create(x1, y1, z1, x2, y2, z2);
  }

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    if (state.get(TYPE).isVertical()) {
      Direction facing = state.get(BlockStateProperties.HORIZONTAL_FACING);
      switch (facing) {
        case EAST:
          return ANGLEEAST;//good
        case NORTH:
          return ANGLENORTH;//good
        case SOUTH:
          return ANGLESOUTH;//good
        case WEST:
          return ANGLEWEST;
        case DOWN:
        case UP:
        break;
      }
    }
    return SHAPE;
  }
  //  @Override
  //  public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
  //    return COLLISION_SHAPE;
  //  }

  @Override
  public boolean isTransparent(BlockState state) {
    return isVertical(state);
  }

  @Override
  public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
    return isVertical(state);
  }

  @Override
  public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
    if (hand == Hand.OFF_HAND)
      return ActionResultType.PASS;
    SimpleImmutableEntry<ConveyorType, Direction> nextState = nextConnectedState(state.get(TYPE), state.get(BlockStateProperties.HORIZONTAL_FACING));
    world.setBlockState(pos, state.with(TYPE, nextState.getKey()).with(BlockStateProperties.HORIZONTAL_FACING, nextState.getValue()));
    return super.onBlockActivated(state, world, pos, player, hand, hit);
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileConveyor();
  }

  @Override
  public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
    //decide properties
    ConveyorSpeed speed = ConveyorSpeed.SLOWEST;
    ConveyorType type = ConveyorType.STRAIGHT;
    //TODO: decide types
    //
    //
    Direction facing = placer != null ? placer.getHorizontalFacing() : Direction.NORTH;
    //now set
    worldIn.setBlockState(pos, state.with(BlockStateProperties.HORIZONTAL_FACING, facing).with(SPEED, speed).with(TYPE, type), 2);
    super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
  }

  @Override
  public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
    if (!world.isRemote) {
      // 
      //      ModCyclic.LOGGER.info("crash " + entity);
      //      //
      //      TileConveyor.makeEntitiesTravel(entity, state, pos, world);
    }
    if (!world.isRemote && entity instanceof ItemEntity && !(entity instanceof ConveyorItemEntity)) {
      ItemEntity e = (ItemEntity) entity;
      //I wanted to make it a custom entity that will just ride on the conveyor somewhat stationary but it's proving problematic
      //ConveyorItemEntity c = new ConveyorItemEntity(world, e.getPosX(), e.getPosY(), e.getPosZ(), e.getItem());
      //ItemEntity e2 = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Blocks.PUMPKIN, 1));
      //world.addEntity(e2);
      //world.addEntity(c);
      //c.setThrowerId(e.getThrowerId());
      //c.setMotion(e.getMotion());
      //c.setNoDespawn();
      //c.setDefaultPickupDelay();
      //e.setItem(ItemStack.EMPTY);
      //e.remove();
    }
    super.onEntityCollision(state, world, pos, entity);
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(BlockStateProperties.HORIZONTAL_FACING).add(SPEED).add(TYPE);
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
      for (Direction d : BlockStateProperties.HORIZONTAL_FACING.getAllowedValues()) {
        pairs.add(new SimpleImmutableEntry<>(t, d));
      }
    }
    return pairs;
  }

  public static boolean isVertical(BlockState state) {
    return state.get(TYPE).isVertical();
  }
}
