package com.lothrazar.cyclic.block.battery;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.capabilities.CustomEnergyStorage;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
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
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class BlockBattery extends BlockCyclic {

  public static final EnumProperty<EnumBatteryPercent> PERCENT = EnumProperty.create("percent", EnumBatteryPercent.class);

  public BlockBattery(Properties properties) {
    super(properties.strength(1.8F));
    this.setHasGui();
    this.registerDefaultState(defaultBlockState().setValue(PERCENT, EnumBatteryPercent.ZERO));
  }

  @Override
  public void registerClient() {
    MenuScreens.register(ContainerScreenRegistry.BATTERY, ScreenBattery::new);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(LIT).add(PERCENT);
  }

  @Override
  public List<ItemStack> getDrops(BlockState state, net.minecraft.world.level.storage.loot.LootContext.Builder builder) {
    //because harvestBlock manually forces a drop  
    return new ArrayList<>();
  }

  @Override
  public void playerDestroy(Level world, Player player, BlockPos pos, BlockState state, BlockEntity ent, ItemStack stack) {
    super.playerDestroy(world, player, pos, state, ent, stack);
    ItemStack newStackBattery = new ItemStack(this);
    if (ent instanceof TileBattery battery) {
      IEnergyStorage newStackCap = newStackBattery.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
      if (newStackCap instanceof CustomEnergyStorage) {
        ((CustomEnergyStorage) newStackCap).setEnergy(battery.energy.getEnergyStored());
      }
      else {
        newStackCap.receiveEnergy(battery.energy.getEnergyStored(), false);
      }
      if (battery.energy.getEnergyStored() > 0) {
        newStackBattery.getOrCreateTag().putInt(ItemBlockBattery.ENERGYTT, battery.energy.getEnergyStored());
        newStackBattery.getOrCreateTag().putInt(ItemBlockBattery.ENERGYTTMAX, battery.energy.getMaxEnergyStored());
      }
    }
    //even if energy fails 
    if (world.isClientSide == false) {
      world.addFreshEntity(new ItemEntity(world, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, newStackBattery));
    }
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new TileBattery(pos, state);
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
    return createTickerHelper(type, TileRegistry.BATTERY.get(), world.isClientSide ? TileBattery::clientTick : TileBattery::serverTick);
  }

  @Override
  public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
    int current = 0;
    IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
    if (stack.hasTag() && stack.getTag().contains(CustomEnergyStorage.NBTENERGY)) {
      current = stack.getTag().getInt(CustomEnergyStorage.NBTENERGY);
    }
    else if (storage != null) {
      current = storage.getEnergyStored();
    }
    TileBattery container = (TileBattery) world.getBlockEntity(pos);
    CustomEnergyStorage storageTile = (CustomEnergyStorage) container.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
    if (storageTile != null) {
      storageTile.setEnergy(current);
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
