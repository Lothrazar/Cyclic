package com.lothrazar.cyclic.item.slingshot;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.library.util.ItemStackUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;

public class SlingshotItem extends ItemBaseCyclic {

  private static final int TICKS_USING = 93000;

  public SlingshotItem(Properties properties) {
    super(properties);
  }

  @Override
  public ItemUseAnimation getUseAnimation(ItemStack stack) {
    return ItemUseAnimation.BOW;
  }

  @Override
  public int getUseDuration(ItemStack stack, LivingEntity entity) {
    return TICKS_USING; //bow has 72000
  }

  @Override
  public InteractionResult use(Level worldIn, Player playerIn, InteractionHand handIn) {
    ItemStack itemstack = playerIn.getItemInHand(handIn);
    playerIn.startUsingItem(handIn);
    return InteractionResult.SUCCESS.heldItemTransformedTo(itemstack);
  }

  @Override
  public boolean releaseUsing(ItemStack stack, Level world, LivingEntity entity, int chargeTimer) {
    int charge = this.getUseDuration(stack, entity) - chargeTimer;
    float percentageCharged = BowItem.getPowerForTime(charge); //never zero, its from [0.03,1];
    if (percentageCharged < 0.1) {
      return false; //not enough force to go with any realistic path
    }
    if (entity instanceof Player player) {
      shootMe(world, player, new StoneEntity(entity, world), 0, percentageCharged * ItemBaseCyclic.VELOCITY_MAX);
      ItemStackUtil.damageItem(player, stack);
      return true;
    }
    return false;
  }
}
