package com.lothrazar.cyclic.block.battery;

import java.util.List;
import com.lothrazar.cyclic.capabilities.item.CapabilityProviderEnergyStack;
import com.lothrazar.cyclic.registry.TextureRegistry;
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
    IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
    return storage != null && storage.getEnergyStored() > 0;
  }

  @Override
  public int getBarWidth(ItemStack stack) {
    float current = 0;
    float max = 0;
    IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
    if (storage != null) {
      current = storage.getEnergyStored();
      max = storage.getMaxEnergyStored();
    }
    else if (stack.hasTag() && stack.getTag().contains(ENERGYTT)) {
      //TODO 1.19 port delete this branch
      current = stack.getTag().getInt(ENERGYTT);
      max = stack.getTag().getInt(ENERGYTTMAX);
    }
    return (max == 0) ? 0 : Math.round(13.0F * current / max);
  }

  @Override
  public int getBarColor(ItemStack stack) {
    return TextureRegistry.COLOUR_RF_BAR;
  }

  @Override
  public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    int current = 0;
    int energyttmax = 0;
    IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
    if (storage != null) {
      current = storage.getEnergyStored();
      energyttmax = storage.getMaxEnergyStored();
      tooltip.add(new TranslatableComponent(current + "/" + energyttmax).withStyle(ChatFormatting.RED));
    }
    else if (stack.hasTag() && stack.getTag().contains(ENERGYTT)) {
      //TODO 1.19 port  delete this branch
      current = stack.getTag().getInt(ENERGYTT);
      energyttmax = stack.getTag().getInt(ENERGYTTMAX);
      tooltip.add(new TranslatableComponent(current + "/" + energyttmax).withStyle(ChatFormatting.BLUE));
    }
    super.appendHoverText(stack, worldIn, tooltip, flagIn);
  }

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
