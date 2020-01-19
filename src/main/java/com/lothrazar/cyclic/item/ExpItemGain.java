package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.base.ItemBase;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;

public class ExpItemGain extends ItemBase {

  public static final int EXP_PER_FOOD = 50;

  public ExpItemGain(Properties properties) {
    super(properties);
  }

  @Override
  public ActionResultType onItemUse(ItemUseContext context) {
    context.getPlayer().giveExperiencePoints(EXP_PER_FOOD);
    context.getPlayer().getHeldItem(context.getHand()).shrink(1);
    return super.onItemUse(context);
  }
}
