package com.lothrazar.cyclicmagic.registry;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.item.Item;

public class LootTableRegistry {
  public static final int RANDODEFAULT = 7;
  public static Map<Item, Integer> iglooChest = new HashMap<Item, Integer>();
  public static Map<Item, Integer> bonusChest = new HashMap<Item, Integer>();
  public static Map<Item, Integer> genericChest = new HashMap<Item, Integer>();
  public static Map<Item, Integer> endCityChest = new HashMap<Item, Integer>();
  public static enum ChestType {
    BONUS, IGLOO, ENDCITY, GENERIC;
  }
  public static void registerLoot(Item i) {
    registerLoot(i, ChestType.GENERIC, RANDODEFAULT);
  }
  public static void registerLoot(Item i, int random) {
    registerLoot(i, ChestType.GENERIC, random);
  }
  public static void registerLoot(Item i, ChestType type) {
    registerLoot(i, type, RANDODEFAULT);
  }
  public static void registerLoot(Item i, ChestType type, int random) {
    switch (type) {
    case BONUS:
      bonusChest.put(i, random);
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
