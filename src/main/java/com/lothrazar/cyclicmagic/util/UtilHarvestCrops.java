package com.lothrazar.cyclicmagic.util;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockDoublePlant;
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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UtilHarvestCrops {
  public static class HarestCropsConfig {
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
  public static int harvestArea(World world, BlockPos pos, int hRadius, HarestCropsConfig conf) {
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
  public static boolean harvestSingle(World world, BlockPos posCurrent, HarestCropsConfig conf) {
    if (posCurrent == null) { return false; }
    boolean doBreakAbove = false;
    boolean doBreakBelow = false;
    boolean doBreak = false;
    IBlockState stateReplant = null;
    IBlockState blockState = world.getBlockState(posCurrent);
    if (blockState == null) { return false; }
    Block blockCheck = blockState.getBlock();
    if (blockCheck == null) { return false; }
    IBlockState bsAbove = world.getBlockState(posCurrent.up());
    IBlockState bsBelow = world.getBlockState(posCurrent.down());
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
    else if (blockCheck == Blocks.RED_FLOWER || blockCheck == Blocks.YELLOW_FLOWER) {
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
    else if (blockCheck.getClass().toString().replaceAll("class ", "").equals("tehnut.resourceful.crops.block.BlockResourcefulCrop")) {
      // https://minecraft.curseforge.com/projects/resourcefulcrops
      int maybeAge = blockCheck.getMetaFromState(blockState);
      if (maybeAge == 7) {//age 7 just like vanilla crops 
        doBreak = true;
      }
    }
    final Item seedItem = getSeedItem(world, blockCheck);
    List<ItemStack> drops;
    try {
      drops = blockCheck.getDrops(world, posCurrent, blockState, 0);
    }
    catch (Exception e) {
      return false;// i did see this happen a few times yes. error in the other mod
    }
    //break above first BECAUSE 2 high tallgrass otherwise will bug out if you break bottom first
//    if (blockCheck.getClass().toString().replaceAll("class ", "").equals("com.infinityraider.agricraft.blocks.BlockCrop")) {
//      // https://minecraft.curseforge.com/projects/agricraft
//     // int maybeAge = blockCheck.getMetaFromState(blockState);
//      //age is always zero. so i suppose 100% its in the tile entity data? so we need to manage dependency if theres no api?
//      //drop this for now
//      return false;
//    }
    // age is the problem int age = ((Integer) blockState.getValue(BlockCocoa.AGE)).intValue();
    // no , for now is fine, do not do blocks
    if (doBreak) {
      // get the Actual drops
      if (doBreakAbove) {
        world.destroyBlock(posCurrent.up(), false);
      }
      if (doBreakBelow) {
        world.destroyBlock(posCurrent.down(), false);
      }
      if (stateReplant != null) {// plant new seed
        //world.setBlockState(posCurrent, blockCheck.getDefaultState());// OLD WAY
        world.destroyBlock(posCurrent, false);//false == no drops. literally just for the sound
        world.setBlockState(posCurrent, stateReplant);// new way
        //whateveer it drops if it wasnt full grown, yeah thats the seed
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
        //now we can upgrade this to also drop in front wooo!
        for (ItemStack drop : drops) {
          if (drop != null)
            UtilItemStack.dropItemStackInWorld(world, posCurrent, drop);
        }
      }
      else {
        //dont replant, but still doBreak. for non farming stuff like grass/leaves
        world.destroyBlock(posCurrent, true);
      }
      return true;
    }
    return false;
  }
  private static Item getSeedItem(World world, Block blockCheck) {
    final Item seedItem = blockCheck.getItemDropped(blockCheck.getDefaultState(), world.rand, 0);
    return seedItem;
  }
}
