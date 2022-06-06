package com.lothrazar.cyclic.item.elemental;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.registry.ItemRegistry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class FireballItem extends ItemBaseCyclic {

  public FireballItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level world, Player shooter, InteractionHand hand) {
    shootMe(world, shooter, new FireEntity(shooter, world), 0, ItemBaseCyclic.VELOCITY_MAX);
    //    UtilItemStack.damageItem(shooter, shooter.getItemInHand(hand));
    if (!shooter.isCreative()) {
      shooter.getItemInHand(hand).shrink(1);
    }
    shooter.getCooldowns().addCooldown(ItemRegistry.FIREBALL_ORANGE.get(), 20);
    return super.use(world, shooter, hand);
  }

  public static void tickHoldingFireball(Player player) {
    if (player.getItemInHand(InteractionHand.MAIN_HAND).is(ItemRegistry.FIREBALL_ORANGE.get())) {
      //hurt
      //not if youre on fire
      int fireProt = EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_PROTECTION, player);
      if (fireProt == 0 &&
          !player.isOnFire() &&
          player.level.random.nextDouble() < 0.03) {
        //i am holding fireball in my main hand
        //i am not on fire right now, i have no fire prot
        player.setRemainingFireTicks(30); // 20 ticks is one secondfireProt
        player.getCooldowns().addCooldown(ItemRegistry.FIREBALL_ORANGE.get(), 5);
        //          player.hurt(DamageSource.IN_FIRE, 0.5F);
      }
    }
  }
}
