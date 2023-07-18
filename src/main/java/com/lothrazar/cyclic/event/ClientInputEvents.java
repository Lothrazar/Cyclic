package com.lothrazar.cyclic.event;

import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.api.IHasClickToggle;
import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.item.crafting.CraftingBagItem;
import com.lothrazar.cyclic.item.crafting.simple.CraftingStickItem;
import com.lothrazar.cyclic.item.food.inventorycake.ItemCakeInventory;
import com.lothrazar.cyclic.item.lunchbox.ItemLunchbox;
import com.lothrazar.cyclic.item.storagebag.ItemStorageBag;
import com.lothrazar.cyclic.net.PacketItemGui;
import com.lothrazar.cyclic.net.PacketItemScroll;
import com.lothrazar.cyclic.net.PacketItemToggle;
import com.lothrazar.cyclic.registry.ClientRegistryCyclic;
import com.lothrazar.cyclic.registry.EnchantRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.util.SoundUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientInputEvents {

  @SubscribeEvent // KeyInputEvent -> KeyPressed
  public void onKeyInput(InputEvent.Key event) {
    EnchantRegistry.LAUNCH.get().onKeyInput(Minecraft.getInstance().player);
    if (ClientRegistryCyclic.CAKE.consumeClick()) {
      ItemCakeInventory.onKeyInput(Minecraft.getInstance().player);
    }
  }

  @SubscribeEvent // MouseScrollEvent -> MouseScrollingEvent
  public void onMouseEvent(InputEvent.MouseScrollingEvent event) {
    LocalPlayer player = Minecraft.getInstance().player;
    if (player.isCrouching() && player.getMainHandItem().getItem() == ItemRegistry.ENDER_BOOK.get()) {
      //
      event.setCanceled(true);
      if (!player.getCooldowns().isOnCooldown(ItemRegistry.ENDER_BOOK.get())) {
        boolean isDown = event.getScrollDelta() < 0;
        PacketRegistry.INSTANCE.sendToServer(new PacketItemScroll(player.getInventory().selected, isDown));
      }
    }
  }

  @SubscribeEvent
  public void onScreenRender(ScreenEvent.Render.Pre event) {
    Minecraft mc = Minecraft.getInstance();
    Screen screen = mc.screen;
    if (screen instanceof AbstractContainerScreen<?> gui && !(screen instanceof CreativeModeInventoryScreen)) {
      //      if (gui.getSlotUnderMouse() != null) {
      //        System.out.println("DrawScreenEvent");
      //        Slot slotHit = gui.getSlotUnderMouse();
      //        ItemStack stackTarget = slotHit.getItem();
      ItemStack maybeFood = mc.player.containerMenu.getCarried();
      List<ItemStack> boxes = ItemBaseCyclic.findAmmos(mc.player, ItemRegistry.LUNCHBOX.get());
      //        if (held.isEdible()) {
      for (ItemStack box : boxes) {
        ItemLunchbox.setHoldingEdible(box, maybeFood.isEdible());
        //        if (maybeFood.isEdible()) System.out.println(maybeFood + "DrawScreenEvent set edible " + box.getTag());
      }
      //      }
    }
  }

  /**
   * mouseclicked doesnt work right for inserting item. mouse released.pre is better for htis use case
   * 
   * REFERENCE https://github.com/P3pp3rF1y/SophisticatedCore/blob/b86b0d5a3997ee570f86d2a0074f987fe5e103ee/src/main/java/net/p3pp3rf1y/sophisticatedcore/client/ClientEventHandler.java#L128
   * 
   * finished porting from https://github.com/Lothrazar/Cyclic/commit/fcd6ea36d83d421dccdabe87e23f7fa099b6dbf2
   */
  @SubscribeEvent
  public void onMouseButtonReleased(ScreenEvent.MouseButtonReleased.Pre event) {
    Minecraft mc = Minecraft.getInstance();
    Screen screen = mc.screen;
    boolean rightClickDown = event.getButton() == 1;
    if (screen instanceof AbstractContainerScreen<?> gui && !(screen instanceof CreativeModeInventoryScreen)) {
      if (gui.getSlotUnderMouse() != null) {
        Slot slotHit = gui.getSlotUnderMouse();
        ItemStack stackTarget = slotHit.getItem();
        if (rightClickDown) {
          if (stackTarget.getItem() instanceof ItemLunchbox
              && mc.player != null
              && mc.player.containerMenu != null) {
            //
            ItemStack maybeFood = mc.player.containerMenu.getCarried();
            //
            if (maybeFood.isEdible()) {
              int slotId = gui.getSlotUnderMouse().getContainerSlot();
              SoundUtil.playSound(mc.player, SoundEvents.UI_BUTTON_CLICK);
              //              System.out.println("item Gui ");
              PacketRegistry.INSTANCE.sendToServer(new PacketItemGui(slotId, stackTarget.getItem()));
              event.setCanceled(true);
              return;
            }
          }
        }
      }
    }
  }

  @SubscribeEvent(priority = EventPriority.HIGH) // WAS MouseClickedEvent
  public void onMouseEvent(ScreenEvent.MouseButtonPressed.Pre event) {
    if (event.getScreen() == null || !(event.getScreen() instanceof AbstractContainerScreen<?>)) {
      return;
    }
    AbstractContainerScreen<?> gui = (AbstractContainerScreen<?>) event.getScreen();
    boolean rightClickDown = event.getButton() == 1;
    try {
      if (rightClickDown && gui.getSlotUnderMouse() != null) {
        Slot slotHit = gui.getSlotUnderMouse();
        ItemStack maybeCharm = slotHit.getItem();
        if (maybeCharm.getItem() instanceof IHasClickToggle) {
          PacketRegistry.INSTANCE.sendToServer(new PacketItemToggle(slotHit.index));
          event.setCanceled(true);
          //            UtilSound.playSound(ModCyclic.proxy.getClientPlayer(), SoundEvents.UI_BUTTON_CLICK);
        }
        else if (maybeCharm.getItem() instanceof ItemStorageBag
            || maybeCharm.getItem() instanceof CraftingStickItem
            || maybeCharm.getItem() instanceof CraftingBagItem) {
              PacketRegistry.INSTANCE.sendToServer(new PacketItemGui(slotHit.index, maybeCharm.getItem()));
              event.setCanceled(true);
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
