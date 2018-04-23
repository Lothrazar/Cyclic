package com.lothrazar.cyclicmagic.villager;

import java.util.Random;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

/**
 * replace ListItemForEmeralds, instead of extending it just like vanilla/forge version BUT we do not ignore stackSize
 * 
 * @author Sam
 *
 */
public class ListItemForEmeraldsFixed implements EntityVillager.ITradeList {

  /** The item that is being bought for emeralds */
  public ItemStack itemToBuy;
  /**
   * The price info for the amount of emeralds to sell for, or if negative, the amount of the item to buy for an emerald.
   */
  public EntityVillager.PriceInfo priceInfo;

  public ListItemForEmeraldsFixed(Item par1Item, EntityVillager.PriceInfo priceInfo) {
    // super(par1Item,priceInfo);
    this.itemToBuy = new ItemStack(par1Item);
    this.priceInfo = priceInfo;
  }

  /**
   * 
   * @param stack
   * @param priceInfo
   */
  public ListItemForEmeraldsFixed(ItemStack stack, EntityVillager.PriceInfo priceInfo) {
    //  super(stack,priceInfo);
    this.itemToBuy = stack;
    this.priceInfo = priceInfo;
  }

  /**
   * Affects the given MerchantRecipeList to possibly add or remove MerchantRecipes.
   */
  public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random) {
    int i = 1;
    if (this.priceInfo != null) {
      i = this.priceInfo.getPrice(random);
    }
    ItemStack itemstack;
    ItemStack itemstack1;
    if (i < 0) {
      itemstack = new ItemStack(Items.EMERALD);
      itemstack1 = new ItemStack(this.itemToBuy.getItem(), -i, this.itemToBuy.getMetadata());
    }
    else {
      itemstack = new ItemStack(Items.EMERALD, i, 0);
      itemstack1 = this.itemToBuy.copy();
      //copy does same thing but yeah. the stackSize is hardcodeed 1 in vanilla
      //THIS IS THE FIX!!!! vanilla is hardcoded to 1 but we just do stacksize
      ////new ItemStack(this.itemToBuy.getItem(), this.itemToBuy.stackSize, this.itemToBuy.getMetadata());
    }
    recipeList.add(new MerchantRecipe(itemstack, itemstack1));
  }

  @Override
  public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random) {}
}