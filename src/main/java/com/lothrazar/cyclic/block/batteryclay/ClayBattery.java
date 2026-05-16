package com.lothrazar.cyclic.block.batteryclay;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.fixers.CapabilityUtil;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.library.cap.EnergyStorageWrapper;
import com.lothrazar.library.util.ItemStackUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;

public class ClayBattery extends BlockCyclic {

  public ClayBattery(Properties properties) {
    super(properties.strength(1.8F));
    this.setHasGui();
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
    if (ent instanceof TileClayBattery battery) {
      IEnergyStorage newStackCap = CapabilityUtil.energy(newStackBattery);// newStackBattery.getCapability(ForgeCapabilities.ENERGY, null).orElse(null);
      if (newStackCap instanceof EnergyStorageWrapper) {
        ((EnergyStorageWrapper) newStackCap).setEnergy(battery.energy.getEnergyStored());
      }
      else {
        newStackCap.receiveEnergy(battery.energy.getEnergyStored(), false);
      }
      if (battery.energy.getEnergyStored() > 0) {
        // energy tag sync disabled
      }
    }
    //even if energy fails 
    ItemStackUtil.dropItemStackMotionless(world, pos, newStackBattery);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new TileClayBattery(pos, state);
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
    return createTickerHelper(type, TileRegistry.BATTERY_CLAY.get(), world.isClientSide ? TileClayBattery::clientTick : TileClayBattery::serverTick);
  }

  @Override
  public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
    int current = 0;
    IEnergyStorage storage = CapabilityUtil.energy(stack);
    if (stack.has(DataComponents.CUSTOM_DATA) && stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().contains(TileBlockEntityCyclic.NBTENERGY)) {
      current = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getInt(TileBlockEntityCyclic.NBTENERGY);
    }
    else if (storage != null) {
      current = storage.getEnergyStored();
    }
    TileClayBattery container = (TileClayBattery) world.getBlockEntity(pos);
    EnergyStorageWrapper storageTile = null; // ForgeCapabilities removed
    if (storageTile != null) {
      storageTile.setEnergy(current);
    }
    super.setPlacedBy(world, pos, state, placer, stack);
  }

  @Override
  public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
    if (state.getBlock() != newState.getBlock()) {
      worldIn.updateNeighbourForOutputSignal(pos, this);
    }
    super.onRemove(state, worldIn, pos, newState, isMoving);
  }
}
