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
import com.lothrazar.cyclic.util.UtilEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EnchantMultishot extends EnchantBase {

  public EnchantMultishot(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType... slots) {
    super(rarityIn, typeIn, slots);
    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public boolean canApply(ItemStack stack) {
    return stack.getItem() instanceof BowItem || stack.getItem() == Items.BOOK;
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
      Vec3d playerDirection = UtilEntity.lookVector(player.rotationYaw, player.rotationPitch);
      Vec3d left = playerDirection.crossProduct(new Vec3d(0, 1, 0));
      Vec3d right = playerDirection.crossProduct(new Vec3d(0, -1, 0));
      spawnArrow(worldIn, player, stackBow, charge, left.normalize());
      spawnArrow(worldIn, player, stackBow, charge, right.normalize());
    }
  }

  public void spawnArrow(World worldIn, PlayerEntity player, ItemStack stackBow, float charge, Vec3d offsetVector) {
    //TODO: custom ammo one day? The event does not send ammo only the bow
    //    ArrowItem item;
    ArrowItem itemarrow = (ArrowItem) (Items.ARROW);
    //    itemarrow.createArrow(worldIn, stack, shooter)
    AbstractArrowEntity entityarrow = itemarrow.createArrow(worldIn, stackBow, player);
    entityarrow.pickupStatus = ArrowEntity.PickupStatus.DISALLOWED;
    //off set bow to the side using the vec, and then aim
    entityarrow.prevPosX += offsetVector.x;
    entityarrow.prevPosY += offsetVector.y;
    entityarrow.prevPosZ += offsetVector.z;
    entityarrow.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, charge, 1.0F);
    //from ItemBow vanilla class
    if (charge == 1.0F) {
      entityarrow.setIsCritical(true);
    }
    //extract enchants from bow
    int power = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stackBow);
    if (power > 0) {
      entityarrow.setDamage(entityarrow.getDamage() + power * 0.5D + 0.5D);
    }
    int punch = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stackBow);
    if (punch > 0) {
      entityarrow.setKnockbackStrength(punch);
    }
    if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stackBow) > 0) {
      entityarrow.setFire(100);
    }
    //and go
    worldIn.addEntity(entityarrow);
  }
}
