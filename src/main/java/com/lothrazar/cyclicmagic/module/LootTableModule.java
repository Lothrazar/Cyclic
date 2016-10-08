package com.lothrazar.cyclicmagic.module;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
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
  private boolean enablePolarbears;
  private boolean enableBats;
  // private boolean enableStrayQuarts;
  private boolean enableElderGuardianDiam;
  private boolean enableEndermiteEyeCrystal;
  private boolean enableShulkerDiamCryst;
  private boolean enableSilverfishIron;
  private boolean enableStrayPackedIce;
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
        ModMain.logger.error("could not insert Loot Pool for table :" + event.getName().toString());
        return;
      }
    }
    if (enableChestLoot) {
      onLootChestTableLoad(main, event);
    }
    onLootEntityTableLoad(main, event); //each entity has own table
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
  private void onLootEntityTableLoad(LootPool main, LootTableLoadEvent event) {
    if (enableBats && event.getName() == LootTableList.ENTITIES_BAT) {
      addLoot(main, Items.LEATHER, 90);
    }
    else if (enablePolarbears && event.getName() == LootTableList.field_189969_E) {
      addLoot(main, Items.LEATHER, 45);
      addLoot(main, Item.getItemFromBlock(Blocks.WOOL), 75);
    }
    else if (enableStrayPackedIce && event.getName() == LootTableList.field_189968_an) { //STRAY
      addLoot(main, Item.getItemFromBlock(Blocks.PACKED_ICE), 35);
    }
    else if (enableEndermiteEyeCrystal && event.getName() == LootTableList.ENTITIES_ENDERMITE) {
      addLoot(main, Items.ENDER_EYE, 25);
      addLoot(main, Items.END_CRYSTAL, 10);
    }
    else if (enableSilverfishIron && event.getName() == LootTableList.ENTITIES_SILVERFISH) {
      addLoot(main, Items.IRON_INGOT, 45);
    }
    else if (enableShulkerDiamCryst && event.getName() == LootTableList.ENTITIES_SHULKER) {
      addLoot(main, Items.DIAMOND, 45);
      addLoot(main, Items.END_CRYSTAL, 25);
    }
    else if (enableElderGuardianDiam && event.getName() == LootTableList.ENTITIES_ELDER_GUARDIAN) {
      addLoot(main, Items.DIAMOND, 95);
      addLoot(main, Item.getItemFromBlock(Blocks.DIAMOND_BLOCK), 35);
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
    addLoot(main, ItemRegistry.tool_harvest_crops);
    addLoot(main, ItemRegistry.tool_spawn_inspect);
    addLoot(main, ItemRegistry.ender_pearl_reuse);
    addLoot(main, ItemRegistry.storage_bag);
    addLoot(main, ItemRegistry.ender_dungeon);
    addLoot(main, ItemRegistry.ender_lightning);
    addLoot(main, ItemRegistry.ender_tnt_1);
    addLoot(main, ItemRegistry.ender_tnt_2);
    addLoot(main, ItemRegistry.emerald_axe);
    addLoot(main, ItemRegistry.emerald_hoe);
    addLoot(main, ItemRegistry.emerald_pickaxe);
    addLoot(main, ItemRegistry.emerald_shovel);
    addLoot(main, ItemRegistry.emerald_sword);
    addLoot(main, ItemRegistry.emerald_boots);
    addLoot(main, ItemRegistry.emerald_chest);
    addLoot(main, ItemRegistry.emerald_head);
    addLoot(main, ItemRegistry.emerald_legs);
  }
  private void fillIglooChest(LootPool main) {
    fillPoolFromMap(main, LootTableRegistry.iglooChest);
  }
  private void fillBonusChest(LootPool main) {
    fillPoolFromMap(main, LootTableRegistry.bonusChest);
  }
  private void addLoot(LootPool main, Item item) {
    addLoot(main, item, LootTableRegistry.RANDODEFAULT);
  }
  private void addLoot(LootPool main, Item item, int rando) {
    if (item != null) {//shortcut fix bc of new module config system that can delete items
      main.addEntry(new LootEntryItem(item, rando, 0, new LootFunction[0], new LootCondition[0], Const.MODRES + item.getUnlocalizedName()));
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    enablePolarbears = config.getBoolean("PolarBearLoot", Const.ConfigCategory.mobs, true, "Polar Bears also drop wool and leather");
    enableBats = config.getBoolean("BatsLeather", Const.ConfigCategory.mobs, true, "Bats can drop leather");
    //private boolean enableStrayQuarts;
    enableElderGuardianDiam = config.getBoolean("ElderGuardianDiamonds", Const.ConfigCategory.mobs, true, "Elder Guardians (the boss ones) can drop diamonds.");
    enableEndermiteEyeCrystal = config.getBoolean("EndermiteEyeCrystal", Const.ConfigCategory.mobs, true, "Endermites can drop ender eyes, and rarely ender crystals");
    enableShulkerDiamCryst = config.getBoolean("ShulkerLoot", Const.ConfigCategory.mobs, true, "Shulkers now drop loot: Diamonds and rare ender crystals");
    enableSilverfishIron = config.getBoolean("SilverfishIron", Const.ConfigCategory.mobs, true, "Silverfish can drop iron ingots");
    enableStrayPackedIce = config.getBoolean("StraySkeletonPackedIce", Const.ConfigCategory.mobs, true, "Strays (he new skeleton variants from cold biomes) can drop packed ice");
    enableChestLoot = config.getBoolean("ChestLoot", Const.ConfigCategory.worldGen, true, "If true, then enabled items and blocks from this mod can appear in loot chests");
  }
}
