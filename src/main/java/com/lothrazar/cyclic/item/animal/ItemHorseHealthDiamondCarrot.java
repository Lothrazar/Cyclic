/*******************************************************************************
 * The MIT License (MIT)
 *
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclic.item.animal;

import com.lothrazar.cyclic.api.IEntityInteractable;
import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.util.EntityUtil;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;

public class ItemHorseHealthDiamondCarrot extends ItemBaseCyclic implements IEntityInteractable {

  public static final int HEARTS_MAX = 40;

  public ItemHorseHealthDiamondCarrot(Properties prop) {
    super(prop);
  }

  @Override
  public void interactWith(EntityInteract event) {
    if (event.getItemStack().getItem() instanceof ItemHorseHealthDiamondCarrot
        && event.getTarget() instanceof Horse) {
      // lets go 
      Horse ahorse = (Horse) event.getTarget();
      float mh = (float) ahorse.getAttribute(Attributes.MAX_HEALTH).getValue();
      if (mh < 2 * ItemHorseHealthDiamondCarrot.HEARTS_MAX) { // 20 hearts == 40 health points
        ahorse.getAttribute(Attributes.MAX_HEALTH).setBaseValue(mh + 2);
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
        event.getItemStack().shrink(1);
        ahorse.mobInteract(event.getPlayer(), event.getHand());
        //processInteract
        //trigger eatingHorse
        EntityUtil.eatingHorse(ahorse);
      }
    }
  }
}
