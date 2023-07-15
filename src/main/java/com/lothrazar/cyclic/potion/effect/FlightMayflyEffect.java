package com.lothrazar.cyclic.potion.effect;

import com.lothrazar.cyclic.net.PacketPlayerSyncToClient;
import com.lothrazar.cyclic.potion.CyclicMobEffect;
import com.lothrazar.cyclic.registry.PacketRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.network.NetworkDirection;

public class FlightMayflyEffect extends CyclicMobEffect {

  public FlightMayflyEffect(MobEffectCategory typeIn, int liquidColorIn) {
    super(typeIn, liquidColorIn);
  }

  private static void setMayFlyFromServer(LivingEntity entity, boolean mayflyIn) {
    if (entity instanceof ServerPlayer sp) {
      //set server-player
      sp.getAbilities().mayfly = mayflyIn;
      if (!mayflyIn) {
        sp.getAbilities().flying = false;
      }
      //sync to client
      PacketRegistry.INSTANCE.sendTo(new PacketPlayerSyncToClient(mayflyIn), sp.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
  }

  @Override
  public void onPotionAdded(MobEffectEvent.Added event) {
    if (event.getEntity() instanceof Player player) {
      if (!player.getAbilities().mayfly) {
        setMayFlyFromServer(event.getEntity(), true);
      }
    }
  }

  @Override
  public void isPotionApplicable(MobEffectEvent.Applicable event) {
    if (event.getEntity() instanceof Player player) {
      if (player.isCreative()) { //no creative players should use this to fly
        event.setResult(Result.DENY);
      }
    }
    else {
      //not a player , so deny
      event.setResult(Result.DENY);
    }
  }

  @Override
  public void onPotionRemove(MobEffectEvent.Remove event) {
    setMayFlyFromServer(event.getEntity(), false);
  }

  @Override
  public void onPotionExpiry(MobEffectEvent.Expired event) {
    setMayFlyFromServer(event.getEntity(), false);
  }
}
