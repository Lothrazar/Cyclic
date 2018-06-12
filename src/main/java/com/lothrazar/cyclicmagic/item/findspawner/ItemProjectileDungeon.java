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
package com.lothrazar.cyclicmagic.item.findspawner;

import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.core.IHasRecipe;
import com.lothrazar.cyclicmagic.core.entity.EntityThrowableDispensable;
import com.lothrazar.cyclicmagic.core.item.BaseItemProjectile;
import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.core.util.UtilChat;
import com.lothrazar.cyclicmagic.core.util.UtilItemStack;
import com.lothrazar.cyclicmagic.core.util.UtilWorld;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

public class ItemProjectileDungeon extends BaseItemProjectile implements IHasRecipe, IHasConfig {

  private static final int COOLDOWN = 10;
  private static int DUNGEONRADIUS = 64;

  public ItemProjectileDungeon() {
    super();
    this.setMaxDamage(1000);
    this.setMaxStackSize(1);
  }

  @Override
  public EntityThrowableDispensable getThrownEntity(World world, ItemStack held, double x, double y, double z) {
    return new EntityDungeonEye(world, x, y, z);
  }

  @Override
  public void syncConfig(Configuration config) {
    DUNGEONRADIUS = config.getInt("Ender Dungeon Radius", Const.ConfigCategory.items, 64, 8, 128, "Search radius of dungeonfinder");
  }

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedOreRecipe(new ItemStack(this),
        " sp",
        " ws",
        "w  ",
        'w', "cropNetherWart",
        's', new ItemStack(Blocks.MOSSY_COBBLESTONE),
        'p', "enderpearl");
  }

  @Override
  public void onItemThrow(ItemStack held, World world, EntityPlayer player, EnumHand hand) {
    player.getCooldownTracker().setCooldown(held.getItem(), COOLDOWN);
    UtilItemStack.damageItem(player, held);
    EntityDungeonEye entityendereye = new EntityDungeonEye(world, player);
    doThrow(world, player, hand, entityendereye, 0.5F);
    //    ModCyclic.logger.log("entityendereye spawned wit hid " + entityendereye.getEntityId());
    Runnable r = new Runnable() {

      @Override
      public void run() {
        if (entityendereye == null || entityendereye.isDead) {
          return;//something happened!
        }
        BlockPos blockpos = UtilWorld.findClosestBlock(player, Blocks.MOB_SPAWNER, DUNGEONRADIUS);
        if (blockpos == null) {
          entityendereye.kill();
          //          world.removeEntity(entityendereye);
          //UtilSound.playSound(player, player.getPosition(), SoundEvents.BLOCK_FIRE_EXTINGUISH);
          if (world.isRemote) {
            UtilChat.sendStatusMessage(player, UtilChat.lang("item.ender_dungeon.notfound") + " " + DUNGEONRADIUS);
          }
        }
        else {
          entityendereye.moveTowards(blockpos);
        }
      }
    };
    Thread t = new Thread(r);
    t.start(); // starts thread in background.
  }

  @Override
  public SoundEvent getSound() {
    return SoundRegistry.dungeonfinder;
  }
}
