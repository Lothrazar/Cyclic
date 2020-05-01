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

import java.util.Arrays;
import java.util.List;
import com.lothrazar.cyclic.base.EnchantBase;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EnchantTraveller extends EnchantBase {

  public static final List<String> PROTS = Arrays.asList(new String[] {
      "sting", DamageSource.FLY_INTO_WALL.damageType,
      DamageSource.CACTUS.damageType,
      DamageSource.SWEET_BERRY_BUSH.damageType
  });

  public EnchantTraveller(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType... slots) {
    super(rarityIn, typeIn, slots);
    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public int getMaxLevel() {
    return 1;
  }

  @Override
  public boolean canApply(ItemStack stack) {
    boolean yes = stack.getItem() == Items.BOOK ||
        (stack.getItem() instanceof ArmorItem)
            && ((ArmorItem) stack.getItem()).getEquipmentSlot() == EquipmentSlotType.LEGS;
    return yes;
  }

  @Override
  public boolean canApplyAtEnchantingTable(ItemStack stack) {
    return this.canApply(stack);
  }

  @SubscribeEvent
  public void onEnderTeleportEvent(EnderTeleportEvent event) {
    int level = getCurrentArmorLevelSlot(event.getEntityLiving(), EquipmentSlotType.LEGS);
    if (level > 0)
      event.setAttackDamage(0.1F);
  }

  @SubscribeEvent
  public void onEntityUpdate(LivingDamageEvent event) {
    int level = getCurrentArmorLevelSlot(event.getEntityLiving(), EquipmentSlotType.LEGS);
    if (level > 0 && PROTS.contains(event.getSource().damageType)) {
      event.setAmount(0.1F);
    }
  }
}
