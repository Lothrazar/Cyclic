package com.lothrazar.cyclic.block.battery;

import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.capability.EnergyCapabilityItemStack;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class ItemBlockBattery extends BlockItem {

  public ItemBlockBattery(Block blockIn, Properties builder) {
    super(blockIn, builder);
  }

  @Override
  public boolean showDurabilityBar(ItemStack stack) {
    //    IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
    return stack.hasTag() && stack.getTag().contains(CustomEnergyStorage.NBTENERGY); //storage != null && storage.getEnergyStored() > 0;
  }

  @Override
  public int getRGBDurabilityForDisplay(ItemStack stack) {
    return 0xBC000C;
  }

  @Override
  public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    int current = 0;
    IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
    if (stack.hasTag() && stack.getTag().contains(CustomEnergyStorage.NBTENERGY)) {
      current = stack.getTag().getInt(CustomEnergyStorage.NBTENERGY);
    }
    else if (storage != null) {
      current = storage.getEnergyStored();
    }
    tooltip.add(new TranslationTextComponent(current + "/" + TileBattery.MAX).mergeStyle(TextFormatting.RED));
    super.addInformation(stack, worldIn, tooltip, flagIn);
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
    double current = 0;
    IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
    if (stack.hasTag() && stack.getTag().contains(CustomEnergyStorage.NBTENERGY)) {
      current = stack.getTag().getInt(CustomEnergyStorage.NBTENERGY);
    }
    else if (storage != null) {
      current = storage.getEnergyStored();
    }
    double max = TileBattery.MAX;
    return 1.0 - current / max;
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
    return new EnergyCapabilityItemStack(stack, TileBattery.MAX);
  }
}
