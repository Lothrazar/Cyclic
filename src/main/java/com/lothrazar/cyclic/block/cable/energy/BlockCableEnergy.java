package com.lothrazar.cyclic.block.cable.energy;

import com.lothrazar.cyclic.block.cable.CableBase;
import com.lothrazar.cyclic.block.cable.EnumConnectType;
import com.lothrazar.cyclic.block.cable.ShapeCache;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockCableEnergy extends CableBase {

  public BlockCableEnergy(Properties properties) {
    super(properties.strength(0.5F));
  }

  @Override
  public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
    if (world.isClientSide) {
      BlockEntity ent = world.getBlockEntity(pos);
      IEnergyStorage handlerHere = ent.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
      //show current
      if (handlerHere != null) {
        int st = handlerHere.getEnergyStored();
        if (st > 0) {
          player.displayClientMessage(new TranslatableComponent(st + ""), true);
        }
      }
    }
    return super.use(state, world, pos, player, hand, hit);
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
    return ShapeCache.getOrCreate(state, CableBase::createShape);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos,BlockState state) {
    return new TileCableEnergy(pos,state);
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
    return createTickerHelper(type, TileRegistry.energy_pipeTile, world.isClientSide ? TileCableEnergy::clientTick : TileCableEnergy::serverTick);
  }
  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(UP, DOWN, NORTH, EAST, SOUTH, WEST);
  }

  @Override
  public void setPlacedBy(Level worldIn, BlockPos pos, BlockState stateIn, LivingEntity placer, ItemStack stack) {
    for (Direction d : Direction.values()) {
      BlockEntity facingTile = worldIn.getBlockEntity(pos.relative(d));
      IEnergyStorage energy = facingTile == null ? null : facingTile.getCapability(CapabilityEnergy.ENERGY, d.getOpposite()).orElse(null);
      if (energy != null) {
        stateIn = stateIn.setValue(FACING_TO_PROPERTY_MAP.get(d), EnumConnectType.INVENTORY);
        worldIn.setBlockAndUpdate(pos, stateIn);
      }
    }
  }

  @Override
  public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
    EnumProperty<EnumConnectType> property = FACING_TO_PROPERTY_MAP.get(facing);
    EnumConnectType oldProp = stateIn.getValue(property);
    if (oldProp.isBlocked() || oldProp.isExtraction()) {
      return stateIn;
    }
    if (isEnergy(stateIn, facing, facingState, world, currentPos, facingPos)) {
      BlockState with = stateIn.setValue(property, EnumConnectType.INVENTORY);
      if (world instanceof Level) {
        //hack to force {any} -> inventory IF its here
        ((Level) world).setBlockAndUpdate(currentPos, with);
      }
      return with;
    }
    else {
      return stateIn.setValue(property, EnumConnectType.NONE);
    }
  }
}
