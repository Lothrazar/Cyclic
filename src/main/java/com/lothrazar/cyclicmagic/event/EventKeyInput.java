package com.lothrazar.cyclicmagic.event;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.net.PacketMovePlayerHotbar;
import com.lothrazar.cyclicmagic.net.PacketMovePlayerColumn;
import com.lothrazar.cyclicmagic.proxy.ClientProxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventKeyInput{

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
	 
		if (ClientProxy.keyBarUp.isPressed()) {
			ModMain.network.sendToServer(new PacketMovePlayerHotbar(false));
		}
		else if (ClientProxy.keyBarDown.isPressed()) {
			ModMain.network.sendToServer(new PacketMovePlayerHotbar(true));
		}
		else if (ClientProxy.keyShiftUp.isPressed()) {
			ModMain.network.sendToServer(new PacketMovePlayerColumn(slot, false));
		}
		else if (ClientProxy.keyShiftDown.isPressed()) {
			ModMain.network.sendToServer(new PacketMovePlayerColumn(slot, true));
		}
	}
}
