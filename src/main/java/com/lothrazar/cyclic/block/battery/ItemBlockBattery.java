package com.lothrazar.cyclic.block.battery;

import java.util.List;
import com.lothrazar.cyclic.capabilities.CapabilityProviderEnergyStack;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
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
  public boolean isBarVisible(ItemStack stack) {
    return stack.hasTag() && stack.getTag().contains(ENERGYTT);
  }

  @Override
  public int getBarColor(ItemStack stack) {
    return 0xBC000C;
  }

  @Override
  public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    if (stack.hasTag() && stack.getTag().contains(ENERGYTT)) {
      int current = stack.getTag().getInt(ENERGYTT);
      int energyttmax = stack.getTag().getInt(ENERGYTTMAX);
      tooltip.add(new TranslatableComponent(current + "/" + energyttmax).withStyle(ChatFormatting.RED));
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
//  @Override
//  public double getDurabilityForDisplay(ItemStack stack) {
//    double current = 0;
//    if (stack.hasTag() && stack.getTag().contains(ENERGYTT)) {
//      current = stack.getTag().getInt(ENERGYTT);
//    }
//    double max = Math.max(1D, stack.getTag().getInt(ENERGYTTMAX));
//    return 1D - current / max;
//  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
    return new CapabilityProviderEnergyStack(TileBattery.MAX);
  }

  // ShareTag for server->client capability data sync
  @Override
  public CompoundTag getShareTag(ItemStack stack) {
    CompoundTag nbt = stack.getOrCreateTag();
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
  public void readShareTag(ItemStack stack, CompoundTag nbt) {
    if (nbt != null) {
      CompoundTag stackTag = stack.getOrCreateTag();
      stackTag.putInt(ENERGYTT, nbt.getInt(ENERGYTT));
      stackTag.putInt(ENERGYTTMAX, nbt.getInt(ENERGYTTMAX));
    }
    super.readShareTag(stack, nbt);
  }
}
