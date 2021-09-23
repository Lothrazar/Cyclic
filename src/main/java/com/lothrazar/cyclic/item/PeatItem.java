package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.block.generatorpeat.TilePeatGenerator;
import com.lothrazar.cyclic.config.ConfigRegistry;
import java.util.List;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PeatItem extends ItemBase {

  public static enum PeatItemType {
    BIOMASS, NORM, ENRICHED;
  }

  public final PeatItemType type;

  public PeatItem(Properties properties, PeatItemType t) {
    super(properties);
    type = t;
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    super.addInformation(stack, worldIn, tooltip, flagIn);
    tooltip.add(new StringTextComponent(getPeatFuelValue() + "RF/t for " + TilePeatGenerator.BURNTIME + " ticks").mergeStyle(TextFormatting.DARK_GREEN));
    //    if(stack.getBurnTime() > 0 )
  }
  //
  //  @Override
  //  public int getBurnTime(ItemStack itemStack, IRecipeType<?> recipeType) {
  //    switch (type) {
  //      case BIOMASS:
  //        return 160;
  //      case NORM:
  //        return 1600 * 2;
  //      case ENRICHED:
  //        return 1600 * 8;
  //    }
  //    return super.getBurnTime(itemStack, recipeType);
  //  }

  public int getPeatFuelValue() {
    switch (type) {
      case BIOMASS:
        return 10;
      case NORM:
        return ConfigRegistry.PEATPOWER.get();
      case ENRICHED:
        return ConfigRegistry.PEATERICHPOWER.get();
      default:
        return 0;
    }
  }
}
