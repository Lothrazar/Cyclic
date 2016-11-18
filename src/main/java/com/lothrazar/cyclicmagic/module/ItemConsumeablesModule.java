package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.item.food.ItemAppleEmerald;
import com.lothrazar.cyclicmagic.item.food.ItemFoodCorruptedChorus;
import com.lothrazar.cyclicmagic.item.food.ItemFoodCrafting;
import com.lothrazar.cyclicmagic.item.food.ItemFoodHeart;
import com.lothrazar.cyclicmagic.item.food.ItemFoodInventory;
import com.lothrazar.cyclicmagic.item.food.ItemHorseUpgrade;
import com.lothrazar.cyclicmagic.item.food.ItemHorseUpgrade.HorseUpgradeType;
import com.lothrazar.cyclicmagic.registry.AchievementRegistry;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry.ChestType;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ItemConsumeablesModule extends BaseEventModule implements IHasConfig {
  private boolean enableEmeraldApple;
  private boolean enableHeartContainer;
  private boolean enableInventoryCrafting;
  private boolean enableInventoryUpgrade;
  private boolean enableCorruptedChorus;
  private boolean enableHorseFoodUpgrades;
  @Override
  public void onInit() {
    if (enableHorseFoodUpgrades) {
      Item emerald_carrot = new ItemHorseUpgrade(HorseUpgradeType.TYPE, new ItemStack(Items.EMERALD));
      Item lapis_carrot = new ItemHorseUpgrade(HorseUpgradeType.VARIANT, new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()));
      Item diamond_carrot = new ItemHorseUpgrade(HorseUpgradeType.HEALTH, new ItemStack(Items.DIAMOND));
      Item redstone_carrot = new ItemHorseUpgrade(HorseUpgradeType.SPEED, new ItemStack(Items.REDSTONE));
      Item ender_carrot = new ItemHorseUpgrade(HorseUpgradeType.JUMP, new ItemStack(Items.ENDER_EYE));
      ItemRegistry.addItem(emerald_carrot, "horse_upgrade_type");
      ItemRegistry.addItem(lapis_carrot, "horse_upgrade_variant");
      ItemRegistry.addItem(diamond_carrot, "horse_upgrade_health");
      ItemRegistry.addItem(redstone_carrot, "horse_upgrade_speed");
      ItemRegistry.addItem(ender_carrot, "horse_upgrade_jump");
      ModCyclic.instance.events.register(this);//for SubcribeEvent hooks
      LootTableRegistry.registerLoot(diamond_carrot, 1);
      AchievementRegistry.registerItemAchievement(diamond_carrot);
    }
    if (enableEmeraldApple) {
      ItemAppleEmerald apple_emerald = new ItemAppleEmerald();
      ItemRegistry.addItem(apple_emerald, "apple_emerald");
      LootTableRegistry.registerLoot(apple_emerald);
    }
    if (enableHeartContainer) {
      ItemFoodHeart heart_food = new ItemFoodHeart();
      ItemRegistry.addItem(heart_food, "heart_food");
      ModCyclic.instance.events.register(heart_food);
      LootTableRegistry.registerLoot(heart_food, ChestType.GENERIC, 3);
      LootTableRegistry.registerLoot(heart_food, ChestType.ENDCITY, 4);
      AchievementRegistry.registerItemAchievement(heart_food);
    }
    if (enableInventoryCrafting) {
      ItemFoodCrafting crafting_food = new ItemFoodCrafting();
      ItemRegistry.addItem(crafting_food, "crafting_food");
      LootTableRegistry.registerLoot(crafting_food, ChestType.GENERIC, 3);
      AchievementRegistry.registerItemAchievement(crafting_food);
    }
    if (enableInventoryUpgrade) {
      ItemFoodInventory inventory_food = new ItemFoodInventory();
      ItemRegistry.addItem(inventory_food, "inventory_food");
      LootTableRegistry.registerLoot(inventory_food, ChestType.GENERIC, 3);
      AchievementRegistry.registerItemAchievement(inventory_food);
    }
    if (enableCorruptedChorus) {
      ItemFoodCorruptedChorus corrupted_chorus = new ItemFoodCorruptedChorus();
      ItemRegistry.addItem(corrupted_chorus, "corrupted_chorus");
      ModCyclic.instance.events.register(corrupted_chorus);
      LootTableRegistry.registerLoot(corrupted_chorus, ChestType.GENERIC, 10);
      LootTableRegistry.registerLoot(corrupted_chorus, ChestType.ENDCITY, 5);
      AchievementRegistry.registerItemAchievement(corrupted_chorus);
    }
  }
  @SubscribeEvent
  public void onEntityInteractEvent(EntityInteract event) {
    if (event.getEntity() instanceof EntityPlayer == false) { return; }
    EntityPlayer entityPlayer = (EntityPlayer) event.getEntity();
    ItemStack held = entityPlayer.getHeldItemMainhand();
    if (held != null && held.getItem() instanceof ItemHorseUpgrade && held.stackSize > 0
        && event.getTarget() instanceof EntityHorse) {
      ItemHorseUpgrade.onHorseInteract((EntityHorse) event.getTarget(), entityPlayer, held, (ItemHorseUpgrade) held.getItem());
      event.setCanceled(true);// stop the GUI inventory opening && horse mounting
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    enableEmeraldApple = config.getBoolean("EmeraldApple", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableHeartContainer = config.getBoolean("HeartContainer(food)", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableInventoryCrafting = config.getBoolean("InventoryCrafting(Food)", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableInventoryUpgrade = config.getBoolean("InventoryUpgrade(Food)", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableCorruptedChorus = config.getBoolean("CorruptedChorus(Food)", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableHorseFoodUpgrades = config.getBoolean("HorseFood", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    String category = Const.ConfigCategory.modpackMisc;
    ItemHorseUpgrade.HEARTS_MAX = config.getInt("HorseFood Max Hearts", category, 20, 1, 100, "Maximum number of upgraded hearts");
    ItemHorseUpgrade.JUMP_MAX = config.getInt("HorseFood Max Jump", category, 6, 1, 20, "Maximum value of jump.  Naturally spawned/bred horses seem to max out at 5.5");
    ItemHorseUpgrade.SPEED_MAX = config.getInt("HorseFood Max Speed", category, 50, 1, 99, "Maximum value of speed (this is NOT blocks/per second or anything like that)");
  }
}
