package com.lothrazar.cyclicmagic.util;
import com.lothrazar.cyclicmagic.ModMain;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockMushroom;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockStem;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UtilHarvestCrops {
  public static class HarestCropsConfig {
    public boolean doesHarvestStem = false;
    public boolean doesHarvestSapling = false;
    public boolean doesHarvestMushroom = false;
    public boolean doesPumpkinBlocks = false;
    public boolean doesMelonBlocks = false;
    public boolean doesFlowers = false;
    public boolean doesLeaves = false;
    public boolean doesCrops = false;
    // this hits both the short regular grass, and tall grass, and 2 high flowers. split it up
    public boolean doesHarvestTallgrass = false;
  }
  public final static boolean dolog = false;
  public static int harvestArea(World world, BlockPos pos, int xRadius, HarestCropsConfig conf) {
    if (dolog)
      ModMain.logger.info("buildradius = " + xRadius);
    int x = pos.getX();
    int eventy = pos.getY();
    int z = pos.getZ();
    // search in a cube
    int xMin = x - xRadius;
    int xMax = x + xRadius;
    int zMin = z - xRadius;
    int zMax = z + xRadius;
    if (dolog)
      ModMain.logger.info("x = " + xMin + ":" + xMax);
    if (dolog)
      ModMain.logger.info("z = " + zMin + ":" + zMax);
    BlockPos posCurrent;
    int countHarvested = 0;
    for (int xLoop = xMin; xLoop <= xMax; xLoop++) {
      for (int zLoop = zMin; zLoop <= zMax; zLoop++) {
        posCurrent = new BlockPos(xLoop, eventy, zLoop);
        if (dolog)
          ModMain.logger.info("posCurrent = " + posCurrent);
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
    boolean doBreakAbove = false;
    boolean doBreakBelow = false;
    boolean doBreak = false;
    boolean doReplant = false;
    IBlockState bs = world.getBlockState(posCurrent);
    if (bs == null) { return false; }
    Block blockCheck = bs.getBlock();
    if (blockCheck == null) { return false; }
    IBlockState bsAbove = world.getBlockState(posCurrent.up());
    IBlockState bsBelow = world.getBlockState(posCurrent.down());
    if (dolog)
      ModMain.logger.info(" harvestSingleAt " + UtilChat.blockPosToString(posCurrent));
    if (dolog)
      ModMain.logger.info("blockCheck = " + blockCheck.getClass().getName() + " -> " + blockCheck.getUnlocalizedName());
    if (blockCheck instanceof IGrowable && conf.doesCrops) {
      IGrowable plant = (IGrowable) blockCheck;
      if (dolog)
        ModMain.logger.info(" IGrowable ");
      // only if its full grown
      if (plant.canGrow(world, posCurrent, bs, world.isRemote) == false) {
        if (dolog)
          ModMain.logger.info(" fully grown ");
        doBreak = true;
        doReplant = true;
      }
    }
    if ((blockCheck instanceof BlockStem) && conf.doesHarvestStem) {
      if (dolog)
        ModMain.logger.info(" stem ");
      doBreak = true;
    }
    else if ((blockCheck instanceof BlockSapling) && conf.doesHarvestSapling) {
      if (dolog)
        ModMain.logger.info(" sapling ");
      doBreak = true;
    }
    else if ((blockCheck instanceof BlockTallGrass || blockCheck instanceof BlockDoublePlant)
        && conf.doesHarvestTallgrass) {
      doBreak = true;
      doReplant = false;
      if (dolog)
        ModMain.logger.info(posCurrent.toString() + " tallgrass ");
      if (blockCheck instanceof BlockDoublePlant && bsAbove != null && bsAbove.getBlock() instanceof BlockDoublePlant) {
        doBreakAbove = true;
        if (dolog)
          ModMain.logger.info("above " + UtilChat.blockPosToString(posCurrent.up()));
      }
      if (bsBelow instanceof BlockDoublePlant && bsBelow != null && bsBelow.getBlock() instanceof BlockDoublePlant) {
        doBreakBelow = true;
        if (dolog)
          ModMain.logger.info("above " + UtilChat.blockPosToString(posCurrent.up()));
      }
    }
    else if ((blockCheck instanceof BlockMushroom) && conf.doesHarvestMushroom) {
      if (dolog)
        ModMain.logger.info(" BlockMushroom ");
      doBreak = true;
    }
    else if (blockCheck == Blocks.PUMPKIN && conf.doesPumpkinBlocks) {
      if (dolog)
        ModMain.logger.info(" pumpkin ");
      doBreak = true;
      doReplant = false;
    }
    else if (blockCheck == Blocks.MELON_BLOCK && conf.doesMelonBlocks) {
      if (dolog)
        ModMain.logger.info(" melon_block ");
      doBreak = true;
      doReplant = false;
    }
    else if ((blockCheck == Blocks.RED_FLOWER || blockCheck == Blocks.YELLOW_FLOWER) && conf.doesFlowers) {
      doBreak = true;
      doReplant = false;
    }
    else if ((blockCheck == Blocks.LEAVES || blockCheck == Blocks.LEAVES2) && conf.doesLeaves) {
      doBreak = true;
      doReplant = false;
    }
    // no , for now is fine, do not do blocks
    if (doBreak) {
      world.destroyBlock(posCurrent, true);
      //break above first BECAUSE 2 high tallgrass otherwise will bug out if you break bottom first
      if (doBreakAbove) {
        world.destroyBlock(posCurrent.up(), false);
      }
      if (doBreakBelow) {
        world.destroyBlock(posCurrent.down(), false);
      }
      if (doReplant) {// plant new seed
        if (dolog)
          ModMain.logger.info(" replant ");
        world.setBlockState(posCurrent, blockCheck.getDefaultState());
      }
      return true;
      //			countHarvested++;
    }
    return false;
  }
}
