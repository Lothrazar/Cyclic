package com.lothrazar.cyclicmagic.util;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.data.Const;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockMushroom;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockStem;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.config.Configuration;

public class UtilHarvestCrops {
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
    public boolean dropInPlace = true;//if false -> then ret
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
    public void resetDrops() {
      drops = new ArrayList<ItemStack>();
    }
    public void addDrops(List<ItemStack> d) {
      this.drops.addAll(d);
    }
    public void setDrops(List<ItemStack> d) {
      this.drops = d;
    }
  }
  private static String[] blacklist;
  public static int harvestArea(World world, BlockPos pos, int hRadius, HarvestSetting conf) {
    conf.resetDrops();
    int x = pos.getX();
    int eventy = pos.getY();
    int z = pos.getZ();
    // search in a cube
    int xMin = x - hRadius;
    int xMax = x + hRadius;
    int zMin = z - hRadius;
    int zMax = z + hRadius;
    BlockPos posCurrent;
    int countHarvested = 0;
    for (int xLoop = xMin; xLoop <= xMax; xLoop++) {
      for (int zLoop = zMin; zLoop <= zMax; zLoop++) {
        posCurrent = new BlockPos(xLoop, eventy, zLoop);
        if (world.isAirBlock(posCurrent)) {
          continue;
        }
        if (harvestSingle(world, posCurrent, conf)) {
          countHarvested++;
        }
      }
    } // end of the outer loop
    return countHarvested;
  }
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
    if (blockCheck instanceof BlockNetherWart) {
      if (conf.doesCrops) {
        int age = ((Integer) blockState.getValue(BlockNetherWart.AGE)).intValue();
        if (age == 3) {//this is hardcoded in base class
          doBreak = true;
          stateReplant = blockCheck.getDefaultState();
        }
      }
    }
    if (blockCheck instanceof BlockCocoa) {
      if (conf.doesCrops) {
        int age = ((Integer) blockState.getValue(BlockCocoa.AGE)).intValue();
        if (age == 2) {//this is hardcoded in base class
          doBreak = true;
          // a new state that copies the property but NOT the age
          stateReplant = blockCheck.getDefaultState().withProperty(BlockCocoa.FACING, blockState.getValue(BlockCocoa.FACING));
        }
      }
    }
    else if (blockCheck instanceof BlockStem) {
      if (conf.doesStem)
        doBreak = true;
    }
    else if (blockCheck instanceof BlockSapling) {
      if (conf.doesSapling)
        doBreak = true;
    }
    else if (blockCheck instanceof BlockTallGrass) {
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
    else if (blockCheck instanceof BlockDoublePlant) {
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
    else if (blockCheck instanceof BlockMushroom) {
      if (conf.doesMushroom)
        doBreak = true;
    }
    else if (blockCheck == Blocks.PUMPKIN) {
      if (conf.doesPumpkinBlocks) {
        doBreak = true;
      }
    }
    else if (blockCheck == Blocks.MELON_BLOCK) {
      if (conf.doesMelonBlocks) {
        doBreak = false;//not the standard break - custom rules to mimic silktouch
        world.destroyBlock(posCurrent, false);
        UtilItemStack.dropItemStackInWorld(world, posCurrent, Blocks.MELON_BLOCK);
      }
    }
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
      if (conf.doesFlowers) {
        doBreak = true;
      }
    }
    else if (blockCheck instanceof BlockLeaves) { //  || blockCheck == Blocks.LEAVES || blockCheck == Blocks.LEAVES2
      if (conf.doesLeaves) {
        doBreak = true;
      }
    }
    else if (blockCheck == Blocks.CACTUS && bsBelow != null && bsBelow.getBlock() == Blocks.CACTUS) {
      if (conf.doesCactus) { //never breaking the bottom one
        doBreak = true;
        if (bsAbove != null && bsAbove.getBlock() == Blocks.CACTUS) {
          doBreakAbove = true;
        }
      }
    }
    else if (blockCheck == Blocks.REEDS && bsBelow != null && bsBelow.getBlock() == Blocks.REEDS) {
      if (conf.doesReeds) {//never breaking the bottom one
        doBreak = true;
        if (bsAbove != null && bsAbove.getBlock() == Blocks.REEDS) {
          doBreakAbove = true;
        }
      }
    }
    else if (blockCheck instanceof IGrowable) {
      if (conf.doesCrops) {
        IGrowable plant = (IGrowable) blockCheck;
        // only if its full grown
        if (plant.canGrow(world, posCurrent, blockState, world.isRemote) == false) {
          doBreak = true;
          stateReplant = blockCheck.getDefaultState();
        }
      }
    }
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
      if (stateReplant != null) {// plant new seed
        //world.setBlockState(posCurrent, blockCheck.getDefaultState());// OLD WAY
        world.setBlockState(posCurrent, stateReplant);// new way
        //whateveer it drops if it wasnt full grown, yeah thats the seed
        //but we cant fix runtime exception since its from somebody elses mod
        // com.polipo.exp.BlockExpPlant.func_180660_a(BlockExpPlant.java:237)
        // https://mods.curse.com/mc-mods/minecraft/230553-giacomos-experience-seedling
        try {
          if (drops.size() > 1 && seedItem != null) {
            //  if it dropped more than one ( seed and a thing)
            for (Iterator<ItemStack> iterator = drops.iterator(); iterator.hasNext();) {
              final ItemStack drop = iterator.next();
              if (drop.getItem() == seedItem) { // Remove exactly one seed (consume for replanting
                iterator.remove();
                //ModMain.logger.info("yay remove seed "+drop.getDisplayName());
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
      if (drops.size() > 0) {
        if (conf.dropInPlace) {
          for (ItemStack drop : drops) {
            UtilItemStack.dropItemStackInWorld(world, posCurrent, drop);
          }
        }
        else {
          conf.addDrops(drops);
        }
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
