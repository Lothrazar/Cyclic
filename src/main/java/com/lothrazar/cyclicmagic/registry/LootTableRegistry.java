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
import java.util.HashMap;
import java.util.Map;
import net.minecraft.item.Item;

public class LootTableRegistry {
  public static int lootChanceDefault = 4;
  public static Map<Item, Integer> iglooChest = new HashMap<Item, Integer>();
  public static Map<Item, Integer> bonusChest = new HashMap<Item, Integer>();
  public static Map<Item, Integer> genericChest = new HashMap<Item, Integer>();
  public static Map<Item, Integer> endCityChest = new HashMap<Item, Integer>();
  public static enum ChestType {
    BONUS, IGLOO, ENDCITY, GENERIC;
  }
  public static void registerLoot(Item i) {
    registerLoot(i, ChestType.GENERIC, lootChanceDefault);
  }
  public static void registerLoot(Item i, ChestType type) {
    registerLoot(i, type, lootChanceDefault);
  }
  public static void registerLoot(Item i, ChestType type, int random) {
    switch (type) {
      case BONUS:
        bonusChest.put(i, random * 2);//nobody uses this anyway, so just for fun we x2
      break;
      case ENDCITY:
        endCityChest.put(i, random);
      break;
      case GENERIC:
        genericChest.put(i, random);
      break;
      case IGLOO:
        iglooChest.put(i, random);
      break;
      default:
      break;
    }
  }
}
