package com.lothrazar.cyclicmagic.guide;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public enum GuideCategory {
  BLOCK, ITEM, WORLD, GEAR, ENCHANT, BLOCKMACHINE, BLOCKPLATE, ITEMBAUBLES, ITEMTHROW, TRANSPORT;

  public String text() {
    return "guide.category." + name().toLowerCase();
  }

  public ItemStack icon() {
    switch (this) {
      case BLOCK:
        return new ItemStack(Blocks.ENDER_CHEST);
      case BLOCKMACHINE:
        return new ItemStack(Blocks.FURNACE);
      case ENCHANT:
        return new ItemStack(Items.ENCHANTED_BOOK);
      case GEAR:
        return new ItemStack(Items.DIAMOND_SWORD);
      case ITEM:
        return new ItemStack(Items.STICK);
      //        case POTION:
      //          return new ItemStack(Items.POTIONITEM);
      case WORLD:
        return new ItemStack(Blocks.GOLD_ORE);
      case BLOCKPLATE:
        return new ItemStack(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE);
      case ITEMBAUBLES:
        return new ItemStack(Items.TOTEM_OF_UNDYING);
      case ITEMTHROW:
        return new ItemStack(Items.ELYTRA);
      case TRANSPORT:
        return new ItemStack(Items.FURNACE_MINECART);
      default:
        return new ItemStack(Blocks.DIRT);//wont happen unless new cat undefined
    }
  }
}