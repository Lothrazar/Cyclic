package com.lothrazar.cyclic.block.expcollect;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.util.UtilSound;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundEvents;

public class ExpItemGain extends ItemBase {

  public static final int EXP_PER_FOOD = 50;

  public ExpItemGain(Properties properties) {
    super(properties);
  }

  @Override
  public ActionResultType onItemUse(ItemUseContext context) {
    context.getPlayer().giveExperiencePoints(EXP_PER_FOOD);
    context.getPlayer().getHeldItem(context.getHand()).shrink(1);
    UtilSound.playSound(context.getPlayer(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP);
    return super.onItemUse(context);
  }
}
