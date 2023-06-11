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

import java.util.Map;
import org.jetbrains.annotations.Nullable;
import com.lothrazar.cyclic.compat.CompatConstants;
import com.lothrazar.cyclic.config.ConfigRegistry;
import com.lothrazar.cyclic.util.ItemStackUtil;
import com.lothrazar.cyclic.util.TagDataUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

public class BeheadingEnchant extends EnchantmentCyclic {

  public static IntValue PERCDROP;
  public static IntValue PERCPERLEVEL;
  public static final String ID = "beheading";
  public static BooleanValue CFG;

  public BeheadingEnchant(Rarity rarityIn, EnchantmentCategory typeIn, EquipmentSlot... slots) {
    super(rarityIn, typeIn, slots);
    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public boolean isEnabled() {
    return CFG.get();
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
  public boolean canEnchant(ItemStack stack) {
    return isEnabled() && super.canEnchant(stack);
  }

  @Override
  public boolean canApplyAtEnchantingTable(ItemStack stack) {
    return isEnabled() && super.canApplyAtEnchantingTable(stack);
  }

  @Override
  public int getMaxLevel() {
    return 3;
  }

  private int percentForLevel(int level) {
    return PERCDROP.get() + (level - 1) * PERCPERLEVEL.get();
  }

  @SubscribeEvent
  public void onEntityKill(LivingDeathEvent event) {
    if (!isEnabled()) {
      return;
    }
    if (event.getSource().getEntity() instanceof Player) {
      Player attacker = (Player) event.getSource().getEntity();
      int level = getCurrentLevelTool(attacker);
      if (level <= 0) {
        return;
      }
      Level world = attacker.level();
      if (Mth.nextInt(world.random, 0, 100) > percentForLevel(level)) {
        return;
      }
      LivingEntity target = event.getEntity();
      if (target == null) {
        return;
      } //probably wont happen just extra safe
      BlockPos pos = target.blockPosition();
      if (target instanceof Player) {
        //player head
        ItemStackUtil.drop(world, pos, TagDataUtil.buildNamedPlayerSkull((Player) target));
        return;
      }
      //else the random number was less than 10, so it passed the 10% chance req
      @Nullable
      ResourceLocation type = ForgeRegistries.ENTITY_TYPES.getKey(target.getType());
      String key = type == null ? "" : type.toString();
      ////we allow all these, which include config, to override the vanilla skulls below 
      Map<String, String> mappedBeheading = ConfigRegistry.getMappedBeheading();
      if (target.getType() == EntityType.ENDER_DRAGON) {
        ItemStackUtil.drop(world, pos, new ItemStack(Items.DRAGON_HEAD));
      }
      else if (target.getType() == EntityType.CREEPER) {
        ItemStackUtil.drop(world, pos, new ItemStack(Items.CREEPER_HEAD));
      }
      else if (target.getType() == EntityType.ZOMBIE) {
        ItemStackUtil.drop(world, pos, new ItemStack(Items.ZOMBIE_HEAD));
      }
      else if (target.getType() == EntityType.SKELETON) {
        ItemStackUtil.drop(world, pos, new ItemStack(Items.SKELETON_SKULL));
      }
      else if (target.getType() == EntityType.WITHER_SKELETON) {
        ItemStackUtil.drop(world, pos, new ItemStack(Items.WITHER_SKELETON_SKULL));
      }
      else if (target.getType() == EntityType.WITHER) { //Drop number of heads equal to level of enchant [1,3] 
        ItemStackUtil.drop(world, pos, new ItemStack(Items.WITHER_SKELETON_SKULL, Math.max(level, 3)));
      }
      else if (ModList.get().isLoaded(CompatConstants.TCONSTRUCT)) {
        //tconstruct: drowned_head husk_head enderman_head cave_spider_head stray_head
        String id = CompatConstants.TCONSTRUCT;
        ItemStack tFound = ItemStack.EMPTY;
        if (target.getType() == EntityType.DROWNED) {
          tFound = ItemStackUtil.findItem(id + ":drowned_head");
        }
        else if (target.getType() == EntityType.HUSK) {
          tFound = ItemStackUtil.findItem(id + ":husk_head");
        }
        else if (target.getType() == EntityType.ENDERMAN) {
          tFound = ItemStackUtil.findItem(id + ":enderman_head");
        }
        else if (target.getType() == EntityType.SPIDER) {
          tFound = ItemStackUtil.findItem(id + ":spider_head");
        }
        else if (target.getType() == EntityType.CAVE_SPIDER) {
          tFound = ItemStackUtil.findItem(id + ":cave_spider_head");
        }
        else if (target.getType() == EntityType.STRAY) {
          tFound = ItemStackUtil.findItem(id + ":stray_head");
        }
        else if (target.getType() == EntityType.BLAZE) {
          tFound = ItemStackUtil.findItem(id + ":blaze_head");
        }
        if (!tFound.isEmpty()) {
          ItemStackUtil.drop(world, pos, tFound);
          return;
        }
      }
      else if (mappedBeheading.containsKey(key)) {
        //otherwise not a real mob, try the config last
        ItemStackUtil.drop(world, pos, TagDataUtil.buildNamedPlayerSkull(mappedBeheading.get(key)));
      }
    }
  }
}
