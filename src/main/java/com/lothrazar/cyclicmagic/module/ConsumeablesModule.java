package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.item.ItemAppleEmerald;
import com.lothrazar.cyclicmagic.item.ItemFoodCorruptedChorus;
import com.lothrazar.cyclicmagic.item.ItemFoodCrafting;
import com.lothrazar.cyclicmagic.item.ItemFoodHeart;
import com.lothrazar.cyclicmagic.item.ItemFoodInventory;
import com.lothrazar.cyclicmagic.registry.AchievementRegistry;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry.ChestType;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraftforge.common.config.Configuration;

public class ConsumeablesModule extends BaseModule {
  private boolean enableEmeraldApple;
  private boolean enableHeartContainer;
  private boolean enableInventoryCrafting;
  private boolean enableInventoryUpgrade;
  private boolean enableCorruptedChorus;
  @Override
  public void onInit() {
    if (enableEmeraldApple) {
      ItemAppleEmerald apple_emerald = new ItemAppleEmerald();
      ItemRegistry.addItem(apple_emerald, "apple_emerald");
      LootTableRegistry.registerLoot(apple_emerald);
    }
    if (enableHeartContainer) {
      ItemFoodHeart heart_food = new ItemFoodHeart();
      ItemRegistry.addItem(heart_food, "heart_food");
      ModMain.instance.events.addEvent(heart_food);
      LootTableRegistry.registerLoot(heart_food, ChestType.GENERIC, 3);
      LootTableRegistry.registerLoot(heart_food, ChestType.ENDCITY, 4);
      AchievementRegistry.registerItemAchievement(heart_food);
    }
    if (enableInventoryCrafting) {
      ItemFoodCrafting crafting_food = new ItemFoodCrafting();
      ItemRegistry.addItem(crafting_food, "crafting_food");
      LootTableRegistry.registerLoot(crafting_food, ChestType.GENERIC, 3);
    }
    if (enableInventoryUpgrade) {
      ItemFoodInventory inventory_food = new ItemFoodInventory();
      ItemRegistry.addItem(inventory_food, "inventory_food");
      LootTableRegistry.registerLoot(inventory_food, ChestType.GENERIC, 3);
    }
    if (enableCorruptedChorus) {
      ItemFoodCorruptedChorus corrupted_chorus = new ItemFoodCorruptedChorus();
      ItemRegistry.addItem(corrupted_chorus, "corrupted_chorus");
      ModMain.instance.events.addEvent(corrupted_chorus);
      LootTableRegistry.registerLoot(corrupted_chorus, ChestType.GENERIC, 10);
      LootTableRegistry.registerLoot(corrupted_chorus, ChestType.ENDCITY, 5);
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    enableEmeraldApple = config.getBoolean("EmeraldApple", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableHeartContainer = config.getBoolean("HeartContainer(food)", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableInventoryCrafting = config.getBoolean("InventoryCrafting(Food)", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableInventoryUpgrade = config.getBoolean("InventoryUpgrade(Food)", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableCorruptedChorus = config.getBoolean("CorruptedChorus(Food)", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
  }
}
