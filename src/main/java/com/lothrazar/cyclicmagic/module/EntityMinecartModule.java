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

import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.core.registry.EntityProjectileRegistry;
import com.lothrazar.cyclicmagic.core.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.guide.GuideCategory;
import com.lothrazar.cyclicmagic.item.minecart.EntityGoldFurnaceMinecart;
import com.lothrazar.cyclicmagic.item.minecart.EntityGoldMinecart;
import com.lothrazar.cyclicmagic.item.minecart.EntityGoldMinecartChest;
import com.lothrazar.cyclicmagic.item.minecart.EntityGoldMinecartDispenser;
import com.lothrazar.cyclicmagic.item.minecart.EntityMinecartDropper;
import com.lothrazar.cyclicmagic.item.minecart.EntityMinecartTurret;
import com.lothrazar.cyclicmagic.item.minecart.EntityStoneMinecart;
import com.lothrazar.cyclicmagic.item.minecart.ItemDropperMinecart;
import com.lothrazar.cyclicmagic.item.minecart.ItemGoldFurnaceMinecart;
import com.lothrazar.cyclicmagic.item.minecart.ItemGoldMinecart;
import com.lothrazar.cyclicmagic.item.minecart.ItemStoneMinecart;
import com.lothrazar.cyclicmagic.item.minecart.ItemTurretMinecart;
import net.minecraftforge.common.config.Configuration;

public class EntityMinecartModule extends BaseModule implements IHasConfig {

  private boolean goldMinecart;
  private boolean stoneMinecart;
  private boolean chestMinecart;
  private boolean dropperMinecart;
  private boolean dispenserMinecart;
  private boolean turretMinecart;

  @Override
  public void onPreInit() {
    super.onPreInit();
    if (goldMinecart) {
      ItemGoldMinecart gold_minecart = new ItemGoldMinecart();
      ItemRegistry.register(gold_minecart, "gold_minecart", GuideCategory.TRANSPORT);
      EntityGoldMinecart.dropItem = gold_minecart;
      EntityProjectileRegistry.registerModEntity(EntityGoldMinecart.class, "goldminecart", 1100);
      ItemGoldFurnaceMinecart gold_furnace_minecart = new ItemGoldFurnaceMinecart();
      ItemRegistry.register(gold_furnace_minecart, "gold_furnace_minecart", GuideCategory.TRANSPORT);
      EntityGoldFurnaceMinecart.dropItem = gold_furnace_minecart;
      EntityProjectileRegistry.registerModEntity(EntityGoldFurnaceMinecart.class, "goldfurnaceminecart", 1101);
    }
    if (stoneMinecart) {
      ItemStoneMinecart stone_minecart = new ItemStoneMinecart();
      ItemRegistry.register(stone_minecart, "stone_minecart", GuideCategory.TRANSPORT);
      EntityStoneMinecart.dropItem = stone_minecart;
      EntityProjectileRegistry.registerModEntity(EntityStoneMinecart.class, "stoneminecart", 1102);
    }
    if (chestMinecart) {
      EntityProjectileRegistry.registerModEntity(EntityGoldMinecartChest.class, "goldchestminecart", 1103);
    }
    if (dropperMinecart) {
      ItemDropperMinecart dropper_minecart = new ItemDropperMinecart();
      ItemRegistry.register(dropper_minecart, "dropper_minecart", GuideCategory.TRANSPORT);
      EntityMinecartDropper.dropItem = dropper_minecart;
      EntityProjectileRegistry.registerModEntity(EntityMinecartDropper.class, "golddropperminecart", 1104);
    }
    if (dispenserMinecart) {
      //BROKEN:
      //it spawns entity in the world. so like an arrow, it flies to the arget but then magically teleports back o teh  cart position
      //stop for now
      EntityProjectileRegistry.registerModEntity(EntityGoldMinecartDispenser.class, "golddispenserminecart", 1105);
    }
    if (turretMinecart) {
      ItemTurretMinecart turret_minecart = new ItemTurretMinecart();
      ItemRegistry.register(turret_minecart, "turret_minecart", GuideCategory.TRANSPORT);
      EntityMinecartTurret.dropItem = turret_minecart;
      EntityProjectileRegistry.registerModEntity(EntityMinecartTurret.class, "turretminecart", 1106);
    }
    //if i have a mob on a LEAD< i can put it in a minecart with thehit
    //maybe 2 passengers..?? idk
    //connect together??
    //DISPENSERR minecart
    //??FLUID CART?
    //TURRET CART:? shoots arrows
    //ONE THAT CAN HOLD ANY ITEM
  }

  @Override
  public void syncConfig(Configuration config) {
    chestMinecart = false;// config.getBoolean("GoldChestMinecart", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    dispenserMinecart = false;//config.getBoolean("GoldDispenserMinecart", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    dropperMinecart = config.getBoolean("GoldDropperMinecart", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    turretMinecart = config.getBoolean("GoldTurretMinecart", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    goldMinecart = config.getBoolean("GoldMinecart", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    stoneMinecart = config.getBoolean("StoneMinecart", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
  }
  //  @SubscribeEvent
  //  public void onMinecartCollision(MinecartCollisionEvent event) {
  //     import net.minecraftforge.event.entity.minecart.MinecartCollisionEvent;
  //   //this never fires. literally never. wtf?
  //  }
}
