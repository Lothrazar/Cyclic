package com.lothrazar.cyclic.item.boomerang;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.util.ItemStackUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class BoomerangItem extends ItemBaseCyclic {

  private static final int TICKS_USING = 93000;

  public static enum Boomer {
    STUN, DAMAGE, CARRY;
  }

  private Boomer type;

  public BoomerangItem(Boomer type, Properties properties) {
    super(properties);
    this.type = type;
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
    //    float amountCharged = percentageCharged * MAX_CHARGE;
    //    float velocityFactor = percentageCharged * 1.5F;
    if (entity instanceof Player == false) {
      return;
    }
    Player player = (Player) entity;
    BoomerangEntity e;
    switch (this.type) {
      case CARRY:
        e = new BoomerangEntityCarry(player, world);
      break;
      case DAMAGE:
        e = new BoomerangEntityDamage(player, world);
      break;
      default:
      case STUN:
        e = new BoomerangEntityStun(player, world);
      break;
    }
    shootMe(world, player, e, 0, percentageCharged * ItemBaseCyclic.VELOCITY_MAX);
    ItemStackUtil.damageItem(player, stack);
    player.setItemInHand(player.getUsedItemHand(), ItemStack.EMPTY);
    e.setBoomerangThrown(stack.copy());
    e.setOwner(player);
  }
}
