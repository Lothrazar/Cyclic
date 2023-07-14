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

import com.lothrazar.cyclic.compat.botania.BotaniaWrapper;
import com.lothrazar.library.enchant.EnchantmentFlib;
import com.lothrazar.library.util.EntityUtil;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MagnetEnchant extends EnchantmentFlib {

  private static final int ITEM_HRADIUS = 4;
  private static final int HRADIUS_PER_LEVEL = 4;
  private static final int ITEM_VRADIUS = 4;
  public static final String ID = "magnet";
  public static BooleanValue CFG;

  public MagnetEnchant(Rarity rarityIn, EnchantmentCategory typeIn, EquipmentSlot... slots) {
    super(rarityIn, typeIn, slots);
    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public boolean isEnabled() {
    return CFG.get();
  }

  @Override
  public boolean isTradeable() {
    return isEnabled() && super.isTradeable();
  }

  @Override
  public boolean isDiscoverable() {
    return isEnabled() && super.isDiscoverable();
  }

  @Override
  public boolean isAllowedOnBooks() {
    return isEnabled() && super.isAllowedOnBooks();
  }

  @Override
  public boolean canEnchant(ItemStack stack) {
    return isEnabled() && super.canEnchant(stack);
  }

  @Override
  public boolean canApplyAtEnchantingTable(ItemStack stack) {
    return isEnabled() && super.canApplyAtEnchantingTable(stack);
  }

  @Override
  public int getMaxLevel() {
    return 3;
  }

  @SubscribeEvent
  public void onEntityUpdate(LivingTickEvent event) {
    if (!isEnabled()) {
      return;
    }
    LivingEntity entity = event.getEntity();
    if (entity instanceof Player) {
      Player p = (Player) entity;
      if (p.isSpectator()) {
        return;
      }
    }
    //Ticking
    int level = getLevelAll(entity);
    if (level > 0 && !BotaniaWrapper.hasSolegnoliaAround(entity)) {
      EntityUtil.moveEntityItemsInRegion(entity.getCommandSenderWorld(), entity.blockPosition(), ITEM_HRADIUS + HRADIUS_PER_LEVEL * level, ITEM_VRADIUS);
    }
  }
}
