package com.lothrazar.cyclic.block.battery;

import com.lothrazar.cyclic.capability.CapabilityProviderEnergyStack;
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

  public static final String ENERGYTTMAX = "energyttmax";
  public static final String ENERGYTT = "energytt";

  public ItemBlockBattery(Block blockIn, Properties builder) {
    super(blockIn, builder);
  }

  @Override
  public boolean showDurabilityBar(ItemStack stack) {
    return stack.hasTag() && stack.getTag().contains(ENERGYTT);
  }

  @Override
  public int getRGBDurabilityForDisplay(ItemStack stack) {
    return 0xBC000C;
  }

  @Override
  public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    if (stack.hasTag() && stack.getTag().contains(ENERGYTT)) {
      int current = stack.getTag().getInt(ENERGYTT);
      int energyttmax = stack.getTag().getInt(ENERGYTTMAX);
      tooltip.add(new TranslationTextComponent(current + "/" + energyttmax).mergeStyle(TextFormatting.RED));
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
    double current = 0;
    if (stack.hasTag() && stack.getTag().contains(ENERGYTT)) {
      current = stack.getTag().getInt(ENERGYTT);
    }
    double max = Math.max(1D, stack.getTag().getInt(ENERGYTTMAX));
    return 1D - current / max;
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
    return new CapabilityProviderEnergyStack(TileBattery.MAX);
  }

  // ShareTag for server->client capability data sync
  @Override
  public CompoundNBT getShareTag(ItemStack stack) {
    CompoundNBT nbt = stack.getOrCreateTag();
    IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
    //on server  this runs . also has correct values.
    //set data for sync to client
    if (storage != null) {
      nbt.putInt(ENERGYTT, storage.getEnergyStored());
      nbt.putInt(ENERGYTTMAX, storage.getMaxEnergyStored());
    }
    return nbt;
  }

  //clientside read tt
  @Override
  public void readShareTag(ItemStack stack, CompoundNBT nbt) {
    if (nbt != null) {
      CompoundNBT stackTag = stack.getOrCreateTag();
      stackTag.putInt(ENERGYTT, nbt.getInt(ENERGYTT));
      stackTag.putInt(ENERGYTTMAX, nbt.getInt(ENERGYTTMAX));
    }
    super.readShareTag(stack, nbt);
  }
}
