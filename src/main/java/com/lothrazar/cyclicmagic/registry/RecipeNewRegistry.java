package com.lothrazar.cyclicmagic.registry;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeNewRegistry {
  private static boolean playerSkull;
  private static boolean mushroomBlocks;
  private static boolean simpleDispenser;
  private static boolean repeaterSimple;
  private static boolean minecartsSimple;
  private static boolean notchApple;
  private static boolean ElytraRepair;
  public static void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.recipes;
    config.setCategoryComment(category, "New and altered recipes");
    playerSkull = config.get(category, "Player Skulls",
        true, "Create a player skull by combining wither, skeleton, zombie, and creeper skulls").getBoolean();
    mushroomBlocks = config.get(category, "Mushroom Blocks",
        true, "Create mushroom blocks from items").getBoolean();
    simpleDispenser = config.get(category, "Simple Dispenser",
        true, "Craft a dispenser with string instead of a bow").getBoolean();
    repeaterSimple = config.get(category, "Simple Repeater",
        true, "Craft repeaters using sticks and redstone in place of redstone torches").getBoolean();
    minecartsSimple = config.get(category, "Simple Minecarts",
        true, "Craft the minecart combinations using five iron as well as minecarts").getBoolean();
    notchApple = config.get(category, "Notch Apple",
        true, "Craft a notch apple with golden blocks as usual").getBoolean();
    ElytraRepair = config.get(category, "Elytra Repair",
        true, "You can mostly repair elytra wings with a wither skull; but it loses all enchants").getBoolean();
    //TODO: melon blocks into slices
  }
  public static void register() {
    if (playerSkull) {
      RecipeNewRegistry.playerSkull();
    }
    if (mushroomBlocks) {
      RecipeNewRegistry.mushroomBlocks();
    }
    if (simpleDispenser) {
      RecipeNewRegistry.simpleDispenser();
    }
    if (repeaterSimple) {
      RecipeNewRegistry.repeaterSimple();
    }
    if (minecartsSimple) {
      RecipeNewRegistry.minecartsSimple();
    }
    if (notchApple) {
      RecipeNewRegistry.notchApple();
    }
    if (ElytraRepair) {
      elytraRepair();
    }
    // https://github.com/PrinceOfAmber/SamsPowerups/blob/master/Recipes/src/main/java/com/lothrazar/samsrecipes/RecipeRegistry.java
  }
  private static void elytraRepair() {
    GameRegistry.addShapelessRecipe(new ItemStack(Items.ELYTRA, 1, UtilItem.getMaxDmgFraction(Items.ELYTRA, 10)),
        new ItemStack(Items.SKULL, 1, Const.skull_wither),
        new ItemStack(Items.ELYTRA, 1, OreDictionary.WILDCARD_VALUE));
  }
  private static void notchApple() {
    // https://www.reddit.com/r/minecraftsuggestions/comments/4d20g5/bring_back_the_notch_apple_crafting_recipe/
    GameRegistry.addRecipe(new ItemStack(Items.GOLDEN_APPLE, 1, 1), "ggg", "gag", "ggg", 'g', new ItemStack(Blocks.GOLD_BLOCK), 'a', new ItemStack(Items.APPLE));
  }
  private static void playerSkull() {
    GameRegistry.addShapelessRecipe(new ItemStack(Items.SKULL, 4, Const.skull_player),
        new ItemStack(Items.SKULL, 1, Const.skull_wither),
        new ItemStack(Items.SKULL, 1, Const.skull_skeleton),
        new ItemStack(Items.SKULL, 1, Const.skull_zombie),
        new ItemStack(Items.SKULL, 1, Const.skull_creeper));
  }
  private static void mushroomBlocks() {
    GameRegistry.addRecipe(new ItemStack(Blocks.RED_MUSHROOM_BLOCK),
        "mm", "mm", 'm', Blocks.RED_MUSHROOM);
    GameRegistry.addRecipe(new ItemStack(Blocks.BROWN_MUSHROOM_BLOCK),
        "mm", "mm", 'm', Blocks.BROWN_MUSHROOM);
  }
  private static void repeaterSimple() {
    GameRegistry.addRecipe(new ItemStack(Items.REPEATER),
        "r r", "srs", "ttt",
        't', new ItemStack(Blocks.STONE), 's', new ItemStack(Items.STICK), 'r', new ItemStack(Items.REDSTONE));
  }
  private static void minecartsSimple() {
    // normally you would need the minecart created in a different step. this is
    // faster
    GameRegistry.addRecipe(new ItemStack(Items.CHEST_MINECART), "   ", "ici", "iii", 'i', Items.IRON_INGOT, 'c', Blocks.CHEST);
    GameRegistry.addRecipe(new ItemStack(Items.TNT_MINECART), "   ", "ici", "iii", 'i', Items.IRON_INGOT, 'c', Blocks.TNT);
    GameRegistry.addRecipe(new ItemStack(Items.HOPPER_MINECART), "   ", "ici", "iii", 'i', Items.IRON_INGOT, 'c', Blocks.HOPPER);
    GameRegistry.addRecipe(new ItemStack(Items.FURNACE_MINECART), "   ", "ici", "iii", 'i', Items.IRON_INGOT, 'c', Blocks.FURNACE);
  }
  private static void simpleDispenser() {
    GameRegistry.addRecipe(new ItemStack(Blocks.DISPENSER),
        "ccc", "csc", "crc",
        'c', Blocks.COBBLESTONE, 's', Items.STRING, 'r', Items.REDSTONE);
  }
}
