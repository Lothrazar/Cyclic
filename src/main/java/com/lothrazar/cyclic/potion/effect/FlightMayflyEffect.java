package com.lothrazar.cyclic.potion.effect;

import com.lothrazar.cyclic.net.PacketPlayerSyncToClient;
import com.lothrazar.cyclic.potion.CyclicMobEffect;
import com.lothrazar.cyclic.registry.PacketRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.PotionEvent.PotionAddedEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionExpiryEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionRemoveEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.network.NetworkDirection;

public class FlightMayflyEffect extends CyclicMobEffect {

  //TODO: this is disabled , doesnt really work
  public FlightMayflyEffect(MobEffectCategory typeIn, int liquidColorIn) {
    super(typeIn, liquidColorIn);
  }

  @Override
  public void onPotionAdded(PotionAddedEvent event) {
    System.out.println("PotionAddedEvent inside the angel   " + event.getEntityLiving());
    System.out.println("v :: isClientSide = " + event.getEntityLiving().getCommandSenderWorld().isClientSide);
    if (event.getEntityLiving() instanceof Player player) {
      if (!player.getAbilities().mayfly) {
        System.out.println("mayfly true " + player);
        setMayFlyTo(event.getEntityLiving(), true);
      }
    }
  }

  @Override
  public void isPotionApplicable(PotionApplicableEvent event) {
    if (event.getEntityLiving() instanceof Player player) {
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
  public void onPotionRemove(PotionRemoveEvent event) {
    System.out.println("PotionRemoveEvent :: isClientSide = " + event.getEntityLiving().getCommandSenderWorld().isClientSide);
    setMayFlyTo(event.getEntityLiving(), false);
  }

  private void setMayFlyTo(LivingEntity entity, boolean mayflyIn) {
    if (entity instanceof ServerPlayer sp) {
      System.out.println("mayfly false " + sp);
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
  public void onPotionExpiry(PotionExpiryEvent event) {
    System.out.println("PotionExpiryEvent :: isClientSide = " + event.getEntityLiving().getCommandSenderWorld().isClientSide);
    setMayFlyTo(event.getEntityLiving(), false);
  }
}
