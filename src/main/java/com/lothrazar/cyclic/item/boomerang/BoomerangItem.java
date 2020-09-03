package com.lothrazar.cyclic.item.boomerang;

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

public class BoomerangItem extends ItemBase {

  private static final float INACCURACY_DEFAULT = 1.0F;
  private static final float PITCHOFFSET = 0.0F;
  private static final float VELOCITY_MAX = 1.5F;
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
  public UseAction getUseAction(ItemStack stack) {
    return UseAction.BOW;
  }

  @Override
  public int getUseDuration(ItemStack stack) {
    return TICKS_USING;//bow has 72000
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
    float percentageCharged = BowItem.getArrowVelocity(charge);//never zero, its from [0.03,1];
    if (percentageCharged < 0.1) {
      return;//not enough force to go with any realistic path 
    }
    //    float amountCharged = percentageCharged * MAX_CHARGE;
    float velocityFactor = percentageCharged * 1.5F;
    if (entity instanceof PlayerEntity == false) {
      return;
    }
    PlayerEntity player = (PlayerEntity) entity;
    BoomerangEntity e;
    switch (this.type) {
      case CARRY:
        e = new BoomerangEntityCarry(world, player);
      break;
      case DAMAGE:
        e = new BoomerangEntityDamage(world, player);
      break;
      default:
      case STUN:
        e = new BoomerangEntityStun(world, player);
      break;
    }
    shootMe(world, player, e);
    stack.damageItem(1, player, (p) -> {
      p.sendBreakAnimation(Hand.MAIN_HAND);
    });
    player.setHeldItem(player.getActiveHand(), ItemStack.EMPTY);
    e.setBoomerangThrown(stack.copy());
    e.setOwner(player);
  }
}
