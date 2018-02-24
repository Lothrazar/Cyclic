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
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.item.ItemScythe.ScytheType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockMushroom;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

public class UtilScythe {
  //private static final int FORTUNE = 5;
  private static NonNullList<String> blacklistAll;
  private static ScytheConfig leafConfig = new ScytheConfig();
  private static ScytheConfig brushConfig = new ScytheConfig();
  private static class ScytheConfig {
    NonNullList<String> blockWhitelist;
    NonNullList<String> oreDictWhitelist;
  }
  public static void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.modpackMisc;
    String[] deflist = new String[] {
        "terraqueous:pergola", "harvestcraft:*_sapling"
    };
    String[] blacklist = config.getStringList("ScytheBlacklist",
        category, deflist, "Crops & leaves that are blocked from harvesting (Brush Scythe and Tree Scythe). A star is for a wildcard ");
    blacklistAll = NonNullList.from("",
        blacklist);
    //TODO: config it after its decided? maybe? maybe not?
/* @formatter:off */
    leafConfig.blockWhitelist = NonNullList.from("",
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
       , "plants2:bush");
    
    leafConfig.oreDictWhitelist = NonNullList.from("",
        "treeLeaves"
        );
    brushConfig.oreDictWhitelist = NonNullList.from("",
        "vine"
        ,"plant"
        ,"flowerYellow"
        ,"stickWood");
    brushConfig.blockWhitelist = NonNullList.from("",
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
        );
   
    /* @formatter:on */
  }
  //TODO:::::::::: brush scythe DOES hit
  //  <harvestcraft:pampeach>
  //which has zero ore dict entries. soo.. hmm. 
  //treescythe <harvestcraft:groundtrap>
  private static boolean doesMatch(Block blockCheck, ScytheConfig type) {
    if (UtilString.isInList(type.blockWhitelist, blockCheck.getRegistryName())) {
      return true;
    }
    else {
      ItemStack bStack = new ItemStack(blockCheck);
      return UtilOreDictionary.doesMatchOreDict(bStack, type.oreDictWhitelist.toArray(new String[0]));
    }
  }
  public static boolean harvestSingle(World world, BlockPos posCurrent, ScytheType type) {
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
      break;
      case CROPS:
      break;
      case LEAVES:
        if (blockCheck instanceof BlockLeaves) {// true for ItemScythe type LEAVES
          doBreak = true;
        }
      break;
    }
    if (doBreak) {
      //break with false so that we can get the drops our own way
      world.destroyBlock(posCurrent, true);//true==drops; false == no drops. literally just for the sound
      //blockCheck.getDrops(drops, world, posCurrent, blockState, FORTUNE);
      //break above first BECAUSE 2 high tallgrass otherwise will bug out if you break bottom first
      if (doBreakAbove) {
        world.destroyBlock(posCurrent.up(), false);
      }
      if (doBreakBelow) {
        world.destroyBlock(posCurrent.down(), false);
      }
      //      for (ItemStack drop : drops) {
      //        UtilItemStack.dropItemStackInWorld(world, posCurrent, drop);
      //      }
      return true;
    }
    if (blockCheck.getRegistryName().getResourceDomain().equals("minecraft") == false) {
      ModCyclic.logger.log("SCYTHE IGNORED " + blockId);
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
