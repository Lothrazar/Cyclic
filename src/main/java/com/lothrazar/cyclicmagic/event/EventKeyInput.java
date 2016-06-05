package com.lothrazar.cyclicmagic.event;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.net.PacketMovePlayerHotbar;
import com.lothrazar.cyclicmagic.net.PacketMovePlayerColumn;
import com.lothrazar.cyclicmagic.proxy.ClientProxy;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;
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

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onGuiKeyboardEvent(GuiScreenEvent.KeyboardInputEvent.Pre event) {
  
		// only for player survival invo
		if (event.getGui() instanceof GuiInventory) {

			if (isGuiKeyDown(ClientProxy.keyBarUp)) {
				ModMain.network.sendToServer(new PacketMovePlayerHotbar(true));
				return;
			}
			else if (isGuiKeyDown(ClientProxy.keyBarDown)) {
				ModMain.network.sendToServer(new PacketMovePlayerHotbar(false));
				return;
			}
			GuiInventory gui = (GuiInventory) event.getGui();
			if (gui.getSlotUnderMouse() != null) {
				// only becuase it expects actually a column number
				int slot = gui.getSlotUnderMouse().slotNumber % 9;

				if (isGuiKeyDown(ClientProxy.keyShiftUp)) {
					ModMain.network.sendToServer(new PacketMovePlayerColumn(slot, false));
				}
				else if (isGuiKeyDown(ClientProxy.keyShiftDown)) {
					ModMain.network.sendToServer(new PacketMovePlayerColumn(slot, true));
				}
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	private boolean isGuiKeyDown(KeyBinding keybinding) {
	
		// inside a GUI , we have to check the keyboard directly
		// thanks to Inventory tweaks, reminding me of alternate way to check
		// keydown while in config
		// https://github.com/Inventory-Tweaks/inventory-tweaks/blob/develop/src/main/java/invtweaks/InvTweaks.java
	
		return keybinding.isPressed() || 
				(Keyboard.isKeyDown(keybinding.getKeyCode()) &&  
					(keybinding.getKeyModifier() == null || 
					 keybinding.getKeyModifier().isActive()  
				 ) );
	}
}
