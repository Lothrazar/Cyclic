package com.lothrazar.cyclic.item.elemental;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.ItemStackUtil;
import com.lothrazar.cyclic.util.SoundUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class FireScepter extends ItemBaseCyclic {

  private static final int COOLDOWN = 16;

  public FireScepter(Properties properties) {
    super(properties);
  }

  @Override
  public UseAnim getUseAnimation(ItemStack stack) {
    return UseAnim.SPEAR;
  }

  @Override
  public int getUseDuration(ItemStack stack) {
    return 72000 / 2;
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
    ItemStack itemstack = playerIn.getItemInHand(handIn);
    playerIn.startUsingItem(handIn);
    return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
  }

  @Override
  public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entity, int chargeTimer) {
    if (entity instanceof Player == false) {
      return;
    }
    Player player = (Player) entity;
    int charge = this.getUseDuration(stack) - chargeTimer;
    float percentageCharged = BowItem.getPowerForTime(charge); //never zero, its from [0.03,1];
    float velocityFactor = percentageCharged * ItemBaseCyclic.VELOCITY_MAX;
    shootMe(worldIn, player, new FireEntity(player, worldIn), 0, velocityFactor);
    shootMe(worldIn, player, new FireEntity(player, worldIn), 10, velocityFactor);
    shootMe(worldIn, player, new FireEntity(player, worldIn), -10, velocityFactor);
    player.getCooldowns().addCooldown(stack.getItem(), COOLDOWN);
    ItemStackUtil.damageItem(player, stack);
    SoundUtil.playSound(player, SoundRegistry.FIREBALL_STAFF_LAUNCH.get());
  }
}
