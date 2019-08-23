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
package com.lothrazar.cyclic.enchant;

import com.lothrazar.cyclic.base.EnchantBase;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@Deprecated
public class EnchantQuickdraw extends EnchantBase {

  protected EnchantQuickdraw(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots) {
    super(rarityIn, typeIn, slots);
  }

  @Override
  public boolean canApply(ItemStack stack) {
    return stack.getItem() instanceof BowItem
        || stack.getItem() == Items.BOOK;
  }

  @Override
  public int getMaxLevel() {
    return 1;
  }

  @SubscribeEvent
  public void onPlayerUpdate(LivingUpdateEvent event) {
    if (event.getEntity() instanceof PlayerEntity) {
      PlayerEntity player = (PlayerEntity) event.getEntity();
      ItemStack heldItem = player.getHeldItem(Hand.MAIN_HAND);
      if (heldItem.getItem() instanceof BowItem == false) {
        heldItem = player.getHeldItem(Hand.OFF_HAND);
      }
      if (heldItem.getItem() instanceof BowItem == false) {
        return;
      }
      if (getCurrentLevelTool(player) <= 0) {
        return;
      }
      if (player.isHandActive()) {
        this.tickHeldBow(player);
        this.tickHeldBow(player);
      }
    }
  }

  private void tickHeldBow(PlayerEntity player) {
    //     player.updateActiveHand();//BUT its protected bahhhh
    //    player.updateActiveHand();
    //    UtilReflection.callPrivateMethod(EntityLivingBase.class, player, "updateActiveHand", "func_184608_ct");
  }
}
