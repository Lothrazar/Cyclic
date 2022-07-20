package com.lothrazar.cyclic.potion.effect;

import com.lothrazar.cyclic.potion.CyclicMobEffect;
import com.lothrazar.cyclic.util.PlayerUtil;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.PotionEvent.PotionAddedEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionExpiryEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionRemoveEvent;
import net.minecraftforge.eventbus.api.Event.Result;

public class FlightMayflyEffect extends CyclicMobEffect {

  public FlightMayflyEffect(MobEffectCategory typeIn, int liquidColorIn) {
    super(typeIn, liquidColorIn);
  }

  @Override
  public void onPotionAdded(PotionAddedEvent event) {
    if (event.getEntityLiving() instanceof Player player) {
      if (!player.getAbilities().mayfly) {
        PlayerUtil.setMayFlyFromServer(event.getEntityLiving(), true);
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
    PlayerUtil.setMayFlyFromServer(event.getEntityLiving(), false);
  }

  @Override
  public void onPotionExpiry(PotionExpiryEvent event) {
    PlayerUtil.setMayFlyFromServer(event.getEntityLiving(), false);
  }
}
