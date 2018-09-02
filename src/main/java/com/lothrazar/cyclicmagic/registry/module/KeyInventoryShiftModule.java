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
package com.lothrazar.cyclicmagic.registry.module;

import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class KeyInventoryShiftModule extends BaseEventModule implements IHasConfig {

  public boolean cancelPotionInventoryShift;
  public static boolean enableInvoKeys;//static because mod proxy looks at this

  @Override
  public void syncConfig(Configuration config) {
    enableInvoKeys = config.getBoolean("KeybindInventoryShift", Const.ConfigCategory.inventory, true, "Set this to false (and restart your client) to remove the inventory shift keybindings");
    String category = Const.ConfigCategory.inventory;
    cancelPotionInventoryShift = config.getBoolean("Potion Inventory Shift", category, true,
        "When true, this blocks the potions moving the inventory over");
  }

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onPotionShiftEvent(GuiScreenEvent.PotionShiftEvent event) {
    event.setCanceled(cancelPotionInventoryShift);
  }

}
