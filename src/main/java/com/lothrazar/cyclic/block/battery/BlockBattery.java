package com.lothrazar.cyclic.block.battery;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.util.CapabilityUtil;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.library.cap.EnergyStorageWrapper;
import com.lothrazar.library.util.ItemStackUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;

public class BlockBattery extends BlockCyclic {

  public static final EnumProperty<EnumBatteryPercent> PERCENT = EnumProperty.create("percent", EnumBatteryPercent.class);

  public BlockBattery(Properties properties) {
    super(properties.strength(1.8F));
    this.setHasGui();
    this.registerDefaultState(defaultBlockState().setValue(PERCENT, EnumBatteryPercent.ZERO));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(LIT).add(PERCENT);
  }

  @Override
  public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
    //because harvestBlock manually forces a drop  
    return new ArrayList<>();
  }

  @Override
  public void playerDestroy(Level world, Player player, BlockPos pos, BlockState state, BlockEntity ent, ItemStack stack) {
    super.playerDestroy(world, player, pos, state, ent, stack);
    ItemStack newStackBattery = new ItemStack(this);
    if (ent instanceof TileBattery battery) {
      //write tile energy into the dropped item's energy capability (which persists into CUSTOM_DATA)
      IEnergyStorage newStackCap = CapabilityUtil.energy(newStackBattery);
      if (newStackCap instanceof EnergyStorageWrapper wrapper) {
        wrapper.setEnergy(battery.energy.getEnergyStored());
      }
      else if (newStackCap != null) {
        newStackCap.receiveEnergy(battery.energy.getEnergyStored(), false);
      }
      if (battery.energy.getEnergyStored() > 0) {
        // energy tag sync removed - handled via DataComponents in 1.21.1
      }
    }
    ItemStackUtil.dropItemStackMotionless(world, pos, newStackBattery);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new TileBattery(pos, state);
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
    return createTickerHelper(type, TileRegistry.BATTERY.get(), world.isClientSide ? null: TileBattery::serverTick);
  }

  @Override
  public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
    //read stored energy from the item's capability (which loads from CUSTOM_DATA)
    int current = 0;
    IEnergyStorage storage = CapabilityUtil.energy(stack);
    if (storage != null) {
      current = storage.getEnergyStored();
    }
    else if (stack.has(DataComponents.CUSTOM_DATA) && stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().contains(TileBlockEntityCyclic.NBTENERGY)) {
      current = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getInt(TileBlockEntityCyclic.NBTENERGY);
    }
//    TileBattery container = (TileBattery) world.getBlockEntity(pos);
    if (current > 0 && world.getBlockEntity(pos) instanceof TileBattery tile) {
//      storageTile.setEnergy(current);
      tile.energy.setEnergy(current);
    }
    super.setPlacedBy(world, pos, state, placer, stack);
  }

  @Override
  public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
    if (state.getBlock() != newState.getBlock()) {
      TileBattery tileentity = (TileBattery) worldIn.getBlockEntity(pos);
      if (tileentity != null && tileentity.batterySlots != null) {
        Containers.dropItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), tileentity.batterySlots.getStackInSlot(0));
      }
      worldIn.updateNeighbourForOutputSignal(pos, this);
    }
    super.onRemove(state, worldIn, pos, newState, isMoving);
  }
}
