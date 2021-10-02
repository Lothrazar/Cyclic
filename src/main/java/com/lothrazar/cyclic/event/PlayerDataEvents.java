package com.lothrazar.cyclic.event;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.data.CyclicFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerDataEvents {

  public static Map<UUID, CyclicFile> DATA_QUEUE = new HashMap<>();
  public static final String FILE_EXT = ".dat";

  public static CyclicFile getOrCreate(PlayerEntity player) {
    UUID id = player.getUniqueID();
    if (!DATA_QUEUE.containsKey(id)) {
      DATA_QUEUE.put(id, new CyclicFile(player.getUniqueID()));
    }
    return DATA_QUEUE.get(id);
  }

  @SubscribeEvent
  public void onSaveFile(PlayerEvent.SaveToFile event) {
    PlayerEntity player = event.getPlayer();
    //if we have datas queued up to be saved
    if (DATA_QUEUE.containsKey(player.getUniqueID())) {
      //yes i have data to save 
      CyclicFile dataToSave = DATA_QUEUE.get(player.getUniqueID());
      CompoundNBT data = dataToSave.write();
      try {
        File mctomb = new File(event.getPlayerDirectory(), getPlayerFile(player));
        FileOutputStream fileoutputstream = new FileOutputStream(mctomb);
        CompressedStreamTools.writeCompressed(data, fileoutputstream);
        fileoutputstream.close();
        ModCyclic.LOGGER.info("Cyclic PlayerEvent.SaveToFile success" + data);
      }
      catch (IOException e) {
        ModCyclic.LOGGER.error("IO cyclic file error", e);
      }
    }
  }

  private String getPlayerFile(PlayerEntity player) {
    return player.getUniqueID() + "_cyclic" + FILE_EXT;
  }

  @SubscribeEvent
  public void onLoadFile(PlayerEvent.LoadFromFile event) {
    PlayerEntity player = event.getPlayer();
    File mctomb = new File(event.getPlayerDirectory(), getPlayerFile(player));
    if (mctomb.exists()) {
      try {
        FileInputStream fileinputstream = new FileInputStream(mctomb);
        CompoundNBT data = CompressedStreamTools.readCompressed(fileinputstream);
        fileinputstream.close();
        CyclicFile dataLoaded = new CyclicFile(player.getUniqueID());
        dataLoaded.read(data);
        if (DATA_QUEUE.containsKey(player.getUniqueID())) {
          // 
          ModCyclic.LOGGER.error("? overwrite PlayerEvent.LoadFromFile " + data);
        }
        DATA_QUEUE.put(player.getUniqueID(), dataLoaded);
        ModCyclic.LOGGER.error("C PlayerEvent.LoadFromFile " + data);
        //        ModCyclic.LOGGER.error("# of tombs " + dataLoaded.playerGraves.size());
      }
      catch (Exception e) {
        ModCyclic.LOGGER.error("IO", e);
      }
    }
    //LOAD player data
  }
}
