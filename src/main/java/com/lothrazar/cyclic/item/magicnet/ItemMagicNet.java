package com.lothrazar.cyclic.item.magicnet;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;

public class ItemMagicNet extends ItemBaseCyclic {

  private static final int TICKS_USING = 93000; //bow has 72000

  public ItemMagicNet(Properties properties) {
    super(properties);
  }

  @Override
  public ItemUseAnimation getUseAnimation(ItemStack stack) {
    return ItemUseAnimation.BOW;
  }

  @Override
  public int getUseDuration(ItemStack stack, LivingEntity entity) {
    return TICKS_USING;
  }

  @Override
  public InteractionResult use(Level worldIn, Player playerIn, InteractionHand handIn) {
    ItemStack itemstack = playerIn.getItemInHand(handIn);
    playerIn.startUsingItem(handIn);
    return InteractionResult.SUCCESS.heldItemTransformedTo(itemstack);
  }

  @Override
  public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entity, int chargeTimer) {
    //
    int charge = this.getUseDuration(stack, entity) - chargeTimer;
    float percentageCharged = BowItem.getPowerForTime(charge); //never zero, its from [0.03,1];
    if (percentageCharged < 0.1) {
      return; //not enough force to go with any realistic path 
    }
    if (entity instanceof Player == false) {
      return;
    }
    Player player = (Player) entity;
    shootMe(worldIn, player, new EntityMagicNetEmpty(worldIn, player), 0F, percentageCharged * ItemBaseCyclic.VELOCITY_MAX);
    if (!player.isCreative()) {
      stack.shrink(1);
    }
  }
}
