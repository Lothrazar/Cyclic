package com.lothrazar.cyclicmagic.event;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.util.Const;

public class EventConfigChanged implements IHasConfig{

	@SubscribeEvent
	public void onConfigChanged(OnConfigChangedEvent event) {

		if (event.getModID().equals(Const.MODID)) {

			ModMain.instance.syncConfig();
		}
	}

	@Override
	public void syncConfig(Configuration config) {
		// TODO Auto-generated method stub
		
	}
}
