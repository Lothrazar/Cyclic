package com.lothrazar.cyclic.block.expcollect;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.util.UtilSound;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundEvents;

public class ExpItemGain extends ItemBase {

  //20mb per xp following convention set by EnderIO; OpenBlocks; and Reliquary https://github.com/PrinceOfAmber/Cyclic/issues/599
  public static final int FLUID_PER_EXP = 20;
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
