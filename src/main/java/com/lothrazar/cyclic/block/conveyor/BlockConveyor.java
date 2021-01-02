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

  protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
  protected static final VoxelShape BOTTOM = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 0.0D, 8.0D, 16.0D);
  protected static final VoxelShape TOP = Block.makeCuboidShape(0.0D, 8.0D, 0.0D, 0.0D, 16.0D, 8.0D);
  protected static final VoxelShape STAIR = VoxelShapes.combine(BOTTOM, TOP, IBooleanFunction.OR);
  public static final EnumProperty<Type> TYPE = EnumProperty.create("type", Type.class);
  public static final EnumProperty<Speed> SPEED = EnumProperty.create("speed", Speed.class);
  //javafx.util class doesnt compile into minecraft with ./gradlew build, swapped to SimpleEntry https://www.baeldung.com/java-pairs
  public static final List<SimpleImmutableEntry<Type, Direction>> STATE_PAIRS = generateStatePairs();

  public enum Type implements IStringSerializable {

    STRAIGHT, UP, DOWN, CORNER_LEFT, CORNER_RIGHT;

    @Override
    public String getString() {
      return this.name().toLowerCase();
    }
  }

  public enum Speed implements IStringSerializable {

    SLOWEST, SLOW;//, MEDIUM, FAST;

    @Override
    public String getString() {
      return this.name().toLowerCase();
    }
  }

  public BlockConveyor(Properties properties) {
    super(properties.notSolid());
  }

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    return SHAPE;
  }

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
    SimpleImmutableEntry<Type, Direction> nextState = nextConnectedState(state.get(TYPE), state.get(BlockStateProperties.HORIZONTAL_FACING));
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
    Speed speed = Speed.SLOWEST;
    Type type = Type.STRAIGHT;
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

  public static SimpleImmutableEntry<Type, Direction> nextState(Type t, Direction d) {
    SimpleImmutableEntry<Type, Direction> pair = new SimpleImmutableEntry<>(t, d);
    if (STATE_PAIRS.contains(pair)) {
      int index = STATE_PAIRS.indexOf(pair) + 1;
      return nextState(STATE_PAIRS, index);
    }
    return pair;
  }

  public static SimpleImmutableEntry<Type, Direction> nextConnectedState(Type t, Direction d) {
    List<SimpleImmutableEntry<Type, Direction>> connectedStates = STATE_PAIRS.stream().filter(pair -> pair.getValue() == d).collect(Collectors.toList());
    SimpleImmutableEntry<Type, Direction> pair = new SimpleImmutableEntry<>(t, d);
    if (connectedStates.contains(pair)) {
      int index = connectedStates.indexOf(pair) + 1;
      return nextState(connectedStates, index);
    }
    return pair;
  }

  private static SimpleImmutableEntry<Type, Direction> nextState(List<SimpleImmutableEntry<Type, Direction>> list, int index) {
    return list.get(nextIndex(list, index));
  }

  private static int nextIndex(List<SimpleImmutableEntry<Type, Direction>> list, int index) {
    return index >= list.size() ? index % list.size() : index;
  }

  public static List<SimpleImmutableEntry<Type, Direction>> generateStatePairs() {
    List<SimpleImmutableEntry<Type, Direction>> pairs = new LinkedList<>();
    for (Type t : Type.values()) {
      for (Direction d : BlockStateProperties.HORIZONTAL_FACING.getAllowedValues()) {
        pairs.add(new SimpleImmutableEntry<>(t, d));
      }
    }
    return pairs;
  }

  public static boolean isVertical(BlockState state) {
    return state.get(TYPE) == Type.UP || state.get(TYPE) == Type.DOWN;
  }
}
