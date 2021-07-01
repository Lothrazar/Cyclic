package com.lothrazar.cyclic.data;

import com.lothrazar.cyclic.ModCyclic;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;

public class CyclicFile {

  public static final String NBTINV = "inv";
  public final UUID playerId;
  public boolean storageVisible = false;
  public boolean todoVisible = false;
  public List<String> todoTasks = new ArrayList<>();
  public int flyTicks = 0;
  public int spectatorTicks = 0;
  public ItemStackHandler inventory = new ItemStackHandler(4 * 9);

  public CyclicFile(UUID playerId) {
    this.playerId = playerId;
  }

  public void read(CompoundNBT tag) {
    ModCyclic.LOGGER.info("READ file " + tag);
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    flyTicks = tag.getInt("flyTicks");
    spectatorTicks = tag.getInt("spectatorTicks");
    if (tag.contains("tasks")) {
      ListNBT glist = tag.getList("tasks", Constants.NBT.TAG_COMPOUND);
      for (int i = 0; i < glist.size(); i++) {
        CompoundNBT row = glist.getCompound(i);
        todoTasks.add(row.getString("todo"));
      }
    }
  }

  public CompoundNBT write() {
    CompoundNBT tag = new CompoundNBT();
    tag.put(NBTINV, inventory.serializeNBT());
    tag.putInt("flyTicks", flyTicks);
    tag.putInt("spectatorTicks", spectatorTicks);
    tag.putBoolean("storage", storageVisible);
    ListNBT glist = new ListNBT();
    int i = 0;
    for (String t : todoTasks) {
      CompoundNBT row = new CompoundNBT();
      row.putInt("index", i);
      row.putString("todo", t);
      glist.add(row);
    }
    tag.put("tasks", glist);
    ModCyclic.LOGGER.info("Write to file " + tag);
    return tag;
  }
}
