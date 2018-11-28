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
package com.lothrazar.cyclicmagic.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import com.lothrazar.cyclicmagic.ModCyclic;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

@SuppressWarnings("rawtypes")
public class UtilReflection {

  public static Field getPrivateField(String name, String mapping, Class c) {
    try {
      for (Field f : c.getDeclaredFields()) {
        //field_175563_bv in my snapshot
        if (f.getName().equals(name) || f.getName().equals(mapping)) {
          f.setAccessible(true);
          return f;
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Return a property of obj that is of type T the first one found
   * 
   * tried but failed to make this generic. i would like the returning casted type Block to be generic and/or passed in. then you would call Block crop = UtilReflection.getFirstOfType<Block >(obj);
   * Item crop = UtilReflection.getFirstOfType<Item >(obj); and so on.
   * 
   * also recursively checks parent classes with getSuperclass()
   * 
   * @param obj
   * @return T
   */
  public static Object getFirstPrivate(Object obj, Class c) {
    Class<?> classLevel = obj.getClass();
    while (classLevel != null) {
      for (Field f : classLevel.getDeclaredFields()) {
        f.setAccessible(true);
        try {
          if (c.isInstance(f.get(obj))) {
            //if (f.get(obj) instanceof Block) {
            return f.get(obj);
          }
        }
        catch (Exception e) {
          continue;
        }
      }
      //not found? try parent class 
      classLevel = classLevel.getSuperclass();
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public static List<LootPool> getLoot(Object obj) {
    for (Field f : obj.getClass().getDeclaredFields()) {
      f.setAccessible(true);
      try {
        if (f.get(obj) instanceof List<?> && (List<LootPool>) f.get(obj) != null) {
          return (List<LootPool>) f.get(obj);
        }
      }
      catch (ClassCastException e) {
        continue;
      }
      catch (IllegalArgumentException e) {
        continue;
      }
      catch (IllegalAccessException e) {
        continue;
      }
    }
    return null;
  }

  public static void callPrivateMethod(Class theClass, Object obj, String name, String obsName) {
    try {
      Method m = ReflectionHelper.findMethod(theClass, name, obsName);
      if (m != null) {
        m.invoke(obj);
      }
      else {
        ModCyclic.logger.error("Private function not found on " + theClass.getName() + " : " + name);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
