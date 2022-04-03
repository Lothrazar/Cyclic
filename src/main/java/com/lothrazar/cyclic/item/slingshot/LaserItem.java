package com.lothrazar.cyclic.item.slingshot;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.util.UtilItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class LaserItem extends ItemBaseCyclic {

  public static final double RANGE_FACTOR = 8;

  public LaserItem(Properties properties) {
    super(properties.defaultDurability(1024 * 4));
  }

  @Override
  public UseAnim getUseAnimation(ItemStack stack) {
    return UseAnim.NONE;
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
    ItemStack itemstack = playerIn.getItemInHand(handIn);
    playerIn.startUsingItem(handIn);
    return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
  }

  @Override
  public int getUseDuration(ItemStack stack) {
    return 72000;
  }

  public static ItemStack getIfHeld(Player player) {
    ItemStack heldItem = player.getMainHandItem();
    if (heldItem.getItem() instanceof LaserItem) {
      return heldItem;
    }
    heldItem = player.getOffhandItem();
    if (heldItem.getItem() instanceof LaserItem) {
      return heldItem;
    }
    return ItemStack.EMPTY;
  }

  @Override
  public void releaseUsing(ItemStack stack, Level world, LivingEntity entity, int chargeTimer) {
    if (entity instanceof Player == false) {
      return;
    }
    Player player = (Player) entity;
    int charge = this.getUseDuration(stack) - chargeTimer;
    float percentageCharged = BowItem.getPowerForTime(charge); //never zero, its from [0.03,1];
    //    playerIn.startUsingItem(handIn);
    //    shootMe(world, player, new LaserEntity(player, world), 0, ItemBaseCyclic.VELOCITY_MAX * 1.4F);
    // TODO: RF POWER NOT DURAB
    UtilItemStack.damageItem(player, stack);
    //    return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
  }
}
