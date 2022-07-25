package com.lothrazar.cyclic.capabilities;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.annotation.Nonnull;
import com.lothrazar.cyclic.capabilities.player.PlayerManaProvider;
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

public class ManaManager extends SavedData {

  private final Map<ChunkPos, ManaManager.ChunkData> manaMap = new HashMap<>();
  private final Random random = new Random();
  // Keep a counter so that we don't send mana back to the client every tick
  private int counter = 0;

  public ManaManager() {}

  public ManaManager(CompoundTag tag) {
    ListTag list = tag.getList("mana", Tag.TAG_COMPOUND);
    for (Tag t : list) {
      CompoundTag manaTag = (CompoundTag) t;
      ChunkData mana = new ChunkData(manaTag.getInt("mana"));
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
      manaTag.putInt("mana", mana.getMana());
      list.add(manaTag);
    });
    tag.put("mana", list);
    return tag;
  }

  // This function can be used to get access to the mana manager for a given level. It can only be called server-side!
  @Nonnull
  public static ManaManager get(Level level) {
    if (level.isClientSide) {
      throw new RuntimeException("Don't access this client-side!");
    }
    // Get the vanilla storage manager from the level
    DimensionDataStorage storage = ((ServerLevel) level).getDataStorage();
    // Get the mana manager if it already exists. Otherwise create a new one. Note that both
    // invocations of ManaManager::new actually refer to a different constructor. One without parameters
    // and the other with a CompoundTag parameter
    return storage.computeIfAbsent(ManaManager::new, ManaManager::new, "manamanager");
  }

  private ChunkData getManaInternal(BlockPos pos) {
    // Get the mana at a certain chunk. If this is the first time then we fill in the manaMap using computeIfAbsent
    ChunkPos chunkPos = new ChunkPos(pos);
    return manaMap.computeIfAbsent(chunkPos, cp -> new ChunkData(0)); //default is what? zero? 9999
  }

  public int getMana(BlockPos pos) {
    ChunkData mana = getManaInternal(pos);
    return mana.getMana();
  }

  public int extractMana(BlockPos pos) {
    ChunkData mana = getManaInternal(pos);
    int present = mana.getMana();
    if (present > 0) {
      mana.setMana(present - 1);
      // Do not forget to call setDirty() whenever making changes that need to be persisted!
      setDirty();
      return 1;
    }
    else {
      return 0;
    }
  }

  // This tick is called from a tick event (see later)
  public void tick(Level level) {
    counter--;
    // Every 10 ticks this code will synchronize the mana of each player and the mana of the current
    // chunk of that player to the client so that it can be displayed on screen
    if (counter <= 0) {
      counter = 10;
      // Synchronize the mana to the players in this world
      // todo expansion: keep the previous data that was sent to the player and only send if changed
      level.players().forEach(player -> {
        if (player instanceof ServerPlayer serverPlayer) {
          int playerMana = serverPlayer.getCapability(PlayerManaProvider.PLAYER_MANA)
              .map(PlayerCapabilityStorage::getMana)
              .orElse(-1);
          int chunkMana = getMana(serverPlayer.blockPosition());
          //send to client 
          PacketRegistry.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new PacketSyncManaToClient(playerMana, chunkMana));
        }
      });
      // todo expansion: here it would be possible to slowly regenerate mana in chunks
    }
  }

  /**
   * MASHUP of 'Mana' chunk stuff and Playermana
   * 
   * @author lothr
   *
   */
  public class ClientManaData {

    private static int playerMana;
    private static int chunkMana;

    public static void set(int playerMana, int chunkMana) {
      ClientManaData.playerMana = playerMana;
      ClientManaData.chunkMana = chunkMana;
    }

    public static int getPlayerMana() {
      return playerMana;
    }

    public static int getChunkMana() {
      return chunkMana;
    }
  }

  public static class PlayerCapabilityStorage {

    private int mana;

    public int getMana() {
      return mana;
    }

    public void setMana(int mana) {
      this.mana = mana;
    }

    public void addMana(int mana) {
      this.mana += mana;
    }

    public void copyFrom(PlayerCapabilityStorage source) {
      mana = source.mana;
    }

    public void saveNBTData(CompoundTag compound) {
      compound.putInt("mana", mana);
    }

    public void loadNBTData(CompoundTag compound) {
      mana = compound.getInt("mana");
    }
  }

  public class ChunkData { // TODO: CHUNK MANA  / per chunk data

    private int mana;

    public ChunkData(int mana) {
      this.mana = mana;
    }

    public int getMana() {
      return mana;
    }

    public void setMana(int mana) {
      this.mana = mana;
    }
  }
}
