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
  protected static final VoxelShape ANGLE = VoxelShapes.or(AG01, AG02, AG03, AG04, AG05, AG06, AG07, AG08, AG09, AG10, AG11, AG12, AG13, AG14, AG15);
  //  protected static final VoxelShape COLLISION_SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.1D, 16.0D);
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

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    if (state.get(TYPE).isVertical())
      return ANGLE;
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
