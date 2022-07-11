package com.lothrazar.cyclic.util;

import com.lothrazar.cyclic.data.CyclicFile;
import com.lothrazar.cyclic.event.PlayerDataEvents;
import net.minecraft.world.entity.player.Player;

public class UtilStepHeight {

  public static void disableStepHeightForced(Player player) {
    disableStepHeightInternal(player);
    CyclicFile datFile = PlayerDataEvents.getOrCreate(player);
    datFile.stepHeightForceOff = false;
  }

  public static void disableStepHeight(Player player) {
    CyclicFile datFile = PlayerDataEvents.getOrCreate(player);
    if (datFile.stepHeight) {
      return;
    }
    disableStepHeightInternal(player);
  }
  // TODO: Fix step height

  private static void disableStepHeightInternal(Player player) {
    player.maxUpStep = 0.6F; // LivingEntity.class constructor defaults to this
  }

  public static void enableStepHeight(Player player) {
    if (player.isCrouching()) {
      //make sure that, when sneaking, dont fall off!!
      player.maxUpStep = 0.9F;
    }
    else {
      player.maxUpStep = 1.0F + (1F / 16F); //PATH BLOCKS etc are 1/16th downif MY feature turns this on, then do it
    }
  }
}
