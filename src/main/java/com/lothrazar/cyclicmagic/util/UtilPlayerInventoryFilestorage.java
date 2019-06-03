/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import javax.annotation.Nonnull;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.playerupgrade.storage.InventoryPlayerExtended;
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

  private static final String newExtension = "cyclicinvo";
  public static HashSet<Integer> playerEntityIds = new HashSet<Integer>();
  private static HashMap<String, InventoryPlayerExtended> playerItems = new HashMap<String, InventoryPlayerExtended>();

  public static void playerSetupOnLoad(PlayerEvent.LoadFromFile event) {
    EntityPlayer player = event.getEntityPlayer();
    clearPlayerInventory(player);
    File playerFile = getPlayerFileID(event.getPlayerDirectory(), event.getEntityPlayer());

    loadPlayerInventory(event.getEntityPlayer(), playerFile);
    playerEntityIds.add(event.getEntityPlayer().getEntityId());
  }

  private static void clearPlayerInventory(EntityPlayer player) {
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
    getPlayerInventory(player).inv.set(slot, itemStack);
  }

  public static void setPlayerInventory(EntityPlayer player, InventoryPlayerExtended inventory) {
    playerItems.put(player.getDisplayNameString(), inventory);
  }

  private  static void loadPlayerInventory(EntityPlayer player, File file1) {
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
        if (file1 == null || !file1.exists() || data == null || data.isEmpty()) {
          ModCyclic.logger.error("Data not found for " + player.getDisplayNameString());//+ ". Trying to load backup data."
        }
        if (data != null) {
          InventoryPlayerExtended inventory = new InventoryPlayerExtended(player);
          inventory.readNBT(data);
          playerItems.put(player.getDisplayNameString(), inventory);
          if (save)
            savePlayerItems(player, file1);
        }
      }
      catch (Exception e) {
        ModCyclic.logger.error("Error loading player extended inventory");
        e.printStackTrace();
      }
    }
  }

  public static void savePlayerItems(@Nonnull EntityPlayer player, File playerDirectory) {
    if (!player.getEntityWorld().isRemote) {
      try {

        //if original fails to save, ID version will not be overwritten! 
        File fileToSaveID = getPlayerFileID(playerDirectory, player);
        if (fileToSaveID != null) {
          InventoryPlayerExtended inventory = getPlayerInventory(player);
          NBTTagCompound data = new NBTTagCompound();
          inventory.saveNBT(data);
          FileOutputStream fileoutputstream = new FileOutputStream(fileToSaveID);
          CompressedStreamTools.writeCompressed(data, fileoutputstream);
          fileoutputstream.close();
          //ModCyclic.logger.log("Successs saved for player " + fileToSaveID.getName());
        }
        else {
          ModCyclic.logger.error("Could not save file for player " + player.getDisplayNameString());
        }
      }
      catch (Exception e) {
        ModCyclic.logger.error("Could not save file for player " + player.getDisplayNameString(), e);
      }
    }
  }

  private static File getPlayerFileID(File playerDirectory, EntityPlayer player) {
    return new File(playerDirectory, player.getUniqueID() + "." + newExtension);
  }

  public static void syncItems(EntityPlayer player) {
    int size = InventoryPlayerExtended.ICOL * InventoryPlayerExtended.IROW + 20;//+20 somehow magically fixes bottom row
    for (int a = 0; a < size; a++) {
      getPlayerInventory(player).syncSlotToClients(a);
    }
  }

  public static void putDataIntoInventory(InventoryPlayerExtended inventory, EntityPlayer player) {
    inventory.inv = getPlayerInventory(player).inv;
  }

  public static int getSize() {
    return InventoryPlayerExtended.ICOL * InventoryPlayerExtended.IROW + 20;
  }
}
