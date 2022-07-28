package com.lothrazar.cyclic.potion.effect;

import com.lothrazar.cyclic.potion.CyclicMobEffect;
import com.lothrazar.cyclic.util.PlayerUtil;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event.Result;

public class FlightMayflyEffect extends CyclicMobEffect {

  public FlightMayflyEffect(MobEffectCategory typeIn, int liquidColorIn) {
    super(typeIn, liquidColorIn);
  }

  @Override
  public void onPotionAdded(MobEffectEvent.Added event) {
    if (event.getEntity() instanceof Player player) {
      if (!player.getAbilities().mayfly) {
        PlayerUtil.setMayFlyFromServer(event.getEntity(), true);
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
    PlayerUtil.setMayFlyFromServer(event.getEntity(), false);
  }

  @Override
  public void onPotionExpiry(MobEffectEvent.Expired event) {
    PlayerUtil.setMayFlyFromServer(event.getEntity(), false);
  }
}
