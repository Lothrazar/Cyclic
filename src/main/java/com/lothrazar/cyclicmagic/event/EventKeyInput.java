package com.lothrazar.cyclicmagic.event;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.net.MessageBarDown;
import com.lothrazar.cyclicmagic.net.MessageBarUp;
import com.lothrazar.cyclicmagic.net.MessageSlotDown;
import com.lothrazar.cyclicmagic.net.MessageSlotUp;
import com.lothrazar.cyclicmagic.proxy.ClientProxy;

public class EventKeyInput{

	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event){

		if(ClientProxy.keyShiftUp.isPressed()){
			ModMain.network.sendToServer(new MessageSlotUp());
		}
		else if(ClientProxy.keyShiftDown.isPressed()){
			ModMain.network.sendToServer(new MessageSlotDown());
		}
		else if(ClientProxy.keyBarUp.isPressed()){
			ModMain.network.sendToServer(new MessageBarUp());
		}
		else if(ClientProxy.keyBarDown.isPressed()){
			ModMain.network.sendToServer(new MessageBarDown());
		}
	}
}
