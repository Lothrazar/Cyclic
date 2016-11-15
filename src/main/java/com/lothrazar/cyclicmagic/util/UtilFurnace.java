package com.lothrazar.cyclicmagic.util;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.math.BlockPos;

public class UtilFurnace {
  // http://minecraft.gamepedia.com/Furnace
  public final static int SLOT_INPUT = 0;
  public final static int SLOT_FUEL = 1;
  public final static int SLOT_OUTPUT = 2;
  public static void tryMergeStackIntoSlot(TileEntityFurnace furnace, EntityPlayer entityPlayer, int playerSlot, int furnaceSlot) {
    ItemStack current = furnace.getStackInSlot(furnaceSlot);
    //removeStackFromSlot//why the FFFFFFFFFF was i using this garbage funtion
    ItemStack held = entityPlayer.inventory.getStackInSlot(playerSlot);
    // ModMain.logger.info("held!!!:" + held.getUnlocalizedName());
    boolean success = false;
    if (current == null) {
      // just done
      // ModMain.logger.info("slot is empty");
      if (entityPlayer.worldObj.isRemote == false) {
        furnace.setInventorySlotContents(furnaceSlot, held.copy());
        held = null;
      }
      success = true;
    }
    else if (held.isItemEqual(current)) {
      //ModMain.logger.info("slot is NOT empty and they match, current old:" + current.stackSize);
      // merging updates the stack size numbers in both furnace and in players
      success = true;
      if (entityPlayer.worldObj.isRemote == false) {
        UtilInventory.mergeItemsBetweenStacks(held, current);
      }
    }
    if (success) {
      //  ModMain.logger.info("success!!!");
      if (entityPlayer.worldObj.isRemote == false) {
        if (held != null && held.stackSize == 0) {// so now we just fix if something is size zero
          held = null;
        }
        entityPlayer.inventory.setInventorySlotContents(playerSlot, held);
        entityPlayer.inventory.markDirty();
      }
      UtilSound.playSound(entityPlayer, SoundEvents.ENTITY_ITEM_PICKUP);
    }
  }
  public static void extractFurnaceOutput(TileEntityFurnace furnace, EntityPlayer player) {
    // ModMain.logger.info("extractFurnaceOutput");
    ItemStack current = furnace.removeStackFromSlot(SLOT_OUTPUT);
    if (current != null) {
      BlockPos pos = player.getPosition();
      if (player.worldObj.isRemote == false) {
        player.dropItemAndGetStack(new EntityItem(player.worldObj, pos.getX(), pos.getY(), pos.getZ(), current));
      }
      UtilSound.playSound(player, SoundEvents.ENTITY_ITEM_PICKUP);
      //UtilEntity.dropItemStackInWorld(furnace.getWorld(), furnace.getPos(), current);
    }
  }
  public static boolean canBeSmelted(ItemStack input) {
    // we literally get the smelt recipe instance to test if it has one
    ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(input);
    return (itemstack != null);
  }
  public static boolean isFuel(ItemStack input) {
    // how long does it burn for? zero means it isnt fuel
    return TileEntityFurnace.getItemBurnTime(input) > 0;
  }
}
