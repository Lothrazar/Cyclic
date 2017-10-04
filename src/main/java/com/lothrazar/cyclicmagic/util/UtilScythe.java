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

public class UtilScythe {
  public static class HarvestSetting {
    public boolean doesStem = false;
    public boolean doesSapling = false;
    public boolean doesMushroom = false;
    public boolean doesPumpkinBlocks = false;
    public boolean doesMelonBlocks = false;
    public boolean doesFlowers = false;
    public boolean doesLeaves = false;
    public boolean doesCrops = false;
    // this hits both the short regular grass, and tall grass, and 2 high flowers. split it up
    public boolean doesTallgrass = false;
    public boolean doesCactus = false;
    public boolean doesReeds = false;
    public boolean doesIShearable = false;
    public List<ItemStack> drops;
    @Override
    public String toString() {
      String s = "";
      s += "doesHarvestStem = " + doesStem + System.lineSeparator();
      s += "doesHarvestSapling = " + doesSapling + System.lineSeparator();
      s += "doesHarvestMushroom = " + doesMushroom + System.lineSeparator();
      s += "doesPumpkinBlocks = " + doesPumpkinBlocks + System.lineSeparator();
      s += "doesMelonBlocks = " + doesMelonBlocks + System.lineSeparator();
      s += "doesFlowers = " + doesFlowers + System.lineSeparator();
      s += "doesCrops = " + doesCrops + System.lineSeparator();
      s += "doesHarvestTallgrass = " + doesTallgrass + System.lineSeparator();
      return s;
    }
  }
  private static String[] blacklist;
  public static boolean harvestSingle(World world, BlockPos posCurrent, HarvestSetting conf) {
    boolean doBreakAbove = false;
    boolean doBreakBelow = false;
    boolean doBreak = false;
    IBlockState stateReplant = null;
    IBlockState blockState = world.getBlockState(posCurrent);
    if (blockState == null) {
      return false;
    }
    boolean addDropsToList = true;
    Block blockCheck = blockState.getBlock();
    if (blockCheck == Blocks.AIR) {
      return false;
    }
    Item seedItem = blockCheck.getItemDropped(blockCheck.getDefaultState(), world.rand, 0);//RuntimeException at this line
    if (isItemInBlacklist(seedItem)) {
      return false;
    }
    String blockClassString = blockCheck.getClass().getName();//TODO: config file eventually but hotfix for now
    //    ModCyclic.logger.info(blockClassString);
    //ModCyclic.logger.info(blockClassString+ posCurrent);
    IBlockState bsAbove = world.getBlockState(posCurrent.up());
    IBlockState bsBelow = world.getBlockState(posCurrent.down());
    final NonNullList<ItemStack> drops = NonNullList.create();
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
    if (blockCheck instanceof BlockTallGrass) {// true for ItemScythe type WEEDS
      if (conf.doesTallgrass) {
        doBreak = true;
        if (blockCheck instanceof BlockTallGrass && bsAbove != null && bsAbove.getBlock() instanceof BlockTallGrass) {
          doBreakAbove = true;
        }
        if (bsBelow instanceof BlockTallGrass && bsBelow != null && bsBelow.getBlock() instanceof BlockTallGrass) {
          doBreakBelow = true;
        }
      }
    }
    else if (blockCheck instanceof BlockDoublePlant) {// true for ItemScythe type WEEDS
      if (conf.doesTallgrass) {
        doBreak = true;
        if (blockCheck instanceof BlockDoublePlant && bsAbove != null && bsAbove.getBlock() instanceof BlockDoublePlant) {
          doBreakAbove = true;
        }
        if (bsBelow instanceof BlockDoublePlant && bsBelow != null && bsBelow.getBlock() instanceof BlockDoublePlant) {
          doBreakBelow = true;
        }
      }
    }
    else if (blockCheck instanceof BlockMushroom) {//remove from harvester tile? used by weeds though
      if (conf.doesMushroom)
        doBreak = true;
    }
    //    else if (blockCheck == Blocks.PUMPKIN) {
    //      if (conf.doesPumpkinBlocks) {
    //        doBreak = true;
    //      }
    //    }
    //    else if (blockCheck == Blocks.MELON_BLOCK) {
    //      if (conf.doesMelonBlocks) {
    //        doBreak = false;//not the standard break - custom rules to mimic silktouch
    //        world.destroyBlock(posCurrent, false);
    //        UtilItemStack.dropItemStackInWorld(world, posCurrent, Blocks.MELON_BLOCK);
    //      }
    //    }
    //cant do BlockBush, too generic, too many things use.  
    else if (blockCheck == Blocks.RED_FLOWER || blockCheck == Blocks.YELLOW_FLOWER
        || blockCheck instanceof BlockFlower
        || blockClassString.equals("shadows.plants.block.PlantBase")
        || blockClassString.equals("shadows.plants.block.internal.cosmetic.BlockHarvestable")
        || blockClassString.equals("shadows.plants.block.internal.cosmetic.BlockMetaBush")
        || blockClassString.equals("de.ellpeck.actuallyadditions.mod.blocks.BlockBlackLotus")
        || blockClassString.equals("de.ellpeck.actuallyadditions.mod.blocks.base.BlockWildPlant")
        || blockClassString.equals("biomesoplenty.common.block.BlockBOPMushroom")
        || blockClassString.equals("rustic.common.blocks.crops.Herbs$1")) {
      if (conf.doesFlowers) { // true for ItemScythe type WEEDS
        doBreak = true;
      }
    }
    else if (blockCheck instanceof BlockLeaves) {// true for ItemScythe type LEAVES
      if (conf.doesLeaves) {
        doBreak = true;
      }
    }
    //    else if (blockCheck == Blocks.CACTUS && bsBelow != null && bsBelow.getBlock() == Blocks.CACTUS) {
    //       
    //      if (conf.doesCactus) { //never breaking the bottom one
    //        doBreak = true;
    //        if (bsAbove != null && bsAbove.getBlock() == Blocks.CACTUS) {
    //          doBreakAbove = true;
    //        }
    //      }
    //    }
    //    else if (blockCheck == Blocks.REEDS && bsBelow != null && bsBelow.getBlock() == Blocks.REEDS) {
    //      if (conf.doesReeds) {//never breaking the bottom one
    //        doBreak = true;
    //        if (bsAbove != null && bsAbove.getBlock() == Blocks.REEDS) {
    //          doBreakAbove = true;
    //        }
    //      }
    //    }
    //    else if (blockCheck instanceof IGrowable) {
    //      if (conf.doesCrops) {
    //        IGrowable plant = (IGrowable) blockCheck;
    //        // only if its full grown
    //        if (plant.canGrow(world, posCurrent, blockState, world.isRemote) == false) {
    //          doBreak = true;
    //          stateReplant = blockCheck.getDefaultState();
    //        }
    //      }
    //    }
    else if (blockCheck instanceof IShearable) {
      if (conf.doesIShearable) {
        addDropsToList = false;
        drops.addAll(((IShearable) blockCheck).onSheared(ItemStack.EMPTY, world, posCurrent, 0));
        //        int test = drops.size();
        doBreak = true;
      }
    }
    //    else  ModCyclic.logger.info("!"+blockClassString);
    // no , for now is fine, do not do blocks
    if (doBreak) {
      //break with false so that we can get the drops our own way
      world.destroyBlock(posCurrent, false);//false == no drops. literally just for the sound
      if (addDropsToList) {
        blockCheck.getDrops(drops, world, posCurrent, blockState, 0);
      }
      //break above first BECAUSE 2 high tallgrass otherwise will bug out if you break bottom first
      if (doBreakAbove) {
        world.destroyBlock(posCurrent.up(), false);
      }
      if (doBreakBelow) {
        world.destroyBlock(posCurrent.down(), false);
      }
      for (ItemStack drop : drops) {
        UtilItemStack.dropItemStackInWorld(world, posCurrent, drop);
      }
      return true;
    }
    return false;
  }
  private static boolean isItemInBlacklist(Item seedItem) {
    String itemName = UtilItemStack.getStringForItem(seedItem);
    for (String s : blacklist) {//dont use .contains on the list. must use .equals on string
      if (s != null && s.equals(itemName)) {
        return true;
      }
    }
    return false;
  }
  public static void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.modpackMisc;
    String[] deflist = new String[] {
        "terraqueous:pergola"
    };
    blacklist = config.getStringList("HarvesterBlacklist", category, deflist, "Crops & bushes that are blocked from harvesting (Garden Scythe and Harvester).  Put an item that gets dropped to blacklist the harvest.  For example, add the item minecraft:potato to stop those from working");
  }
}
