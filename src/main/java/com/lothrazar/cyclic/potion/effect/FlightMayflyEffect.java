package com.lothrazar.cyclic.potion.effect;

import com.lothrazar.cyclic.net.PacketPlayerSyncToClient;
import com.lothrazar.cyclic.potion.CyclicMobEffect;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
// import net.neoforged.bus.api.Event.Result;
import net.neoforged.neoforge.network.PacketDistributor;

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
      PacketDistributor.sendToPlayer(sp, new PacketPlayerSyncToClient(mayflyIn));
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

  // MobEffectEvent.Added does NOT fire when an effect is restored from NBT on rejoin, and vanilla
  // GameType.updatePlayerAbilities() forces mayfly=false on non-creative players during placeNewPlayer.
  // Re-assert mayfly every tick while the effect is active — cheap (no-op once set), and corrects rejoin / gamemode-flip drift.
  @Override
  public void tick(EntityTickEvent.Pre event) {
    if (event.getEntity() instanceof ServerPlayer sp && !sp.getAbilities().mayfly) {
      setMayFlyFromServer(sp, true);
    }
  }

  @Override
  public void isPotionApplicable(MobEffectEvent.Applicable event) {
    if (event.getEntity() instanceof Player player) {
      if (player.isCreative() || player.isSpectator()) { //no creative players should use this to fly
        event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
      }
    }
    else {
      //not a player, so deny
      event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
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
