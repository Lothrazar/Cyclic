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
import com.lothrazar.cyclic.registry.MenuTypeRegistry;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.cyclic.util.ChatUtil;
import com.lothrazar.cyclic.util.ItemStackUtil;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;

public class ItemLunchbox extends ItemBaseCyclic {

  private static final String HOLDING = "holding";
  public static final int SLOTS = 7;

  public ItemLunchbox(Properties prop) {
    super(prop.stacksTo(1));
  }

  @Override
  public Rarity getRarity(ItemStack stack) {
    return Rarity.UNCOMMON;
  }

  @Override
  public int getUseDuration(ItemStack st) {
    return 34;
  }

  @Override
  public UseAnim getUseAnimation(ItemStack st) {
    return UseAnim.EAT;
  }

  @Override
  public boolean isBarVisible(ItemStack stack) {
    return stack.hasTag() || super.isBarVisible(stack);
  }

  //show emptiness in fake durability bar
  @Override
  public int getBarColor(ItemStack stack) {
    return TextureRegistry.COLOUR_FOOD_BAR;
  }

  @Override
  public int getBarWidth(ItemStack stack) {
    if (!stack.hasTag()) {
      return 0;
    }
    float max = stack.getTag().getInt("count_max");
    float current = max - stack.getTag().getInt("count_empty");
    return (max == 0) ? 0 : Math.round(13.0F * current / max);
    //    }
    //    return super.getBarWidth(stack);
  }

  // ShareTag for server->client capability data sync
  @Override
  public CompoundTag getShareTag(ItemStack stack) {
    CompoundTag nbt = stack.getOrCreateTag();
    IItemHandler handler = stack.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
    //on server  this runs . also has correct values.
    //set data for sync to client
    if (handler != null) {
      int empty = ItemStackUtil.countEmptySlots(handler);
      nbt.putInt("count_empty", empty);
      nbt.putInt("count_max", handler.getSlots());
    }
    return nbt;
  }

  //clientside read tt
  @Override
  public void readShareTag(ItemStack stack, CompoundTag nbt) {
    if (nbt != null) {
      CompoundTag stackTag = stack.getOrCreateTag();
      stackTag.putInt("count_empty", nbt.getInt("count_empty"));
      stackTag.putInt("count_max", nbt.getInt("count_max"));
    }
    super.readShareTag(stack, nbt);
  }

  @Override
  public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
    if (!worldIn.isClientSide && entityLiving instanceof Player player) {
      IItemHandler handler = stack.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
      if (handler != null) {
        ItemStack found = ItemStack.EMPTY;
        //just go left to right and eat in order
        for (int i = 0; i < handler.getSlots(); i++) {
          ItemStack test = handler.getStackInSlot(i);
          if (test.isEdible() && !player.getCooldowns().isOnCooldown(test.getItem())) {
            found = test;
            break;
          }
        }
        if (!found.isEmpty()) {
          ChatUtil.addServerChatMessage(player, found.getDisplayName());
          //          entityLiving.eat(worldIn, found); 
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
    //    ItemStack itemstack = player.getItemInHand(handIn);
    if (player.isCrouching()) {
      if (!worldIn.isClientSide) {
        NetworkHooks.openScreen((ServerPlayer) player, new ContainerProviderLunchbox(), player.blockPosition());
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
    MenuScreens.register(MenuTypeRegistry.LUNCHBOX.get(), ScreenLunchbox::new);
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
    return new CapabilityLunchbox(stack, nbt);
  }

  public static void setHoldingEdible(ItemStack box, boolean edible) {
    box.getOrCreateTag().putBoolean(HOLDING, edible);
  }

  public static int getColour(ItemStack stack) {
    if (stack.hasTag() && stack.getTag().getBoolean(HOLDING)) {
      // green? return 0x00AAAAFF;
      return 0x000000FF; //  0xFFFF0011;
    }
    return 0xFFFFFFFF;
  }
}
