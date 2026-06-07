package com.lothrazar.cyclic.cache;

import com.lothrazar.library.data.LivingTileCache;

/**
 * Server side multi-cache
 */
public class ServerCacheHolder {

  // @TilePeace.java and @BlockSpawnEvents.java
  public static LivingTileCache CANDLE_PEACE = new LivingTileCache("PEACE");
  // @TileAltar.java and @BlockSpawnEvents.java
  public static LivingTileCache ALTAR_SOLICITING = new LivingTileCache("NOS");
}
