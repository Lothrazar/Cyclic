package com.lothrazar.cyclic.item.slingshot;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.util.UtilItemStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class SlingshotItem extends ItemBase {

  private static final int TICKS_USING = 93000;

  public SlingshotItem(Properties properties) {
    super(properties);
  }

  @Override
  public UseAction getUseAction(ItemStack stack) {
    return UseAction.BOW;
  }

  @Override
  public int getUseDuration(ItemStack stack) {
    return TICKS_USING; //bow has 72000
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
    ItemStack itemstack = playerIn.getHeldItem(handIn);
    playerIn.setActiveHand(handIn);
    return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
  }

  @Override
  public void onPlayerStoppedUsing(ItemStack stack, World world, LivingEntity entity, int chargeTimer) {
    int charge = this.getUseDuration(stack) - chargeTimer;
    float percentageCharged = BowItem.getArrowVelocity(charge); //never zero, its from [0.03,1];
    if (percentageCharged < 0.1) {
      return; //not enough force to go with any realistic path 
    }
    if (!(entity instanceof PlayerEntity)) {
      return;
    }
    PlayerEntity player = (PlayerEntity) entity;
    shootMe(world, player, new StoneEntity(entity, world), 0, percentageCharged * ItemBase.VELOCITY_MAX);
    UtilItemStack.damageItem(player, stack);
  }
}
