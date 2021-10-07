package com.lothrazar.cyclic.block.battery;

import com.lothrazar.cyclic.capability.EnergyCapabilityItemStack;
import java.util.List;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import net.minecraft.world.item.Item.Properties;

public class ItemBlockBattery extends BlockItem {

  public ItemBlockBattery(Block blockIn, Properties builder) {
    super(blockIn, builder);
  }

  @Override
  public boolean showDurabilityBar(ItemStack stack) {
    IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
    return storage != null && storage.getEnergyStored() > 0;
  }

  @Override
  public int getRGBDurabilityForDisplay(ItemStack stack) {
    return 0xBC000C;
  }

  @Override
  public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
    if (storage != null) {
      TranslatableComponent t = new TranslatableComponent(storage.getEnergyStored() + "/" + storage.getMaxEnergyStored());
      t.withStyle(ChatFormatting.RED);
      tooltip.add(t);
    }
    super.appendHoverText(stack, worldIn, tooltip, flagIn);
  }

  /**
   * Queries the percentage of the 'Durability' bar that should be drawn.
   *
   * @param stack
   *          The current ItemStack
   * @return 0.0 for 100% (no damage / full bar), 1.0 for 0% (fully damaged / empty bar)
   */
  @Override
  public double getDurabilityForDisplay(ItemStack stack) {
    IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
    if (storage == null) {
      return 0;
    }
    double energy = storage.getEnergyStored();
    return 1 - energy / storage.getMaxEnergyStored();
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
    return new EnergyCapabilityItemStack(stack, TileBattery.MAX);
  }
}
