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
package com.lothrazar.cyclicmagic.item.dynamite;

import com.lothrazar.cyclicmagic.core.entity.EntityThrowableDispensable;
import com.lothrazar.cyclicmagic.core.item.BaseItemProjectile;
import com.lothrazar.cyclicmagic.core.util.UtilPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class ItemProjectileTNT extends BaseItemProjectile {

  public static enum ExplosionType {
    NORMAL, BLOCKSAFE, MINING;
  }

  private ExplosionType type;
  private int strength;

  public ItemProjectileTNT(int str, ExplosionType t) {
    super();
    this.strength = str;
    this.type = t;
  }

  @Override
  public EntityThrowableDispensable getThrownEntity(World world, ItemStack held, double x, double y, double z) {
    switch (type) {
      case NORMAL:
        return new EntityDynamite(world, this.strength, x, y, z);
      case MINING:
        return new EntityDynamiteMining(world, this.strength, x, y, z);
      case BLOCKSAFE:
        return new EntityDynamiteBlockSafe(world, this.strength, x, y, z);
    }
    return null;
  }

  @Override
  public void onItemThrow(ItemStack held, World world, EntityPlayer player, EnumHand hand) {
    EntityThrowableDispensable d = null;
    switch (type) {
      case NORMAL:
        d = new EntityDynamite(world, player, this.strength);
      break;
      case MINING:
        d = new EntityDynamiteMining(world, player, this.strength);
      break;
      case BLOCKSAFE:
        d = new EntityDynamiteBlockSafe(world, player, this.strength);
      break;
      default:
      break;
    }
    this.doThrow(world, player, hand, d);
    UtilPlayer.decrStackSize(player, hand);
  }

  @Override
  public SoundEvent getSound() {
    // TODO Auto-generated method stub
    return SoundEvents.ENTITY_EGG_THROW;
  }
}
