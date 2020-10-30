package com.lothrazar.cyclic.item.datacard;

import java.util.List;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.data.RelativeShape;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ShapeCard extends ItemBase {

  public static final String VALID_SHAPE = "cyclic-shape";

  public ShapeCard(Properties properties) {
    super(properties);
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    RelativeShape shape = RelativeShape.read(stack);
    if (shape != null) {
      tooltip.add(new TranslationTextComponent(
          getTranslationKey() + ".count"
              + shape.getCount()));
      if (flagIn.isAdvanced()) {
        //        String side = "S: " + dim.getSide().toString().toUpperCase();
        //        tooltip.add(new TranslationTextComponent(side));
        //        String sideF = "F: " + dim.getSidePlayerFacing().toString().toUpperCase();
        //        tooltip.add(new TranslationTextComponent(sideF));
        //        tooltip.add(new TranslationTextComponent("H: " + dim.getHitVec().toString()));
      }
    }
    else {
      TranslationTextComponent t = new TranslationTextComponent(getTranslationKey() + ".tooltip");
      t.mergeStyle(TextFormatting.GRAY);
      tooltip.add(t);
    }
  }
}
