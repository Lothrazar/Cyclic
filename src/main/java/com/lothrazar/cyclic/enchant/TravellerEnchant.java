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

import com.lothrazar.library.util.ParticleUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TravellerEnchant extends EnchantmentCyclic {

  public static final String ID = "traveler";
  public static BooleanValue CFG;

  public TravellerEnchant(Rarity rarityIn, EnchantmentCategory typeIn, EquipmentSlot... slots) {
    super(rarityIn, typeIn, slots);
    MinecraftForge.EVENT_BUS.register(this);
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
    boolean yes = isEnabled()
        && (stack.getItem() instanceof ArmorItem)
        && ((ArmorItem) stack.getItem()).getType() == ArmorItem.Type.LEGGINGS;
    return yes;
  }

  @Override
  public boolean isEnabled() {
    return CFG.get();
  }

  @Override
  public int getMaxLevel() {
    return 1;
  }

  @SubscribeEvent
  public void onEnderTeleportEvent(EntityTeleportEvent.EnderPearl event) {
    if (!isEnabled()) {
      return;
    }
    if (event.getEntity() instanceof LivingEntity) {
      int level = getCurrentArmorLevelSlot((LivingEntity) event.getEntity(), EquipmentSlot.LEGS);
      if (level > 0) {
        event.setAttackDamage(0F);
      }
    }
  }

  //  public static final List<String> PROTS = Arrays.asList(new String[] {
  //      "sting", DamageSource.FLY_INTO_WALL.msgId,
  //      DamageSource.CACTUS.msgId,
  //      DamageSource.SWEET_BERRY_BUSH.msgId
  //  });
  @SubscribeEvent
  public void onEntityUpdate(LivingDamageEvent event) {
    if (!isEnabled()) {
      return;
    }
    int level = getCurrentArmorLevelSlot(event.getEntity(), EquipmentSlot.LEGS);
    DamageSource source = event.getSource(); // .type();
    DamageSources bullshit = event.getEntity().level().damageSources();
    if (level > 0 && (source == bullshit.cactus()
        || source == bullshit.flyIntoWall()
        || source == bullshit.sweetBerryBush()
        || source == bullshit.sting(null))) {
      event.setAmount(0.1F);
    }
    if (level > 0 && source == bullshit.fall()) {
      //normal is zero damage up to 3 distance. 1 damage (half heart) at 4 distance. and each distance up goes up by that
      // so 8 fall damage would be 5 damage
      if (event.getEntity().fallDistance <= 8) {
        //flatten damage up to 8 instead of default 3
        event.setAmount(0.1F);
        //but then 9 and onward falls back to original formula 
      }
      else if (event.getEntity().getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof ElytraItem) { //== Items.ELYTRA
        //this leg enchant combos with any elytra, so never die from any fall damage, at worst it maxes out at leaving you alive at 1/2 heart
        if (event.getAmount() > event.getEntity().getHealth() - 0.5F) {
          //either you crashed flying straight into the ground, or just fell while wearing elytra (you still die to void tho)
          event.setAmount(event.getEntity().getHealth() - 1F);
          ParticleUtil.spawnParticle(event.getEntity().level(), ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, event.getEntity().blockPosition(), 4);
        }
      }
    }
  }
}
