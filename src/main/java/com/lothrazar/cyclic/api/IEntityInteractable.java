package com.lothrazar.cyclic.api;

import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;

public interface IEntityInteractable {

  /**
   * Interact with your item when this event is triggered
   * 
   * @param event
   *          for the entity interaction on this
   */
  void interactWith(EntityInteract event);
}
