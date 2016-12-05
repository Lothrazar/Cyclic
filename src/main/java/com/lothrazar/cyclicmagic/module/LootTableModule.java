package com.lothrazar.cyclicmagic.module;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.ModCyclic;
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
        ModCyclic.logger.error("could not insert Loot Pool for table :" + event.getName().toString());
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
      addLoot(main, Items.LEATHER, ConfigLootTable.BatLeather);
    }
    else if (enablePolarbears && event.getName() == LootTableList.field_189969_E) {
      addLoot(main, Items.LEATHER, ConfigLootTable.PolarbearLeather);
      addLoot(main, Item.getItemFromBlock(Blocks.WOOL), ConfigLootTable.PolarbearWool);
    }
    else if (enableStrayPackedIce && event.getName() == LootTableList.field_189968_an) { //STRAY
      addLoot(main, Item.getItemFromBlock(Blocks.PACKED_ICE), ConfigLootTable.StrayPackedIce);
    }
    else if (enableEndermiteEyeCrystal && event.getName() == LootTableList.ENTITIES_ENDERMITE) {
      addLoot(main, Items.ENDER_EYE, ConfigLootTable.EndermiteEnderEye);
      addLoot(main, Items.END_CRYSTAL, ConfigLootTable.EndermiteCrystal);
    }
    else if (enableSilverfishIron && event.getName() == LootTableList.ENTITIES_SILVERFISH) {
      addLoot(main, Items.IRON_INGOT, ConfigLootTable.SilverfishIron);
    }
    else if (enableShulkerDiamCryst && event.getName() == LootTableList.ENTITIES_SHULKER) {
      addLoot(main, Items.DIAMOND, ConfigLootTable.ShulkerDiam);
      addLoot(main, Items.END_CRYSTAL, ConfigLootTable.ShulkerCrystal);
    }
    else if (enableElderGuardianDiam && event.getName() == LootTableList.ENTITIES_ELDER_GUARDIAN) {
      addLoot(main, Items.DIAMOND, ConfigLootTable.ElderGuardianDiam);
      addLoot(main, Item.getItemFromBlock(Blocks.DIAMOND_BLOCK), ConfigLootTable.ElderGuardianDiamBlock);
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
  @SuppressWarnings("unused")
  private void addLoot(LootPool main, Item item) {
    addLoot(main, item, LootTableRegistry.lootChanceDefault);
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
    enableElderGuardianDiam = config.getBoolean("ElderGuardianDiamonds", Const.ConfigCategory.mobs, true, "Elder Guardians (the boss ones) can drop diamonds.");
    enableEndermiteEyeCrystal = config.getBoolean("EndermiteEyeCrystal", Const.ConfigCategory.mobs, true, "Endermites can drop ender eyes, and rarely ender crystals");
    enableShulkerDiamCryst = config.getBoolean("ShulkerLoot", Const.ConfigCategory.mobs, true, "Shulkers now drop loot: Diamonds and rare ender crystals");
    enableSilverfishIron = config.getBoolean("SilverfishIron", Const.ConfigCategory.mobs, true, "Silverfish can drop iron ingots");
    enableStrayPackedIce = config.getBoolean("StraySkeletonPackedIce", Const.ConfigCategory.mobs, true, "Strays (he new skeleton variants from cold biomes) can drop packed ice");
    enableChestLoot = config.getBoolean("ChestLoot", Const.ConfigCategory.worldGen, true, "If true, then enabled items and blocks from this mod can appear in loot chests");
    LootTableRegistry.lootChanceDefault = config.getInt("ChestLootChance", Const.ConfigCategory.worldGen, 4, 1, 99, "If ChestLoot is true, this is the default chance a cyclic item will show up as treasure.");
    String category = Const.ConfigCategory.mobs + ".mobloottable";
    String comment = "Customize mob loot table percentage.  Only works if the respective config is true";
    int minValue = 1, maxValue = 99;
    ConfigLootTable.BatLeather = config.getInt("BatLeather", category, 90, minValue, maxValue, comment);
    ConfigLootTable.PolarbearWool = config.getInt("PolarbearWool", category, 75, minValue, maxValue, comment);
    ConfigLootTable.PolarbearLeather = config.getInt("PolarbearLeather", category, 95, minValue, maxValue, comment);
    ConfigLootTable.StrayPackedIce = config.getInt("StrayPackedIce", category, 35, minValue, maxValue, comment);
    ConfigLootTable.EndermiteCrystal = config.getInt("EndermiteCrystal", category, 10, minValue, maxValue, comment);
    ConfigLootTable.EndermiteEnderEye = config.getInt("EndermiteEnderEye", category, 25, minValue, maxValue, comment);
    ConfigLootTable.SilverfishIron = config.getInt("SilverfishIron", category, 25, minValue, maxValue, comment);
    ConfigLootTable.ShulkerCrystal = config.getInt("ShulkerCrystal", category, 5, minValue, maxValue, comment);
    ConfigLootTable.ShulkerDiam = config.getInt("ShulkerDiamond", category, 45, minValue, maxValue, comment);
    ConfigLootTable.ElderGuardianDiamBlock = config.getInt("ElderGuardianDiamBlock", category, 35, minValue, maxValue, comment);
    ConfigLootTable.ElderGuardianDiam = config.getInt("ElderGuardianDiam", category, 95, minValue, maxValue, comment);
    //TODO: ZOMIBE VILL EMERALD
  }
  public static class ConfigLootTable {
    public static int BatLeather;
    public static int PolarbearWool;
    public static int PolarbearLeather;
    public static int StrayPackedIce;
    public static int EndermiteCrystal;
    public static int EndermiteEnderEye;
    public static int SilverfishIron;
    public static int ShulkerCrystal;
    public static int ShulkerDiam;
    public static int ElderGuardianDiamBlock;
    public static int ElderGuardianDiam;
  }
}
