package com.lothrazar.cyclic.event;

import com.lothrazar.cyclic.potion.TickableEffect;
import com.lothrazar.cyclic.registry.PotionEffectRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PotionEvents {

  @SubscribeEvent
  public void onEntityUpdate(LivingUpdateEvent event) {
    LivingEntity entity = event.getEntityLiving();
    if (entity == null) {
      return;
    }
    for (TickableEffect effect : PotionEffectRegistry.EFFECTS) {
      if (effect != null && entity.hasEffect(effect)) {
        effect.tick(event);
      }
    }
    //    if (!entity.level.isClientSide && entity instanceof Player player) {
    //      //onPotionDisable hack trigger
    //      if (player.getAbilities().mayfly
    //          && !player.isCreative()
    //          && !player.hasEffect(PotionEffectRegistry.angelic.get())) {
    //        //youre not angelic potion'd, and you ARE allowed to fly, 
    //        //and youre not creative
    //        CyclicFile file = PlayerDataEvents.getOrCreate(player);
    //        //        file.todoVisible = true; // set fly is from potion. les yes
    //        if (file.todoVisible) {
    //          file.todoVisible = false;
    //          .println("booted from flight");
    //          player.fallDistance = 0.0F;
    //          player.getAbilities().flying = false;
    //          player.getAbilities().mayfly = false;
    //        }
    //      }
    //    }
  }
}
