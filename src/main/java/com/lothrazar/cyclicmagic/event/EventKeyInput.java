package com.lothrazar.cyclicmagic.event;

import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.net.MessageBarMove;
import com.lothrazar.cyclicmagic.net.MessageSlotMove;
import com.lothrazar.cyclicmagic.proxy.ClientProxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventKeyInput implements IHasConfig{

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {

		int slot = Minecraft.getMinecraft().thePlayer.inventory.currentItem;
		detectAndFireKey(slot);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onGuiKeyboardEvent(GuiScreenEvent.KeyboardInputEvent.Pre event) {

		// only for player survival invo
		if (event.getGui() instanceof GuiInventory) {

			GuiInventory gui = (GuiInventory) event.getGui();
			if (gui.getSlotUnderMouse() != null) {
				// only becuase it expects actually a column number
				detectAndFireKey(gui.getSlotUnderMouse().slotNumber % 9);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	private void detectAndFireKey(int slot) {

		if (ClientProxy.isKeyDown(ClientProxy.keyShiftUp)) {
			ModMain.network.sendToServer(new MessageSlotMove(slot, false));
		}
		else if (ClientProxy.isKeyDown(ClientProxy.keyShiftDown)) {
			ModMain.network.sendToServer(new MessageSlotMove(slot, true));
		}
		else if (ClientProxy.isKeyDown(ClientProxy.keyBarUp)) {
			ModMain.network.sendToServer(new MessageBarMove(false));
		}
		else if (ClientProxy.isKeyDown(ClientProxy.keyBarDown)) {
			ModMain.network.sendToServer(new MessageBarMove(true));
		}
	}

	@Override
	public void syncConfig(Configuration config) {
		// TODO Auto-generated method stub
		
	}
}
