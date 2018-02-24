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
package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.item.gear.ItemEmeraldArmor;
import com.lothrazar.cyclicmagic.item.gear.ItemEmeraldAxe;
import com.lothrazar.cyclicmagic.item.gear.ItemEmeraldHoe;
import com.lothrazar.cyclicmagic.item.gear.ItemEmeraldPickaxe;
import com.lothrazar.cyclicmagic.item.gear.ItemEmeraldSpade;
import com.lothrazar.cyclicmagic.item.gear.ItemEmeraldSword;
import com.lothrazar.cyclicmagic.item.gear.ItemGlowingHelmet;
import com.lothrazar.cyclicmagic.item.gear.ItemPowerArmor;
import com.lothrazar.cyclicmagic.item.gear.ItemPowerSword;
import com.lothrazar.cyclicmagic.item.gear.ItemSandstoneAxe;
import com.lothrazar.cyclicmagic.item.gear.ItemSandstoneHoe;
import com.lothrazar.cyclicmagic.item.gear.ItemSandstonePickaxe;
import com.lothrazar.cyclicmagic.item.gear.ItemSandstoneSpade;
import com.lothrazar.cyclicmagic.registry.GuideRegistry;
import com.lothrazar.cyclicmagic.registry.GuideRegistry.GuideCategory;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry.ChestType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;

public class GearModule extends BaseModule implements IHasConfig {
  //from ArmorMaterial.DIAMOND, second constuctor param
  //used as a ratio for durability
  // only because theyre private, with no getters
  // 
  //private static final int[]  diamondreductionAmounts = new int[] { 3, 6, 8, 3 };
  private boolean enableEmeraldGear;
  private boolean enableSandstoneTools;
  private boolean enablePurpleGear;
  private boolean enablePurpleSwords;
  private boolean glowingHelmet;
  @Override
  public void onPreInit() {
    if (enableEmeraldGear) {
      ItemEmeraldArmor emerald_head = new ItemEmeraldArmor(EntityEquipmentSlot.HEAD);
      ItemRegistry.register(emerald_head, "emerald_helmet", null);
      Item emerald_chest = new ItemEmeraldArmor(EntityEquipmentSlot.CHEST);
      ItemRegistry.register(emerald_chest, "emerald_chestplate", null);
      Item emerald_legs = new ItemEmeraldArmor(EntityEquipmentSlot.LEGS);
      ItemRegistry.register(emerald_legs, "emerald_leggings", null);
      Item emerald_boots = new ItemEmeraldArmor(EntityEquipmentSlot.FEET);
      ItemRegistry.register(emerald_boots, "emerald_boots", null);
      Item emerald_sword = new ItemEmeraldSword();
      ItemRegistry.register(emerald_sword, "emerald_sword", null);
      Item emerald_pickaxe = new ItemEmeraldPickaxe();
      ItemRegistry.register(emerald_pickaxe, "emerald_pickaxe", null);
      Item emerald_axe = new ItemEmeraldAxe();
      ItemRegistry.register(emerald_axe, "emerald_axe", null);
      Item emerald_shovel = new ItemEmeraldSpade();
      ItemRegistry.register(emerald_shovel, "emerald_spade", null);
      Item emerald_hoe = new ItemEmeraldHoe();
      ItemRegistry.register(emerald_hoe, "emerald_hoe", null);
      LootTableRegistry.registerLoot(emerald_pickaxe);
      LootTableRegistry.registerLoot(emerald_sword);
      LootTableRegistry.registerLoot(emerald_chest);
      GuideRegistry.register(GuideCategory.GEAR, emerald_head, "item.emeraldgear.title", "item.emeraldgear.guide");
    }
    if (enablePurpleGear) {
      Item purple_boots = new ItemPowerArmor(EntityEquipmentSlot.FEET);
      ItemRegistry.register(purple_boots, "purple_boots", GuideCategory.GEAR);
      Item purple_leggings = new ItemPowerArmor(EntityEquipmentSlot.LEGS);
      ItemRegistry.register(purple_leggings, "purple_leggings", GuideCategory.GEAR);
      Item purple_chestplate = new ItemPowerArmor(EntityEquipmentSlot.CHEST);
      ItemRegistry.register(purple_chestplate, "purple_chestplate", GuideCategory.GEAR);
      Item purple_helmet = new ItemPowerArmor(EntityEquipmentSlot.HEAD);
      ItemRegistry.register(purple_helmet, "purple_helmet", GuideCategory.GEAR);
    }
    if (glowingHelmet) {
      Item glowing_helmet = new ItemGlowingHelmet(EntityEquipmentSlot.HEAD);
      ItemRegistry.register(glowing_helmet, "glowing_helmet", GuideCategory.GEAR);
      ModCyclic.instance.events.register(glowing_helmet);
    }
    if (enablePurpleSwords) {
      ItemPowerSword sword_weakness = new ItemPowerSword(ItemPowerSword.SwordType.WEAK);
      ItemRegistry.register(sword_weakness, "sword_weakness", GuideCategory.GEAR);
      ItemPowerSword sword_slowness = new ItemPowerSword(ItemPowerSword.SwordType.SLOW);
      ItemRegistry.register(sword_slowness, "sword_slowness", GuideCategory.GEAR);
      ItemPowerSword sword_ender = new ItemPowerSword(ItemPowerSword.SwordType.ENDER);
      ItemRegistry.register(sword_ender, "sword_ender", GuideCategory.GEAR);
    }
    if (enableSandstoneTools) {
      Item sandstone_pickaxe = new ItemSandstonePickaxe();
      ItemRegistry.register(sandstone_pickaxe, "sandstone_pickaxe", null);
      Item sandstone_axe = new ItemSandstoneAxe();
      ItemRegistry.register(sandstone_axe, "sandstone_axe", null);
      Item sandstone_spade = new ItemSandstoneSpade();
      ItemRegistry.register(sandstone_spade, "sandstone_spade", null);
      Item sandstone_hoe = new ItemSandstoneHoe();
      ItemRegistry.register(sandstone_hoe, "sandstone_hoe", null);
      LootTableRegistry.registerLoot(sandstone_pickaxe, ChestType.BONUS);
      LootTableRegistry.registerLoot(sandstone_axe, ChestType.BONUS);
      LootTableRegistry.registerLoot(sandstone_spade, ChestType.BONUS);
      GuideRegistry.register(GuideCategory.GEAR, sandstone_axe, "item.sandstonegear.title", "item.sandstonegear.guide");
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    glowingHelmet = config.getBoolean("GlowingHelmet", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enablePurpleGear = config.getBoolean("PurpleArmor", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableSandstoneTools = config.getBoolean("SandstoneTools", Const.ConfigCategory.content, true, "Sandstone tools are between wood and stone. " + Const.ConfigCategory.contentDefaultText);
    enableEmeraldGear = config.getBoolean("Emerald Gear", Const.ConfigCategory.content, true, "Emerald armor and tools that are slightly weaker than diamond. " + Const.ConfigCategory.contentDefaultText);
    enablePurpleSwords = config.getBoolean("SwordsFrostEnder", Const.ConfigCategory.content, true, "Enable the epic swords. " + Const.ConfigCategory.contentDefaultText);
  }
}
