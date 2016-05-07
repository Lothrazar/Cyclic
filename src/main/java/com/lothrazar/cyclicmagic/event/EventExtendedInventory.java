package com.lothrazar.cyclicmagic.event;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import com.google.common.io.Files;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.gui.player.InventoryPlayerExtended;
import com.lothrazar.cyclicmagic.util.UtilPlayerInventoryFilestorage;

public class EventExtendedInventory implements IFeatureEvent{


	public static boolean dropOnDeath = false;//TODO: from config
	static HashSet<Integer> playerEntityIds = new HashSet<Integer>();

	@SubscribeEvent
	public void playerLoggedInEvent(PlayerLoggedInEvent event) {
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		if (side == Side.SERVER) {
			EventExtendedInventory.playerEntityIds.add(event.player.getEntityId());
		}
	}

	public static void syncBaubles(EntityPlayer player) {
		int size = InventoryPlayerExtended.ICOL*InventoryPlayerExtended.IROW;
		for (int a = 0; a < size; a++) {
			UtilPlayerInventoryFilestorage.getPlayerInventory(player).syncSlotToClients(a);
		}
	}

	@SubscribeEvent
	public void playerTick(PlayerEvent.LivingUpdateEvent event) {

		// player events
		if (event.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntity();

			if (!playerEntityIds.isEmpty() && playerEntityIds.contains(player.getEntityId())) {
				syncBaubles(player);
				playerEntityIds.remove(player.getEntityId());
			}
		}
	}
	@SubscribeEvent
	public void playerDeath(PlayerDropsEvent event) {
		if(dropOnDeath == false){
			return;
		}
		//else drop on death is true, so do it
		Entity entity = event.getEntity();
		World world = entity.getEntityWorld();
		
		if (entity instanceof EntityPlayer && !world.isRemote && !world.getGameRules().getBoolean("keepInventory")) {
			UtilPlayerInventoryFilestorage.getPlayerInventory(event.getEntityPlayer()).dropItemsAt(event.getDrops(), event.getEntityPlayer());
		}
	}

	@SubscribeEvent
	public void playerLoad(PlayerEvent.LoadFromFile event) {
		UtilPlayerInventoryFilestorage.clearPlayerInventory(event.getEntityPlayer());

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

		UtilPlayerInventoryFilestorage.loadPlayerInventory(event.getEntityPlayer(), playerFile, getPlayerFile(extback, event.getPlayerDirectory(), event.getEntityPlayer().getDisplayNameString()));
		EventExtendedInventory.playerEntityIds.add(event.getEntityPlayer().getEntityId());
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
		UtilPlayerInventoryFilestorage.savePlayerBaubles(event.getEntityPlayer(), getPlayerFile(ext, event.getPlayerDirectory(), event.getEntityPlayer().getDisplayNameString()), getPlayerFile(extback, event.getPlayerDirectory(), event.getEntityPlayer().getDisplayNameString()));
	}

	@Override
	public void syncConfig(Configuration config) {
		// TODO Auto-generated method stub
		
	}
}
