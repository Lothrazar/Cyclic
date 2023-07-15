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

import com.lothrazar.library.enchant.EnchantmentFlib;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;

public class MultiBowEnchant extends EnchantmentFlib {

  public static final String ID = "multishot";
  public static BooleanValue CFG;

  public MultiBowEnchant(Rarity rarityIn, EnchantmentCategory typeIn, EquipmentSlot... slots) {
    super(rarityIn, typeIn, slots);
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
  public boolean canApplyAtEnchantingTable(ItemStack stack) {
    return isEnabled() && super.canApplyAtEnchantingTable(stack);
  }

  @Override
  public boolean canEnchant(ItemStack stack) {
    return isEnabled() && stack.getItem() instanceof BowItem;
  }

  @Override
  public int getMaxLevel() {
    return 1;
  }

  public static void spawnArrow(Level worldIn, Player player, ItemStack stackBow, int charge, Vec3 offsetVector) {
    ArrowItem arrowitem = (ArrowItem) Items.ARROW;
    AbstractArrow abstractarrowentity = arrowitem.createArrow(worldIn, stackBow, player);
    abstractarrowentity.pickup = AbstractArrow.Pickup.DISALLOWED;
    abstractarrowentity.setPos(abstractarrowentity.getX() + offsetVector.x(), abstractarrowentity.getY(), abstractarrowentity.getZ() + offsetVector.z());
    float f = BowItem.getPowerForTime(charge);
    abstractarrowentity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f * 3.0F, 1.0F);
    if (f == 1.0F) {
      abstractarrowentity.setCritArrow(true);
    }
    int j = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.POWER_ARROWS, stackBow);
    if (j > 0) {
      abstractarrowentity.setBaseDamage(abstractarrowentity.getBaseDamage() + j * 0.5D + 0.5D);
    }
    int k = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.PUNCH_ARROWS, stackBow);
    if (k > 0) {
      abstractarrowentity.setKnockback(k);
    }
    if (EnchantmentHelper.getTagEnchantmentLevel(Enchantments.FLAMING_ARROWS, stackBow) > 0) {
      abstractarrowentity.setSecondsOnFire(100);
    }
    worldIn.addFreshEntity(abstractarrowentity);
  }
}
