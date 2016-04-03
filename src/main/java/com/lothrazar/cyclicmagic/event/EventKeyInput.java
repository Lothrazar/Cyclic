package com.lothrazar.cyclicmagic.event;

import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.net.MessageBarDown;
import com.lothrazar.cyclicmagic.net.MessageBarUp;
import com.lothrazar.cyclicmagic.net.MessageSlotDown;
import com.lothrazar.cyclicmagic.net.MessageSlotUp;
import com.lothrazar.cyclicmagic.proxy.ClientProxy;

public class EventKeyInput{

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event){

		detectAndFireKey();
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onGuiKeyboardEvent(GuiScreenEvent.KeyboardInputEvent.Pre event){

		// only for player survival invo
		if(event.getGui() instanceof GuiInventory){

			detectAndFireKey();
		}
	}

	@SideOnly(Side.CLIENT)
	private void detectAndFireKey(){

		if(ClientProxy.isKeyDown(ClientProxy.keyShiftUp)){
			ModMain.network.sendToServer(new MessageSlotUp());
		}
		else if(ClientProxy.isKeyDown(ClientProxy.keyShiftDown)){
			ModMain.network.sendToServer(new MessageSlotDown());
		}
		else if(ClientProxy.isKeyDown(ClientProxy.keyBarUp)){
			ModMain.network.sendToServer(new MessageBarUp());
		}
		else if(ClientProxy.isKeyDown(ClientProxy.keyBarDown)){
			ModMain.network.sendToServer(new MessageBarDown());
		}
	}
}
