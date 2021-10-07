package com.lothrazar.cyclic.base;

import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;

import net.minecraft.world.item.Item.Properties;

public abstract class ItemEntityInteractable extends ItemBase {

  public ItemEntityInteractable(Properties properties) {
    super(properties);
  }

  public abstract void interactWith(EntityInteract event);
}
