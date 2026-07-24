package com.lothrazar.cyclic.item.animal;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.util.HorseFeedUtil;
import com.lothrazar.library.core.IEntityInteractable;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class ItemHorseQuartzStep extends ItemBaseCyclic implements IEntityInteractable {

  public static final double STEP_TARGET = 2.6;

  public ItemHorseQuartzStep(Properties prop) {
    super(prop);
  }

  @Override
  public void interactWith(PlayerInteractEvent.EntityInteract event) {
    if (event.getItemStack().getItem() == this
        && event.getTarget() instanceof AbstractHorse ahorse) {
      AttributeInstance step = ahorse.getAttribute(Attributes.STEP_HEIGHT);
      if (step != null && step.getBaseValue() < STEP_TARGET) {
        step.setBaseValue(STEP_TARGET);
        HorseFeedUtil.finishFeed(event, ahorse);
      }
    }
  }
}
