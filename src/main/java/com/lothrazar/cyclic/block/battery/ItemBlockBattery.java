package com.lothrazar.cyclic.block.battery;

import java.util.List;

import com.lothrazar.cyclic.fixers.CapabilityFixer;
import com.lothrazar.cyclic.registry.TextureRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class ItemBlockBattery extends BlockItem {

  public static final String ENERGYTTMAX = "energyttmax";
  public static final String ENERGYTT = "energytt";

  public ItemBlockBattery(Block blockIn, Properties builder) {
    super(blockIn, builder);
  }

  @Override
  public boolean isBarVisible(ItemStack stack) {
    IEnergyStorage storage = CapabilityFixer.energy(stack);//stack.getCapability(ForgeCapabilities.ENERGY, null).orElse(null);
    return storage != null && storage.getEnergyStored() > 0;
  }

  @Override
  public int getBarWidth(ItemStack stack) {
    float current = 0;
    float max = 0;
    IEnergyStorage storage =CapabilityFixer.energy(stack);// stack.getCapability(ForgeCapabilities.ENERGY, null).orElse(null);
    if (storage != null) {
      current = storage.getEnergyStored();
      max = storage.getMaxEnergyStored();
    }
    return (max == 0) ? 0 : Math.round(13.0F * current / max);
  }

  @Override
  public int getBarColor(ItemStack stack) {
    return TextureRegistry.COLOUR_RF_BAR;
  }

  @Override
  public void appendHoverText(ItemStack stack, Item.TooltipContext  worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    int current = 0;
    int energyttmax = 0;
    IEnergyStorage storage = CapabilityFixer.energy(stack) ;//stack.getCapability(ForgeCapabilities.ENERGY, null).orElse(null);
    if (storage != null) {
      current = storage.getEnergyStored();
      energyttmax = storage.getMaxEnergyStored();
      tooltip.add(Component.translatable(current + "/" + energyttmax).withStyle(ChatFormatting.RED));
    }
    super.appendHoverText(stack, worldIn, tooltip, flagIn);
  }

//  @Override
//  // public Object initCapabilities(ItemStack stack, CompoundTag nbt) {
//    return new CapabilityProviderEnergyStack(TileBattery.MAX.get());
//  }

  }

