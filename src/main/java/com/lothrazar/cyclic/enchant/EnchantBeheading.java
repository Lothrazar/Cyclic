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

import com.lothrazar.cyclic.ConfigRegistry;
import com.lothrazar.cyclic.base.EnchantBase;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilNBT;
import java.util.Map;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EnchantBeheading extends EnchantBase {

  public EnchantBeheading(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType... slots) {
    super(rarityIn, typeIn, slots);
    MinecraftForge.EVENT_BUS.register(this);
  }

  private static final int percentDrop = 20;
  private static final int percentPerLevel = 25;
  public static final String id = "beheading";
  public static BooleanValue CFG;

  @Override
  public boolean isEnabled() {
    return CFG == null || CFG.get();
  }

  @Override
  public int getMaxLevel() {
    return 3;
  }

  private int percentForLevel(int level) {
    return percentDrop + (level - 1) * percentPerLevel;
  }

  @SubscribeEvent
  public void onEntityKill(LivingDeathEvent event) {
    if (event.getSource().getTrueSource() instanceof PlayerEntity) {
      PlayerEntity attacker = (PlayerEntity) event.getSource().getTrueSource();
      int level = getCurrentLevelTool(attacker);
      if (level <= 0) {
        return;
      }
      World world = attacker.world;
      if (MathHelper.nextInt(world.rand, 0, 100) > percentForLevel(level)) {
        return;
      }
      LivingEntity target = (LivingEntity) event.getEntity();
      if (target == null) {
        return;
      } //probably wont happen just extra safe
      BlockPos pos = target.getPosition();
      if (target instanceof PlayerEntity) {
        //player head
        UtilItemStack.drop(world, pos, UtilNBT.buildNamedPlayerSkull((PlayerEntity) target));
        return;
      }
      //else the random number was less than 10, so it passed the 10% chance req
      String key = target.getType().getRegistryName().toString();
      ////we allow all these, which include config, to override the vanilla skulls below
      //first do my wacky class mapping// TODO delete and go to minecraft:blah
      Map<String, String> mappedBeheading = ConfigRegistry.getMappedBeheading();
      if (mappedBeheading.containsKey(key)) {
        UtilItemStack.drop(world, pos, UtilNBT.buildNamedPlayerSkull(mappedBeheading.get(key)));
      }
      else if (target.getType() == EntityType.ENDER_DRAGON) {
        UtilItemStack.drop(world, pos, new ItemStack(Items.DRAGON_HEAD));
      }
      else if (target.getType() == EntityType.CREEPER) {
        UtilItemStack.drop(world, pos, new ItemStack(Items.CREEPER_HEAD));
      }
      else if (target.getType() == EntityType.ZOMBIE) {
        UtilItemStack.drop(world, pos, new ItemStack(Items.ZOMBIE_HEAD));
      }
      else if (target.getType() == EntityType.SKELETON) {
        UtilItemStack.drop(world, pos, new ItemStack(Items.SKELETON_SKULL));
      }
      else if (target.getType() == EntityType.WITHER_SKELETON) {
        UtilItemStack.drop(world, pos, new ItemStack(Items.WITHER_SKELETON_SKULL));
      }
      else if (target.getType() == EntityType.WITHER) {
        //Drop number of heads equal to level of enchant [1,3] 
        UtilItemStack.drop(world, pos, new ItemStack(Items.WITHER_SKELETON_SKULL, level));
      }
      //      else {
      //        ModCyclic.LOGGER.error("Beheading : mob not found in EntityList, update config file " + target.getName());
      //      }
    }
  }
}
