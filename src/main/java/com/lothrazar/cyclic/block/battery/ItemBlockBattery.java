package com.lothrazar.cyclic.block.battery;

import com.lothrazar.cyclic.capability.EnergyCapabilityItemStack;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

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
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
    if (storage != null) {
      TranslationTextComponent t = new TranslationTextComponent(storage.getEnergyStored() + "/" + storage.getMaxEnergyStored());
      t.mergeStyle(TextFormatting.RED);
      tooltip.add(t);
    }
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
    IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
    if (storage == null) {
      return 0;
    }
    double energy = storage.getEnergyStored();
    return 1 - energy / storage.getMaxEnergyStored();
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
    return new EnergyCapabilityItemStack(stack, TileBattery.MAX);
  }
}
