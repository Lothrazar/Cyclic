package com.lothrazar.cyclicmagic.block.tileentity;

import java.util.ArrayList;
import java.util.Random;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

public class TileEntityFishing extends TileEntityBaseMachineInvo implements ITickable {
  private static final String NBT_INV = "Inventory";
  private static final String NBT_SLOT = "Slot";
  public ArrayList<Block> waterBoth = new ArrayList<Block>();
  private ItemStack[] inv;
  public TileEntityFishing() {
    inv = new ItemStack[9];

    waterBoth.add(Blocks.FLOWING_WATER);
    waterBoth.add(Blocks.WATER);
  }
  public boolean isValidPlacement(){
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
  @Override
  public int getSizeInventory() {
    return inv.length;
  }
  @Override
  public ItemStack getStackInSlot(int index) {
    return inv[index];
  }

  @Override
  public ItemStack decrStackSize(int index, int count) {
    ItemStack stack = getStackInSlot(index);
    if (stack != null) {
      if (stack.stackSize <= count) {
        setInventorySlotContents(index, null);
      }
      else {
        stack = stack.splitStack(count);
        if (stack.stackSize == 0) {
          setInventorySlotContents(index, null);
        }
      }
    }
    return stack;
  }
  @Override
  public ItemStack removeStackFromSlot(int index) {
    ItemStack stack = getStackInSlot(index);
    if (stack != null) {
      setInventorySlotContents(index, null);
    }
    return stack;
  }

  @Override
  public void setInventorySlotContents(int index, ItemStack stack) {
    inv[index] = stack;
    if (stack != null && stack.stackSize > getInventoryStackLimit()) {
      stack.stackSize = getInventoryStackLimit();
    }
  }  private int[] hopperInput = { 0, 1, 2, 3, 4, 5, 6, 7, 8 };// all slots for all faces
  
  @Override
  public int[] getSlotsForFace(EnumFacing side) {
    return hopperInput;
  }

  @Override
  public void readFromNBT(NBTTagCompound tagCompound) {
    super.readFromNBT(tagCompound);
//    timer = tagCompound.getInteger(NBT_TIMER);
    NBTTagList tagList = tagCompound.getTagList(NBT_INV, 10);
    for (int i = 0; i < tagList.tagCount(); i++) {
      NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
      byte slot = tag.getByte(NBT_SLOT);
      if (slot >= 0 && slot < inv.length) {
        inv[slot] = ItemStack.loadItemStackFromNBT(tag);
      }
    }
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
//    tagCompound.setInteger(NBT_TIMER, timer);
    NBTTagList itemList = new NBTTagList();
    for (int i = 0; i < inv.length; i++) {
      ItemStack stack = inv[i];
      if (stack != null) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setByte(NBT_SLOT, (byte) i);
        stack.writeToNBT(tag);
        itemList.appendTag(tag);
      }
    }
    tagCompound.setTag(NBT_INV, itemList);
    return super.writeToNBT(tagCompound);
  }
  
}
