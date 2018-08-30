/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic.event;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.capability.IPlayerExtendedProperties;
import com.lothrazar.cyclicmagic.core.item.IHasClickToggle;
import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.core.util.UtilChat;
import com.lothrazar.cyclicmagic.core.util.UtilSound;
import com.lothrazar.cyclicmagic.core.util.UtilSpellCaster;
import com.lothrazar.cyclicmagic.item.cyclicwand.PacketSpellShiftLeft;
import com.lothrazar.cyclicmagic.item.cyclicwand.PacketSpellShiftRight;
import com.lothrazar.cyclicmagic.net.PacketItemToggle;
import com.lothrazar.cyclicmagic.net.PacketMovePlayerColumn;
import com.lothrazar.cyclicmagic.net.PacketMovePlayerHotbar;
import com.lothrazar.cyclicmagic.playerupgrade.PacketOpenExtendedInventory;
import com.lothrazar.cyclicmagic.playerupgrade.PacketOpenFakeWorkbench;
import com.lothrazar.cyclicmagic.playerupgrade.crafting.GuiPlayerExtWorkbench;
import com.lothrazar.cyclicmagic.playerupgrade.skill.GuiSkills;
import com.lothrazar.cyclicmagic.playerupgrade.storage.GuiPlayerExtended;
import com.lothrazar.cyclicmagic.playerupgrade.tools.GuiTools;
import com.lothrazar.cyclicmagic.proxy.ClientProxy;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.registry.SpellRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventKeyInput {

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onMouseInput(MouseEvent event) {
    EntityPlayer player = ModCyclic.proxy.getClientPlayer();
    if (!player.isSneaking() || event.getDwheel() == 0) {
      return;
    }
    ItemStack wand = UtilSpellCaster.getPlayerWandIfHeld(player);
    if (wand.isEmpty()) {
      return;
    }
    //if theres only one spell, do nothing
    if (SpellRegistry.getSpellbook(wand) == null || SpellRegistry.getSpellbook(wand).size() <= 1) {
      return;
    }
    if (event.getDwheel() < 0) {
      ModCyclic.network.sendToServer(new PacketSpellShiftRight());
      event.setCanceled(true);
      UtilSound.playSound(player, player.getPosition(), SoundRegistry.tool_mode);
    }
    else if (event.getDwheel() > 0) {
      ModCyclic.network.sendToServer(new PacketSpellShiftLeft());
      event.setCanceled(true);
      UtilSound.playSound(player, player.getPosition(), SoundRegistry.tool_mode);
    }
  }

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onKeyInput(InputEvent.KeyInputEvent event) {
    EntityPlayer thePlayer = ModCyclic.proxy.getClientPlayer();
    int slot = thePlayer.inventory.currentItem;
    if (ClientProxy.keyBarUp != null && ClientProxy.keyBarUp.isPressed()) {
      ModCyclic.network.sendToServer(new PacketMovePlayerHotbar(false));
    }
    else if (ClientProxy.keyBarDown != null && ClientProxy.keyBarDown.isPressed()) {
      ModCyclic.network.sendToServer(new PacketMovePlayerHotbar(true));
    }
    else if (ClientProxy.keyShiftUp != null && ClientProxy.keyShiftUp.isPressed()) {
      ModCyclic.network.sendToServer(new PacketMovePlayerColumn(slot, false));
    }
    else if (ClientProxy.keyShiftDown != null && ClientProxy.keyShiftDown.isPressed()) {
      ModCyclic.network.sendToServer(new PacketMovePlayerColumn(slot, true));
    }
    else if (ClientProxy.keyExtraInvo != null && ClientProxy.keyExtraInvo.isPressed()) {
      final IPlayerExtendedProperties data = CapabilityRegistry.getPlayerProperties(thePlayer);
      if (data.hasInventoryExtended()) {
        ModCyclic.network.sendToServer(new PacketOpenExtendedInventory());
      }
      else {
        UtilChat.sendStatusMessage(thePlayer, "locked.extended");
      }
    }
    else if (ClientProxy.keyExtraCraftin != null && ClientProxy.keyExtraCraftin.isPressed()) {
      final IPlayerExtendedProperties data = CapabilityRegistry.getPlayerProperties(thePlayer);
      if (data.hasInventoryCrafting()) {
        ModCyclic.network.sendToServer(new PacketOpenFakeWorkbench());
      }
      else {
        UtilChat.sendStatusMessage(thePlayer, "locked.crafting");
      }
    }
    else if (ClientProxy.keyWheel != null && ClientProxy.keyWheel.isPressed()) {
      final IPlayerExtendedProperties data = CapabilityRegistry.getPlayerProperties(thePlayer);
      if (data.hasInventoryExtended()) {
        // TESTING ONLY 
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
          Minecraft.getMinecraft().displayGuiScreen(new GuiSkills(thePlayer));
        else
          Minecraft.getMinecraft().displayGuiScreen(new GuiTools(thePlayer));
      }
      else {
        UtilChat.sendStatusMessage(thePlayer, "locked.extended");
      }
    }
  }

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onGuiKeyboardEvent(GuiScreenEvent.KeyboardInputEvent.Pre event) {
    // only for player survival invo
    EntityPlayer thePlayer = ModCyclic.proxy.getClientPlayer();
    if (event.getGui() instanceof GuiInventory) {
      if (ClientProxy.keyBarUp != null && isGuiKeyDown(ClientProxy.keyBarUp)) {
        ModCyclic.network.sendToServer(new PacketMovePlayerHotbar(true));
        return;
      }
      else if (ClientProxy.keyBarDown != null && isGuiKeyDown(ClientProxy.keyBarDown)) {
        ModCyclic.network.sendToServer(new PacketMovePlayerHotbar(false));
        return;
      }
      GuiInventory gui = (GuiInventory) event.getGui();
      if (gui.getSlotUnderMouse() != null) {
        // only becuase it expects actually a column number
        int slot = gui.getSlotUnderMouse().slotNumber % Const.HOTBAR_SIZE;
        if (ClientProxy.keyShiftUp != null && isGuiKeyDown(ClientProxy.keyShiftUp)) {
          ModCyclic.network.sendToServer(new PacketMovePlayerColumn(slot, false));
        }
        else if (ClientProxy.keyShiftDown != null && isGuiKeyDown(ClientProxy.keyShiftDown)) {
          ModCyclic.network.sendToServer(new PacketMovePlayerColumn(slot, true));
        }
      }
    }
    if (ClientProxy.keyExtraInvo != null && isGuiKeyDown(ClientProxy.keyExtraInvo) && event.getGui() instanceof GuiPlayerExtended) {
      thePlayer.closeScreen();
    }
    else if (ClientProxy.keyExtraCraftin != null && isGuiKeyDown(ClientProxy.keyExtraCraftin) && event.getGui() instanceof GuiPlayerExtWorkbench) {
      thePlayer.closeScreen();
    }
  }

  @SideOnly(Side.CLIENT)
  @SubscribeEvent(priority = EventPriority.HIGH)
  public void onMouseEvent(GuiScreenEvent.MouseInputEvent.Pre event) {
    if (event.getGui() == null || !(event.getGui() instanceof GuiContainer)) {
      return;
    }
    GuiContainer gui = (GuiContainer) event.getGui();
    boolean rightClickDown = false;
    //   event 
    try {
      //if you press and hold the button, eventButton becomes -1 even when buttonDown(1) is true
      //so event button is on the mouseDown and mouseUp triggers
      rightClickDown = (Mouse.getEventButton() == 1) && Mouse.isButtonDown(1);
    }
    catch (Exception e) { //array out of bounds, crazy weird unexpected mouse 
      //EXAMPLE:  mod.chiselsandbits.bitbag.BagGui
      // so this fixes ithttps://github.com/PrinceOfAmber/Cyclic/issues/410
    }
    //    System.out.println(" Mouse.getEventButton() " + Mouse.getEventButton());
    try {
      if (rightClickDown && gui.getSlotUnderMouse() != null) {
        int slot = gui.getSlotUnderMouse().slotNumber;
        if (gui.inventorySlots != null && slot < gui.inventorySlots.inventorySlots.size() &&
            gui.inventorySlots.getSlot(slot) != null && !gui.inventorySlots.getSlot(slot).getStack().isEmpty()) {
          ItemStack maybeCharm = gui.inventorySlots.getSlot(slot).getStack();
          if (maybeCharm.getItem() instanceof IHasClickToggle) {
            //example: is a charm or something
            ModCyclic.network.sendToServer(new PacketItemToggle(slot));
            EntityPlayer thePlayer = ModCyclic.proxy.getClientPlayer();
            UtilSound.playSound(thePlayer, SoundEvents.UI_BUTTON_CLICK);
            event.setCanceled(true);
          }
        }
      }
    }
    catch (Exception e) {//array out of bounds, or we are in a strange third party GUI that doesnt have slots like this
      //EXAMPLE:  mod.chiselsandbits.bitbag.BagGui
      // so this fixes ithttps://github.com/PrinceOfAmber/Cyclic/issues/410
    }
  }

  @SuppressWarnings("deprecation")
  @SideOnly(Side.CLIENT)
  private boolean isGuiKeyDown(KeyBinding keybinding) {
    if (keybinding == null) {
      return false;
    } //i think this fixes the bug? : // https://github.com/PrinceOfAmber/Cyclic/issues/198
    // inside a GUI , we have to check the keyboard directly
    // thanks to Inventory tweaks, reminding me of alternate way to check
    // keydown while in config
    // https://github.com/Inventory-Tweaks/inventory-tweaks/blob/develop/src/main/java/invtweaks/InvTweaks.java
    try { //but just to be careful, add the trycatch also
      boolean bindingPressed = keybinding.isPressed();
      boolean isKeyDown = Keyboard.isCreated() && Keyboard.isKeyDown(keybinding.getKeyCode());
      boolean validKeyModifier = (keybinding.getKeyModifier() == null ||
          keybinding.getKeyModifier().isActive());
      return bindingPressed || //either keybinding object knows its presed, ir the keyboard knows its pressed with the mod
          (isKeyDown && validKeyModifier);
    }
    catch (Exception e) {
      //java.lang.IndexOutOfBoundsException  from org.lwjgl.input.Keyboard.isKeyDown(Keyboard.java:407)
      return false;
    }
  }

  /**
   * Witchery Author Tribute
   */
  @SubscribeEvent
  public void onWitchKingReturn(PlayerLoggedInEvent event) {
    try {
      if (event.player.getGameProfile().getName().equalsIgnoreCase("Emoniph")) {
        for (EntityPlayer p : event.player.world.playerEntities) {
          p.sendMessage(new TextComponentString("The Witch King has returned!"));
        }
      }
    }
    catch (Exception e) {
      // no big deal
    }
  }
}
