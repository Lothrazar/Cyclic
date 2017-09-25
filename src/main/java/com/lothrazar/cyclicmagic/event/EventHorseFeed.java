package com.lothrazar.cyclicmagic.event;
import com.lothrazar.cyclicmagic.item.food.ItemHorseUpgrade;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHorseFeed {
  @SubscribeEvent
  public void onEntityInteractEvent(EntityInteract event) {
    if (event.getEntity() instanceof EntityPlayer == false) {
      return;
    }
    EntityPlayer entityPlayer = (EntityPlayer) event.getEntity();
    ItemStack held = entityPlayer.getHeldItemMainhand();
    if (held != null && held.getItem() instanceof ItemHorseUpgrade && held.getCount() > 0
        && event.getTarget() instanceof AbstractHorse) {
      ItemHorseUpgrade.onHorseInteract((AbstractHorse) event.getTarget(), entityPlayer, held, (ItemHorseUpgrade) held.getItem());
      event.setCanceled(true);// stop the GUI inventory opening && horse mounting
    }
  }
}
