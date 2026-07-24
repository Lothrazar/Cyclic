package com.lothrazar.cyclic.item.elemental;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.library.util.ItemStackUtil;
import com.lothrazar.library.util.SoundUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;

public class FireScepter extends ItemBaseCyclic {

  private static final int COOLDOWN = 16;

  public FireScepter(Properties properties) {
    super(properties);
  }

  @Override
  public ItemUseAnimation getUseAnimation(ItemStack stack) {
    return ItemUseAnimation.SPEAR;
  }

  @Override
  public int getUseDuration(ItemStack stack, LivingEntity entity) {
    return 72000 / 2;
  }

  @Override
  public InteractionResult use(Level worldIn, Player playerIn, InteractionHand handIn) {
    ItemStack itemstack = playerIn.getItemInHand(handIn);
    playerIn.startUsingItem(handIn);
    return InteractionResult.SUCCESS.heldItemTransformedTo(itemstack);
  }

  @Override
  public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entity, int chargeTimer) {
    if (entity instanceof Player == false) {
      return;
    }
    Player player = (Player) entity;
    int charge = this.getUseDuration(stack, entity) - chargeTimer;
    float percentageCharged = BowItem.getPowerForTime(charge); //never zero, its from [0.03,1];
    float velocityFactor = percentageCharged * ItemBaseCyclic.VELOCITY_MAX;
    shootMe(worldIn, player, new FireEntity(player, worldIn), 0, velocityFactor);
    shootMe(worldIn, player, new FireEntity(player, worldIn), 10, velocityFactor);
    shootMe(worldIn, player, new FireEntity(player, worldIn), -10, velocityFactor);
    player.getCooldowns().addCooldown(stack, COOLDOWN);
    ItemStackUtil.damageItem(player, stack);
    SoundUtil.playSound(player, SoundRegistry.FIREBALL_STAFF_LAUNCH.get());
  }
}
