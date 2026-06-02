package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.fixers.CapabilityUtil;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.library.item.ItemFlib;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.List;

public class ItemHasEnergy extends ItemFlib {
  public static final int MAX_ENERGY = 16000;
  public static final String NBT_TAG = "energy";

  public ItemHasEnergy(Properties properties) {
    super(properties);
  }
  public ItemHasEnergy(Properties properties, Settings settings) {
    super(properties, settings);
  }

  @Override
  public int getBarColor(ItemStack stack) {
      return TextureRegistry.COLOUR_RF_BAR;
  }

  @Override
  public boolean isBarVisible(ItemStack stack) {
    IEnergyStorage storage = CapabilityUtil.energy(stack);
    return storage != null; // && storage.getEnergyStored() > 0;
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void appendHoverText(ItemStack stack, TooltipContext worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    super.appendHoverText(stack, worldIn, tooltip, flagIn);

    IEnergyStorage storage = CapabilityUtil.energy(stack);
    if (storage != null) {
      tooltip.add(Component.translatable(storage.getEnergyStored() + "/" + storage.getMaxEnergyStored()).withStyle(ChatFormatting.RED));
    }
  }

  @Override
  public int getBarWidth(ItemStack stack) {
    float current = 0;
    float max = 0;
    IEnergyStorage storage = CapabilityUtil.energy(stack);
    if (storage != null) {
      current = storage.getEnergyStored();
      max = storage.getMaxEnergyStored();
    }
    return (max == 0) ? 0 : Math.round(13.0F * current / max);
  }

}
