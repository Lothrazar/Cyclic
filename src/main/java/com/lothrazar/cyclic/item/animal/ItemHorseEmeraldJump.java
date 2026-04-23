package com.lothrazar.cyclic.item.animal;
import com.lothrazar.cyclic.api.IEntityInteractable;
import com.lothrazar.cyclic.item.ItemBaseCyclic;
import net.minecraft.world.InteractionResult;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class ItemHorseEmeraldJump extends ItemBaseCyclic implements IEntityInteractable {
  public ItemHorseEmeraldJump(Properties prop) { super(prop); }
  @Override public void interactWith(PlayerInteractEvent.EntityInteract event) { }
}
