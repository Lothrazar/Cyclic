package com.lothrazar.cyclicmagic.block.tileentity;

import java.util.ArrayList;
import java.util.Random;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEntityFishing  extends TileEntity implements ITickable {

  public ArrayList<Block> waterBoth = new ArrayList<Block>();
  
  public TileEntityFishing(){

    waterBoth.add(Blocks.FLOWING_WATER);
    waterBoth.add(Blocks.WATER);
  }
  private boolean isValidPlacement(){
    return waterBoth.contains(worldObj.getBlockState(pos.down()).getBlock()) &&
    waterBoth.contains(worldObj.getBlockState(pos.down(2)).getBlock()) &&
    //  waterBoth.contains(worldObj.getBlockState(pos.down(3)).getBlock()   ) &&
    waterBoth.contains(worldObj.getBlockState(pos.north()).getBlock()) &&
    waterBoth.contains(worldObj.getBlockState(pos.east()).getBlock()) &&
    waterBoth.contains(worldObj.getBlockState(pos.west()).getBlock()) &&
    waterBoth.contains(worldObj.getBlockState(pos.south()).getBlock());
  }
  @Override
  public void update() {
    Random rand = worldObj.rand;
 
  //make sure surrounded by water
    if (rand.nextDouble() < 0.001 && isValidPlacement()) {
      //reference for chances 
      // http://minecraft.gamepedia.com/Fishing_Rod#Junk_and_treasures
      //i know junk can do stuff like leather, stick, string, etc
      //but for this, junk gives us NADA
      //and treasure is nada as well
      ItemStack plain = new ItemStack(Items.FISH, 1, 0);
      double plainChance = 60;
      ItemStack salmon = new ItemStack(Items.FISH, 1, 1);
      double salmonChance = 25 + plainChance;//so it is between 60 and 85
      ItemStack clownfish = new ItemStack(Items.FISH, 1, 2);
      double clownfishChance = 2 + salmonChance;//so between 85 and 87
      ItemStack pufferfish = new ItemStack(Items.FISH, 1, 3);
      double diceRoll = rand.nextDouble() * 100;
      ItemStack fishSpawned;
      if (diceRoll < plainChance) {
        fishSpawned = plain;
      }
      else if (diceRoll < salmonChance) {
        fishSpawned = salmon;
      }
      else if (diceRoll < clownfishChance) {
        fishSpawned = clownfish;
      }
      else {
        fishSpawned = pufferfish;
      }
      UtilEntity.dropItemStackInWorld(worldObj, pos, fishSpawned);
      //      EntityItem ei = ModFarmingBlocks.dropItemStackInWorld(worldObj, pos.up(), fishSpawned);
      //  
      //      worldObj.playSoundAtEntity(ei, "game.neutral.swim.splash", 1.0F, 1.0F);
    }
  }
}
