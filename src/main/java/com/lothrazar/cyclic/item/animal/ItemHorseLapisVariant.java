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

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.util.HorseFeedUtil;
import com.lothrazar.library.core.IEntityInteractable;
import net.minecraft.world.entity.animal.equine.Horse;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class ItemHorseLapisVariant extends ItemBaseCyclic implements IEntityInteractable {

  public ItemHorseLapisVariant(Properties prop) {
    super(prop);
  }

  @Override
  public void interactWith(PlayerInteractEvent.EntityInteract event) {
    if (event.getItemStack().getItem() == this
        && event.getTarget() instanceof Horse
        //        && event.getWorld().isRemote == false
        && !event.getEntity().getCooldowns().isOnCooldown(event.getItemStack())) {
      // lets go 
      Horse ahorse = (Horse) event.getTarget();
      int seed = event.getLevel().getRandom().nextInt(7);
      //setHorseVariant
      //  access transformers
      // ahorse.getEntityData().set(Horse.DATA_ID_TYPE_VARIANT, (seed | event.getLevel().random.nextInt(5) << 8));
      event.getEntity().getCooldowns().addCooldown(event.getItemStack(), 10);
      HorseFeedUtil.finishFeed(event, ahorse);
    }
  }
}
