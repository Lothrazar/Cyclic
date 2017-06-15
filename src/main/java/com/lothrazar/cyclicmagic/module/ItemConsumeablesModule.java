package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.component.playerextensions.ItemFoodCrafting;
import com.lothrazar.cyclicmagic.component.playerextensions.ItemFoodInventory;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.item.ItemAppleEmerald;
import com.lothrazar.cyclicmagic.item.ItemAppleLapis;
import com.lothrazar.cyclicmagic.item.ItemChorusCorrupted;
import com.lothrazar.cyclicmagic.item.ItemChorusGlowing;
import com.lothrazar.cyclicmagic.item.ItemHeartContainer;
import com.lothrazar.cyclicmagic.item.ItemHorseUpgrade;
import com.lothrazar.cyclicmagic.item.ItemHorseUpgrade.HorseUpgradeType;
import com.lothrazar.cyclicmagic.registry.AchievementRegistry;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry.ChestType;
import net.minecraft.entity.passive.AbstractHorse;
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
  private boolean enableGlowingChorus;
  private boolean enableLapisApple;
  @Override
  public void onPreInit() {
    if (enableHorseFoodUpgrades) {
      Item emerald_carrot = new ItemHorseUpgrade(HorseUpgradeType.TYPE, new ItemStack(Items.EMERALD));
      Item lapis_carrot = new ItemHorseUpgrade(HorseUpgradeType.VARIANT, new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()));
      Item diamond_carrot = new ItemHorseUpgrade(HorseUpgradeType.HEALTH, new ItemStack(Items.DIAMOND));
      Item redstone_carrot = new ItemHorseUpgrade(HorseUpgradeType.SPEED, new ItemStack(Items.REDSTONE));
      Item ender_carrot = new ItemHorseUpgrade(HorseUpgradeType.JUMP, new ItemStack(Items.ENDER_EYE));
      ItemRegistry.register(emerald_carrot, "horse_upgrade_type");
      ItemRegistry.register(lapis_carrot, "horse_upgrade_variant");
      ItemRegistry.register(diamond_carrot, "horse_upgrade_health");
      ItemRegistry.register(redstone_carrot, "horse_upgrade_speed");
      ItemRegistry.register(ender_carrot, "horse_upgrade_jump");
      ModCyclic.instance.events.register(this);//for SubcribeEvent hooks
      AchievementRegistry.registerItemAchievement(diamond_carrot);
      ItemRegistry.registerWithJeiDescription(emerald_carrot);
      ItemRegistry.registerWithJeiDescription(lapis_carrot);
      ItemRegistry.registerWithJeiDescription(diamond_carrot);
      ItemRegistry.registerWithJeiDescription(redstone_carrot);
      ItemRegistry.registerWithJeiDescription(ender_carrot);
    }
    if (enableLapisApple) {
      ItemAppleLapis apple_lapis = new ItemAppleLapis();
      ItemRegistry.register(apple_lapis, "apple_lapis");
      ModCyclic.instance.events.register(apple_lapis);
    }
    if (enableEmeraldApple) {
      ItemAppleEmerald apple_emerald = new ItemAppleEmerald();
      ItemRegistry.register(apple_emerald, "apple_emerald");
      LootTableRegistry.registerLoot(apple_emerald);
      ItemRegistry.registerWithJeiDescription(apple_emerald);
      ModCyclic.instance.events.register(apple_emerald);
    }
    if (enableHeartContainer) {
      ItemHeartContainer heart_food = new ItemHeartContainer();
      ItemRegistry.register(heart_food, "heart_food");
      ModCyclic.instance.events.register(heart_food);
      LootTableRegistry.registerLoot(heart_food);
      LootTableRegistry.registerLoot(heart_food, ChestType.ENDCITY);
      LootTableRegistry.registerLoot(heart_food, ChestType.IGLOO);
      AchievementRegistry.registerItemAchievement(heart_food);
      ItemRegistry.registerWithJeiDescription(heart_food);
    }
    if (enableInventoryCrafting) {
      ItemFoodCrafting crafting_food = new ItemFoodCrafting();
      ItemRegistry.register(crafting_food, "crafting_food");
      LootTableRegistry.registerLoot(crafting_food);
      AchievementRegistry.registerItemAchievement(crafting_food);
      ItemRegistry.registerWithJeiDescription(crafting_food);
    }
    if (enableInventoryUpgrade) {
      ItemFoodInventory inventory_food = new ItemFoodInventory();
      ItemRegistry.register(inventory_food, "inventory_food");
      LootTableRegistry.registerLoot(inventory_food);
      AchievementRegistry.registerItemAchievement(inventory_food);
      ItemRegistry.registerWithJeiDescription(inventory_food);
    }
    if (enableCorruptedChorus) {
      ItemChorusCorrupted corrupted_chorus = new ItemChorusCorrupted();
      ItemRegistry.register(corrupted_chorus, "corrupted_chorus");
      ModCyclic.instance.events.register(corrupted_chorus);
      LootTableRegistry.registerLoot(corrupted_chorus);
      LootTableRegistry.registerLoot(corrupted_chorus, ChestType.ENDCITY);
      AchievementRegistry.registerItemAchievement(corrupted_chorus);
      ItemRegistry.registerWithJeiDescription(corrupted_chorus);
    }
    if (enableGlowingChorus) {
      ItemChorusGlowing glowing_chorus = new ItemChorusGlowing();
      ItemRegistry.register(glowing_chorus, "glowing_chorus");
      ModCyclic.instance.events.register(glowing_chorus);
    }
  }
  @SubscribeEvent
  public void onEntityInteractEvent(EntityInteract event) {
    if (event.getEntity() instanceof EntityPlayer == false) { return; }
    EntityPlayer entityPlayer = (EntityPlayer) event.getEntity();
    ItemStack held = entityPlayer.getHeldItemMainhand();
    if (held != null && held.getItem() instanceof ItemHorseUpgrade && held.getCount() > 0
        && event.getTarget() instanceof AbstractHorse) {
      ItemHorseUpgrade.onHorseInteract((AbstractHorse) event.getTarget(), entityPlayer, held, (ItemHorseUpgrade) held.getItem());
      event.setCanceled(true);// stop the GUI inventory opening && horse mounting
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    enableLapisApple = config.getBoolean("LapisApple", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableGlowingChorus = config.getBoolean("GlowingChorus(Food)", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
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
