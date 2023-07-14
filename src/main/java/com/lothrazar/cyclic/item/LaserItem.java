package com.lothrazar.cyclic.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class LaserItem extends ItemBaseCyclic {

  public static final int DELAYDAMAGETICKS = 5;
  public static final int DMG_FAR = 4;
  public static final int COST = 50;
  public static final int DMG_CLOSE = 6;
  public static final double RANGE_FACTOR = 8;
  public static final double RANGE_MAX = 6000;

  public LaserItem(Properties properties) {
    super(properties.stacksTo(1));
    this.setUsesEnergy();
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
    lasercannon.getOrCreateTag().putLong("damagecooldown", gametime);
  }

  public static int getDamageCooldown(ItemStack lasercannon) {
    int thisOne = lasercannon.getOrCreateTag().getInt("damagecooldown");
    return thisOne;
  }
}
