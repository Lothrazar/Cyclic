package com.lothrazar.cyclic.potion;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.PotionEffectRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

public class PotionEventHandler {

  @SubscribeEvent
  public void onPotionAdded(MobEffectEvent.Added event) {
    if (event.getEffectInstance().getEffect().value() instanceof CyclicMobEffect self) {
      self.onPotionAdded(event);
    }
  }

  @SubscribeEvent
  public void isPotionApplicable(MobEffectEvent.Applicable event) {
    if (event.getEffectInstance().getEffect().value() instanceof CyclicMobEffect self) {
      self.isPotionApplicable(event);
    }
    BlockRegistry.ANTI_BEACON.get().isPotionApplicable(event);
  }

  @SubscribeEvent
  public void onPotionRemove(MobEffectEvent.Remove event) {
    if (event.getEffect().value() instanceof CyclicMobEffect self) {
      self.onPotionRemove(event);
    }
  }

  @SubscribeEvent
  public void onPotionExpiry(MobEffectEvent.Expired event) {
    if (event.getEffectInstance().getEffect().value() instanceof CyclicMobEffect self) {
      self.onPotionExpiry(event);
    }
  }

  @SubscribeEvent
  public void onEntityUpdate(EntityTickEvent.Pre event) {
    if (!(event.getEntity() instanceof LivingEntity entity)) {
      return;
    }
    for (CyclicMobEffect effect : PotionEffectRegistry.EFFECTS) {
      if (effect != null && entity.hasEffect(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(effect))) {
        effect.tick(event);
      }
    }
  }

  // Milk-resistance for the Flight effect (NoClip and other CyclicMobEffects still get cleared normally).
  //
  // Vanilla MilkBucketItem.finishUsingItem() calls LivingEntity.removeAllEffects()   there's no per-effect
  // opt-out from that path. Instead we snapshot the Flight MobEffectInstance during the drink animation
  // (via LivingEntityUseItemEvent.Tick) and re-apply it after the drink completes (Finish). Vanilla's order is:
  //   completeUsingItem → finishUsingItem (kills all effects) → fires Finish event
  // so by Finish time the effect is gone; we re-add from the snapshot taken on the previous Tick event.
  private static final Map<UUID, MobEffectInstance> FLIGHT_PRE_MILK = new ConcurrentHashMap<>();

  @SubscribeEvent
  public void onUseItemTick(LivingEntityUseItemEvent.Tick event) {
    if (!(event.getEntity() instanceof Player player)) {
      return;
    }
    if (event.getItem().getItem() != Items.MILK_BUCKET) {
      return;
    }
    MobEffectInstance flight = player.getEffect(PotionEffectRegistry.FLIGHT);
    if (flight != null) {
      FLIGHT_PRE_MILK.put(player.getUUID(), new MobEffectInstance(flight));
    }
  }

  @SubscribeEvent
  public void onUseItemFinish(LivingEntityUseItemEvent.Finish event) {
    if (!(event.getEntity() instanceof Player player)) {
      return;
    }
    MobEffectInstance saved = FLIGHT_PRE_MILK.remove(player.getUUID());
    if (saved != null && event.getItem().getItem() == Items.MILK_BUCKET) {
      player.addEffect(saved);
    }
  }

  @SubscribeEvent
  public void onUseItemStop(LivingEntityUseItemEvent.Stop event) {
    if (event.getEntity() instanceof Player player) {
      FLIGHT_PRE_MILK.remove(player.getUUID());
    }
  }
}
