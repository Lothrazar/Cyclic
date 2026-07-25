package com.lothrazar.cyclic.item.datacard;

import java.util.List;
import java.util.function.Consumer;
import com.lothrazar.cyclic.item.ItemBaseCyclic;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

public class StructureCard extends ItemBaseCyclic {

  private static final String NBTSTRUCTURE = "structure";

  public StructureCard(Properties properties) {
    super(properties);
  }

  public static Identifier readDisk(ItemStack item) {
    CompoundTag tag = item.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    if (!tag.contains(NBTSTRUCTURE)) {
      return null;
    }
    return Identifier.tryParse(tag.getStringOr(NBTSTRUCTURE, ""));
  }

  public static void deleteDisk(ItemStack item) {
    item.remove(DataComponents.CUSTOM_DATA);
  }

  public static void saveDisk(ItemStack item, Identifier saved) {
    CompoundTag tag = item.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    tag.putString(NBTSTRUCTURE, saved.toString());
    item.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
  }

  @Override
  public void appendHoverText(ItemStack stack, Item.TooltipContext worldIn, TooltipDisplay tooltipDisplay, Consumer<Component> tooltip, TooltipFlag flagIn) {
    super.appendHoverText(stack, worldIn, tooltipDisplay, tooltip, flagIn);
    if (stack.has(DataComponents.CUSTOM_DATA)) {
      MutableComponent t = Component.translatable(
          stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getStringOr(NBTSTRUCTURE, ""));
      t.withStyle(ChatFormatting.GRAY);
      tooltip.accept(t);
    }
  }
}
