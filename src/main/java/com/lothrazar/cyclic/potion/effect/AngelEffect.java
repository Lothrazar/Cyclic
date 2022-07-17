package com.lothrazar.cyclic.potion.effect;

import com.lothrazar.cyclic.potion.TickableEffect;
import com.lothrazar.cyclic.registry.PotionEffectRegistry;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class AngelEffect extends TickableEffect {

  //TODO: this is disabled , doesnt really work
  public AngelEffect(MobEffectCategory typeIn, int liquidColorIn) {
    super(typeIn, liquidColorIn);
    PotionEffectRegistry.EFFECTS.add(this);
  }
  //  @Override
  //  public boolean isInstantenous() {
  //    return true;
  //  }
  //
  //  @Override
  //  public void applyInstantenousEffect(@Nullable Entity attacker, @Nullable Entity owner, LivingEntity target, int ampl, double pct) {
  //    super.applyInstantenousEffect(attacker, owner, target, ampl, pct);
  //    if (target instanceof Player player && !target.getLevel().isClientSide) {
  //      if (player.getAbilities().mayfly) {
  //        CyclicFile file = PlayerDataEvents.getOrCreate(player);
  //        player.getAbilities().mayfly = false;
  //        player.getAbilities().flying = false;
  //        player.fallDistance = 0F;
  //      }
  //      else {
  //        player.getAbilities().mayfly = true; // good luck!
  //      }
  //    }
  //  }

  @Override
  public void tick(LivingUpdateEvent event) {
    LivingEntity living = event.getEntityLiving();
    //    int amp = living.getEffect(this).getAmplifier();
    if (living instanceof Player player) {
      //      player.getAbilities().invulnerable = true;
      if (!player.getAbilities().mayfly) {
        player.getAbilities().mayfly = true;
        player.getAbilities().flying = true;
        //.println("mayflyht on !!!! ");
        if (!living.getLevel().isClientSide) {
          //          CyclicFile file = PlayerDataEvents.getOrCreate(player);
          //          file.todoVisible = true; // set fly is from potion. les yes
        }
      }
    }
  }
}
