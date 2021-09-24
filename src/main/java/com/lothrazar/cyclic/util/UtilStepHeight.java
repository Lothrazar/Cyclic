package com.lothrazar.cyclic.util;

import com.lothrazar.cyclic.data.CyclicFile;
import com.lothrazar.cyclic.event.PlayerDataEvents;
import net.minecraft.entity.player.PlayerEntity;

public class UtilStepHeight {

  public static void disableStepHeightForced(PlayerEntity player) {
    disableStepHeightInternal(player);
    CyclicFile datFile = PlayerDataEvents.getOrCreate(player);
    datFile.stepHeightForceOff = false;
  }

  public static void disableStepHeight(PlayerEntity player) {
    CyclicFile datFile = PlayerDataEvents.getOrCreate(player);
    if (datFile.stepHeight) {
      return;
    }
    disableStepHeightInternal(player);
  }

  private static void disableStepHeightInternal(PlayerEntity player) {
    player.stepHeight = 0.6F; // LivingEntity.class constructor defaults to this
  }

  public static void enableStepHeight(PlayerEntity player) {
    if (player.isCrouching()) {
      //make sure that, when sneaking, dont fall off!!
      player.stepHeight = 0.9F;
    }
    else {
      player.stepHeight = 1.0F + (1F / 16F); //PATH BLOCKS etc are 1/16th downif MY feature turns this on, then do it
    }
  }
}
