package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.block.generatorpeat.TileGeneratorPeat;
import com.lothrazar.cyclic.config.ConfigRegistry;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
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
  public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    super.appendHoverText(stack, worldIn, tooltip, flagIn);
    tooltip.add(new TextComponent(getPeatFuelValue() + "RF/t for " + TileGeneratorPeat.BURNTIME + " ticks").withStyle(ChatFormatting.DARK_GREEN));
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
