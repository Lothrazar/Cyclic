package com.lothrazar.cyclic.item.animal;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.util.HorseFeedUtil;
import com.lothrazar.library.core.IEntityInteractable;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class ItemHorseCopperRadar extends ItemBaseCyclic implements IEntityInteractable {

  public static final String NBT_KEY = ModCyclic.MODID + "_carrot_copper";

  public ItemHorseCopperRadar(Properties prop) {
    super(prop);
  }

  @Override
  public void interactWith(PlayerInteractEvent.EntityInteract event) {
    if (event.getItemStack().getItem() == this
        && event.getTarget() instanceof AbstractHorse ahorse
        && !ahorse.getPersistentData().getBooleanOr(NBT_KEY, false)) {
      ahorse.getPersistentData().putBoolean(NBT_KEY, true);
      HorseFeedUtil.finishFeed(event, ahorse);
    }
  }
}
