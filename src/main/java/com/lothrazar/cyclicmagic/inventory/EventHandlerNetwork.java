package com.lothrazar.cyclicmagic.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side; 

public class EventHandlerNetwork {

	@SubscribeEvent
	public void playerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		if (side == Side.SERVER) {
			EventHandlerEntity.playerEntityIds.add(event.player.getEntityId());
		}
	}

	public static void syncBaubles(EntityPlayer player) {
		for (int a = 0; a < 4; a++) {
			PlayerHandler.getPlayerInventory(player).syncSlotToClients(a);
		}
	}

}
