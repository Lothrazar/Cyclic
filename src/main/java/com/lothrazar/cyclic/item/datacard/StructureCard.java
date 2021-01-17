package com.lothrazar.cyclic.item.datacard;

import com.lothrazar.cyclic.base.ItemBase;
import java.util.List;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class StructureCard extends ItemBase {

  private static final String NBTSTRUCTURE = "structure";

  public StructureCard(Properties properties) {
    super(properties);
  }

  public static ResourceLocation readDisk(ItemStack item) {
    CompoundNBT tag = item.getOrCreateTag();
    if (!tag.contains(NBTSTRUCTURE)) {
      return null;
    }
    return ResourceLocation.tryCreate(tag.getString(NBTSTRUCTURE));
  }

  public static void deleteDisk(ItemStack item) {
    item.setTag(null);
  }

  public static void saveDisk(ItemStack item, ResourceLocation saved) {
    CompoundNBT tag = item.getOrCreateTag();
    tag.putString(NBTSTRUCTURE, saved.toString());
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    super.addInformation(stack, worldIn, tooltip, flagIn);
    if (stack.hasTag()) {
      TranslationTextComponent t = new TranslationTextComponent(
          stack.getTag().getString(NBTSTRUCTURE));
      t.mergeStyle(TextFormatting.GRAY);
      tooltip.add(t);
    }
  }
}
