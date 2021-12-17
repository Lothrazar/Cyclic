package com.lothrazar.cyclic.block.battery;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import java.util.Collections;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class BlockBattery extends BlockBase {

  public static final EnumProperty<EnumBatteryPercent> PERCENT = EnumProperty.create("percent", EnumBatteryPercent.class);

  public BlockBattery(Properties properties) {
    super(properties.hardnessAndResistance(1.8F));
    this.setHasGui();
    this.setDefaultState(getDefaultState().with(PERCENT, EnumBatteryPercent.ZERO));
  }

  @Override
  public void registerClient() {
    ScreenManager.registerFactory(ContainerScreenRegistry.batteryCont, ScreenBattery::new);
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(LIT).add(PERCENT);
  }

  @Override
  public List<ItemStack> getDrops(BlockState state, net.minecraft.loot.LootContext.Builder builder) {
    //because harvestBlock manually forces a drop  
    return Collections.emptyList();
  }

  @Override
  public void harvestBlock(World world, PlayerEntity player, BlockPos pos, BlockState state, TileEntity ent, ItemStack stack) {
    final ItemStack newStackBattery = new ItemStack(state.getBlock());
    if (ent instanceof TileBattery) {
      final TileBattery tileBattery = (TileBattery) ent;
      final IEnergyStorage energyStorage = tileBattery.getEnergyStorage();
      IEnergyStorage newStackCap = newStackBattery.getCapability(CapabilityEnergy.ENERGY, null).resolve().orElse(null);
      if (newStackCap instanceof CustomEnergyStorage) {
        ((CustomEnergyStorage) newStackCap).setEnergy(energyStorage.getEnergyStored());
      }
      else if (newStackCap != null) {
        newStackCap.receiveEnergy(energyStorage.getEnergyStored(), false);
      }
      if (energyStorage.getEnergyStored() > 0) {
        newStackBattery.getOrCreateTag().putInt(ItemBlockBattery.ENERGYTT, energyStorage.getEnergyStored());
        newStackBattery.getOrCreateTag().putInt(ItemBlockBattery.ENERGYTTMAX, energyStorage.getMaxEnergyStored());
      }
    }
    //Hack as getDrops returns an empty list
    if (!world.isRemote) {
      world.addEntity(new ItemEntity(world, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, newStackBattery));
    }
    super.harvestBlock(world, player, pos, state, ent, newStackBattery);
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileBattery();
  }

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
    int current = 0;
    IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
    if (stack.hasTag() && stack.getTag().contains(CustomEnergyStorage.NBTENERGY)) {
      current = stack.getTag().getInt(CustomEnergyStorage.NBTENERGY);
    }
    else if (storage != null) {
      current = storage.getEnergyStored();
    }
    TileBattery container = (TileBattery) world.getTileEntity(pos);
    CustomEnergyStorage storageTile = (CustomEnergyStorage) container.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
    if (storageTile != null) {
      storageTile.setEnergy(current);
    }
    super.onBlockPlacedBy(world, pos, state, placer, stack);
  }

  @Override
  public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
    if (state.getBlock() != newState.getBlock()) {
      TileBattery tileentity = (TileBattery) worldIn.getTileEntity(pos);
      if (tileentity != null && tileentity.batterySlots != null) {
        InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), tileentity.batterySlots.getStackInSlot(0));
      }
      worldIn.updateComparatorOutputLevel(pos, this);
    }
    super.onReplaced(state, worldIn, pos, newState, isMoving);
  }
}
