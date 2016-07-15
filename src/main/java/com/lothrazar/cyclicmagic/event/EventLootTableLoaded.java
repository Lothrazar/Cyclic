package com.lothrazar.cyclicmagic.event;
import java.util.HashSet;
import java.util.Set;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventLootTableLoaded {
  private static final int RANDODEFAULT = 7;
  private Set<ResourceLocation> chests;
  public EventLootTableLoaded() {
    chests = new HashSet<ResourceLocation>();
    //anything but the starter chest
    chests.add(LootTableList.CHESTS_ABANDONED_MINESHAFT);
    chests.add(LootTableList.CHESTS_DESERT_PYRAMID);
    chests.add(LootTableList.CHESTS_END_CITY_TREASURE);
    chests.add(LootTableList.CHESTS_IGLOO_CHEST);
    chests.add(LootTableList.CHESTS_JUNGLE_TEMPLE);
    chests.add(LootTableList.CHESTS_JUNGLE_TEMPLE_DISPENSER);
    chests.add(LootTableList.CHESTS_NETHER_BRIDGE);
    chests.add(LootTableList.CHESTS_SIMPLE_DUNGEON);
    chests.add(LootTableList.CHESTS_STRONGHOLD_CORRIDOR);
    chests.add(LootTableList.CHESTS_STRONGHOLD_CROSSING);
    chests.add(LootTableList.CHESTS_STRONGHOLD_LIBRARY);
    chests.add(LootTableList.CHESTS_VILLAGE_BLACKSMITH);
  }
  @SubscribeEvent
  public void onLootTableLoad(LootTableLoadEvent event) {
    LootPool main = event.getTable().getPool("main");
    if (main == null) { return; } //TODO: any other types?
    if (event.getName() == LootTableList.CHESTS_SPAWN_BONUS_CHEST) {//when the world starts
      fillBonusChest(main);
    }
    else if (chests.contains(event.getName())) { // every pool except for spawn 
      fillGenericChest(main);
    }
    else if (event.getName() == LootTableList.CHESTS_IGLOO_CHEST) {//when the world starts
      fillIglooChest(main);
    }
    else if (event.getName() == LootTableList.CHESTS_END_CITY_TREASURE) {//when the world starts
      fillEndCityChest(main);
    }
  }
  private void fillEndCityChest(LootPool main) {
    addLoot(main, ItemRegistry.book_ender,10);
    addLoot(main, ItemRegistry.cyclic_wand_build,15);
  }
  private void fillGenericChest(LootPool main) {
    addLoot(main, ItemRegistry.tool_push);
    addLoot(main, ItemRegistry.corrupted_chorus);
    addLoot(main, ItemRegistry.emerald_boots);
    addLoot(main, ItemRegistry.sprout_seed);
    addLoot(main, ItemRegistry.heart_food);
    addLoot(main, ItemRegistry.apple_emerald);
    addLoot(main, ItemRegistry.tool_harvest_crops);
    addLoot(main, ItemRegistry.chest_sack_empty);
    addLoot(main, ItemRegistry.tool_spawn_inspect);
    addLoot(main, ItemRegistry.ender_pearl_reuse);
  }
  private void fillIglooChest(LootPool main) {
    addLoot(main, ItemRegistry.potion_snow);
    addLoot(main, ItemRegistry.ender_snow, 19);
  }
  private void fillBonusChest(LootPool main) {
    addLoot(main, ItemRegistry.sleeping_mat);
  }
  private void addLoot(LootPool main, Item item) {
    addLoot(main, item, RANDODEFAULT);
  }
  private void addLoot(LootPool main, Item item, int rando) {
    main.addEntry(new LootEntryItem(item, rando, 0, new LootFunction[0], new LootCondition[0], Const.MODRES + item.getUnlocalizedName()));
  }
}
