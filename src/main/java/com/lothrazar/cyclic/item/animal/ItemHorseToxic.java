package com.lothrazar.cyclic.item.animal;
import com.lothrazar.library.core.IEntityInteractable;
import com.lothrazar.cyclic.item.ItemBaseCyclic;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class ItemHorseToxic extends ItemBaseCyclic implements IEntityInteractable {
  public ItemHorseToxic(Properties prop) { super(prop); }
  @Override public void interactWith(PlayerInteractEvent.EntityInteract event) { }
}
