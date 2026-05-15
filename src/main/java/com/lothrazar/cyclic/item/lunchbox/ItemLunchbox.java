/*******************************************************************************
 * The MIT License (MIT)
 *
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclic.item.lunchbox;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.library.util.ChatUtil;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.capabilities.Capabilities;

public class ItemLunchbox extends ItemBaseCyclic {

  private static final String HOLDING = "holding";
  public static final int SLOTS = 7;

  public ItemLunchbox(Properties prop) {
    super(prop.stacksTo(1));
  }

  

  @Override
  public int getUseDuration(ItemStack st, LivingEntity entity) {
    return 34;
  }

  @Override
  public UseAnim getUseAnimation(ItemStack st) {
    return UseAnim.EAT;
  }

  // Show durability if our lunchbox has tagData, meaning it has or had food
  @Override
  public boolean isBarVisible(ItemStack stack) {
    return stack.has(DataComponents.CUSTOM_DATA) || super.isBarVisible(stack);
  }

  //show emptiness in fake durability bar
  @Override
  public int getBarColor(ItemStack stack) {
    return TextureRegistry.COLOUR_FOOD_BAR;
  }

  @Override
  public int getBarWidth(ItemStack stack) {
    if (!stack.has(DataComponents.CUSTOM_DATA)) {
      return 0;
    }
    float max = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getInt("count_max");
    float current = max - stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getInt("count_empty");
    return (max == 0) ? 0 : Math.round(13.0F * current / max);
    //    }
    //    return super.getBarWidth(stack);
  }

  

  

  @Override
  public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
    if (!worldIn.isClientSide && entityLiving instanceof Player player) { // && !player.isCrouching()
      IItemHandler handler = stack.getCapability(Capabilities.ItemHandler.ITEM);
      if (handler != null) {
        ItemStack found = ItemStack.EMPTY;
        //just go left to right and eat in order
        for (int i = 0; i < handler.getSlots(); i++) {
          ItemStack test = handler.getStackInSlot(i);
          if (test.has(DataComponents.FOOD) && !player.getCooldowns().isOnCooldown(test.getItem())) {
            found = test;
            break;
          }
        }
        if (!found.isEmpty()) {
          // found is edible and is not on cooldown
          ChatUtil.addServerChatMessage(player, found.getDisplayName());
          //          found.getItem().finishUsingItem(found, worldIn, entityLiving); 
          //moved from. eat() to forwarding the .finishUsingItem call
          //allow mods to override finishUsingItem on their own
          //for exmaple artifiacts everlasting beef calls .eat with found.copy() essentially
          found.getItem().finishUsingItem(found, worldIn, entityLiving);
        }
      }
    }
    return super.finishUsingItem(stack, worldIn, entityLiving);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level worldIn, Player player, InteractionHand handIn) {
    if (player.isCrouching()) {
      if (!worldIn.isClientSide) {
        ((ServerPlayer) player).openMenu(new ContainerProviderLunchbox(), player.blockPosition());
      }
      return super.use(worldIn, player, handIn);
    }
    else if (player.canEat(false)) {
      //not crouching so try to eat it
      //if we arent full 
      player.startUsingItem(handIn);
    }
    return super.use(worldIn, player, handIn);
  }

  @Override
  public void registerClient() {
    // // // MenuScreens.register
  }



  public static void setHoldingEdible(ItemStack box, boolean edible) {
    CustomData.EMPTY.copyTag().putBoolean(HOLDING, edible);
  }

  public static int getColour(ItemStack stack) {
    if (stack.has(DataComponents.CUSTOM_DATA) && stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBoolean(HOLDING)) {
      // green? return 0x00AAAAFF;
      return 0x000000FF; //  0xFFFF0011;
    }
    return 0xFFFFFFFF;
  }

  /**
   * for use by Item slot GUI Screen interactions. Mouse is holding an item to be inserted into a closed luncbbox from a different GUI
   *
   * @param lunchbox
   *          assumes this is a valid lunchbox with item as instance of this
   * @param itemFoodMouse
   *          a valid food item nonempty and not a lunchbox
   * @param player
   *          instance that is doing the insert
   */
  public static void insertFoodIntoLunchbox(ItemStack lunchbox, ItemStack itemFoodMouse, ServerPlayer player) {
    IItemHandler boxCap = lunchbox.getCapability(Capabilities.ItemHandler.ITEM);
    if (boxCap == null) {
      return;
    }
    //try to put/stack into each slot
    int i = 0;
    while (i < boxCap.getSlots()) {
      itemFoodMouse = boxCap.insertItem(i, itemFoodMouse, false);
      i++;
    }
    player.containerMenu.setCarried(itemFoodMouse);
  }
}
