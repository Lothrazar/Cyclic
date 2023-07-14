package com.lothrazar.cyclic.block.batteryclay;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.library.cap.CustomEnergyStorage;
import com.lothrazar.cyclic.registry.MenuTypeRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.library.util.ItemStackUtil;
import net.minecraft.client.gui.screens.MenuScreens;
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
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;

public class ClayBattery extends BlockCyclic {

  public ClayBattery(Properties properties) {
    super(properties.strength(1.8F));
    this.setHasGui();
  }

  @Override
  public void registerClient() {
    MenuScreens.register(MenuTypeRegistry.BATTERY_CLAY.get(), ScreenClayBattery::new);
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
      IEnergyStorage newStackCap = newStackBattery.getCapability(ForgeCapabilities.ENERGY, null).orElse(null);
      if (newStackCap instanceof CustomEnergyStorage) {
        ((CustomEnergyStorage) newStackCap).setEnergy(battery.energy.getEnergyStored());
      }
      else {
        newStackCap.receiveEnergy(battery.energy.getEnergyStored(), false);
      }
      if (battery.energy.getEnergyStored() > 0) {
        newStackBattery.getOrCreateTag().putInt(ItemBlockClayBattery.ENERGYTT, battery.energy.getEnergyStored());
        newStackBattery.getOrCreateTag().putInt(ItemBlockClayBattery.ENERGYTTMAX, battery.energy.getMaxEnergyStored());
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
    IEnergyStorage storage = stack.getCapability(ForgeCapabilities.ENERGY, null).orElse(null);
    if (stack.hasTag() && stack.getTag().contains(CustomEnergyStorage.NBTENERGY)) {
      current = stack.getTag().getInt(CustomEnergyStorage.NBTENERGY);
    }
    else if (storage != null) {
      current = storage.getEnergyStored();
    }
    TileClayBattery container = (TileClayBattery) world.getBlockEntity(pos);
    CustomEnergyStorage storageTile = (CustomEnergyStorage) container.getCapability(ForgeCapabilities.ENERGY, null).orElse(null);
    if (storageTile != null) {
      storageTile.setEnergy(current);
    }
    super.setPlacedBy(world, pos, state, placer, stack);
  }
  //
  //  @Override
  //  public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
  //    if (state.getBlock() != newState.getBlock()) {
  //      TileClayBattery tileentity = (TileClayBattery) worldIn.getBlockEntity(pos);
  //      //      if (tileentity != null && tileentity.batterySlots != null) {
  //      //        Containers.dropItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), tileentity.batterySlots.getStackInSlot(0));
  //      //      }
  //      worldIn.updateNeighbourForOutputSignal(pos, this);
  //    }
  //    super.onRemove(state, worldIn, pos, newState, isMoving);
  //  }
}
