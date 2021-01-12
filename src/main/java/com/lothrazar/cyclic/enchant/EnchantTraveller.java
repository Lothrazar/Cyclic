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
import com.lothrazar.cyclic.util.UtilParticle;
import java.util.Arrays;
import java.util.List;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
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

  public static BooleanValue CFG;
  public static final String id = "traveler";

  @Override
  public boolean isEnabled() {
    return CFG.get();
  }

  @Override
  public int getMaxLevel() {
    return 1;
  }

  @Override
  public boolean canApply(ItemStack stack) {
    boolean yes = (stack.getItem() instanceof ArmorItem)
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
    if (level > 0) {
      event.setAttackDamage(0F);
    }
  }

  @SubscribeEvent
  public void onEntityUpdate(LivingDamageEvent event) {
    int level = getCurrentArmorLevelSlot(event.getEntityLiving(), EquipmentSlotType.LEGS);
    if (level > 0 && PROTS.contains(event.getSource().damageType)) {
      event.setAmount(0.1F);
    }
    if (level > 0 && event.getSource() == DamageSource.FALL) {
      //normal is zero damage up to 3 distance. 1 damage (half heart) at 4 distance. and each distance up goes up by that
      // so 8 fall damage would be 5 damage
      if (event.getEntityLiving().fallDistance <= 8) {
        //flatten damage up to 8 instead of default 3
        event.setAmount(0.1F);
        //but then 9 and onward falls back to original formula 
      }
      else if (event.getEntityLiving().getItemStackFromSlot(EquipmentSlotType.CHEST).getItem() instanceof ElytraItem) { //== Items.ELYTRA
        //this leg enchant combos with any elytra, so never die from any fall damage, at worst it maxes out at leaving you alive at 1/2 heart
        if (event.getAmount() > event.getEntityLiving().getHealth() - 0.5F) {
          //either you crashed flying straight into the ground, or just fell while wearing elytra (you still die to void tho)
          event.setAmount(event.getEntityLiving().getHealth() - 1F);
          UtilParticle.spawnParticle(event.getEntity().world, ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, event.getEntity().getPosition(), 4);
        }
      }
    }
  }
}
