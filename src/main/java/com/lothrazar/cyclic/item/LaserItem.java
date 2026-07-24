package com.lothrazar.cyclic.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.component.CustomData;

public class LaserItem extends ItemHasEnergy {

  public static final int DELAYDAMAGETICKS = 5;
  //  public static final double RANGE_FACTOR = 8;

  public LaserItem(Properties properties) {
    super(properties.stacksTo(1));
  }

  @Override
  public UseAnim getUseAnimation(ItemStack stack) {
    return UseAnim.NONE;
  }

  @Override
  public InteractionResult use(Level worldIn, Player playerIn, InteractionHand handIn) {
    ItemStack itemstack = playerIn.getItemInHand(handIn);
    playerIn.startUsingItem(handIn);
    return InteractionResult.SUCCESS.heldItemTransformedTo(itemstack);
  }

  @Override
  public int getUseDuration(ItemStack stack, LivingEntity entity) {
    return 72000 * 2;
  }

  public static ItemStack getIfHeld(Player player) {
    ItemStack heldItem = player.getMainHandItem();
    if (heldItem.getItem() instanceof LaserItem) {
      return heldItem;
    }
    //MAIN HAND ONLY for this case 
    return ItemStack.EMPTY;
  }

  @Override
  public void releaseUsing(ItemStack stack, Level world, LivingEntity entity, int chargeTimer) {}

  public static void resetStackDamageCool(ItemStack lasercannon, long gametime) {
    CustomData.EMPTY.copyTag().putLong("damagecooldown", gametime);
  }

  public static int getDamageCooldown(ItemStack lasercannon) {
    int thisOne = CustomData.EMPTY.copyTag().getIntOr("damagecooldown", 0);
    return thisOne;
  }
}
