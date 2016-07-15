package com.lothrazar.cyclicmagic.event;
import java.util.HashSet;
import java.util.Set;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilReflection;
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

public class EventLootTableLoaded implements IHasConfig {
  private static final int RANDODEFAULT = 7;
  private static final String LOOTPOOLNAME = "main";
  private Set<ResourceLocation> chests;
  private boolean enablePolarbears;
  private boolean enableBats;
 // private boolean enableStrayQuarts;
  private boolean enableElderGuardianDiam;
  private boolean enableEndermiteEyeCrystal;
  private boolean enableShulkerDiamCryst;
  private boolean enableSilverfishIron;
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
    LootPool main = event.getTable().getPool(LOOTPOOLNAME);
    if (main == null) {
      ModMain.logger.warn("remake NULL loot table :" + event.getName().toString());
     // LootEntry[] lootEntriesIn, LootCondition[] poolConditionsIn, RandomValueRange rollsIn, RandomValueRange bonusRollsIn, String name)
      
      System.out.println(event.getTable().toString());
    event.getTable().addPool(new LootPool(new LootEntry[0] , new LootCondition[0], new RandomValueRange(5F),new RandomValueRange(50F),  LOOTPOOLNAME)
   );
      
      System.out.println( UtilReflection.getLoot(event.getTable()));
      main = event.getTable().getPool(LOOTPOOLNAME);
      //doesnt FUCKING work items dont drup. balls.
//      event.getTable().addPool(new LootPool(new LootEntry[0] , new LootCondition[0], new RandomValueRange(0.1F),new RandomValueRange(0.8F),  "main")
//      );
      //main = event.getTable().getPool("main");
        //  [19:35:44] [Server thread/WARN] [cyclicmagic]: Loot table null for :minecraft:entities/bat

//] [cyclicmagic]: Loot table null for :minecraft:entities/endermite
//[cyclicmagic]: Loot table null for :minecraft:entities/shulker
// [cyclicmagic]: Loot table null for :minecraft:entities/wolf
     ///[cyclicmagic]: Loot table null for :minecraft:entities/ocelot
     ///[cyclicmagic]: Loot table null for :minecraft:entities/silverfish

    } //TODO: any other types?
    ModMain.logger.warn("Loot table    :" + event.getName().toString());
    if (event.getName() == LootTableList.CHESTS_SPAWN_BONUS_CHEST) {
      fillBonusChest(main);
    }
    else if (event.getName() == LootTableList.CHESTS_IGLOO_CHEST) {
      fillIglooChest(main);
    }
    else if (event.getName() == LootTableList.CHESTS_END_CITY_TREASURE) {
      fillEndCityChest(main);
    }
    else if (enableBats && event.getName() == LootTableList.ENTITIES_BAT) {
      addLoot(main, Items.LEATHER, 90);
    }
    else if (enablePolarbears && event.getName() == LootTableList.field_189969_E) {
      addLoot(main, Items.LEATHER, 45);
      addLoot(main, Item.getItemFromBlock(Blocks.WOOL), 75);
    }
//    else if (enableStrayQuarts && event.getName() == LootTableList.field_189968_an) { //STRAY
//      addLoot(main, Items.QUARTZ, 45);
//    }
    else if (enableEndermiteEyeCrystal && event.getName() == LootTableList.ENTITIES_ENDERMITE) {
      addLoot(main, Items.ENDER_EYE, 45);
      addLoot(main, Items.END_CRYSTAL, 15);
    }
    else if (enableSilverfishIron && event.getName() == LootTableList.ENTITIES_SILVERFISH) {
      addLoot(main, Items.IRON_INGOT, 45);
    }
    else if (enableShulkerDiamCryst && event.getName() == LootTableList.ENTITIES_SHULKER) {
      System.out.println("ENTITIES_SHULKER");
      System.out.println("ENTITIES_SHULKER");
      System.out.println("ENTITIES_SHULKER");
      System.out.println("ENTITIES_SHULKER");
      addLoot(main, Items.DIAMOND, 45);
      addLoot(main, Items.END_CRYSTAL, 25);
    }
    else if (enableElderGuardianDiam && event.getName() == LootTableList.ENTITIES_ELDER_GUARDIAN) {
      addLoot(main, Items.DIAMOND, 95);
      addLoot(main, Item.getItemFromBlock(Blocks.DIAMOND_BLOCK), 35);
    }
    //no else on this one, its a catch all
    if (chests.contains(event.getName())) { // every pool except for spawn 
      fillGenericChest(main);
    }
  }
  private void fillEndCityChest(LootPool main) {
    addLoot(main, ItemRegistry.book_ender, 10);
    addLoot(main, ItemRegistry.cyclic_wand_build, 15);
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
  @Override
  public void syncConfig(Configuration config) {
    enablePolarbears = config.getBoolean("PolarBearLoot", Const.ConfigCategory.mobs, true, "Polar Bears also drop wool and leather");
    enableBats = config.getBoolean("BatsLeather", Const.ConfigCategory.mobs, true, "Bats can drop leather");
    //private boolean enableStrayQuarts;
    enableElderGuardianDiam = config.getBoolean("ElderGuardianDiamonds", Const.ConfigCategory.mobs, true, "Bats can drop leather");
    enableEndermiteEyeCrystal = config.getBoolean("EndermiteEyeCrystal", Const.ConfigCategory.mobs, true, "Endermites can drop ender eyes, and rarely ender crystals");
    enableShulkerDiamCryst = config.getBoolean("ShulkerLoot", Const.ConfigCategory.mobs, true, "Shulkers now drop loot: Diamonds and rare ender crystals");
    enableSilverfishIron = config.getBoolean("SilverfishIron", Const.ConfigCategory.mobs, true, "Silverfish can drop iron ingots");
  }
}
