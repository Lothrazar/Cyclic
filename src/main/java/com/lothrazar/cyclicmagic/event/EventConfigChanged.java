package com.lothrazar.cyclicmagic.event;

import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.util.Const;

public class EventConfigChanged {

	@SubscribeEvent
	public void onConfigChanged(OnConfigChangedEvent event) {

		if (event.getModID().equals(Const.MODID)) {

			ModMain.syncConfig();
		}
	}
}
