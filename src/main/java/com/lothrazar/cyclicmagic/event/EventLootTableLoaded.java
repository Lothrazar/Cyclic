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
    
    

    chests.add(LootTableList.CHESTS_SPAWN_BONUS_CHEST);//when the world starts
  }
  @SubscribeEvent
  public void onLootTableLoad(LootTableLoadEvent event) {
    if (chests.contains(event.getName())) {
      LootPool main = event.getTable().getPool("main");
      if (main != null) {
        System.out.println("woo adding items"+event.getName());
        addLoot(main, ItemRegistry.tool_push);
        addLoot(main, ItemRegistry.corrupted_chorus);
        addLoot(main, ItemRegistry.emerald_boots);
        addLoot(main, ItemRegistry.sprout_seed);
        addLoot(main, ItemRegistry.heart_food);
      }
    }
  }
  private void addLoot(LootPool main, Item item) {
    main.addEntry(new LootEntryItem(item, 7, 0, new LootFunction[0], new LootCondition[0], Const.MODRES + item.getUnlocalizedName()));
  }
}
