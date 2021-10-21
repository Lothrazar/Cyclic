package com.lothrazar.cyclic.event;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.data.CyclicFile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerDataEvents {

  public static Map<UUID, CyclicFile> DATA_QUEUE = new HashMap<>();
  public static final String FILE_EXT = ".dat";

  public static CyclicFile getOrCreate(Player player) {
    UUID id = player.getUUID();
    if (!DATA_QUEUE.containsKey(id)) {
      DATA_QUEUE.put(id, new CyclicFile(player.getUUID()));
    }
    return DATA_QUEUE.get(id);
  }

  @SubscribeEvent
  public void onSaveFile(PlayerEvent.SaveToFile event) {
    Player player = event.getPlayer();
    //if we have datas queued up to be saved
    if (DATA_QUEUE.containsKey(player.getUUID())) {
      //yes i have data to save 
      CyclicFile dataToSave = DATA_QUEUE.get(player.getUUID());
      CompoundTag data = dataToSave.write();
      try {
        File mctomb = new File(event.getPlayerDirectory(), getPlayerFile(player));
        FileOutputStream fileoutputstream = new FileOutputStream(mctomb);
        NbtIo.writeCompressed(data, fileoutputstream);
        fileoutputstream.close();
        ModCyclic.LOGGER.info("Cyclic PlayerEvent.SaveToFile success" + data);
      }
      catch (IOException e) {
        ModCyclic.LOGGER.error("IO cyclic file error", e);
      }
    }
  }

  private String getPlayerFile(Player player) {
    return player.getUUID() + "_cyclic" + FILE_EXT;
  }

  @SubscribeEvent
  public void onLoadFile(PlayerEvent.LoadFromFile event) {
    Player player = event.getPlayer();
    File mctomb = new File(event.getPlayerDirectory(), getPlayerFile(player));
    if (mctomb.exists()) {
      try {
        FileInputStream fileinputstream = new FileInputStream(mctomb);
        CompoundTag data = NbtIo.readCompressed(fileinputstream);
        fileinputstream.close();
        CyclicFile dataLoaded = new CyclicFile(player.getUUID());
        dataLoaded.read(data);
        DATA_QUEUE.put(player.getUUID(), dataLoaded);
      }
      catch (Exception e) {
        ModCyclic.LOGGER.error("IO", e);
      }
    }
    //LOAD player data
  }
}
