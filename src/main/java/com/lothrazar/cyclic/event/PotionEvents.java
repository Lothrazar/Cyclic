package com.lothrazar.cyclic.event;

import com.lothrazar.cyclic.potion.CyclicMobEffect;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.PotionEffectRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PotionEvents {

  @SubscribeEvent
  public void onPotionAdded(PotionEvent.PotionAddedEvent event) {
    System.out.println("PotionAddedEvent   " + event);
    if (event.getPotionEffect().getEffect() instanceof CyclicMobEffect self) {
      self.onPotionAdded(event);
    }
  }

  @SubscribeEvent
  public void isPotionApplicable(PotionEvent.PotionApplicableEvent event) {
    if (event.getPotionEffect().getEffect() instanceof CyclicMobEffect self) {
      self.isPotionApplicable(event);
    }
    BlockRegistry.ANTI_BEACON.get().isPotionApplicable(event);
  }

  @SubscribeEvent
  public void onPotionRemove(PotionEvent.PotionRemoveEvent event) {
    if (event.getPotion() instanceof CyclicMobEffect self) {
      self.onPotionRemove(event);
    }
  }

  @SubscribeEvent
  public void onPotionExpiry(PotionEvent.PotionExpiryEvent event) {
    if (event.getPotionEffect().getEffect() instanceof CyclicMobEffect self) {
      self.onPotionExpiry(event);
    }
  }

  @SubscribeEvent
  public void onEntityUpdate(LivingUpdateEvent event) {
    LivingEntity entity = event.getEntityLiving();
    if (entity == null) {
      return;
    }
    for (CyclicMobEffect effect : PotionEffectRegistry.EFFECTS) {
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
