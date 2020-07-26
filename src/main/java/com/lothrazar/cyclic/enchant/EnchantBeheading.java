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

import java.util.HashMap;
import java.util.Map;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.EnchantBase;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilNBT;
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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EnchantBeheading extends EnchantBase {

  public EnchantBeheading(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType... slots) {
    super(rarityIn, typeIn, slots);
    buildDefaultHeadList();
    MinecraftForge.EVENT_BUS.register(this);
  }

  private Map<String, String> mapResourceToSkin = new HashMap<String, String>();
  private static final int percentDrop = 20;
  private static final int percentPerLevel = 25;

  private void buildDefaultHeadList() {
    mapResourceToSkin = new HashMap<String, String>();
    //http://minecraft.gamepedia.com/Player.dat_format#Player_Heads
    //mhf https://twitter.com/Marc_IRL/status/542330244473311232  https://pastebin.com/5mug6EBu
    //other https://www.planetminecraft.com/blog/minecraft-playerheads-2579899/
    //NBT image data from  http://www.minecraft-heads.com/custom/heads/animals/6746-llama
    //TODO config file for extra mod support
    mapResourceToSkin.put("minecraft:blaze", "MHF_Blaze");
    mapResourceToSkin.put("minecraft:cat", "MHF_Ocelot");
    mapResourceToSkin.put("minecraft:cave_spider", "MHF_CaveSpider");
    mapResourceToSkin.put("minecraft:chicken", "MHF_Chicken");
    mapResourceToSkin.put("minecraft:cow", "MHF_Cow");
    mapResourceToSkin.put("minecraft:enderman", "MHF_Enderman");
    mapResourceToSkin.put("minecraft:ghast", "MHF_Ghast");
    mapResourceToSkin.put("minecraft:iron_golem", "MHF_Golem");
    mapResourceToSkin.put("minecraft:magma_cube", "MHF_LavaSlime");
    mapResourceToSkin.put("minecraft:mooshroom", "MHF_MushroomCow");
    mapResourceToSkin.put("minecraft:ocelot", "MHF_Ocelot");
    mapResourceToSkin.put("minecraft:pig", "MHF_Pig");
    mapResourceToSkin.put("minecraft:zombie_pigman", "MHF_PigZombie");
    mapResourceToSkin.put("minecraft:sheep", "MHF_Sheep");
    mapResourceToSkin.put("minecraft:slime", "MHF_Slime");
    mapResourceToSkin.put("minecraft:spider", "MHF_Spider");
    mapResourceToSkin.put("minecraft:squid", "MHF_Squid");
    mapResourceToSkin.put("minecraft:villager", "MHF_Villager");
    mapResourceToSkin.put("minecraft:witch", "MHF_Witch");
    mapResourceToSkin.put("minecraft:wolf", "MHF_Wolf");
    mapResourceToSkin.put("minecraft:guardian", "MHF_Guardian");
    mapResourceToSkin.put("minecraft:elder_guardian", "MHF_Guardian");
    mapResourceToSkin.put("minecraft:snow_golem", "MHF_SnowGolem");
    mapResourceToSkin.put("minecraft:silverfish", "MHF_Silverfish");
    mapResourceToSkin.put("minecraft:endermite", "MHF_Endermite");
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
      //      ModCyclic.LOGGER.info(level + "---" + percentForLevel(level));
      if (MathHelper.nextInt(world.rand, 0, 100) > percentForLevel(level)) {
        return;
      }
      LivingEntity target = (LivingEntity) event.getEntity();
      if (target == null) {
        return;
      } //probably wont happen just extra safe
      BlockPos pos = target.getPosition();
      if (target instanceof PlayerEntity) {
        UtilItemStack.drop(world, pos, UtilNBT.buildNamedPlayerSkull((PlayerEntity) target));
        return;
      }
      //else the random number was less than 10, so it passed the 10% chance req
      String key = target.getType().getRegistryName().toString();
      ////we allow all these, which include config, to override the vanilla skulls below
      //first do my wacky class mapping// TODO delete and go to minecraft:blah
      if (mapResourceToSkin.containsKey(key)) {
        UtilItemStack.drop(world, pos, UtilNBT.buildNamedPlayerSkull(mapResourceToSkin.get(key)));
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
      else {
        ModCyclic.LOGGER.error("Beheading : mob not found in EntityList " + target.getName());
      }
    }
  }
}
