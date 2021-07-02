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
import com.lothrazar.cyclic.util.UtilEntity;
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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EnchantMultishot extends EnchantBase {

  public EnchantMultishot(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType... slots) {
    super(rarityIn, typeIn, slots);
    MinecraftForge.EVENT_BUS.register(this);
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

  @SubscribeEvent
  public void onPlayerUpdate(ArrowLooseEvent event) {
    ItemStack stackBow = event.getBow();
    int level = this.getCurrentLevelTool(stackBow);
    if (level <= 0) {
      return;
    }
    PlayerEntity player = event.getPlayer();
    World worldIn = player.world;
    if (worldIn.isRemote == false) {
      float charge = BowItem.getArrowVelocity(stackBow.getUseDuration() - event.getCharge());
      //use cross product to push arrows out to left and right
      Vector3d playerDirection = UtilEntity.lookVector(player.rotationYaw, player.rotationPitch);
      Vector3d left = playerDirection.crossProduct(new Vector3d(0, 1, 0));
      Vector3d right = playerDirection.crossProduct(new Vector3d(0, -1, 0));
      spawnArrow(worldIn, player, stackBow, charge, left.normalize());
      spawnArrow(worldIn, player, stackBow, charge, right.normalize());
    }
  }

  /**
   * Gets the velocity of the arrow entity from the bow's charge
   */
  public static float getArrowVelocity(float charge) {
    float f = charge / 20.0F;
    f = (f * f + f * 2.0F) / 3.0F;
    if (f > 1.0F) {
      f = 1.0F;
    }
    return f;
  }

  public void spawnArrow(World worldIn, PlayerEntity player, ItemStack stackBow, float charge, Vector3d offsetVector) {
    //    ArrowItem item;
    //    BowItem yy;
    ArrowItem arrowitem = (ArrowItem) Items.ARROW;
    AbstractArrowEntity abstractarrowentity = arrowitem.createArrow(worldIn, stackBow, player);
    //    abstractarrowentity = customArrow(abstractarrowentity);
    float f = getArrowVelocity(charge);
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
    stackBow.damageItem(1, player, (p) -> {
      p.sendBreakAnimation(player.getActiveHand());
    });
    //    if (flag1 || playerentity.abilities.isCreativeMode && (itemstack.getItem() == Items.SPECTRAL_ARROW || itemstack.getItem() == Items.TIPPED_ARROW)) {
    //       abstractarrowentity.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
    //    }
    ModCyclic.LOGGER.info("new arrow" + abstractarrowentity);
    worldIn.addEntity(abstractarrowentity);
    //    ArrowItem itemarrow = (ArrowItem) (Items.ARROW);
    //    //    itemarrow.createArrow(worldIn, stack, shooter)
    //    AbstractArrowEntity entityarrow = itemarrow.createArrow(worldIn, stackBow, player);
    //    entityarrow.pickupStatus = ArrowEntity.PickupStatus.DISALLOWED;
    //    //off set bow to the side using the vec, and then aim
    //    entityarrow.prevPosX += offsetVector.x;
    //    entityarrow.prevPosY += offsetVector.y;
    //    entityarrow.prevPosZ += offsetVector.z;
    //    //    entityarrow.shoot(x, y, z, velocity, inaccuracy); 
    //    ModCyclic.LOGGER.info("charge arrow multi " + charge);
    //    //see CrossbowItem
    //    BowItem yy;
    //    entityarrow.shoot(offsetVector.x, offsetVector.y, offsetVector.z, charge, 0.1F);
    //    //from ItemBow vanilla class
    //    if (charge == 1.0F) {
    //      entityarrow.setIsCritical(true);
    //    }
    //    //extract enchants from bow
    //    int power = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stackBow);
    //    if (power > 0) {
    //      entityarrow.setDamage(entityarrow.getDamage() + power * 0.5D + 0.5D);
    //    }
    //    int punch = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stackBow);
    //    if (punch > 0) {
    //      entityarrow.setKnockbackStrength(punch);
    //    }
    //    if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stackBow) > 0) {
    //      entityarrow.setFire(100);
    //    }
    //    //and go
    //    worldIn.addEntity(entityarrow);
  }
}
