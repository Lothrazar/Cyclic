package com.lothrazar.cyclicmagic.util;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import com.google.common.collect.UnmodifiableIterator;
import com.lothrazar.cyclicmagic.ModCyclic;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.config.Configuration;

public class UtilHarvester {
  private static NonNullList<String> blocksBreak;
  private static NonNullList<String> blocksSilkTouch;
  private static NonNullList<String> blockIgnore;
  static final boolean tryRemoveOneSeed = true;
  public static void syncConfig(Configuration config) {
    //TODO: config it after its decided? maybe? maybe not?
    blockIgnore = NonNullList.from("",
        "minecraft:pumpkin_stem", "minecraft:melon_stem");
    blocksBreak = NonNullList.from("",
        "minecraft:pumpkin", "croparia:block_plant_emerald");
    blocksSilkTouch = NonNullList.from("",
        "minecraft:melon_block");
  }
  private static boolean isIgnored(String blockId) {
 
    for (String s : blockIgnore) {
      if (s.equals(blockId)) {
        return true;
      }
    }
    return false;
  }
  private static boolean isSimpleBreak(String blockId) {
 
    for (String s : blocksBreak) {
      if (s.equals(blockId)) {
        return true;
      }
    }
    return false;
  }
  private static boolean isSimpleSilktouch(String blockId) {
 
    for (String s : blocksSilkTouch) {
 
      if (s.equals(blockId)) {
        return true;
      }
    }
    return false;
  }
  public static NonNullList<ItemStack> harvestSingle(World world, BlockPos posCurrent) {
    final NonNullList<ItemStack> drops = NonNullList.create();
    if (world.isAirBlock(posCurrent)) {
      return drops;
    }
    IBlockState blockState = world.getBlockState(posCurrent);
    Block blockCheck = blockState.getBlock();
    String blockId = blockCheck.getRegistryName().toString();
    if (isIgnored(blockId)) {
      ModCyclic.logger.log("isIgnored MATCH " + blockId);
      return drops;
    }
    if (isSimpleBreak(blockId)) {
      ModCyclic.logger.log("isSimpleBreak MATCH " + blockId);
      blockCheck.getDrops(drops, world, posCurrent, blockState, 0);
      world.setBlockToAir(posCurrent);
      return drops;
    }
    if (isSimpleSilktouch(blockId)) {
      ModCyclic.logger.log("SILK TOUCH MATCH " + blockId);
      drops.add(new ItemStack(blockCheck));
      world.setBlockToAir(posCurrent);
      return drops;
    }
    //    String blockId = blockCheck.getRegistryName().toString();
    //new generic harvest
    UnmodifiableIterator<Entry<IProperty<?>, Comparable<?>>> unmodifiableiterator = blockState.getProperties().entrySet().iterator();
    boolean isDone = false;
    while (unmodifiableiterator.hasNext()) {
      Entry<IProperty<?>, Comparable<?>> entry = unmodifiableiterator.next();
      IProperty<?> iproperty = entry.getKey();
      if (iproperty.getName() == "age" && iproperty instanceof PropertyInteger) {
        PropertyInteger propInt = (PropertyInteger) iproperty;
        int currentAge = blockState.getValue(propInt);
        int minAge = Collections.min(propInt.getAllowedValues());
        int maxAge = Collections.max(propInt.getAllowedValues());
        if (minAge == maxAge) {
          //degenerate edge case: either this was made wrong OR its not meant to grow
          //like a stem or log or something;
          continue;
        }
        if (currentAge == maxAge) {
          isDone = true;
          //dont set a brand new state, we want to keep all properties the same and only reset age
          //EXAMPLE cocoa beans have a property for facing direction == where they attach to log
          //so when replanting, keep that facing data
          world.setBlockState(posCurrent, blockState.withProperty(propInt, minAge));
          blockCheck.getDrops(drops, world, posCurrent, blockState, 0);
          ModCyclic.logger.log("harvesting age " + currentAge + " == " + maxAge + ":" + blockCheck.getLocalizedName() + " dropSize=>" + drops.size());
          if (tryRemoveOneSeed) {
            Item seedItem = blockCheck.getItemDropped(blockCheck.getDefaultState(), world.rand, 0);
            if (seedItem == null) {
              seedItem = Item.getItemFromBlock(blockCheck);
            }
            try {
              if (drops.size() > 1 && seedItem != null) {
                //  if it dropped more than one ( seed and a thing)
                for (Iterator<ItemStack> iterator = drops.iterator(); iterator.hasNext();) {
                  final ItemStack drop = iterator.next();
                  if (drop.getItem() == seedItem) { // Remove exactly one seed (consume for replanting
                    iterator.remove();
                    ModCyclic.logger.info("yay remove seed " + drop.getDisplayName());
                    break;
                  }
                }
              }
            }
            catch (Exception e) {
              ModCyclic.logger.error("Crop could not be harvested by Cyclic, contact both mod authors");
              ModCyclic.logger.error(e.getMessage());
              e.printStackTrace();
            }
          }
        }
        break;
      }
    }
    return drops;
  }
}
