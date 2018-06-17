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
package com.lothrazar.cyclicmagic.registry;

import java.util.ArrayList;
import com.lothrazar.cyclicmagic.config.EventConfigChanged;
import com.lothrazar.cyclicmagic.event.EventKeyInput;
import com.lothrazar.cyclicmagic.event.EventPlayerData;
import com.lothrazar.cyclicmagic.event.EventRender;
import com.lothrazar.cyclicmagic.item.mobs.EventHorseFeed;
import com.lothrazar.cyclicmagic.playerupgrade.EventExtendedInventory;
import com.lothrazar.cyclicmagic.potion.EventPotionTick;
import net.minecraftforge.common.MinecraftForge;

public class EventRegistry {

  private ArrayList<Object> events = new ArrayList<Object>();

  public void registerCoreEvents() {
    //    this.register(new ParticleEventManager());
    this.register(new EventConfigChanged());
    this.register(new EventExtendedInventory());
    this.register(new EventKeyInput());
    this.register(new EventPlayerData());
    this.register(new EventPotionTick());
    this.register(new EventRender());
    this.register(new EventHorseFeed());
  }

  public void register(Object e) {
    events.add(e);
  }

  public void registerAll() {
    for (Object e : events) {
      MinecraftForge.EVENT_BUS.register(e);
    }
  }
}
