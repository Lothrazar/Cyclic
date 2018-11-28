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
package com.lothrazar.cyclicmagic.tweak;

import java.util.HashMap;
import java.util.Map;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.registry.module.BaseModule;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

@SuppressWarnings("deprecation")
public class FuelAdditionModule extends BaseModule implements IHasConfig {

  private boolean enabled;

  //links existing vanilla items as burnable fuel
  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.items;
    Property prop = config.get(category, "More Furnace Fuel", true, "Tons more wood and plant related items now can burn as fuel");
    enabled = prop.getBoolean();
  }

  @Override
  public void onInit() {
    if (enabled) {
      GameRegistry.registerFuelHandler(new FuelHandler());
    }
  }

  private class FuelHandler implements IFuelHandler {

    Map<Item, Integer> fuelMap = new HashMap<Item, Integer>();

    public FuelHandler() {
      // http://minecraft.gamepedia.com/Smelting
      int stick = TileEntityFurnace.getItemBurnTime(new ItemStack(Items.STICK));
      int log = TileEntityFurnace.getItemBurnTime(new ItemStack(Blocks.LOG));
      // int coal = TileEntityFurnace.getItemBurnTime(new ItemStack(Items.coal));
      // blazerod, coalblock, lava bucket are above these
      fuelMap.put(Items.SIGN, log);
      fuelMap.put(Items.ACACIA_BOAT, log);
      fuelMap.put(Items.JUNGLE_BOAT, log);
      fuelMap.put(Items.BIRCH_BOAT, log);
      fuelMap.put(Items.BOAT, log);//oak
      fuelMap.put(Items.DARK_OAK_BOAT, log);
      fuelMap.put(Items.SPRUCE_DOOR, log);
      fuelMap.put(Items.ACACIA_DOOR, log);
      fuelMap.put(Items.JUNGLE_DOOR, log);
      fuelMap.put(Items.BIRCH_DOOR, log);
      fuelMap.put(Items.DARK_OAK_DOOR, log);
      fuelMap.put(Items.OAK_DOOR, log);
      fuelMap.put(Items.SPRUCE_BOAT, log);
      fuelMap.put(Item.getItemFromBlock(Blocks.LADDER), log);
      fuelMap.put(Items.PAINTING, log);
      fuelMap.put(Items.WHEAT_SEEDS, stick);
      fuelMap.put(Items.PUMPKIN_SEEDS, stick);
      fuelMap.put(Items.MELON_SEEDS, stick);
      fuelMap.put(Items.BEETROOT_SEEDS, stick);
      fuelMap.put(Items.PAPER, stick);
      fuelMap.put(Item.getItemFromBlock(Blocks.VINE), stick);
      fuelMap.put(Item.getItemFromBlock(Blocks.TALLGRASS), stick);
      fuelMap.put(Item.getItemFromBlock(Blocks.WATERLILY), stick);
      fuelMap.put(Item.getItemFromBlock(Blocks.DEADBUSH), stick);
      fuelMap.put(Item.getItemFromBlock(Blocks.LEAVES), stick);
      fuelMap.put(Item.getItemFromBlock(Blocks.LEAVES2), stick);
    }

    @Override
    public int getBurnTime(ItemStack fuel) {
      if (fuelMap.containsKey(fuel.getItem())) {
        return fuelMap.get(fuel.getItem());
      }
      return 0;
    }
  }
}
