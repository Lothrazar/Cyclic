package com.lothrazar.cyclicmagic.util;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map.Entry;
import com.google.common.collect.UnmodifiableIterator;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.data.Const;
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
  private static final int FORTUNE = 1;
  private static final String AGE = "age";
  static final boolean tryRemoveOneSeed = true;
  private static NonNullList<String> blocksBreakInPlace;
  private static NonNullList<String> blocksGetDrop;
  private static NonNullList<String> blocksSilkTouch;
  private static NonNullList<String> blockIgnore;
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
    blocksGetDrop = NonNullList.from("",
        "minecraft:pumpkin"
        , "croparia:block_plant_*"
        , "croparia:block_cane_*"
        );
    blocksBreakInPlace = NonNullList.from("",
        "attaineddrops2:bulb"
        );
    blocksSilkTouch = NonNullList.from(""
        ,"minecraft:melon_block");
    /* @formatter:on */
  }
  private static boolean isBreakInPlace(ResourceLocation blockId) {
    return UtilString.isInList(blocksBreakInPlace, blockId);
  }
  private static boolean isIgnored(ResourceLocation blockId) {
    return UtilString.isInList(blockIgnore, blockId);
  }
  private static boolean isGetDrops(ResourceLocation blockId) {
    return UtilString.isInList(blocksGetDrop, blockId);
  }
  private static boolean isSimpleSilktouch(ResourceLocation blockId) {
    return UtilString.isInList(blocksSilkTouch, blockId);
  }
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
    if (isGetDrops(blockId)) {
      ModCyclic.logger.log("getDrops on " + blockId);
      blockCheck.getDrops(drops, world, posCurrent, blockState, FORTUNE);
      world.setBlockToAir(posCurrent);
      return drops;
    }
    if (isSimpleSilktouch(blockId)) {
      ModCyclic.logger.log("isSimpleSilktouch on " + blockId);
      drops.add(new ItemStack(blockCheck));
      world.setBlockToAir(posCurrent);
      return drops;
    }
    if (isBreakInPlace(blockId)) {
      ModCyclic.logger.log("isBreakInPlace on " + blockId);
      world.destroyBlock(posCurrent, true);
      return drops;
    }
    //new generic harvest
    UnmodifiableIterator<Entry<IProperty<?>, Comparable<?>>> unmodifiableiterator = blockState.getProperties().entrySet().iterator();
    while (unmodifiableiterator.hasNext()) {
      Entry<IProperty<?>, Comparable<?>> entry = unmodifiableiterator.next();
      IProperty<?> iproperty = entry.getKey();
      if (iproperty.getName() != null &&
          iproperty.getName().equals(AGE) && iproperty instanceof PropertyInteger) {
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
          //  isDone = true;
          //dont set a brand new state, we want to keep all properties the same and only reset age
          //EXAMPLE cocoa beans have a property for facing direction == where they attach to log
          //so when replanting, keep that facing data
          world.setBlockState(posCurrent, blockState.withProperty(propInt, minAge));
          blockCheck.getDrops(drops, world, posCurrent, blockState, FORTUNE);
          ModCyclic.logger.log("harvesting  " + blockId + " dropSize=>" + drops.size() + " currentAge = " + currentAge + " maxAge= " + maxAge);
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
                    ModCyclic.logger.log("yay remove seed " + drop.getDisplayName());
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
    //    if (blockCheck.getRegistryName().getResourceDomain().equals("minecraft") == false) {
    //      ModCyclic.logger.log("HARVEST IGNORED " + blockId);
    //    }
    return drops;
  }
}
