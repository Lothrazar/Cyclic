package com.lothrazar.cyclic.event;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.IHasClickToggle;
import com.lothrazar.cyclic.net.PacketItemToggle;
import com.lothrazar.cyclic.registry.PacketRegistry;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandler {

  @OnlyIn(Dist.CLIENT)
  @SubscribeEvent(priority = EventPriority.HIGH)
  public void onMouseEvent(GuiScreenEvent.MouseClickedEvent.Pre event) {
    if (event.getGui() == null || !(event.getGui() instanceof ContainerScreen)) {
      return;
    }
    ContainerScreen gui = (ContainerScreen) event.getGui();
    boolean rightClickDown = event.getButton() == 1;
    try {
      if (rightClickDown && gui.getSlotUnderMouse() != null) {
        Slot slotHit = gui.getSlotUnderMouse();
        if (!slotHit.getStack().isEmpty()) {
          ItemStack maybeCharm = slotHit.getStack();
          System.out.println(slotHit.slotNumber + "=slotHit.slotNumber;  maybehcharm eventhandler is .slot() " + maybeCharm);
          if (maybeCharm.getItem() instanceof IHasClickToggle) {
            PacketRegistry.INSTANCE.sendToServer(new PacketItemToggle(slotHit.slotNumber));
            //            UtilSound.playSound(ModCyclic.proxy.getClientPlayer(), SoundEvents.UI_BUTTON_CLICK);
            event.setCanceled(true);
          }
        }
      }
    }
    catch (Exception e) {//array out of bounds, or we are in a strange third party GUI that doesnt have slots like this
      //EXAMPLE:  mod.chiselsandbits.bitbag.BagGui
      ModCyclic.LOGGER.error("click error", e);
      // so this fixes ithttps://github.com/PrinceOfAmber/Cyclic/issues/410
    }
  }
}
