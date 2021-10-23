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
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;

public class EnchantMultishot extends EnchantBase {

  public EnchantMultishot(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType... slots) {
    super(rarityIn, typeIn, slots);
  }

  public static BooleanValue CFG;
  public static final String ID = "multishot";

  @Override
  public boolean isEnabled() {
    return CFG.get();
  }

  @Override
  public boolean canApply(ItemStack stack) {
    return stack.getItem() instanceof BowItem;
  }

  @Override
  public int getMaxLevel() {
    return 1;
  }

  public static void spawnArrow(World worldIn, PlayerEntity player, ItemStack stackBow, int charge, Vector3d offsetVector) {
    ArrowItem arrowitem = (ArrowItem) Items.ARROW;
    AbstractArrowEntity abstractarrowentity = arrowitem.createArrow(worldIn, stackBow, player);
    abstractarrowentity.pickupStatus = AbstractArrowEntity.PickupStatus.DISALLOWED;
    abstractarrowentity.forceSetPosition(abstractarrowentity.getPosX() + offsetVector.getX(), abstractarrowentity.getPosY(), abstractarrowentity.getPosZ() + offsetVector.getZ());
    float f = BowItem.getArrowVelocity(charge);
    abstractarrowentity.func_234612_a_(player, player.rotationPitch, player.rotationYaw, 0.0F, f * 3.0F, 1.0F);
    if (f == 1.0F) {
      abstractarrowentity.setIsCritical(true);
    }
    int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stackBow);
    if (j > 0) {
      abstractarrowentity.setDamage(abstractarrowentity.getDamage() + j * 0.5D + 0.5D);
    }
    int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stackBow);
    if (k > 0) {
      abstractarrowentity.setKnockbackStrength(k);
    }
    if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stackBow) > 0) {
      abstractarrowentity.setFire(100);
    }
    worldIn.addEntity(abstractarrowentity);
  }
}
