package com.lothrazar.cyclicmagic.event;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.net.PacketMovePlayerHotbar;
import com.lothrazar.cyclicmagic.net.PacketOpenExtendedInventory;
import com.lothrazar.cyclicmagic.net.PacketFakeWorkbench;
import com.lothrazar.cyclicmagic.net.PacketMovePlayerColumn;
import com.lothrazar.cyclicmagic.proxy.ClientProxy;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry.IPlayerExtendedProperties;
import com.lothrazar.cyclicmagic.util.Const;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventKeyInput {
  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onKeyInput(InputEvent.KeyInputEvent event) {
    EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
    int slot = thePlayer.inventory.currentItem;
    if (ClientProxy.keyBarUp != null && ClientProxy.keyBarUp.isPressed()) {
      ModMain.network.sendToServer(new PacketMovePlayerHotbar(false));
    }
    else if (ClientProxy.keyBarDown != null && ClientProxy.keyBarDown.isPressed()) {
      ModMain.network.sendToServer(new PacketMovePlayerHotbar(true));
    }
    else if (ClientProxy.keyShiftUp != null && ClientProxy.keyShiftUp.isPressed()) {
      ModMain.network.sendToServer(new PacketMovePlayerColumn(slot, false));
    }
    else if (ClientProxy.keyShiftDown != null && ClientProxy.keyShiftDown.isPressed()) {
      ModMain.network.sendToServer(new PacketMovePlayerColumn(slot, true));
    }
    else if (ClientProxy.keyExtraInvo != null && ClientProxy.keyExtraInvo.isPressed()) {
      final IPlayerExtendedProperties data = CapabilityRegistry.getPlayerProperties(thePlayer);
      if (data.hasInventoryExtended() == false) {
        //then open the normal inventory
        Minecraft.getMinecraft().displayGuiScreen(new GuiInventory(thePlayer));
      }
      else {
        ModMain.network.sendToServer(new PacketOpenExtendedInventory());
      }
    }
    else if (ClientProxy.keyExtraCraftin != null && ClientProxy.keyExtraCraftin.isPressed()) {
      final IPlayerExtendedProperties data = CapabilityRegistry.getPlayerProperties(thePlayer);
      if (data.hasInventoryCrafting() == false) {
        //then open the normal inventory
        Minecraft.getMinecraft().displayGuiScreen(new GuiInventory(thePlayer));
      }
      else {
        ModMain.network.sendToServer(new PacketFakeWorkbench());
      }
    }
  }
  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onGuiKeyboardEvent(GuiScreenEvent.KeyboardInputEvent.Pre event) {
    // only for player survival invo
    if (event.getGui() instanceof GuiInventory) {
      if (ClientProxy.keyBarUp != null && isGuiKeyDown(ClientProxy.keyBarUp)) {
        ModMain.network.sendToServer(new PacketMovePlayerHotbar(true));
        return;
      }
      else if (ClientProxy.keyBarDown != null && isGuiKeyDown(ClientProxy.keyBarDown)) {
        ModMain.network.sendToServer(new PacketMovePlayerHotbar(false));
        return;
      }
      GuiInventory gui = (GuiInventory) event.getGui();
      if (gui.getSlotUnderMouse() != null) {
        // only becuase it expects actually a column number
        int slot = gui.getSlotUnderMouse().slotNumber % Const.HOTBAR_SIZE;
        if (ClientProxy.keyShiftUp != null && isGuiKeyDown(ClientProxy.keyShiftUp)) {
          ModMain.network.sendToServer(new PacketMovePlayerColumn(slot, false));
        }
        else if (ClientProxy.keyShiftDown != null && isGuiKeyDown(ClientProxy.keyShiftDown)) {
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
                keybinding.getKeyModifier().isActive()));
  }
}
