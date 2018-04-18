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
package com.lothrazar.cyclicmagic.module;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LootTableModule extends BaseEventModule implements IHasConfig {

  private static final String LOOTPOOLNAME = "main";
  private Set<ResourceLocation> chests;
  private boolean enableChestLoot;

  public LootTableModule() {
    chests = new HashSet<ResourceLocation>();
    //anything but the starter chest
    chests.add(LootTableList.CHESTS_ABANDONED_MINESHAFT);
    chests.add(LootTableList.CHESTS_DESERT_PYRAMID);
    chests.add(LootTableList.CHESTS_JUNGLE_TEMPLE);
    //    chests.add(LootTableList.CHESTS_JUNGLE_TEMPLE_DISPENSER);
    chests.add(LootTableList.CHESTS_NETHER_BRIDGE);
    chests.add(LootTableList.CHESTS_SIMPLE_DUNGEON);
    chests.add(LootTableList.CHESTS_STRONGHOLD_CORRIDOR);
    chests.add(LootTableList.CHESTS_STRONGHOLD_CROSSING);
    chests.add(LootTableList.CHESTS_STRONGHOLD_LIBRARY);
    chests.add(LootTableList.CHESTS_VILLAGE_BLACKSMITH);
  }

  @SubscribeEvent
  public void onLootTableLoad(LootTableLoadEvent event) {
    LootPool main = event.getTable().getPool(LOOTPOOLNAME);
    if (main == null) {
      //create my own.  EX: mobs that have no drops (bats) also have empty loot table, so i have to inject an entry in the table before I fill it
      event.getTable().addPool(new LootPool(new LootEntry[0], new LootCondition[0], new RandomValueRange(1F, 2F), new RandomValueRange(1F, 1F), LOOTPOOLNAME));
      main = event.getTable().getPool(LOOTPOOLNAME);
      if (main == null) {
        ModCyclic.logger.error("could not insert Loot Pool for table :" + event.getName().toString());
        return;
      }
    }
    if (enableChestLoot) {
      onLootChestTableLoad(main, event);
    }
  }

  private void onLootChestTableLoad(LootPool main, LootTableLoadEvent event) {
    if (event.getName() == LootTableList.CHESTS_SPAWN_BONUS_CHEST) {
      fillBonusChest(main);
    }
    else if (event.getName() == LootTableList.CHESTS_IGLOO_CHEST) {
      fillIglooChest(main);
    }
    else if (event.getName() == LootTableList.CHESTS_END_CITY_TREASURE) {
      fillEndCityChest(main);
    }
    else if (chests.contains(event.getName())) { // every other pool 
      fillGenericChest(main);
    }
  }

  private void fillEndCityChest(LootPool main) {
    fillPoolFromMap(main, LootTableRegistry.endCityChest);
  }

  private void fillPoolFromMap(LootPool main, Map<Item, Integer> map) {
    synchronized (map) {
      for (Map.Entry<Item, Integer> entry : map.entrySet()) {
        addLoot(main, entry.getKey(), entry.getValue());
      }
    }
  }

  private void fillGenericChest(LootPool main) {
    fillPoolFromMap(main, LootTableRegistry.genericChest);
  }

  private void fillIglooChest(LootPool main) {
    fillPoolFromMap(main, LootTableRegistry.iglooChest);
  }

  private void fillBonusChest(LootPool main) {
    fillPoolFromMap(main, LootTableRegistry.bonusChest);
  }

  //  @SuppressWarnings("unused")
  //  private void addLoot(LootPool main, Item item) {
  //    addLoot(main, item, LootTableRegistry.lootChanceDefault);
  //  }
  private void addLoot(LootPool main, Item item, int rando) {
    if (item != null) {//shortcut fix bc of new module config system that can delete items   
      main.addEntry(new LootEntryItem(item, rando, 0, new LootFunction[0], new LootCondition[0], Const.MODRES + item.getUnlocalizedName()));
    }
  }

  @Override
  public void syncConfig(Configuration config) {
    enableChestLoot = config.getBoolean("ChestLoot", Const.ConfigCategory.worldGen, true, "If true, then enabled items and blocks from this mod can appear in loot chests");
    LootTableRegistry.lootChanceDefault = config.getInt("ChestLootChance", Const.ConfigCategory.worldGen, 4, 1, 99, "If ChestLoot is true, this is the default chance a cyclic item will show up as treasure.");
  }
}
