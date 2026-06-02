package com.lothrazar.cyclic.render;

import java.util.HashMap;
import java.util.Map;
import com.lothrazar.cyclic.net.PacketSyncHorseCarrots;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Client-side cache of horse carrot state, populated by PacketSyncHorseCarrots
 * sent from the server when the player starts tracking a horse or after a feed.
 * Entity persistent NBT is server-only in NeoForge 21.1.x, so this is needed
 * for the inventory overlay to display correct values.
 */
@OnlyIn(Dist.CLIENT)
public class HorseCarrotClientCache {

  private static final Map<Integer, PacketSyncHorseCarrots> CACHE = new HashMap<>();

  public static void put(PacketSyncHorseCarrots msg) {
    CACHE.put(msg.entityId, msg);
  }

  public static PacketSyncHorseCarrots get(int entityId) {
    return CACHE.get(entityId);
  }

  public static void clear() {
    CACHE.clear();
  }
}
