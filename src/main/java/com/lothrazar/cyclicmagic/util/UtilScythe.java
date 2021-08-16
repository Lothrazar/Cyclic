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
package com.lothrazar.cyclicmagic.util;

import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.item.scythe.ItemScythe.ScytheType;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

import java.util.Arrays;
import java.util.Objects;

public class UtilScythe {

  //private static final int FORTUNE = 5;
  private static NonNullList<String> blacklistAll;
  private static ScytheConfig leafConfig = new ScytheConfig();
  private static ScytheConfig brushConfig = new ScytheConfig();
  private static boolean weedsClassCheck;
  private static boolean leavesClassCheck;

  private static class ScytheConfig {

    NonNullList<ResourceLocation> blockWhitelist;
    NonNullList<String> oreDictWhitelist;
  }

  public static void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.modpackMisc;
    weedsClassCheck = config.getBoolean("tool_harvest_weeds.class_defaults", category, true, "If true this will try an auto-detect blocks that are a weed/bush/tallgrass type by class type, which "
        + "means harvesting many blocks that are not explicitly in the whitelist.  This is useful for handling vanilla and most modded blocks.  If you want more precise control you can turn this false "
        + "which means ONLY the whitelists will be harvested, so you would have to add every block yourself. ");
    leavesClassCheck = config.getBoolean("tool_harvest_leaves.class_defaults", category, true, "If true this will try an auto-detect blocks that are a leaf-type by class type, which "
        + "means harvesting many blocks that are not explicitly in the whitelist.  This is useful for handling vanilla and most modded blocks.  If you want more precise control you can turn this false "
        + "which means ONLY the whitelists will be harvested, so you would have to add every block yourself. ");
    blacklistAll = NonNullList.from("", config.getStringList("ScytheBlacklist", category, new String[] {
        "terraqueous:pergola", "harvestcraft:*_sapling"
    }, "Crops & leaves that are blocked from harvesting (Brush Scythe and Tree Scythe). A star is for a wildcard "));
    leafConfig.blockWhitelist = NonNullList.from(new ResourceLocation("", ""),
            Arrays.stream(config.getStringList("tool_harvest_leaves.whitelist", category, new String[] {
                    /* @formatter:off */
                    "extratrees:leaves.decorative.0"
                    , "extratrees:leaves.decorative.1"
                    , "extratrees:leaves.decorative.2"
                    , "extratrees:leaves.decorative.3"
                    , "extratrees:leaves.decorative.4"
                    , "extratrees:leaves.decorative.5"
                    , "forestry:leaves.decorative.0"
                    , "forestry:leaves.decorative.1"
                    , "terraqueous:foliage3:5"
                    , "plants2:nether_leaves"
                    , "plants2:crystal_leaves"
                    , "plants2:leaves_0"
                    , "plants2:bush"
                    /* @formatter:on */
            }, "Blocks that the Tree Scythe will attempt to harvest as if they are leaves.  A star is for a wildcard ")).map(s -> {
              String[] split = s.split(":");
              if (split.length < 2) {
                ModCyclic.logger.error("Invalid tool_harvest_leaves.whitelist config value for block : " + s);
                return null;
              }
              return new ResourceLocation(split[0], split[1]);
            }).filter(Objects::nonNull).filter(r -> !r.getPath().isEmpty()).toArray(ResourceLocation[]::new));
    leafConfig.oreDictWhitelist = NonNullList.from("", config.getStringList("tool_harvest_leaves.whitelist_oredict", category, new String[] {
        "treeLeaves"
    }, "Ore dictionary entries that the Tree Scythe will attempt to harvest as if they are leaves.   "));
    brushConfig.oreDictWhitelist = NonNullList.from("", config.getStringList("tool_harvest_weeds.whitelist_oredict", category, new String[] {
        "vine", "plant", "flowerYellow", "stickWood" }, "Ore dictionary entries that the Brush Scythe will attempt to harvest as if they are leaves.  "));
    brushConfig.blockWhitelist = NonNullList.from(new ResourceLocation("", ""),
            Arrays.stream(config.getStringList("tool_harvest_weeds.whitelist", category, new String[] {
                    /* @formatter:off */
                    "plants2:cosmetic_0"
                    ,"plants2:cosmetic_1"
                    ,"plants2:cosmetic_2"
                    ,"plants2:cosmetic_3"
                    ,"plants2:cosmetic_4"
                    ,"plants2:desert_0"
                    ,"plants2:desert_1"
                    ,"plants2:double_0"
                    ,"plants2:cataplant"
                    ,"botany:flower"
                    ,"biomesoplenty:bamboo"
                    ,"biomesoplenty:flower_0"
                    ,"biomesoplenty:flower_1"
                    ,"biomesoplenty:plant_0"
                    ,"biomesoplenty:plant_1"
                    ,"biomesoplenty:mushroom"
                    ,"biomesoplenty:doubleplant"
                    ,"biomesoplenty:flower_vine"
                    ,"biomesoplenty:ivy"
                    ,"biomesoplenty:tree_moss"
                    ,"biomesoplenty:willow_vine"
                    ,"croparia:fruit_grass"
                    ,"plants2:androsace_a"
                    ,"plants2:akebia_q_vine"
                    ,"plants2:ampelopsis_a_vine"
                    ,"plants2:adlumia_f"
                    ,"abyssalcraft:wastelandsthorn"
                    ,"abyssalcraft:luminousthistle"
                    ,"harvestcraft:garden"
                    ,"harvestcraft:windygarden"
                    ,"minecraft:double_plant"
                    ,"minecraft:red_flower"
                    ,"minecraft:yellow_flower"
                    ,"minecraft:brown_mushroom"
                    ,"minecraft:red_mushroom"
                    ,"ferdinandsflowers:block_cff_*"
                    ,"extraplanets:*_flowers"
                    ,"primal:cineris_grass"
                    ,"primal:cineris_bloom"
                    ,"primal:sinuous_weed"
                    ,"primal:dry_grass_root"
                    ,"primal:nether_root"
                    ,"primal:corypha_stalk"
                    ,"twilightforest:*_plant"
                    ,"tconstruct:*_grass_tall"
                    ,"thebetweenlands:*_flower"
                    ,"thebetweenlands:*_tallgrass"
                    ,"thebetweenlands:*_stalk"
                    ,"thebetweenlands:moss"
                    ,"thebetweenlands:cattail"
                    ,"thebetweenlands:*_cattail"
                    ,"thebetweenlands:*_plant"
                    ,"thebetweenlands:*_coral"
                    ,"thebetweenlands:*_bush"
                    ,"thebetweenlands:*_ivy"
                    ,"thebetweenlands:algae"
                    ,"thebetweenlands:hanger"
                    ,"thebetweenlands:nettle"
                    ,"thebetweenlands:*_iris"
                    ,"thebetweenlands:*_kelp"
                    ,"thebetweenlands:fallen_leaves"
                    ,"thebetweenlands:swamp_reed_*"
                    ,"thebetweenlands:*_mushroom"
                    ,"natura:*_vines"
                    ,"nex:plant_thornstalk"
                    /* @formatter:on */
            }, "Blocks that the Brush Scythe will attempt to harvest as if they are leaves.  A star is for a wildcard ")).map(s -> {
              String[] split = s.split(":");
              if (split.length < 2) {
                ModCyclic.logger.error("Invalid tool_harvest_weeds.whitelist config value for block : " + s);
                return null;
              }
              return new ResourceLocation(split[0], split[1]);
            }).filter(Objects::nonNull).filter(r -> !r.getPath().isEmpty()).toArray(ResourceLocation[]::new));
  }

  private static boolean doesMatch(Block blockCheck, ScytheConfig type) {
    if (UtilString.isInList(type.blockWhitelist, blockCheck.getRegistryName())) {
      return true;
    }
    else {
      ItemStack bStack = new ItemStack(blockCheck);
      return UtilOreDictionary.doesMatchOreDict(bStack, type.oreDictWhitelist.toArray(new String[0]));
    }
  }

  public static boolean harvestSingle(World world, EntityPlayer player, BlockPos posCurrent, ScytheType type) {
    boolean doBreakAbove = false;
    boolean doBreakBelow = false;
    boolean doBreak = false;
    IBlockState blockState = world.getBlockState(posCurrent);
    Block blockCheck = blockState.getBlock();
    if (blockCheck == Blocks.AIR) {
      return false;
    }
    String blockId = blockCheck.getRegistryName().toString();
    Item seedItem = blockCheck.getItemDropped(blockCheck.getDefaultState(), world.rand, 0);//RuntimeException at this line
    if (isItemInBlacklist(UtilItemStack.getStringForItem(seedItem))
        || isItemInBlacklist(blockId)) {
      return false;
    }
    if (blockCheck.getRegistryName() == null) {
      ModCyclic.logger.error("Error: a block has not been registered correctly");
      return false;
    }
    else {
      switch (type) {
        case CROPS:
        break;
        case LEAVES:
          if (doesMatch(blockCheck, leafConfig)) {
            doBreak = true;
          }
        break;
        case WEEDS:
          if (doesMatch(blockCheck, brushConfig)) {
            doBreak = true;
          }
        break;
        default:
        break;
      }
    }
    IBlockState bsAbove = world.getBlockState(posCurrent.up());
    IBlockState bsBelow = world.getBlockState(posCurrent.down());
    //final NonNullList<ItemStack> drops = NonNullList.create();
    // (A): garden scythe and harvester use this;
    //      BUT type LEAVES and WEEDS harvester use DIFFERENT NEW class
    //     then each scythe has config list of what it breaks (maybe just scythe for all the modplants. also maybe hardcoded)
    // (B):  use this new "age" finding thing by default for harvest/replant
    // (C): figure out config system for anything that DOESNT work with this "age" system
    // (D): another  list of "just break it" blocks mapped by "mod:name"
    // PROBABLY: scythe will not have the break-it methods
    // "minecraft:pumpkin","minecraft:cactus", "minecraft:melon_block","minecraft:reeds"
    //  EXAMPLE: pumpkin, melon
    // (E): an ignore list of ones to skip EXAMPLE: stem
    switch (type) {
      case WEEDS:
        if (weedsClassCheck) {
          if (blockCheck instanceof BlockTallGrass) {// true for ItemScythe type WEEDS
            doBreak = true;
            if (blockCheck instanceof BlockTallGrass && bsAbove != null && bsAbove.getBlock() instanceof BlockTallGrass) {
              doBreakAbove = true;
            }
            if (bsBelow instanceof BlockTallGrass && bsBelow != null && bsBelow.getBlock() instanceof BlockTallGrass) {
              doBreakBelow = true;
            }
          }
          else if (blockCheck instanceof BlockDoublePlant) {// true for ItemScythe type WEEDS
            doBreak = true;
            if (blockCheck instanceof BlockDoublePlant && bsAbove != null && bsAbove.getBlock() instanceof BlockDoublePlant) {
              doBreakAbove = true;
            }
            if (bsBelow instanceof BlockDoublePlant && bsBelow != null && bsBelow.getBlock() instanceof BlockDoublePlant) {
              doBreakBelow = true;
            }
          }
          else if (blockCheck instanceof BlockMushroom) {//remove from harvester tile? used by weeds though
            doBreak = true;
          }
        }
      break;
      case CROPS:
      break;
      case LEAVES:
        if (blockCheck instanceof BlockLeaves && leavesClassCheck) {
          doBreak = true;
        }
      break;
    }
    if (doBreak) {
      //harvest block with player context: better mod compatibility
      blockCheck.harvestBlock(world, player, posCurrent, blockState, world.getTileEntity(posCurrent), player.getHeldItemMainhand());
      //sometimes this doesnt work and/or doesnt sync ot client, so force it
      world.destroyBlock(posCurrent, false);
      //break with false to disable dropsfor the above versions, dont want to dupe tallflowers
      if (doBreakAbove) {
        world.destroyBlock(posCurrent.up(), false);
      }
      if (doBreakBelow) {
        world.destroyBlock(posCurrent.down(), false);
      }
      return true;
    }
    if (blockCheck.getRegistryName().getNamespace().equals("minecraft") == false) {
      ModCyclic.logger.log("SCYTHE could not clip " + blockId);
    }
    return false;
  }

  private static boolean isItemInBlacklist(String itemName) {
    for (String s : blacklistAll) {//dont use .contains on the list. must use .equals on string
      if (s != null && s.equals(itemName)) {
        return true;
      }
    }
    return false;
  }
}
