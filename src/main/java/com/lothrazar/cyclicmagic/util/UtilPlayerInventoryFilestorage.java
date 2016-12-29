package com.lothrazar.cyclicmagic.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import com.google.common.io.Files;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.gui.player.InventoryPlayerExtended;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.PlayerEvent;

/*Thank you so much for the help azanor
 * for basically writing this class and releasing it open source
 * 
 * https://github.com/Azanor/Baubles
 * 
 * which is under Attribution-NonCommercial-ShareAlike 3.0 Unported (CC BY-NC-SA 3.0) license.
 * so i was able to use parts of that to make this
 * **/
public class UtilPlayerInventoryFilestorage {
  public static HashSet<Integer> playerEntityIds = new HashSet<Integer>();
  private static HashMap<String, InventoryPlayerExtended> playerItems = new HashMap<String, InventoryPlayerExtended>();
  public static void playerSetupOnLoad(PlayerEvent.LoadFromFile event) {
    EntityPlayer player = event.getEntityPlayer();
    clearPlayerInventory(player);
    File playerFile = getPlayerFile(ext, event.getPlayerDirectory(), event.getEntityPlayer().getDisplayNameString());
    if (!playerFile.exists()) {
      File fileNew = event.getPlayerFile(ext);
      if (fileNew.exists()) {
        try {
          Files.copy(fileNew, playerFile);
          ModCyclic.logger.info("Using and converting UUID savefile for " + player.getDisplayNameString());
          fileNew.delete();
          File fb = event.getPlayerFile(extback);
          if (fb.exists())
            fb.delete();
        }
        catch (IOException e) {
        }
      }
    }
    loadPlayerInventory(event.getEntityPlayer(), playerFile, getPlayerFile(extback, event.getPlayerDirectory(), event.getEntityPlayer().getDisplayNameString()));
    playerEntityIds.add(event.getEntityPlayer().getEntityId());
  }
  public static void clearPlayerInventory(EntityPlayer player) {
    playerItems.remove(player.getDisplayNameString());
  }
  public static InventoryPlayerExtended getPlayerInventory(EntityPlayer player) {
    if (!playerItems.containsKey(player.getDisplayNameString())) {
      InventoryPlayerExtended inventory = new InventoryPlayerExtended(player);
      playerItems.put(player.getDisplayNameString(), inventory);
    }
    return playerItems.get(player.getDisplayNameString());
  }
  public static ItemStack getPlayerInventoryStack(EntityPlayer player, int slot) {
    return getPlayerInventory(player).getStackInSlot(slot);
  }
  public static void setPlayerInventoryStack(EntityPlayer player, int slot, ItemStack itemStack) {
    //    UtilPlayerInventoryFilestorage.getPlayerInventory(player).setInventorySlotContents(slot, itemStack);
    getPlayerInventory(player).stackList[slot] = itemStack;
  }
  public static void setPlayerInventory(EntityPlayer player, InventoryPlayerExtended inventory) {
    playerItems.put(player.getDisplayNameString(), inventory);
  }
  public static void loadPlayerInventory(EntityPlayer player, File file1, File file2) {
    if (player != null && !player.getEntityWorld().isRemote) {
      try {
        NBTTagCompound data = null;
        boolean save = false;
        if (file1 != null && file1.exists()) {
          try {
            FileInputStream fileinputstream = new FileInputStream(file1);
            data = CompressedStreamTools.readCompressed(fileinputstream);
            fileinputstream.close();
          }
          catch (Exception e) {
            e.printStackTrace();
          }
        }
        if (file1 == null || !file1.exists() || data == null || data.hasNoTags()) {
          ModCyclic.logger.warn("Data not found for " + player.getDisplayNameString() + ". Trying to load backup data.");
          if (file2 != null && file2.exists()) {
            try {
              FileInputStream fileinputstream = new FileInputStream(file2);
              data = CompressedStreamTools.readCompressed(fileinputstream);
              fileinputstream.close();
              save = true;
            }
            catch (Exception e) {
              e.printStackTrace();
            }
          }
        }
        if (data != null) {
          InventoryPlayerExtended inventory = new InventoryPlayerExtended(player);
          inventory.readNBT(data);
          playerItems.put(player.getDisplayNameString(), inventory);
          if (save)
            savePlayerItems(player, file1, file2);
        }
      }
      catch (Exception e) {
        ModCyclic.logger.error("Error loading player extended inventory");
        e.printStackTrace();
      }
    }
  }
  public static void savePlayerItems(EntityPlayer player, File file1, File file2) {
    if (player != null && !player.getEntityWorld().isRemote) {
      try {
        if (file1 != null && file1.exists()) {
          try {
            Files.copy(file1, file2);
          }
          catch (Exception e) {
            ModCyclic.logger.error("Could not backup old file for player " + player.getDisplayNameString());
          }
        }
        try {
          if (file1 != null) {
            InventoryPlayerExtended inventory = getPlayerInventory(player);
            NBTTagCompound data = new NBTTagCompound();
            inventory.saveNBT(data);
            FileOutputStream fileoutputstream = new FileOutputStream(file1);
            CompressedStreamTools.writeCompressed(data, fileoutputstream);
            fileoutputstream.close();
          }
        }
        catch (Exception e) {
          ModCyclic.logger.error("Could not save file for player " + player.getDisplayNameString());
          e.printStackTrace();
          if (file1.exists()) {
            try {
              file1.delete();
            }
            catch (Exception e2) {
            }
          }
        }
      }
      catch (Exception exception1) {
        ModCyclic.logger.error("Error saving inventory");
        exception1.printStackTrace();
      }
    }
  }
  public static final String ext = "invo";
  public static final String extback = "backup";
  public static final String regex = "[^a-zA-Z0-9_]";
  public static File getPlayerFile(String suffix, File playerDirectory, String playername) {
    // some other mods/servers/plugins add things like "[Owner] > " prefix to player names
    //which are invalid filename chars.   https://github.com/PrinceOfAmber/Cyclic/issues/188
    //mojang username rules https://help.mojang.com/customer/en/portal/articles/928638-minecraft-usernames
    String playernameFiltered = playername.replaceAll(regex, "");
    return new File(playerDirectory, "_" + playernameFiltered + "." + suffix);
  }
  public static void syncItems(EntityPlayer player) {
    int size = InventoryPlayerExtended.ICOL * InventoryPlayerExtended.IROW;
    for (int a = 0; a < size; a++) {
      getPlayerInventory(player).syncSlotToClients(a);
    }
  }
  public static void putDataIntoInventory(InventoryPlayerExtended inventory, EntityPlayer player) {
    inventory.stackList = getPlayerInventory(player).stackList;
  }
}
