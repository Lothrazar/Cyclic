package com.lothrazar.cyclic.filesystem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.items.ItemStackHandler;

public class CyclicFile {

  //TODO: deprecate everything EXCEPT item stacks. replace with player caps 
  public static final String NBTINV = "inv";
  public final UUID playerId;
  public boolean storageVisible = false;
  public boolean todoVisible = false;
  public boolean stepHeight = false;
  public List<String> todoTasks = new ArrayList<>();
  public int spectatorTicks = 0;
  // first 27 slots are for inventory cake storage. remaining unused
  public ItemStackHandler inventory = new ItemStackHandler(5 * 9);

  @Override
  public String toString() {
    return "CyclicFile [playerId=" + playerId + ", storageVisible=" + storageVisible + ", todoVisible=" + todoVisible + ", stepHeight=" + stepHeight +
        ", todoTasks=" + todoTasks + ", spectatorTicks=" + spectatorTicks + ", inventory=" + inventory + "]";
  }

  public CyclicFile(UUID playerId) {
    this.playerId = playerId;
  }

  public void read(CompoundTag tag) {
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    spectatorTicks = tag.getInt("spectatorTicks");
    storageVisible = tag.getBoolean("storageVisible");
    stepHeight = tag.getBoolean("stepHeight");
    if (tag.contains("tasks")) {
      ListTag glist = tag.getList("tasks", Tag.TAG_COMPOUND);
      for (int i = 0; i < glist.size(); i++) {
        CompoundTag row = glist.getCompound(i);
        todoTasks.add(row.getString("todo"));
      }
    }
  }

  public CompoundTag write() {
    CompoundTag tag = new CompoundTag();
    tag.put(NBTINV, inventory.serializeNBT());
    tag.putInt("spectatorTicks", spectatorTicks);
    tag.putBoolean("stepHeight", stepHeight);
    tag.putBoolean("storageVisible", storageVisible);
    ListTag glist = new ListTag();
    int i = 0;
    for (String t : todoTasks) {
      CompoundTag row = new CompoundTag();
      row.putInt("index", i);
      row.putString("todo", t);
      glist.add(row);
    }
    tag.put("tasks", glist);
    return tag;
  }

  public void toggleStepHeight() {
    this.stepHeight = !this.stepHeight;
  }
}
