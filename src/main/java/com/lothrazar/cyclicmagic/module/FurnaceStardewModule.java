package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilInventory;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FurnaceStardewModule extends BaseEventModule implements IHasConfig {
  // inspired by stardew valley
  // http://minecraft.gamepedia.com/Furnace
  private static boolean stardewFurnace;
  final static int SLOT_INPUT = 0;
  final static int SLOT_FUEL = 1;
  final static int SLOT_OUTPUT = 2;
  @SubscribeEvent
  public void onPlayerFurnace(PlayerInteractEvent.LeftClickBlock event) {//extends PlayerInteractEvent
    if (stardewFurnace) {
      EntityPlayer entityPlayer = event.getEntityPlayer();
      // ignore in creative// left clicking just breaks it anyway
      if (entityPlayer.capabilities.isCreativeMode) { return; }
      BlockPos pos = event.getPos();
      World worldObj = event.getWorld();
      if (pos == null) { return; }
      ItemStack held = entityPlayer.getHeldItem(event.getHand());
      int playerSlot = 0;// entityPlayer.inventory.currentItem;
      boolean wasMain = event.getHand() == EnumHand.MAIN_HAND;
      if (wasMain) {
        playerSlot = entityPlayer.inventory.currentItem;
      }
      else {
        //just dont use offhand, ignore it for now. is easier
        playerSlot = 40;
      }
      TileEntity tile = worldObj.getTileEntity(pos);
      if (tile instanceof TileEntityFurnace) {
        TileEntityFurnace furnace = (TileEntityFurnace) tile;
        if (held == null) {
          extractFurnaceOutput(furnace, entityPlayer);
        }
        else {
          //  ModMain.logger.info("held:" + held.getUnlocalizedName());
          //holding a non null stack for sure
          if (canBeSmelted(held)) {
            //  ModMain.logger.info("SLOT_INPUT");
            tryMergeStackIntoSlot(furnace, entityPlayer, playerSlot, SLOT_INPUT);
          }
          else if (isFuel(held)) {
            // ModMain.logger.info("SLOT_FUEL");
            tryMergeStackIntoSlot(furnace, entityPlayer, playerSlot, SLOT_FUEL);
          }
        }
      }
    }
  }
  private void tryMergeStackIntoSlot(TileEntityFurnace furnace, EntityPlayer entityPlayer, int playerSlot, int furnaceSlot) {
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
  private void extractFurnaceOutput(TileEntityFurnace furnace, EntityPlayer player) {
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
  private boolean canBeSmelted(ItemStack input) {
    // we literally get the smelt recipe instance to test if it has one
    ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(input);
    return (itemstack != null);
  }
  private boolean isFuel(ItemStack input) {
    // how long does it burn for? zero means it isnt fuel
    return TileEntityFurnace.getItemBurnTime(input) > 0;
  }
  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.player;
    stardewFurnace = config.getBoolean("Furnace Speed", category, true,
        "Stardew Furnaces: Quickly fill a furnace by hitting it with fuel or an item, or interact with an empty hand to pull out the results [Inspired by Stardew Valley.  Left click only]");
  }
}
