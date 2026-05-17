package com.lothrazar.cyclic.util;

import com.lothrazar.cyclic.net.PacketSyncHorseCarrots;
import com.lothrazar.library.util.EntityUtil;
import com.lothrazar.library.util.ItemStackUtil;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class HorseFeedUtil {

  /**
   * Standard finishing actions for a successful horse-carrot feed:
   *  - sets cancellation result BEFORE canceling (NeoForge requires this order so the
   *    client doesn't fall through to the mount interaction)
   *  - cancels the event so vanilla's mount-on-right-click is suppressed
   *  - shrinks the held stack respecting creative mode
   *  - if the player still ended up riding this horse (client prediction edge case),
   *    immediately stops them
   *  - triggers the horse eating animation when applicable
   */
  public static void finishFeed(PlayerInteractEvent.EntityInteract event, AbstractHorse horse) {
    event.setCancellationResult(InteractionResult.SUCCESS);
    event.setCanceled(true);
    Player player = event.getEntity();
    ItemStackUtil.shrink(player, event.getItemStack());
    if (player.getVehicle() == horse) {
      player.stopRiding();
    }
    if (horse instanceof Horse h) {
      EntityUtil.eatingHorse(h);
    }
    PacketSyncHorseCarrots.broadcastToTrackers(horse);
  }
}
