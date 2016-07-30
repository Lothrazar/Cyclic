package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.item.ItemAppleEmerald;
import com.lothrazar.cyclicmagic.item.ItemFoodCorruptedChorus;
import com.lothrazar.cyclicmagic.item.ItemFoodCrafting;
import com.lothrazar.cyclicmagic.item.ItemFoodHeart;
import com.lothrazar.cyclicmagic.item.ItemFoodInventory;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
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
      ItemRegistry.apple_emerald = new ItemAppleEmerald();
      ItemRegistry.addItem(ItemRegistry.apple_emerald, "apple_emerald");
    }
    if (enableHeartContainer) {
      ItemRegistry.heart_food = new ItemFoodHeart();
      ItemRegistry.addItem(ItemRegistry.heart_food, "heart_food");
    }
    if (enableInventoryCrafting) {
      ItemRegistry.crafting_food = new ItemFoodCrafting();
      ItemRegistry.addItem(ItemRegistry.crafting_food, "crafting_food");
    }
    if (enableInventoryUpgrade) {
      ItemRegistry.inventory_food = new ItemFoodInventory();
      ItemRegistry.addItem(ItemRegistry.inventory_food, "inventory_food");
    }
    if (enableCorruptedChorus) {
      ItemRegistry.corrupted_chorus = new ItemFoodCorruptedChorus();
      ItemRegistry.addItem(ItemRegistry.corrupted_chorus, "corrupted_chorus");
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
