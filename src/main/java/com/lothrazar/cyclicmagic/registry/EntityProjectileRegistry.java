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
import java.util.List;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

@Mod.EventBusSubscriber(modid = Const.MODID)
public class EntityProjectileRegistry {

  private static List<EntityEntry> ENTITIES = new ArrayList<>();
  final static int trackingRange = 64;
  final static int updateFrequency = 20;// maybe 20 
  final static boolean sendsVelocityUpdates = true;
  static int modEntityId = 1100;

  public static void registerModEntity(Class<? extends Entity> entityClass, String entityName, @Deprecated int id) {
    ENTITIES.add(createEntityEntry(entityName, entityClass));
  }

  @SubscribeEvent
  public static void registerEntities(final RegistryEvent.Register<EntityEntry> event) {
    event.getRegistry().registerAll(ENTITIES.toArray(new EntityEntry[0]));
  }

  private static <T extends Entity> EntityEntry createEntityEntry(String name, Class<T> cls) {
    EntityEntryBuilder<T> builder = EntityEntryBuilder.create();
    builder.entity(cls);
    builder.name(Const.MODCONF + name);
    builder.id(new ResourceLocation(Const.MODID, name), modEntityId++);
    builder.tracker(trackingRange, updateFrequency, sendsVelocityUpdates);
    //    builder.egg(primaryColorIn, secondaryColorIn);
    return builder.build();
  }
}
