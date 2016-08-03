package com.lothrazar.cyclicmagic.util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockMushroom;
import net.minecraft.block.BlockNetherWart;
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
    public String toString(){
      String s = "";
      s += "doesHarvestStem = "+doesStem +System.lineSeparator();
      s += "doesHarvestSapling = "+doesSapling +System.lineSeparator();
      s += "doesHarvestMushroom = "+doesMushroom +System.lineSeparator();
      s += "doesPumpkinBlocks = "+doesPumpkinBlocks +System.lineSeparator();
      s += "doesMelonBlocks = "+doesMelonBlocks +System.lineSeparator();
      s += "doesFlowers = "+doesFlowers +System.lineSeparator();
      s += "doesCrops = "+doesCrops +System.lineSeparator();
      s += "doesHarvestTallgrass = "+doesTallgrass +System.lineSeparator();
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
    
    if (blockCheck instanceof BlockNetherWart ) {
      if(conf.doesCrops){
        int age = ((Integer)bs.getValue(BlockNetherWart.AGE)).intValue();
        if(age == 3){//this is hardcoded in base class
          doBreak = true;
          doReplant = true;
        }
      }
    }
    else if (blockCheck instanceof BlockStem ) {
      if(conf.doesStem)
        doBreak = true;
    }
    else if (blockCheck instanceof BlockSapling ) {
      if(conf.doesSapling)
        doBreak = true;
    }
    else if (blockCheck instanceof BlockTallGrass    ) {
      if( conf.doesTallgrass){
        doBreak = true;
        doReplant = false;
        if (blockCheck instanceof BlockTallGrass && bsAbove != null && bsAbove.getBlock() instanceof BlockTallGrass) {
          doBreakAbove = true;
        }
        if (bsBelow instanceof BlockTallGrass && bsBelow != null && bsBelow.getBlock() instanceof BlockTallGrass) {
          doBreakBelow = true;
        }
      }
    }
    else if (blockCheck instanceof BlockDoublePlant      ) {
      if( conf.doesTallgrass){
        doBreak = true;
        doReplant = false;
        if (blockCheck instanceof BlockDoublePlant && bsAbove != null && bsAbove.getBlock() instanceof BlockDoublePlant) {
          doBreakAbove = true;
        }
        if (bsBelow instanceof BlockDoublePlant && bsBelow != null && bsBelow.getBlock() instanceof BlockDoublePlant) {
          doBreakBelow = true;
        }
      }
    }
    else if (blockCheck instanceof BlockMushroom) {
      if( conf.doesMushroom)
        doBreak = true;
    }
    else if (blockCheck == Blocks.PUMPKIN ) {
      if(conf.doesPumpkinBlocks){
        doBreak = true;
        doReplant = false;
      }
    }
    else if (blockCheck == Blocks.MELON_BLOCK) {
      if(conf.doesMelonBlocks){
        doBreak = false;//not the standard break - custom rules to mimic silktouch
        doReplant = false;
        world.destroyBlock(posCurrent, false);
        UtilEntity.dropItemStackInWorld(world, posCurrent, Blocks.MELON_BLOCK);
      }
    }
    else if (blockCheck == Blocks.RED_FLOWER || blockCheck == Blocks.YELLOW_FLOWER ) {
      if(conf.doesFlowers){
        doBreak = true;
        doReplant = false;
      }
    }
    else if (blockCheck == Blocks.LEAVES || blockCheck == Blocks.LEAVES2) {
      if(conf.doesLeaves){
        doBreak = true;
        doReplant = false;
      }
    }
    else if (blockCheck == Blocks.CACTUS && bsBelow != null && bsBelow.getBlock() == Blocks.CACTUS) {
      if(conf.doesCactus){//never breaking the bottom one
        doBreak = true;
        doReplant = false;
        if(bsAbove != null && bsAbove.getBlock() == Blocks.CACTUS){
          doBreakAbove = true;
        }
      }
    }
    else if (blockCheck == Blocks.REEDS && bsBelow != null && bsBelow.getBlock() == Blocks.REEDS) {
      if(conf.doesReeds){//never breaking the bottom one
        doBreak = true;
        doReplant = false;
      }
    }
    else if (blockCheck instanceof IGrowable  ) {
      if(conf.doesCrops){
        IGrowable plant = (IGrowable) blockCheck;
        // only if its full grown
        if (plant.canGrow(world, posCurrent, bs, world.isRemote) == false) {
          doBreak = true;
          doReplant = true;
        }
      }
    }
    // no , for now is fine, do not do blocks
    if (doBreak) {
//      ModMain.logger.info("posCurrent="+UtilChat.blockPosToString(posCurrent));
//      ModMain.logger.info("h"+blockCheck.getUnlocalizedName());
//      ModMain.logger.info("doBreakAbove="+doBreakAbove);
//      ModMain.logger.info("doBreakBelow="+doBreakBelow);
//      ModMain.logger.info("doReplant="+doReplant);
      
      world.destroyBlock(posCurrent, true);
      //break above first BECAUSE 2 high tallgrass otherwise will bug out if you break bottom first
      if (doBreakAbove) {
        world.destroyBlock(posCurrent.up(), false);
      }
      if (doBreakBelow) {
        world.destroyBlock(posCurrent.down(), false);
      }
      if (doReplant) {// plant new seed
        world.setBlockState(posCurrent, blockCheck.getDefaultState());
      }
      return true;
    }
    return false;
  }
}
