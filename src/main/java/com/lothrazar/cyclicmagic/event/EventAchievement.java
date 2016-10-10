package com.lothrazar.cyclicmagic.event;
import com.lothrazar.cyclicmagic.registry.AchievementRegistry;
import net.minecraft.item.Item;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

public class EventAchievement {
  @SubscribeEvent
  public void onPickup(EntityItemPickupEvent event) {
    if (event.getItem() != null && event.getItem().getEntityItem() != null) {
      Item stuff = event.getItem().getEntityItem().getItem();
      AchievementRegistry.trigger(event.getEntityPlayer(), stuff);
    }
  }
  @SubscribeEvent
  public void onCrafted(ItemCraftedEvent event) {
    if (event.crafting != null) {
      Item stuff = event.crafting.getItem();
      AchievementRegistry.trigger(event.player, stuff);
    }
  }
}
