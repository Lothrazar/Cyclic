package com.lothrazar.cyclic.event;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.enchant.MultiJumpEnchant;
import com.lothrazar.cyclic.item.crafting.CraftingBagItem;
import com.lothrazar.cyclic.item.crafting.PacketItemGui;
import com.lothrazar.cyclic.item.crafting.simple.CraftingStickItem;
import com.lothrazar.cyclic.item.enderbook.PacketItemScroll;
import com.lothrazar.cyclic.item.lunchbox.ItemLunchbox;
import com.lothrazar.cyclic.item.storagebag.ItemStorageBag;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.library.core.IHasClickToggle;
import com.lothrazar.library.packet.PacketItemToggle;
import com.lothrazar.library.util.SoundUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class ClientInputEventHandler {

  @SubscribeEvent
  public void onKeyInput(InputEvent.Key event) {
    MultiJumpEnchant.onKeyInput(Minecraft.getInstance().player);
  }

  @SubscribeEvent // MouseScrollEvent -> MouseScrollingEvent
  public void onMouseEvent(InputEvent.MouseScrollingEvent event) {
    LocalPlayer player = Minecraft.getInstance().player;
    if (player.isCrouching() && player.getMainHandItem().getItem() == ItemRegistry.ENDER_BOOK.get()) {
      //
      event.setCanceled(true);
      if (!player.getCooldowns().isOnCooldown(player.getMainHandItem())) {
        boolean isDown = event.getScrollDeltaY() < 0;
        ClientPacketDistributor.sendToServer(new PacketItemScroll(player.getInventory().getSelectedSlot(), isDown));
      }
    }
  }

  /**
   * mouseclicked doesnt work right for inserting item. mouse released.pre is better for htis use case
   * <p>
   * REFERENCE https://github.com/P3pp3rF1y/SophisticatedCore/blob/b86b0d5a3997ee570f86d2a0074f987fe5e103ee/src/main/java/net/p3pp3rf1y/sophisticatedcore/client/ClientEventHandler.java#L128
   * <p>
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
            ItemStack maybeFood = mc.player.containerMenu.getCarried();
            // 26.1: ItemStack#getFoodProperties(LivingEntity) removed - read DataComponents.FOOD directly
            // and replicate the same canEat(canAlwaysEat) hunger-aware check it used to do internally.
            FoodProperties maybeFoodProps = maybeFood.get(DataComponents.FOOD);
            boolean isEdible = maybeFoodProps != null && mc.player.canEat(maybeFoodProps.canAlwaysEat());
            if (isEdible) {
              // inserting food must be done onMouse Released event
              // this is important. opening screens is on the other event
              // send the slot and info to the server to process with the lunchbox
              int slotId = gui.getSlotUnderMouse().getContainerSlot();
              SoundUtil.playSound(mc.player, SoundEvents.UI_BUTTON_CLICK.value());
              ClientPacketDistributor.sendToServer(new PacketItemGui(slotId, stackTarget.getItem()));
              event.setCanceled(true);
            }
          }
        }
      }
    }
  }

  @SubscribeEvent(priority = EventPriority.HIGH) // WAS MouseClickedEvent
  public void onMouseEvent(ScreenEvent.MouseButtonPressed.Pre event) {
    Minecraft mc = Minecraft.getInstance();
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
          ClientPacketDistributor.sendToServer(new PacketItemToggle(slotHit.index));
          event.setCanceled(true);
          //            UtilSound.playSound(ModCyclic.proxy.getClientPlayer(), SoundEvents.UI_BUTTON_CLICK);
        }
        else if (maybeCharm.getItem() instanceof ItemStorageBag
            || maybeCharm.getItem() instanceof CraftingStickItem
            || maybeCharm.getItem() instanceof CraftingBagItem) {
          ClientPacketDistributor.sendToServer(new PacketItemGui(slotHit.index, maybeCharm.getItem()));
          event.setCanceled(true);
        }
        else if (maybeCharm.getItem() instanceof ItemLunchbox) {
          // if you have an EMPTY hand, use this to open the GUI screen of the lunchbox
          ItemStack maybeFood = mc.player.containerMenu.getCarried();
          if (maybeFood.isEmpty()) {
            ClientPacketDistributor.sendToServer(new PacketItemGui(slotHit.index, maybeCharm.getItem()));
            event.setCanceled(true);
          }
        }
      }
    } catch (
        Exception e) { //array out of bounds, or we are in a strange third party GUI that doesnt have slots like this
      //EXAMPLE:  mod.chiselsandbits.bitbag.BagGui
      ModCyclic.LOGGER.error("click error", e);
      // so this fixes ithttps://github.com/PrinceOfAmber/Cyclic/issues/410
    }
  }
}
