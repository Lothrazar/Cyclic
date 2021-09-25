package com.lothrazar.cyclic.item.magicnet;

import com.lothrazar.cyclic.base.ItemBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ItemMagicNet extends ItemBase {

  private static final int TICKS_USING = 93000; //bow has 72000

  public ItemMagicNet(Properties properties) {
    super(properties);
  }

  @Override
  public UseAction getUseAction(ItemStack stack) {
    return UseAction.BOW;
  }

  @Override
  public int getUseDuration(ItemStack stack) {
    return TICKS_USING;
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
    ItemStack itemstack = playerIn.getHeldItem(handIn);
    playerIn.setActiveHand(handIn);
    return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
  }

  @Override
  public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entity, int chargeTimer) {
    //
    int charge = this.getUseDuration(stack) - chargeTimer;
    float percentageCharged = BowItem.getArrowVelocity(charge); //never zero, its from [0.03,1];
    if (percentageCharged < 0.1) {
      return; //not enough force to go with any realistic path 
    }
    if (entity instanceof PlayerEntity == false) {
      return;
    }
    PlayerEntity player = (PlayerEntity) entity;
    shootMe(worldIn, player, new EntityMagicNetEmpty(worldIn, player), 0F, percentageCharged * ItemBase.VELOCITY_MAX);
    if (!player.isCreative()) {
      stack.shrink(1);
    }
  }
}
