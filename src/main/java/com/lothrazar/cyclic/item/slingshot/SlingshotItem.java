package com.lothrazar.cyclic.item.slingshot;

import com.lothrazar.cyclic.base.ItemBase;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class SlingshotItem extends ItemBase {

  private static final int TICKS_USING = 93000;

  public SlingshotItem(Properties properties) {
    super(properties);
  }

  @Override
  public UseAnim getUseAnimation(ItemStack stack) {
    return UseAnim.BOW;
  }

  @Override
  public int getUseDuration(ItemStack stack) {
    return TICKS_USING; //bow has 72000
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
    ItemStack itemstack = playerIn.getItemInHand(handIn);
    playerIn.startUsingItem(handIn);
    return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
  }

  @Override
  public void releaseUsing(ItemStack stack, Level world, LivingEntity entity, int chargeTimer) {
    int charge = this.getUseDuration(stack) - chargeTimer;
    float percentageCharged = BowItem.getPowerForTime(charge); //never zero, its from [0.03,1];
    if (percentageCharged < 0.1) {
      return; //not enough force to go with any realistic path 
    }
    if (entity instanceof Player == false) {
      return;
    }
    Player player = (Player) entity;
    shootMe(world, player, new StoneEntity(entity, world), 0, percentageCharged * ItemBase.VELOCITY_MAX);
    stack.hurtAndBreak(1, player, (p) -> {
      p.broadcastBreakEvent(InteractionHand.MAIN_HAND);
    });
    //    player.setHeldItem(player.getActiveHand(), ItemStack.EMPTY);
    //    e.setBoomerangThrown(stack.copy());
    //    e.setOwner(player);
  }
}
