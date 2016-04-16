package com.lothrazar.cyclicmagic.inventory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import com.google.common.io.Files;
import com.lothrazar.cyclicmagic.ModMain;


/*Thank you so much for the help azanor
 * https://github.com/Azanor/Baubles
 * which is under Attribution-NonCommercial-ShareAlike 3.0 Unported (CC BY-NC-SA 3.0) license.
 * so i was able to use parts of that to make this
 * **/

public class PlayerHandler {

	private static HashMap<String, InventoryPlayerExtended> playerItems = new HashMap<String, InventoryPlayerExtended>();

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

	public static void setPlayerInventory(EntityPlayer player, InventoryPlayerExtended inventory) {
		playerItems.put(player.getDisplayNameString(), inventory);
	}

	public static void loadPlayerInventory(EntityPlayer player, File file1, File file2) {
		if (player != null && !player.worldObj.isRemote) {
			try {
				NBTTagCompound data = null;
				boolean save = false;
				if (file1 != null && file1.exists()) {
					try {
						FileInputStream fileinputstream = new FileInputStream(file1);
						data = CompressedStreamTools.readCompressed(fileinputstream);
						fileinputstream.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				if (file1 == null || !file1.exists() || data == null || data.hasNoTags()) {
					ModMain.logger.warn("Data not found for " + player.getDisplayNameString() + ". Trying to load backup data.");
					if (file2 != null && file2.exists()) {
						try {
							FileInputStream fileinputstream = new FileInputStream(file2);
							data = CompressedStreamTools.readCompressed(fileinputstream);
							fileinputstream.close();
							save = true;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}

				if (data != null) {
					InventoryPlayerExtended inventory = new InventoryPlayerExtended(player);
					inventory.readNBT(data);
					playerItems.put(player.getDisplayNameString(), inventory);
					if (save)
						savePlayerBaubles(player, file1, file2);
				}
			} catch (Exception exception1) {
				ModMain.logger.fatal("Error loading baubles inventory");
				exception1.printStackTrace();
			}
		}
	}

	public static void savePlayerBaubles(EntityPlayer player, File file1, File file2) {
		if (player != null && !player.worldObj.isRemote) {
			try {
				if (file1 != null && file1.exists()) {
					try {
						Files.copy(file1, file2);
					} catch (Exception e) {
						ModMain.logger.error("Could not backup old baubles file for player " + player.getDisplayNameString());
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
				} catch (Exception e) {
					ModMain.logger.error("Could not save baubles file for player " + player.getDisplayNameString());
					e.printStackTrace();
					if (file1.exists()) {
						try {
							file1.delete();
						} catch (Exception e2) {}
					}
				}
			} catch (Exception exception1) {
				ModMain.logger.fatal("Error saving baubles inventory");
				exception1.printStackTrace();
			}
		}
	}
}
