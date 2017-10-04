package com.lothrazar.cyclicmagic.util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import com.google.common.collect.UnmodifiableIterator;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.data.Const;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockMushroom;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockStem;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.IGrowable;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.config.Configuration;

public class UtilHarvester {
  static final boolean tryRemoveOneSeed = true;
  public static NonNullList<ItemStack> harvestSingle(World world, BlockPos posCurrent) {
    final NonNullList<ItemStack> drops = NonNullList.create();
    IBlockState blockState = world.getBlockState(posCurrent);
    Block blockCheck = blockState.getBlock();
    if (blockCheck == Blocks.AIR) {
      return drops;
    }
    String blockId = blockCheck.getRegistryName().toString();
    //new generic harvest
    UnmodifiableIterator<Entry<IProperty<?>, Comparable<?>>> unmodifiableiterator = blockState.getProperties().entrySet().iterator();
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
    if (blockCheck instanceof IShearable) {
      //      addDropsToList = false;
      //      drops.addAll(((IShearable) blockCheck).onSheared(ItemStack.EMPTY, world, posCurrent, 0));
      //      doBreak = true;
    }
    return drops;
  }
  //  private static boolean isItemInBlacklist(Item seedItem) {
  //    String itemName = UtilItemStack.getStringForItem(seedItem);
  //    for (String s : blacklist) {//dont use .contains on the list. must use .equals on string
  //      if (s != null && s.equals(itemName)) {
  //        return true;
  //      }
  //    }
  //    return false;
  //  }
}
