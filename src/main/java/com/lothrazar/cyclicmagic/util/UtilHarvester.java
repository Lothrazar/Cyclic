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

import com.google.common.collect.UnmodifiableIterator;
import com.lothrazar.cyclicmagic.ModCyclic;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

import java.util.*;
import java.util.Map.Entry;

public class UtilHarvester {

  private static final int FORTUNE = 5;
  private static final String AGE = "age";
  static final boolean tryRemoveOneSeed = true;
  //a break config means ignore age and go right for it
  //  a harvest config means check AGE
  private static NonNullList<ResourceLocation> breakGetDrops;
  private static NonNullList<ResourceLocation> breakSilkTouch;
  private static NonNullList<ResourceLocation> blockIgnore;
  private static NonNullList<ResourceLocation> blockIgnoreInternal;
  private static NonNullList<ResourceLocation> harvestReflectionRegrow;
  private static NonNullList<ResourceLocation> harvestGetDropsDeprecated;
  private static NonNullList<ResourceLocation> breakGetDropsDeprecated;
  private static NonNullList<ResourceLocation> blocksBreakAboveIfMatching;
  private static NonNullList<ResourceLocation> blocksBreakAboveIfMatchingAfterHarvest;
  private static Map<String, Integer> harvestCustomMaxAge;
  private static Map<String, String> modsThatDontUseAge = new HashMap<String, String>();
  private static Map<String, String> useBooleanProperty = new HashMap<String, String>();
  private static NonNullList<ResourceLocation> blocksDoNotRemoveSeeds;

  public static void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.modpackMisc;
    String[] deflist = new String[] {
        "terraqueous:pergola", "minecraft:*_stem", "croparia:stem_*", "rustic:grape_stem",
    };
    String[] blacklist = config.getStringList("HarvesterBlacklist", category, deflist, "Crops & bushes that are blocked from harvesting (Garden Scythe and Harvester).  A star is for a wildcard");
    //TODO: config it after its decided? maybe? maybe not?
    /* @formatter:off */
    blockIgnore = NonNullList.from(
            new ResourceLocation("", ""),
            Arrays.stream(blacklist).map(s -> {
              String[] split = s.split(":");
              if (split.length < 2) {
                ModCyclic.logger.error("Invalid HarvesterBlacklist config value for block : " + s);
                return null;
              }
              return new ResourceLocation(split[0], split[1]);
            }).filter(Objects::nonNull).filter(r -> !r.getPath().isEmpty()).toArray(ResourceLocation[]::new)
    );
    internalStaticConfig();
  }

  private static void internalStaticConfig() {

    breakGetDrops = NonNullList.from(new ResourceLocation("", ""),
            new ResourceLocation("minecraft:pumpkin"),
            new ResourceLocation("croparia:block_plant_*"),
            new ResourceLocation("croparia:block_cane_*"),
            new ResourceLocation("extrautils2:redorchid")
    );
    blockIgnoreInternal = NonNullList.from(new ResourceLocation("", ""),
            new ResourceLocation("pizzacraft:corn_plant_bottom")
    );
    breakSilkTouch = NonNullList.from(new ResourceLocation("", ""),
            new ResourceLocation("minecraft:melon_block")
    );
    //there are two versions of the getDrops method in block class
    //and re check it for harvest vs break
    harvestGetDropsDeprecated = NonNullList.from(new ResourceLocation("", ""),
            new ResourceLocation("rustic:tomato_crop"),
            new ResourceLocation("rustic:chili_crop"),
            new ResourceLocation("harvestcraft:pamapple"),
            new ResourceLocation("harvestcraft:pamalmond"),
            new ResourceLocation("harvestcraft:pamapricot"),
            new ResourceLocation("harvestcraft:pamavocado"),
            new ResourceLocation("harvestcraft:pamcashew"),
            new ResourceLocation("harvestcraft:pamcherry"),
            new ResourceLocation("harvestcraft:pamchestnut"),
            new ResourceLocation("harvestcraft:pamcoconut"),
            new ResourceLocation("harvestcraft:pamdate"),
            new ResourceLocation("harvestcraft:pamdragonfruit"),
            new ResourceLocation("harvestcraft:pamdurian"),
            new ResourceLocation("harvestcraft:pamfig"),
            new ResourceLocation("harvestcraft:pamgooseberry"),
            new ResourceLocation("harvestcraft:pamlemon"),
            new ResourceLocation("harvestcraft:pamlime"),
            new ResourceLocation("harvestcraft:pammango"),
            new ResourceLocation("harvestcraft:pamnutmeg"),
            new ResourceLocation("harvestcraft:pamolive"),
            new ResourceLocation("harvestcraft:pamorange"),
            new ResourceLocation("harvestcraft:pampapaya"),
            new ResourceLocation("harvestcraft:pampeach"),
            new ResourceLocation("harvestcraft:pampear"),
            new ResourceLocation("harvestcraft:pampecan"),
            new ResourceLocation("harvestcraft:pampeppercorn"),
            new ResourceLocation("harvestcraft:pampersimmon"),
            new ResourceLocation("harvestcraft:pampistachio"),
            new ResourceLocation("harvestcraft:pamplum"),
            new ResourceLocation("harvestcraft:pampomegranate"),
            new ResourceLocation("harvestcraft:pamstarfruit"),
            new ResourceLocation("harvestcraft:pamvanillabean"),
            new ResourceLocation("harvestcraft:pamwalnut"),
            new ResourceLocation("harvestcraft:pamspiderweb"),
            new ResourceLocation("harvestcraft:pamcinnamon"),
            new ResourceLocation("harvestcraft:pammaple"),
            new ResourceLocation("harvestcraft:pampaperbark")
        );
    harvestReflectionRegrow =  NonNullList.from(new ResourceLocation("", ""),
            new ResourceLocation("natura:overworld_berrybush_*"),
            new ResourceLocation("natura:overworld_berrybush_blackberry"),
            new ResourceLocation("natura:overworld_berrybush_raspberry"),
            new ResourceLocation("natura:overworld_berrybush_maloberry"),
            new ResourceLocation("natura:nether_berrybush_duskberry"),
            new ResourceLocation("natura:nether_berrybush_stingberry"),
            new ResourceLocation("natura:nether_berrybush_skyberry"),
            new ResourceLocation("natura:nether_berrybush_blightberry"),
            new ResourceLocation("natura:nether_berrybush_duskberry")
    );
   breakGetDropsDeprecated = NonNullList.from(new ResourceLocation("", ""),
           new ResourceLocation("attaineddrops2:bulb")
   );
    blocksBreakAboveIfMatching = NonNullList.from(new ResourceLocation("", ""),
            new ResourceLocation("immersiveengineering:hemp"),
            new ResourceLocation("minecraft:reeds"),
            new ResourceLocation("minecraft:cactus")
    );
    blocksBreakAboveIfMatchingAfterHarvest = NonNullList.from(new ResourceLocation("", ""),
            new ResourceLocation("simplecorn:corn")
    );
    blocksDoNotRemoveSeeds = NonNullList.from(new ResourceLocation("", ""),
            new ResourceLocation("plants2:crop_1")
    );

    useBooleanProperty.put("rustic:grape_leaves", "grapes");
    modsThatDontUseAge.put("rustic:leaves_apple", "apple_age");
    modsThatDontUseAge.put("extrautils2:enderlilly", "growth");
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
    return UtilString.isInList(blockIgnore, blockId)
        || UtilString.isInList(blockIgnoreInternal, blockId);
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

  private static PropertyBool getBoolProperty(IBlockState blockState, String property) {
    UnmodifiableIterator<Entry<IProperty<?>, Comparable<?>>> unmodifiableiterator = blockState.getProperties().entrySet().iterator();
    while (unmodifiableiterator.hasNext()) {
      //      Entry<IProperty<?>, Comparable<?>> entry = unmodifiableiterator.next();
      IProperty<?> iproperty = unmodifiableiterator.next().getKey();
      if (iproperty instanceof PropertyBool && iproperty.getName().equals(property)) {
        return (PropertyBool) iproperty;
      }
    }
    return null;
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
      else if (modsThatDontUseAge.containsKey(blockId.toString()) &&
          iproperty.getName().equals(modsThatDontUseAge.get(blockId.toString())) && iproperty instanceof PropertyInteger) {
            return (PropertyInteger) iproperty;
          }
    }
    return null;
  }

  @SuppressWarnings("deprecation")
  public static NonNullList<ItemStack> harvestSingle(World world, BlockPos posCurrent) {
    //
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
    //boolean hasberry type of flag harvests
    if (useBooleanProperty.containsKey(blockId.toString())) {
      String property = useBooleanProperty.get(blockId.toString());
      PropertyBool propFlag = getBoolProperty(blockState, property);
      if (blockState.getValue(propFlag)) {
        blockCheck.getDrops(drops, world, posCurrent, blockState, FORTUNE);
        if (drops.size() == 0)//use deprecated only if main is offline
          drops.addAll(blockCheck.getDrops(world, posCurrent, blockState, FORTUNE));
        // and then reset to off
        world.setBlockState(posCurrent, blockState.withProperty(propFlag, false));
      }
    }
    PropertyInteger propInt = null;
    //age growth
    propInt = getAgeProperty(blockState, blockId);
    if (propInt != null) {
      int currentAge = blockState.getValue(propInt);
      int minAge = Collections.min(propInt.getAllowedValues());
      int maxAge = Collections.max(propInt.getAllowedValues());
      //for custom overrides
      if (blockCheck instanceof BlockCrops) {
        BlockCrops crop = (BlockCrops) blockCheck;
        maxAge = crop.getMaxAge();//broccoli used to say 7 but with this is 6
      }
      // ModCyclic.logger.log("[harvest] test " + blockId + "-" + propInt.getName() + minAge + "_" + maxAge + "CURRENT" + currentAge);
      if (harvestCustomMaxAge.containsKey(blockId.toString())) {
        maxAge = harvestCustomMaxAge.get(blockId.toString());
      }
      if (minAge == maxAge || currentAge < maxAge) {
        //degenerate edge case: either this was made wrong OR its not meant to grow
        //like a stem or log or something;
        return drops;
      }
      if (isHarvestReflectionRegrow(blockId)) {
        //
        Object toDrop = UtilReflection.getFirstPrivate(blockCheck, ItemStack.class);
        if (toDrop != null) {
          ItemStack crop = (ItemStack) toDrop;
          if (!crop.isEmpty()) {
            drops.add(crop.copy());
            //regrow : so only do -1, not full reset
            world.setBlockState(posCurrent, blockState.withProperty(propInt, maxAge - 1));
            return drops;
          }
        }
      }
      //first get the drops
      if (isHarvestingGetDropsOld(blockId)) {
        //        ModCyclic.logger.log("[harvest] isHarvestingGetDropsOld " + blockId);
        //added for rustic, it uses this version, other one does not work
        //https://github.com/the-realest-stu/Rustic/blob/c9bbdece4a97b159c63c7e3ba9bbf084aa7245bb/src/main/java/rustic/common/blocks/crops/BlockStakeCrop.java#L119
        drops.addAll(blockCheck.getDrops(world, posCurrent, blockState, FORTUNE));
      }
      else {
        blockCheck.getDrops(drops, world, posCurrent, blockState.withProperty(propInt, maxAge), FORTUNE);
      }
      world.setBlockState(posCurrent, blockState.withProperty(propInt, minAge));
      if (isBreakAboveIfMatchingAfterHarvest(blockId)) {
        //for example: a 3 high block like corn
        if (doesBlockMatch(world, blockCheck, posCurrent.up())) {
          //ModCyclic.logger.log("[harvest] up(1) break " + blockId);
          //TODO: corn still drops a few from multiblock on ground. not the worst.
          world.destroyBlock(posCurrent.up(), false);
        }
        if (doesBlockMatch(world, blockCheck, posCurrent.up(2))) {
          //  ModCyclic.logger.log("[harvest] up(2) break " + blockId);
          //TODO: corn still drops a few from multiblock on ground. not the worst.
          world.destroyBlock(posCurrent.up(2), false);
        }
        if (world.isAirBlock(posCurrent.down()) && world.isAirBlock(posCurrent.down(2))) {
          world.destroyBlock(posCurrent, false);
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
                //                ModCyclic.logger.log("Harvester remove seed item " + seedItem);
                iterator.remove();
                // ModCyclic.logger.log("yay remove seed " + drop.getDisplayName());
                break;
              }
            }
          }
        }
        catch (Exception e) {
          ModCyclic.logger.error("Crop could not be harvested by Cyclic, contact both mod authors    " + blockId, e);
        }
      } //else dont remove seed
    }
    return drops;
  }
}
