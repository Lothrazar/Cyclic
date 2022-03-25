package com.lothrazar.cyclic.util;

import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeMod;

public class UtilPlayer {

  public static final UUID REACH_ID = UUID.fromString("1abcdef2-eff2-4a81-b92b-a1cb95f115c6");

  public static void removePlayerReach(PlayerEntity playerIn) {
    ModifiableAttributeInstance attr = playerIn.getAttribute(ForgeMod.REACH_DISTANCE.get());
    attr.removeModifier(REACH_ID);
  }

  public static void addPlayerReach(PlayerEntity playerIn, int reachBoost) {
    removePlayerReach(playerIn);
    ModifiableAttributeInstance attr = playerIn.getAttribute(ForgeMod.REACH_DISTANCE.get());
    //vanilla is 5, so +11 it becomes 16
    AttributeModifier enchantment = new AttributeModifier(REACH_ID, "ReachEnchantmentCyclic", reachBoost, AttributeModifier.Operation.ADDITION);
    attr.applyPersistentModifier(enchantment);
  }
  //  public static void setPlayerReach(PlayerEntity player, int currentReach) {
  //    //thank you ForgeMod for adding this when mojang removed
  //    player.getAttribute(ForgeMod.REACH_DISTANCE.get()).setBaseValue(currentReach);
  //  }

  public static double getExpTotal(PlayerEntity player) {
    //  validateExpPositive(player);
    int level = player.experienceLevel;
    // numeric reference:
    // http://minecraft.gamepedia.com/Experience#Leveling_up
    double totalExp = getXpForLevel(level);
    double progress = Math.round(player.xpBarCap() * player.experience);
    totalExp += (int) progress;
    return totalExp;
  }

  public static int getXpForLevel(int level) {
    // numeric reference:
    // http://minecraft.gamepedia.com/Experience#Leveling_up
    int totalExp = 0;
    if (level <= 15) {
      totalExp = level * level + 6 * level;
    }
    else if (level <= 30) {
      totalExp = (int) (2.5 * level * level - 40.5 * level + 360);
    }
    else {
      // level >= 31
      totalExp = (int) (4.5 * level * level - 162.5 * level + 2220);
    }
    return totalExp;
  }

  public static ItemStack getPlayerItemIfHeld(PlayerEntity player) {
    ItemStack wand = player.getHeldItemMainhand();
    if (wand.isEmpty()) {
      wand = player.getHeldItemOffhand();
    }
    return wand;
  }

  public static int getFirstSlotWithBlock(PlayerEntity player, BlockState targetState) {
    int ret = -1;
    ItemStack stack;
    for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
      stack = player.inventory.getStackInSlot(i);
      if (!stack.isEmpty() &&
          stack.getItem() != null &&
          Block.getBlockFromItem(stack.getItem()) == targetState.getBlock()) {
        return i;
      }
    }
    return ret;
  }

  public static BlockState getBlockstateFromSlot(PlayerEntity player, int slot) {
    ItemStack stack = player.inventory.getStackInSlot(slot);
    if (!stack.isEmpty() &&
        stack.getItem() != null &&
        Block.getBlockFromItem(stack.getItem()) != null) {
      Block b = Block.getBlockFromItem(stack.getItem());
      return b.getDefaultState();
    }
    return null;
  }

  public static void decrStackSize(PlayerEntity player, int slot) {
    if (player.isCreative() == false && slot >= 0) {
      player.inventory.decrStackSize(slot, 1);
    }
  }

  public static Item getItemArmorSlot(PlayerEntity player, EquipmentSlotType slot) {
    ItemStack inslot = player.inventory.armorInventory.get(slot.getIndex());
    //    ItemStack inslot = player.inventory.armorInventory[slot.getIndex()];
    Item item = (inslot.isEmpty()) ? null : inslot.getItem();
    return item;
  }
}
