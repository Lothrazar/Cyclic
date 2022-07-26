package com.lothrazar.cyclic.capabilities;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.annotation.Nonnull;
import com.lothrazar.cyclic.capabilities.chunk.ChunkDataStorage;
import com.lothrazar.cyclic.capabilities.player.PlayerCapProvider;
import com.lothrazar.cyclic.capabilities.player.PlayerCapabilityStorage;
import com.lothrazar.cyclic.net.PacketSyncManaToClient;
import com.lothrazar.cyclic.registry.PacketRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraftforge.network.PacketDistributor;

public class CyclicWorldSavedData extends SavedData {

  private final Map<ChunkPos, ChunkDataStorage> manaMap = new HashMap<>();
  private final Random random = new Random();
  //TODO: ticker in new whole thing Keep a counter so that we don't send mana back to the client every tick
  private int syncToClientCounter = 0;

  public CyclicWorldSavedData() {}

  public CyclicWorldSavedData(CompoundTag tag) {
    ListTag list = tag.getList("mana", Tag.TAG_COMPOUND);
    for (Tag t : list) {
      CompoundTag manaTag = (CompoundTag) t;
      ChunkDataStorage mana = new ChunkDataStorage(manaTag);
      ChunkPos pos = new ChunkPos(manaTag.getInt("x"), manaTag.getInt("z"));
      manaMap.put(pos, mana);
    }
  }

  @Override
  public CompoundTag save(CompoundTag tag) {
    ListTag list = new ListTag();
    manaMap.forEach((chunkPos, mana) -> {
      CompoundTag manaTag = new CompoundTag();
      manaTag.putInt("x", chunkPos.x);
      manaTag.putInt("z", chunkPos.z);
      mana.save(manaTag);
      list.add(manaTag);
    });
    tag.put("mana", list);
    return tag;
  }

  // This function can be used to get access to the mana manager for a given level. It can only be called server-side!
  @Nonnull
  public static CyclicWorldSavedData get(Level level) {
    if (level.isClientSide) {
      throw new RuntimeException("Don't access this client-side!");
    }
    // Get the vanilla storage manager from the level
    DimensionDataStorage storage = ((ServerLevel) level).getDataStorage();
    // Get the mana manager if it already exists. Otherwise create a new one. Note that both
    // invocations of ManaManager::new actually refer to a different constructor. One without parameters
    // and the other with a CompoundTag parameter
    return storage.computeIfAbsent(CyclicWorldSavedData::new, CyclicWorldSavedData::new, "data");
  }

  private ChunkDataStorage getDataForPos(BlockPos pos) {
    return getDataForChunk(new ChunkPos(pos));
  }

  private ChunkDataStorage getDataForChunk(ChunkPos chunkPos) {
    return manaMap.computeIfAbsent(chunkPos, cp -> new ChunkDataStorage(random.nextInt(4444, 7777))); //default is what? zero? 9999
  }
  //  public int getMana(BlockPos pos) {
  //    ChunkData mana = getManaInternal(pos);
  //    return mana.getMana();
  //  }
  //
  //  public int extractMana(BlockPos pos) {
  //    ChunkData mana = getManaInternal(pos);
  //    int present = mana.getMana();
  //    if (present > 0) {
  //      mana.setMana(present - 1);
  //      // Do not forget to call setDirty() whenever making changes that need to be persisted!
  //      setDirty();
  //      return 1;
  //    }
  //    else {
  //      return 0;
  //    }
  //  }

  public void onWorldTick(Level level) {
    syncToClientCounter--;
    if (syncToClientCounter <= 0) {
      syncToClientCounter = 10;
      level.players().forEach(p -> {
        if (p instanceof ServerPlayer serverPlayer) {
          //sync players own DATA
          PlayerCapabilityStorage playerData = serverPlayer.getCapability(PlayerCapProvider.CYCLIC_PLAYER).orElse(null);
          ChunkDataStorage chunkData = getDataForPos(serverPlayer.blockPosition());
          //
          //
          //and at the same time, get data for the CHUNK you are in and sync at the same time
          //do both instead of once
          //send playerData and chunkData to client 
          PacketRegistry.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new PacketSyncManaToClient(
              //TODO: send whole obj
              playerData == null ? -1 : playerData.getMana(),
              //TODO: obj
              chunkData.getMana()));
        }
      });
    }
  }
}
