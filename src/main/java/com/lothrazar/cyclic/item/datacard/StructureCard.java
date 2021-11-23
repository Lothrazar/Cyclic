package com.lothrazar.cyclic.item.datacard;

import java.util.List;
import com.lothrazar.cyclic.item.ItemBaseCyclic;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class StructureCard extends ItemBaseCyclic {

  private static final String NBTSTRUCTURE = "structure";

  public StructureCard(Properties properties) {
    super(properties);
  }

  public static ResourceLocation readDisk(ItemStack item) {
    CompoundTag tag = item.getOrCreateTag();
    if (!tag.contains(NBTSTRUCTURE)) {
      return null;
    }
    return ResourceLocation.tryParse(tag.getString(NBTSTRUCTURE));
  }

  public static void deleteDisk(ItemStack item) {
    item.setTag(null);
  }

  public static void saveDisk(ItemStack item, ResourceLocation saved) {
    CompoundTag tag = item.getOrCreateTag();
    tag.putString(NBTSTRUCTURE, saved.toString());
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    super.appendHoverText(stack, worldIn, tooltip, flagIn);
    if (stack.hasTag()) {
      TranslatableComponent t = new TranslatableComponent(
          stack.getTag().getString(NBTSTRUCTURE));
      t.withStyle(ChatFormatting.GRAY);
      tooltip.add(t);
    }
  }
}
