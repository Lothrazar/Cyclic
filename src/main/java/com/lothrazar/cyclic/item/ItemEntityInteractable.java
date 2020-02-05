package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.base.ItemBase;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;

public abstract class ItemEntityInteractable extends ItemBase {

  public ItemEntityInteractable(Properties properties) {
    super(properties);
  }

  public abstract void interactWith(EntityInteract event);
}
