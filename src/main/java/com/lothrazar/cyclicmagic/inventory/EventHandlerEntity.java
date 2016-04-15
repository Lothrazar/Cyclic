package com.lothrazar.cyclicmagic.inventory;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.google.common.io.Files;
import com.lothrazar.cyclicmagic.ModMain;

public class EventHandlerEntity {

	static HashSet<Integer> playerEntityIds = new HashSet<Integer>();

	@SubscribeEvent
	public void playerTick(PlayerEvent.LivingUpdateEvent event) {

		// player events
		if (event.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntity();

			if (!playerEntityIds.isEmpty() && playerEntityIds.contains(player.getEntityId())) {
				EventHandlerNetwork.syncBaubles(player);
				playerEntityIds.remove(player.getEntityId());
			}

			/*
			 * InventoryBaubles baubles = PlayerHandler.getPlayerBaubles(player);
			 * for (int a = 0; a < baubles.getSizeInventory(); a++) {
			 * if (baubles.getStackInSlot(a) != null
			 * && baubles.getStackInSlot(a).getItem() instanceof IBauble) {
			 * ((IBauble) baubles.getStackInSlot(a).getItem()).onWornTick(
			 * baubles.getStackInSlot(a), player);
			 * }
			 * }
			 */

		}
	}

	@SubscribeEvent
	public void playerDeath(PlayerDropsEvent event) {
		if (event.getEntity() instanceof EntityPlayer && !event.getEntity().worldObj.isRemote && !event.getEntity().worldObj.getGameRules().getBoolean("keepInventory")) {
			PlayerHandler.getPlayerBaubles(event.getEntityPlayer()).dropItemsAt(event.getDrops(), event.getEntityPlayer());
		}
	}

	@SubscribeEvent
	public void playerLoad(PlayerEvent.LoadFromFile event) {
		PlayerHandler.clearPlayerBaubles(event.getEntityPlayer());

		File playerFile = getPlayerFile(ext, event.getPlayerDirectory(), event.getEntityPlayer().getDisplayNameString());
		if (!playerFile.exists()) {
			File fileNew = event.getPlayerFile(ext);
			if (fileNew.exists()) {
				try {
					Files.copy(fileNew, playerFile);
					ModMain.logger.info("Using and converting UUID Baubles savefile for " + event.getEntityPlayer().getDisplayNameString());
					fileNew.delete();
					File fb = event.getPlayerFile(extback);
					if (fb.exists())
						fb.delete();
				} catch (IOException e) {}
			}
		}

		PlayerHandler.loadPlayerBaubles(event.getEntityPlayer(), playerFile, getPlayerFile(extback, event.getPlayerDirectory(), event.getEntityPlayer().getDisplayNameString()));
		EventHandlerEntity.playerEntityIds.add(event.getEntityPlayer().getEntityId());
	}
	
	final String ext = "invo";
	final String extback = "backup";

	public File getPlayerFile(String suffix, File playerDirectory, String playername) {
	//	if ("dat".equals(suffix))
			//throw new IllegalArgumentException("The suffix 'dat' is reserved");
		return new File(playerDirectory, "_" + playername + "." + suffix);
	}

	@SubscribeEvent
	public void playerSave(PlayerEvent.SaveToFile event) {
		PlayerHandler.savePlayerBaubles(event.getEntityPlayer(), getPlayerFile(ext, event.getPlayerDirectory(), event.getEntityPlayer().getDisplayNameString()), getPlayerFile(extback, event.getPlayerDirectory(), event.getEntityPlayer().getDisplayNameString()));
	}

}
