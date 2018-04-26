/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic.core.util;

import net.minecraft.util.ResourceLocation;

public class Const {

  public static class Res {

    public static final String folder = "textures/gui/";
    public static final ResourceLocation SLOT = new ResourceLocation(Const.MODID, folder + "inventory_slot.png");
    public static final ResourceLocation SLOT_SAPLING = new ResourceLocation(Const.MODID, folder + "slot_sapling.png");
    public static final ResourceLocation SLOT_WATER = new ResourceLocation(Const.MODID, folder + "slot_bucket_water.png");
    public static final ResourceLocation SLOT_BOTTLE = new ResourceLocation(Const.MODID, folder + "inventory_slot_bottle.png");
    public static final ResourceLocation SLOT_BUCKET = new ResourceLocation(Const.MODID, folder + "slot_bucket.png");
    public static final ResourceLocation PROGRESS = new ResourceLocation(Const.MODID, folder + "progress.png");
    public static final ResourceLocation PROGRESSCTR = new ResourceLocation(Const.MODID, folder + "progress_ctr.png");
    public static final ResourceLocation TABLEDEFAULT = new ResourceLocation(Const.MODID, folder + "table.png");
    public static final ResourceLocation TABLEPLAIN = new ResourceLocation(Const.MODID, folder + "table_plain.png");
    public static final ResourceLocation TABLELARGE = new ResourceLocation(Const.MODID, folder + "pattern.png");
    public static final ResourceLocation TABLEFILTER = new ResourceLocation(Const.MODID, folder + "filter.png");
    public static final ResourceLocation VILLAGER = new ResourceLocation(Const.MODID, Const.Res.folder + "villager.png");
    public static final ResourceLocation SLOT_COAL = new ResourceLocation(Const.MODID, Const.Res.folder + "inventory_slot_coal.png");
    //    public static final ResourceLocation FUEL_CTR = new ResourceLocation(Const.MODID, Const.Res.folder + "fuel_ctr.png");
    //    public static final ResourceLocation FUEL_INNER = new ResourceLocation(Const.MODID, Const.Res.folder + "fuel_inner.png");
    public static final ResourceLocation ENERGY_CTR = new ResourceLocation(Const.MODID, Const.Res.folder + "energy_ctr.png");
    public static final ResourceLocation ENERGY_INNER = new ResourceLocation(Const.MODID, Const.Res.folder + "energy_inner.png");
    //    public static final ResourceLocation FUEL_CTRVERT = new ResourceLocation(Const.MODID, Const.Res.folder + "fuel_ctr_vert.png");
    //    public static final ResourceLocation FUEL_INNERVERT = new ResourceLocation(Const.MODID, Const.Res.folder + "fuel_inner_vert.png");
    public static final ResourceLocation FLUID = new ResourceLocation(Const.MODID, Const.Res.folder + "fluid.png");
    public static final ResourceLocation FLUID_WATER = new ResourceLocation(Const.MODID, Const.Res.folder + "fluid_water.png");
    public static final ResourceLocation FLUID_EXP = new ResourceLocation(Const.MODID, Const.Res.folder + "fluid_exp.png");
    public static final ResourceLocation FLUID_LAVA = new ResourceLocation(Const.MODID, Const.Res.folder + "fluid_lava.png");
    public static final ResourceLocation SLOT_GLOWSTONE = new ResourceLocation(Const.MODID, "textures/gui/inventory_slot_glowstone.png");
    public static final ResourceLocation SLOT_EBOTTLE = new ResourceLocation(Const.MODID, "textures/gui/inventory_slot_ebottle.png");
    public static final ResourceLocation SLOT_BOOK = new ResourceLocation(Const.MODID, "textures/gui/inventory_slot_book.png");
    public static final ResourceLocation SLOT_REDST = new ResourceLocation(Const.MODID, "textures/gui/inventory_slot_redstone.png");
  }

  public static enum ScreenSize {
    STANDARD, STANDARDPLAIN, LARGEWIDE, LARGE;

    public int width() {
      switch (this) {
        case STANDARD:
        case STANDARDPLAIN:
        case LARGE:
          return 176;
        case LARGEWIDE:
          return 250;
      }
      return 0;
    }

    public int height() {
      switch (this) {
        case STANDARD:
        case STANDARDPLAIN:
          return 166;
        case LARGE:
        case LARGEWIDE:
          return 212;
      }
      return 0;
    }

    public int playerOffsetY() {
      switch (this) {
        case STANDARD:
        case STANDARDPLAIN:
          return 84;
        case LARGE:
        case LARGEWIDE:
          return 130;
      }
      return 0;
    }

    public int playerOffsetX() {
      switch (this) {
        case LARGE:
        case STANDARD:
        case STANDARDPLAIN:
          return Const.PAD;
        case LARGEWIDE:
          return 48;//currently just merchant
      }
      return 0;
    }

    public ResourceLocation texture() {
      switch (this) {
        case STANDARD:
          return Res.TABLEDEFAULT;
        case STANDARDPLAIN:
          return Res.TABLEPLAIN;
        case LARGE:
          return Res.TABLELARGE;
        case LARGEWIDE:
          return Res.VILLAGER;
      }
      return null;
    }
  }

  public static final String MODID = "cyclicmagic";
  public static final String MODRES = Const.MODID + ":";
  public static final String MODCONF = Const.MODID + ".";

  public class ConfigCategory {

    //to store categories. basically an enum/lookup table
    public static final String global = MODCONF + "global";
    public static final String player = MODCONF + "player";
    public static final String worldGen = MODCONF + "world generation";
    public static final String mobs = MODCONF + "mobs";
    public static final String mobspawns = MODCONF + "Mob Spawns";
    public static final String blocks = MODCONF + "blocks";
    public static final String cables = blocks + ".cables";
    public static final String fuelCost = MODCONF + "FuelCost";
    public static final String inventory = MODCONF + "Inventory";
    public static final String items = MODCONF + "items";
    public static final String logging = MODCONF + "logging";
    public static final String itemsTack = items + ".StackSize";
    public static final String recipes = MODCONF + "recipes";
    public static final String villagers = MODCONF + "villagers";
    public static final String content = MODCONF + "content";
    public static final String contentDefaultText = "Set false to delete - requires restart";
    public static final String modpackMisc = "modpacks";
    public static final String uncrafter = modpackMisc + ".uncrafter";
    public static final String InventoryButtonsModpack = modpackMisc + ".TerrariaButtons";
    public static final String commands = modpackMisc + ".Commands";
    public static final String worldGenOceans = worldGen + ".ocean";
  }

  public class ConfigText {

    public static final String fuelCost = "Fuel/Energy/RF cost to run machine";
  }

  public class ToolStrings {

    public static final String pickaxe = "pickaxe";
    public static final String shovel = "shovel";
    public static final String axe = "axe";
  }

  public static final int SQ = 18;
  public static final int ARMOR_SIZE = 4;
  public static final int ROWS_VANILLA = 3;
  public static final int COLS_VANILLA = 9;
  public static final int btnHeight = 20;
  // not a regular propert. : class ItemSkull:  {"skeleton", "wither", "zombie", "char", "creeper", "dragon"};
  public static final int skull_skeleton = 0;
  public static final int skull_wither = 1;
  public static final int skull_zombie = 2;
  public static final int skull_player = 3;
  public static final int skull_creeper = 4;
  public static final int skull_dragon = 5;
  //saplings
  public static final int sapling_oak = 0;
  public static final int sapling_spruce = 1;
  public static final int sapling_birch = 2;
  public static final int sapling_jungle = 3;
  public static final int sapling_acacia = 4;
  public static final int sapling_darkoak = 5;
  //	public static final int skull_dragon = 5;
  public static final int NOTIFY = 2;
  public static final int TICKS_PER_SEC = 20;
  public static final int CHUNK_SIZE = 16;
  public static final int DIR_WEST = 1;
  public static final int DIR_SOUTH = 0;
  public static final int DIR_EAST = 3;
  public static final int DIR_NORTH = 2;
  public static final int HOTBAR_SIZE = 9;
  public static final String SkullOwner = "SkullOwner";

  public class Dimension {

    public static final int overworld = 0;
    public static final int end = 1;
    public static final int nether = -1;
  }

  public class Potions {

    public final static int I = 0;
    public final static int II = 1;
    public final static int III = 2;
    public final static int IV = 3;
    public final static int V = 4;
  }

  public final static int SPAWN_RADIUS = 8 * Const.CHUNK_SIZE;// 128 is spawn size
  public static final int WORLDHEIGHT = 256;
  //http://minecraft.gamepedia.com/Light#Mobs
  //this or lower
  public static final int LIGHT_MOBSPAWN = 7;
  public static final int LIGHT_MOBSPAWN_BLAZE = 11;
  public static final int PAD = 8;
  /**
   * defined in vanilla VluidRegistry where Fluid LAVA is registered (tile.lava)
   */
  public static final int LAVA_TEMPERATURE = 1300;

  public static class HorseMeta {

    public static final int variant_white = 0;
    public static final int variant_creamy = 1;
    public static final int variant_chestnut = 2;
    public static final int variant_brown = 3;
    public static final int variant_black = 4;
    public static final int variant_gray = 5;
    public static final int variant_brown_dark = 6;
    public static final int type_standard = 0;
    public static final int type_donkey = 1;
    public static final int type_mule = 2;
    public static final int type_zombie = 3;
    public static final int type_skeleton = 4;
  }
}// ends class reference
