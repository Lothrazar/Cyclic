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
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class ItemMagicNet extends ItemBase {

  private static final float INACCURACY_DEFAULT = 1.0F;
  private static final float VELOCITY_MAX = 1.5F;
  private static final int TICKS_USING = 93000;

  public ItemMagicNet(Properties properties) {
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
  public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entity, int chargeTimer) {
    //
    int charge = this.getUseDuration(stack) - chargeTimer;
    float percentageCharged = BowItem.getArrowVelocity(charge); //never zero, its from [0.03,1];
    if (percentageCharged < 0.1) {
      return; //not enough force to go with any realistic path 
    }
    float velocityFactor = percentageCharged * 1.5F;
    if (entity instanceof PlayerEntity == false) {
      return;
    }
    PlayerEntity player = (PlayerEntity) entity;
    EntityMagicNetEmpty e = new EntityMagicNetEmpty(worldIn, player);
    Vector3d lookVector = player.getLookVec();
    e.shoot(lookVector.getX(), lookVector.getY(), lookVector.getZ(), velocityFactor * VELOCITY_MAX, INACCURACY_DEFAULT);
    worldIn.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_ENDER_PEARL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
    worldIn.addEntity(e);
    if (!player.isCreative()) {
      stack.shrink(1);
    }
  }
}
