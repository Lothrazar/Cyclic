package com.lothrazar.cyclic.world.cache;
/**
 * Server side multi-cache
 */
public class ServerCacheHolder {

  // @TilePeace.java and @BlockSpawnEvents.java
  public static LivingTileCache PEACE_CANDLE = new LivingTileCache("PEACE");
  // @TileAltar.java and @BlockSpawnEvents.java
  public static LivingTileCache NO_SOLICITING = new LivingTileCache("NOS");
}
