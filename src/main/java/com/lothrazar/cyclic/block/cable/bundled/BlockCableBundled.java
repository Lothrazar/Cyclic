package com.lothrazar.cyclic.block.cable.bundled;

import com.lothrazar.cyclic.block.cable.CableBase;
import com.lothrazar.cyclic.block.cable.EnumConnectType;
import com.lothrazar.library.data.ShapeCache;
import com.lothrazar.cyclic.config.ConfigRegistry;
import com.lothrazar.cyclic.util.CapabilityUtil;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.util.RandomSource;

public class BlockCableBundled extends CableBase {

  public BlockCableBundled(Properties properties) {
    super(properties.strength(0.5F));
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
    boolean facadesEnabled = false;
    try {
      facadesEnabled = ConfigRegistry.CABLE_FACADES.get();
    } catch (Exception e) {}
    if (facadesEnabled) {
      VoxelShape facade = this.getFacadeShape(state, worldIn, pos, context);
      if (facade != null) {
        return facade;
      }
    }
    return ShapeCache.getOrCreate(state, CableBase::createShape);
  }

  @Override
  protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel worldIn, BlockPos pos, boolean movedByPiston) {
    if (true) {
      TileCableBundled tile = (TileCableBundled) worldIn.getBlockEntity(pos);
      if (tile != null) {
        // drop filterz
        if (tile.itemFilter != null) {
          Containers.dropItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), tile.itemFilter.getStackInSlot(0));
        }
        if (tile.fluidFilter != null) {
          Containers.dropItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), tile.fluidFilter.getStackInSlot(0));
        }
      }
      worldIn.updateNeighbourForOutputSignal(pos, this);
    }
    super.affectNeighborsAfterRemoval(state, worldIn, pos, movedByPiston);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new TileCableBundled(pos, state);
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
    return createTickerHelper(type, TileRegistry.BUNDLED_PIPE.get(), world.isClientSide() ? null : TileCableBundled::serverTick);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(UP, DOWN, NORTH, EAST, SOUTH, WEST);
  }

  @Override
  public void setPlacedBy(Level worldIn, BlockPos pos, BlockState stateIn, LivingEntity placer, ItemStack stack) {
    for (Direction d : Direction.values()) {
      if (CapabilityUtil.energy(worldIn, pos.relative(d)) != null
          || CapabilityUtil.item(worldIn, pos.relative(d), d.getOpposite()) != null
          || CapabilityUtil.fluid(worldIn, pos.relative(d), d.getOpposite()) != null) {
        stateIn = stateIn.setValue(FACING_TO_PROPERTY_MAP.get(d), EnumConnectType.INVENTORY);
        worldIn.setBlockAndUpdate(pos, stateIn);
      }
    }
    super.setPlacedBy(worldIn, pos, stateIn, placer, stack);
  }

  @Override
  public BlockState updateShape(BlockState stateIn, LevelReader world, ScheduledTickAccess ticks, BlockPos currentPos,
      Direction facing, BlockPos facingPos, BlockState facingState, RandomSource random) {
    EnumProperty<EnumConnectType> property = FACING_TO_PROPERTY_MAP.get(facing);
    EnumConnectType oldProp = stateIn.getValue(property);
    if (oldProp.isBlocked() || oldProp.isExtraction()) {
      return stateIn;
    }
    boolean hasAnyCap = world instanceof Level lvl
        && (CapabilityUtil.isEnergy(facing, lvl, facingPos)
            || CapabilityUtil.isItem(facing, lvl, facingPos)
            || CapabilityUtil.isFluid(facing, lvl, facingPos));
    if (hasAnyCap) {
      BlockState with = stateIn.setValue(property, EnumConnectType.INVENTORY);
      if (world.getBlockState(currentPos).getBlock() == this && world instanceof Level lvl) {
        lvl.setBlockAndUpdate(currentPos, with);
      }
      return with;
    } else {
      return stateIn.setValue(property, EnumConnectType.NONE);
    }
  }
}
