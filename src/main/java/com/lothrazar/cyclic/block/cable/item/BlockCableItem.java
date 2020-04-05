package com.lothrazar.cyclic.block.cable.item;

import javax.annotation.Nullable;
import com.lothrazar.cyclic.block.cable.CableBase;
import com.lothrazar.cyclic.block.cable.DirectionNullable;
import com.lothrazar.cyclic.block.cable.EnumConnectType;
import com.lothrazar.cyclic.util.UtilItemStack;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class BlockCableItem extends CableBase {

  public BlockCableItem(Properties properties) {
    super(properties.hardnessAndResistance(0.5F));
  }

  private boolean shapeConnects(BlockState state, EnumProperty<EnumConnectType> dirctionProperty) {
    return state.get(dirctionProperty).equals(EnumConnectType.INVENTORY);
  }

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    VoxelShape shape = AABB;
    if (shapeConnects(state, UP)) {
      shape = VoxelShapes.combine(shape, AABB_UP, IBooleanFunction.OR);
    }
    if (shapeConnects(state, DOWN)) {
      shape = VoxelShapes.combine(shape, AABB_DOWN, IBooleanFunction.OR);
    }
    if (state.get(WEST).equals(EnumConnectType.INVENTORY)) {
      shape = VoxelShapes.combine(shape, AABB_WEST, IBooleanFunction.OR);
    }
    if (state.get(EAST).equals(EnumConnectType.INVENTORY)) {
      shape = VoxelShapes.combine(shape, AABB_EAST, IBooleanFunction.OR);
    }
    if (state.get(NORTH).equals(EnumConnectType.INVENTORY)) {
      shape = VoxelShapes.combine(shape, AABB_NORTH, IBooleanFunction.OR);
    }
    if (state.get(SOUTH).equals(EnumConnectType.INVENTORY)) {
      shape = VoxelShapes.combine(shape, AABB_SOUTH, IBooleanFunction.OR);
    }
    return shape;
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
    if (state.getBlock() != newState.getBlock()) {
      TileEntity tileentity = worldIn.getTileEntity(pos);
      for (Direction d : Direction.values()) {
        IItemHandler items = tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, d).orElse(null);
        UtilItemStack.dropAll(items, worldIn, pos);
      }
      worldIn.updateComparatorOutputLevel(pos, this);
      super.onReplaced(state, worldIn, pos, newState, isMoving);
    }
  }
  //  @Override
  //  public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
  //    int calc = 0;
  //    TileEntity tileentity = worldIn.getTileEntity(pos);
  //    if (tileentity != null) {
  //      for (Direction d : Direction.values()) {
  //        IItemHandler items = tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, d).orElse(null);
  //        if (items != null) {
  //          //ok 
  //          if (items.getStackInSlot(0).isEmpty() == false) {
  //            calc += 2;
  //          }
  //        }
  //      }
  //    }
  //    return calc;
  //  }
  //
  //  @Override
  //  public boolean hasComparatorInputOverride(BlockState state) {
  //    return true;
  //  }

  @Nullable
  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileCableItem();
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    super.fillStateContainer(builder);
    builder.add(UP, DOWN, NORTH, EAST, SOUTH, WEST, EXTR);
  }

  @Override
  public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState stateIn, @Nullable LivingEntity placer, ItemStack stack) {
    stateIn = stateIn.with(EXTR, DirectionNullable.NONE);
    for (Direction d : Direction.values()) {
      TileEntity facingTile = worldIn.getTileEntity(pos.offset(d));
      IItemHandler cap = facingTile == null ? null : facingTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
      if (cap != null) {
        stateIn = stateIn.with(FACING_TO_PROPERTY_MAP.get(d), EnumConnectType.INVENTORY);
      }
    }
    worldIn.setBlockState(pos, stateIn);
  }

  @Override
  public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
    EnumProperty<EnumConnectType> property = FACING_TO_PROPERTY_MAP.get(facing);
    if (stateIn.get(property) == EnumConnectType.BLOCKED) {
      return stateIn;
    }
    if (isItem(stateIn, facing, facingState, world, currentPos, facingPos)) {
      return stateIn.with(property, EnumConnectType.INVENTORY);
    }
    else {
      return stateIn.with(property, EnumConnectType.NONE);
    }
  }
}
