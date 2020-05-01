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

import com.lothrazar.cyclic.ModCyclic;
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
    //anything that goes on your feet
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
    if (level == 0) {
      return;
    }
    event.setAttackDamage(0);
    ModCyclic.log("ender teleport safe");
  }

  @SubscribeEvent
  public void onEntityUpdate(LivingDamageEvent event) {
    int level = getCurrentArmorLevelSlot(event.getEntityLiving(), EquipmentSlotType.LEGS);
    if (level == 0) {
      return;
    }
    if (event.getSource() == DamageSource.FLY_INTO_WALL) {
      ModCyclic.log("cancel fly into wall");
      event.setCanceled(true);
      return;
    }
    ModCyclic.log(" wat  " + event.getSource().damageType);
    ModCyclic.log(" isElytraFlying  " + event.getEntityLiving().isElytraFlying());
    if (event.getEntityLiving().isElytraFlying()) {
      ModCyclic.log("cancel fly into wall elytra flyuing " + event.getSource().damageType);
      event.setCanceled(true);
    }
  }
}
