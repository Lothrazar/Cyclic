package com.lothrazar.cyclic.item.datacard;

import java.util.List;
import com.lothrazar.cyclic.item.ItemBaseCyclic;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class StructureCard extends ItemBaseCyclic {

  private static final String NBTSTRUCTURE = "structure";

  public StructureCard(Properties properties) {
    super(properties);
  }

  public static ResourceLocation readDisk(ItemStack item) {
    CompoundTag tag = item.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag();
    if (!tag.contains(NBTSTRUCTURE)) {
      return null;
    }
    return ResourceLocation.tryParse(tag.getString(NBTSTRUCTURE));
  }

  public static void deleteDisk(ItemStack item) {
    item.remove(net.minecraft.core.component.DataComponents.CUSTOM_DATA);
  }

  public static void saveDisk(ItemStack item, ResourceLocation saved) {
    CompoundTag tag = item.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag();
    tag.putString(NBTSTRUCTURE, saved.toString());
    item.set(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.of(tag));
  }

  @Override
  public void appendHoverText(ItemStack stack, Item.TooltipContext worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    super.appendHoverText(stack, worldIn, tooltip, flagIn);
    if (stack.has(net.minecraft.core.component.DataComponents.CUSTOM_DATA)) {
      MutableComponent t = Component.translatable(
          stack.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag().getString(NBTSTRUCTURE));
      t.withStyle(ChatFormatting.GRAY);
      tooltip.add(t);
    }
  }
}
