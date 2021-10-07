package com.lothrazar.cyclic.block.battery;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockBattery extends BlockBase {

  public static final EnumProperty<EnumBatteryPercent> PERCENT = EnumProperty.create("percent", EnumBatteryPercent.class);

  public BlockBattery(Properties properties) {
    super(properties.strength(1.8F));
    this.setHasGui();
    this.registerDefaultState(defaultBlockState().setValue(PERCENT, EnumBatteryPercent.ZERO));
  }

  @Override
  public void registerClient() {
    MenuScreens.register(ContainerScreenRegistry.batteryCont, ScreenBattery::new);
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
    //    worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());//is this needed???
    ItemStack battery = new ItemStack(this);
    if (ent != null) {
      IEnergyStorage handlerHere = ent.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
      IEnergyStorage storage = battery.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
      if (storage != null) {
        ((CustomEnergyStorage) storage).setEnergy(handlerHere.getEnergyStored());
      }
    }
    //even if energy fails 
    if (world.isClientSide == false) {
      world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), battery));
    }
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos,BlockState state) {
    return new TileBattery(pos,state);
  }

  @Override
  public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
    try {
      IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
      if (storage != null) {
        TileBattery container = (TileBattery) world.getBlockEntity(pos);
        CustomEnergyStorage storageTile = (CustomEnergyStorage) container.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
        if (storageTile != null) {
          storageTile.setEnergy(storage.getEnergyStored());
        }
      }
    }
    catch (Exception e) {
      //
    }
  }


}
