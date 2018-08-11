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

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import com.google.common.collect.UnmodifiableIterator;
import com.lothrazar.cyclicmagic.ModCyclic;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

public class UtilHarvester {

  private static final int FORTUNE = 5;
  private static final String AGE = "age";
  static final boolean tryRemoveOneSeed = true;
  //a break config means ignore age and go right for it
  //  a harvest config means check AGE
  private static NonNullList<String> breakGetDrops;
  private static NonNullList<String> breakSilkTouch;
  private static NonNullList<String> blockIgnore;
  private static NonNullList<String> harvestReflectionRegrow;
  private static NonNullList<String> harvestGetDropsDeprecated;
  private static NonNullList<String> breakGetDropsDeprecated;
  private static NonNullList<String> blocksBreakAboveIfMatching;
  private static NonNullList<String> blocksBreakAboveIfMatchingAfterHarvest;
  private static Map<String, Integer> harvestCustomMaxAge;
  private static Map<String, String> stupidModsThatDontUseAge = new HashMap<String, String>();
  private static NonNullList<String> blocksDoNotRemoveSeeds;

  public static void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.modpackMisc;
    String[] deflist = new String[] {
        "terraqueous:pergola", "minecraft:*_stem", "croparia:stem_*"
    };
    String[] blacklist = config.getStringList("HarvesterBlacklist", category, deflist, "Crops & bushes that are blocked from harvesting (Garden Scythe and Harvester).  A star is for a wildcard");
    //TODO: config it after its decided? maybe? maybe not?
    /* @formatter:off */
    blockIgnore = NonNullList.from("",
        blacklist);
    breakGetDrops = NonNullList.from("",
        "minecraft:pumpkin"
        , "croparia:block_plant_*"
        , "croparia:block_cane_*"
        );
    
    breakSilkTouch = NonNullList.from(""
        ,"minecraft:melon_block"
        );
    //there are two versions of the getDrops method in block class
    //and re check it for harvest vs break
    harvestGetDropsDeprecated = NonNullList.from(""
        ,"rustic:tomato_crop"
        ,"rustic:chili_crop"
        ,"harvestcraft:pamapple"
        ,"harvestcraft:pamalmond"
        ,"harvestcraft:pamapricot"
        ,"harvestcraft:pamavocado"
        ,"harvestcraft:pamcashew"
        ,"harvestcraft:pamcherry"
        ,"harvestcraft:pamchestnut"
        ,"harvestcraft:pamcoconut"
        ,"harvestcraft:pamdate"
        ,"harvestcraft:pamdragonfruit"
        ,"harvestcraft:pamdurian"
        ,"harvestcraft:pamfig"
        ,"harvestcraft:pamgooseberry"
        ,"harvestcraft:pamlemon"
        ,"harvestcraft:pamlime"
        ,"harvestcraft:pammango"
        ,"harvestcraft:pamnutmeg"
        ,"harvestcraft:pamolive"
        ,"harvestcraft:pamorange"
        ,"harvestcraft:pampapaya"
        ,"harvestcraft:pampeach"
        ,"harvestcraft:pampear"
        ,"harvestcraft:pampecan"
        ,"harvestcraft:pampeppercorn"
        ,"harvestcraft:pampersimmon"
        ,"harvestcraft:pampistachio"
        ,"harvestcraft:pamplum"
        ,"harvestcraft:pampomegranate"
        ,"harvestcraft:pamstarfruit"
        ,"harvestcraft:pamvanillabean"
        ,"harvestcraft:pamwalnut"
        ,"harvestcraft:pamspiderweb"
        ,"harvestcraft:pamcinnamon"
        ,"harvestcraft:pammaple"
        ,"harvestcraft:pampaperbark" 
        );    
    harvestReflectionRegrow =  NonNullList.from(""
//        ,"natura:*"
        ,"natura:overworld_berrybush_*"
        ,"natura:overworld_berrybush_blackberry"
        ,"natura:overworld_berrybush_raspberry"
        ,"natura:overworld_berrybush_maloberry"
        ,"natura:nether_berrybush_duskberry"
        ,"natura:nether_berrybush_stingberry"
        ,"natura:nether_berrybush_skyberry"
        ,"natura:nether_berrybush_blightberry"
        ,"natura:nether_berrybush_duskberry"
        );    
   breakGetDropsDeprecated = NonNullList.from(""
        ,  "attaineddrops2:bulb"
        );    
    blocksBreakAboveIfMatching = NonNullList.from(""
        ,"immersiveengineering:hemp"
        ,"minecraft:reeds"
        ,"minecraft:cactus"
        );  
    blocksBreakAboveIfMatchingAfterHarvest = NonNullList.from(""
         ,"simplecorn:corn"
        );  
    blocksDoNotRemoveSeeds = NonNullList.from(""
        ,"plants2:crop_1"
       );  
    
    stupidModsThatDontUseAge.put("rustic:leaves_apple", "apple_age");
    harvestCustomMaxAge = new HashMap<String, Integer>();
    //max metadata is 11, but 9 is the lowest level when full grown
    //its a 3high multiblock
    harvestCustomMaxAge.put("simplecorn:corn", 9);
    /* @formatter:on */
  }

  private static boolean doNotRemoveSeeds(ResourceLocation blockId) {
    return UtilString.isInList(blocksDoNotRemoveSeeds, blockId);
  }

  private static boolean isHarvestReflectionRegrow(ResourceLocation blockId) {
    return UtilString.isInList(harvestReflectionRegrow, blockId);
  }

  private static boolean isIgnored(ResourceLocation blockId) {
    return UtilString.isInList(blockIgnore, blockId);
  }

  private static boolean isBreakGetDrops(ResourceLocation blockId) {
    return UtilString.isInList(breakGetDrops, blockId);
  }

  private static boolean isBreakGetDropsDeprec(ResourceLocation blockId) {
    return UtilString.isInList(breakGetDropsDeprecated, blockId);
  }

  private static boolean isSimpleSilktouch(ResourceLocation blockId) {
    return UtilString.isInList(breakSilkTouch, blockId);
  }

  private static boolean isHarvestingGetDropsOld(ResourceLocation blockId) {
    return UtilString.isInList(harvestGetDropsDeprecated, blockId);
  }

  private static boolean isBreakAboveIfMatching(ResourceLocation blockId) {
    return UtilString.isInList(blocksBreakAboveIfMatching, blockId);
  }

  private static boolean isBreakAboveIfMatchingAfterHarvest(ResourceLocation blockId) {
    return UtilString.isInList(blocksBreakAboveIfMatchingAfterHarvest, blockId);
  }

  private static boolean doesBlockMatch(World world, Block blockCheck, BlockPos pos) {
    return world.getBlockState(pos).getBlock().equals(blockCheck);
  }

  private static PropertyInteger getAgeProperty(IBlockState blockState, ResourceLocation blockId) {
    UnmodifiableIterator<Entry<IProperty<?>, Comparable<?>>> unmodifiableiterator = blockState.getProperties().entrySet().iterator();
    while (unmodifiableiterator.hasNext()) {
      Entry<IProperty<?>, Comparable<?>> entry = unmodifiableiterator.next();
      IProperty<?> iproperty = entry.getKey();
      if (iproperty.getName() == null) {
        continue;
      }
      if (iproperty.getName().equals(AGE) && iproperty instanceof PropertyInteger) {
        return (PropertyInteger) iproperty;
      }
      else if (stupidModsThatDontUseAge.containsKey(blockId.toString()) &&
          iproperty.getName().equals(stupidModsThatDontUseAge.get(blockId.toString())) && iproperty instanceof PropertyInteger) {
        return (PropertyInteger) iproperty;
      }
    }
    return null;
  }

  @SuppressWarnings("deprecation")
  public static NonNullList<ItemStack> harvestSingle(World world, BlockPos posCurrent) {
    final NonNullList<ItemStack> drops = NonNullList.create();
    if (world.isAirBlock(posCurrent)) {
      return drops;
    }
    IBlockState blockState = world.getBlockState(posCurrent);
    Block blockCheck = blockState.getBlock();
    ResourceLocation blockId = blockCheck.getRegistryName();
    if (isIgnored(blockId)) {
      return drops;
    }
    if (isBreakGetDrops(blockId)) {
      blockCheck.getDrops(drops, world, posCurrent, blockState, FORTUNE);
      world.setBlockToAir(posCurrent);
      return drops;
    }
    if (isBreakGetDropsDeprec(blockId)) {
      drops.addAll(blockCheck.getDrops(world, posCurrent, blockState, FORTUNE));
      world.setBlockToAir(posCurrent);
      return drops;
    }
    if (isSimpleSilktouch(blockId)) {
      drops.add(new ItemStack(blockCheck));
      world.setBlockToAir(posCurrent);
      return drops;
    }
    if (isBreakAboveIfMatching(blockId) && doesBlockMatch(world, blockCheck, posCurrent.up())) {
      blockCheck.getDrops(drops, world, posCurrent, world.getBlockState(posCurrent.up()), FORTUNE);
      if (doesBlockMatch(world, blockCheck, posCurrent.up(2))) {
        blockCheck.getDrops(drops, world, posCurrent, world.getBlockState(posCurrent.up(2)), FORTUNE);
        world.destroyBlock(posCurrent.up(2), false);//also break this one too - thanks darkosto
      }
      world.destroyBlock(posCurrent.up(), false);
      return drops;
    }
    //new generic harvest
    PropertyInteger propInt = getAgeProperty(blockState, blockId);
    if (propInt != null) {
      int currentAge = blockState.getValue(propInt);
      int minAge = Collections.min(propInt.getAllowedValues());
      int maxAge = Collections.max(propInt.getAllowedValues());
      if (harvestCustomMaxAge.containsKey(blockId.toString())) {
        maxAge = harvestCustomMaxAge.get(blockId.toString());
      }
      if (minAge == maxAge || currentAge < maxAge) {
        //degenerate edge case: either this was made wrong OR its not meant to grow
        //like a stem or log or something;
        return drops;
      }
      if (isHarvestReflectionRegrow(blockId)) {
        //      if (blockId.getResourceDomain().equals("natura")) {
        Object toDrop = UtilReflection.getFirstPrivate(blockCheck, ItemStack.class);
        if (toDrop != null) {
          ItemStack crop = (ItemStack) toDrop;
          if (crop.isEmpty() == false) {
            drops.add(crop.copy());
            //regrow : so only do -1, not full reset
            world.setBlockState(posCurrent, blockState.withProperty(propInt, maxAge - 1));
            return drops;
          }
        }
      }
      //first get the drops
      if (isHarvestingGetDropsOld(blockId)) {
        //added for rustic, it uses this version, other one does not work
        //https://github.com/the-realest-stu/Rustic/blob/c9bbdece4a97b159c63c7e3ba9bbf084aa7245bb/src/main/java/rustic/common/blocks/crops/BlockStakeCrop.java#L119
        drops.addAll(blockCheck.getDrops(world, posCurrent, blockState, FORTUNE));
      }
      else {
        blockCheck.getDrops(drops, world, posCurrent, blockState.withProperty(propInt, maxAge), FORTUNE);
      }
      world.setBlockState(posCurrent, blockState.withProperty(propInt, minAge));
      if (isBreakAboveIfMatchingAfterHarvest(blockId)) {
        if (doesBlockMatch(world, blockCheck, posCurrent.up())) {
          //TODO: corn still drops a few from multiblock on ground. not the worst.
          world.destroyBlock(posCurrent.up(), false);
        }
      }
      // we have a blackist of crops to skip the remove-seed step
      boolean removeSeed = tryRemoveOneSeed && drops.size() > 1 && !doNotRemoveSeeds(blockId);
      if (removeSeed) {
        Item seedItem = blockCheck.getItemDropped(blockCheck.getDefaultState(), world.rand, 0);
        if (seedItem == null) {
          seedItem = Item.getItemFromBlock(blockCheck);
        }
        try {
          if (seedItem != null) {
            //  if it dropped more than one ( seed and a thing)
            for (Iterator<ItemStack> iterator = drops.iterator(); iterator.hasNext();) {
              final ItemStack drop = iterator.next();
              if (drop.getItem() == seedItem) { // Remove exactly one seed (consume for replanting
                ModCyclic.logger.log("Harvester remove seed item " + seedItem);
                iterator.remove();
                // ModCyclic.logger.log("yay remove seed " + drop.getDisplayName());
                break;
              }
            }
          }
        }
        catch (Exception e) {
          ModCyclic.logger.error("Crop could not be harvested by Cyclic, contact both mod authors    " + blockId);
          ModCyclic.logger.error(e.getMessage());
          e.printStackTrace();
        }
      } //else dont remove seed
    }
    return drops;
  }
}
