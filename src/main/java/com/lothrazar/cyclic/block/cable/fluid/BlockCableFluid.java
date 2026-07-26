package com.lothrazar.cyclic.block.cable.fluid;

import java.util.List;
import com.lothrazar.cyclic.block.cable.CableBase;
import com.lothrazar.cyclic.block.cable.EnumConnectType;
import com.lothrazar.library.data.ShapeCache;
import com.lothrazar.cyclic.config.ConfigRegistry;
import com.lothrazar.cyclic.util.CapabilityUtil;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
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
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class BlockCableFluid extends CableBase {

  public BlockCableFluid(Properties properties) {
    super(properties.strength(0.5F));
  }

  @Override
  public void appendHoverText(ItemStack stack, Item.TooltipContext worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    super.appendHoverText(stack, worldIn, tooltip, flagIn);
    if (Screen.hasShiftDown()) {
      tooltip.add(Component.translatable("block.cyclic.fluid_pipe.tooltip0").withStyle(ChatFormatting.GRAY));
    }
    else {
      tooltip.add(Component.translatable("item.cyclic.shift").withStyle(ChatFormatting.DARK_GRAY));
    }
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
    boolean facadesEnabled = false;
    try { facadesEnabled = ConfigRegistry.CABLE_FACADES.get(); } catch (Exception e) {}
    if (facadesEnabled) {
      var facade = this.getFacadeShape(state, worldIn, pos, context);
      if (facade != null) {
        return facade;
      }
    }
    return ShapeCache.getOrCreate(state, CableBase::createShape);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new TileCableFluid(pos, state);
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
    return createTickerHelper(type, TileRegistry.FLUID_PIPE.get(), world.isClientSide() ? null : TileCableFluid::serverTick);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(UP, DOWN, NORTH, EAST, SOUTH, WEST);
  }

  @Override
  public void setPlacedBy(Level worldIn, BlockPos pos, BlockState stateIn, LivingEntity placer, ItemStack stack) {
    for (Direction d : Direction.values()) {
//      BlockEntity facingTile = worldIn.getBlockEntity(pos.relative(d));
      IFluidHandler cap = CapabilityUtil.fluid(worldIn,pos.relative(d),d.getOpposite()); //facingTile == null ? null : facingTile.getCapability(ForgeCapabilities.FLUID_HANDLER, d.getOpposite()).orElse(null);
      if (cap != null) {
        stateIn = stateIn.setValue(FACING_TO_PROPERTY_MAP.get(d), EnumConnectType.INVENTORY);
        worldIn.setBlockAndUpdate(pos, stateIn);
      }
    }
    super.setPlacedBy(worldIn, pos, stateIn, placer, stack);
  }

  @Override
  protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel worldIn, BlockPos pos, boolean movedByPiston) {
    if (true) {
      TileCableFluid tileentity = (TileCableFluid) worldIn.getBlockEntity(pos);
      if (tileentity != null && tileentity.fluidFilter != null) {
        Containers.dropItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), tileentity.fluidFilter.getStackInSlot(0));
      }
      worldIn.updateNeighbourForOutputSignal(pos, this);
    }
    super.affectNeighborsAfterRemoval(state, worldIn, pos, movedByPiston);
  }

  @Override
  public BlockState updateShape(BlockState stateIn, LevelReader world, ScheduledTickAccess ticks, BlockPos currentPos, Direction facing, BlockPos facingPos, BlockState facingState, RandomSource random) {
    EnumProperty<EnumConnectType> property = FACING_TO_PROPERTY_MAP.get(facing);
    EnumConnectType oldProp = stateIn.getValue(property);
    if (oldProp.isBlocked() || oldProp.isExtraction()) {
      //  updateConnection(world, currentPos, facing, oldProp);
      return stateIn;
    }
    if (CapabilityUtil.isFluid( facing,  (Level)world, facingPos)) {
      BlockState with = stateIn.setValue(property, EnumConnectType.INVENTORY);
      if (world instanceof Level lvl && world.getBlockState(currentPos).getBlock() == this) {
        //hack to force {any} -> inventory IF its here
        lvl.setBlockAndUpdate(currentPos, with);
      }
      return with;
    }
    else {
      return stateIn.setValue(property, EnumConnectType.NONE);
    }
  }
}
