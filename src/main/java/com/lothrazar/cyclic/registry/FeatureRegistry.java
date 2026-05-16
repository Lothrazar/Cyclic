package com.lothrazar.cyclic.registry;
//hax to disable incomplete features
public class FeatureRegistry {

  // TODO: move mana system to separate mod.  player/chunk mana system — incomplete. Before flipping true, finish:
  //   - CyclicWorldSavedData: read/write via AttachmentRegistry.CYCLIC_PLAYER; throttle sync (counter exists, wire it)
  //   - PacketSyncManaToClient / ClientDataManager: replace raw ints with PlayerCapabilityStorage + ChunkDataStorage
  //   - ChunkDataStorage: decide on per-chunk attachment vs current SavedData map
  public static final boolean PLAYER_SYNC_CAPS = false;
  public static final boolean PLAYER_RENDER_CAPS = false;
}
