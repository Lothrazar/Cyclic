package com.lothrazar.cyclic.item.elemental;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.library.util.SoundUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class FireballItem extends ItemBaseCyclic {

  public FireballItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult use(Level world, Player shooter, InteractionHand hand) {
    shootMe(world, shooter, new FireEntity(shooter, world), 0, ItemBaseCyclic.VELOCITY_MAX);
    //    UtilItemStack.damageItem(shooter, shooter.getItemInHand(hand));
    if (!shooter.isCreative()) {
      shooter.getItemInHand(hand).shrink(1);
    }
//    shooter.getCooldowns().addCooldown(ItemRegistry.FIREBALL_ORANGE.get(), 20);
    SoundUtil.playSound(shooter, SoundRegistry.FIRELAUNCH.get()); // or GOODLAUNCH ?
    return super.use(world, shooter, hand);
  }

  public static void tickHoldingFireball(Player player) {
    if (player.getItemInHand(InteractionHand.MAIN_HAND).is(ItemRegistry.FIREBALL_ORANGE.get())) {
      //hurt
      //not if youre on fire
      int fireProt = 0; // fireProt lookup disabled
      if (fireProt == 0 &&
          !player.isOnFire() &&
          player.level().getRandom().nextDouble() < 0.03) {
        //i am holding fireball in my main hand
        //i am not on fire right now, i have no fire prot
        player.setRemainingFireTicks(30);

      }
    }
  }
}
