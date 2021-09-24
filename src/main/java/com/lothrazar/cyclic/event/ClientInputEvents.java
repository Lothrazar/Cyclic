package com.lothrazar.cyclic.event;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.IHasClickToggle;
import com.lothrazar.cyclic.item.inventorycake.ItemCakeInventory;
import com.lothrazar.cyclic.item.storagebag.ItemStorageBag;
import com.lothrazar.cyclic.net.PacketItemGui;
import com.lothrazar.cyclic.net.PacketItemScroll;
import com.lothrazar.cyclic.net.PacketItemToggle;
import com.lothrazar.cyclic.registry.ClientRegistryCyclic;
import com.lothrazar.cyclic.registry.EnchantRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.PacketRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientInputEvents {

  @SubscribeEvent
  public void onKeyInput(KeyInputEvent event) {
    EnchantRegistry.LAUNCH.onKeyInput(Minecraft.getInstance().player);
    if (ClientRegistryCyclic.CAKE.isPressed()) {
      ItemCakeInventory.onKeyInput(Minecraft.getInstance().player);
    }
  }

  @SubscribeEvent
  public void onMouseEvent(InputEvent.MouseScrollEvent event) {
    //    PlayerEvent.Visibility
    ClientPlayerEntity player = Minecraft.getInstance().player;
    if (player.isCrouching() && player.getHeldItemMainhand().getItem() == ItemRegistry.ENDER_BOOK.get()) {
      //
      event.setCanceled(true);
      if (!player.getCooldownTracker().hasCooldown(ItemRegistry.ENDER_BOOK.get())) {
        boolean isDown = event.getScrollDelta() < 0;
        PacketRegistry.INSTANCE.sendToServer(new PacketItemScroll(player.inventory.currentItem, isDown));
      }
    }
  }

  @SubscribeEvent(priority = EventPriority.HIGH)
  public void onMouseEvent(GuiScreenEvent.MouseClickedEvent.Pre event) {
    if (event.getGui() == null || !(event.getGui() instanceof ContainerScreen<?>)) {
      return;
    }
    ContainerScreen<?> gui = (ContainerScreen<?>) event.getGui();
    boolean rightClickDown = event.getButton() == 1;
    try {
      if (rightClickDown && gui.getSlotUnderMouse() != null) {
        Slot slotHit = gui.getSlotUnderMouse();
        if (!slotHit.getStack().isEmpty()) {
          ItemStack maybeCharm = slotHit.getStack();
          if (maybeCharm.getItem() instanceof IHasClickToggle) {
            PacketRegistry.INSTANCE.sendToServer(new PacketItemToggle(slotHit.slotNumber));
            event.setCanceled(true);
            //            UtilSound.playSound(ModCyclic.proxy.getClientPlayer(), SoundEvents.UI_BUTTON_CLICK);
          }
          else if (maybeCharm.getItem() instanceof ItemStorageBag) {
            PacketRegistry.INSTANCE.sendToServer(new PacketItemGui(slotHit.slotNumber));
            event.setCanceled(true);
          }
        }
      }
    }
    catch (Exception e) { //array out of bounds, or we are in a strange third party GUI that doesnt have slots like this
      //EXAMPLE:  mod.chiselsandbits.bitbag.BagGui
      ModCyclic.LOGGER.error("click error", e);
      // so this fixes ithttps://github.com/PrinceOfAmber/Cyclic/issues/410
    }
  }
}
