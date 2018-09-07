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

import java.util.HashMap;
import java.util.Map;
import com.lothrazar.cyclicmagic.command.CommandEnderChest;
import com.lothrazar.cyclicmagic.command.CommandGetHome;
import com.lothrazar.cyclicmagic.command.CommandHeal;
import com.lothrazar.cyclicmagic.command.CommandHearts;
import com.lothrazar.cyclicmagic.command.CommandHome;
import com.lothrazar.cyclicmagic.command.CommandNbt;
import com.lothrazar.cyclicmagic.command.CommandPing;
import com.lothrazar.cyclicmagic.command.CommandSearchItem;
import com.lothrazar.cyclicmagic.command.CommandSearchSpawner;
import com.lothrazar.cyclicmagic.command.CommandSearchTrades;
import com.lothrazar.cyclicmagic.command.CommandTodoList;
import com.lothrazar.cyclicmagic.command.CommandVillageInfo;
import com.lothrazar.cyclicmagic.command.CommandWorldHome;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class CommandModule extends BaseModule implements IHasConfig {

  private static Map<String, Boolean> configToggle = new HashMap<String, Boolean>();
  private static Map<String, Boolean> commandNeedsOp = new HashMap<String, Boolean>();
  private static String category;

  @Override
  public void onServerStarting(FMLServerStartingEvent event) {
    if (configToggle.get(CommandNbt.name)) {
      event.registerServerCommand(new CommandNbt(commandNeedsOp.get(CommandNbt.name)));
    }
    if (configToggle.get(CommandEnderChest.name)) {
      event.registerServerCommand(new CommandEnderChest(commandNeedsOp.get(CommandEnderChest.name)));
    }
    if (configToggle.get(CommandGetHome.name)) {
      event.registerServerCommand(new CommandGetHome(commandNeedsOp.get(CommandGetHome.name)));
    }
    if (configToggle.get(CommandHeal.name)) {
      event.registerServerCommand(new CommandHeal(commandNeedsOp.get(CommandHeal.name)));
    }
    if (configToggle.get(CommandHearts.name)) {
      event.registerServerCommand(new CommandHearts(commandNeedsOp.get(CommandHearts.name)));
    }
    if (configToggle.get(CommandHome.name)) {
      event.registerServerCommand(new CommandHome(commandNeedsOp.get(CommandHome.name)));
    }
    if (configToggle.get(CommandPing.name)) {
      event.registerServerCommand(new CommandPing(commandNeedsOp.get(CommandPing.name)));
    }
    if (configToggle.get(CommandSearchItem.name)) {
      event.registerServerCommand(new CommandSearchItem(commandNeedsOp.get(CommandSearchItem.name)));
    }
    if (configToggle.get(CommandSearchSpawner.name)) {
      event.registerServerCommand(new CommandSearchSpawner(commandNeedsOp.get(CommandSearchSpawner.name)));
    }
    if (configToggle.get(CommandSearchTrades.name)) {
      event.registerServerCommand(new CommandSearchTrades(commandNeedsOp.get(CommandSearchTrades.name)));
    }
    if (configToggle.get(CommandTodoList.name)) {
      event.registerServerCommand(new CommandTodoList(commandNeedsOp.get(CommandTodoList.name)));
    }
    if (configToggle.get(CommandVillageInfo.name)) {
      event.registerServerCommand(new CommandVillageInfo(commandNeedsOp.get(CommandVillageInfo.name)));
    }
    if (configToggle.get(CommandWorldHome.name)) {
      event.registerServerCommand(new CommandWorldHome(commandNeedsOp.get(CommandWorldHome.name)));
    }
  }

  private void syncCommandConfig(Configuration config, String name, boolean defaultNeedsOp, String comment) {
    Property prop = config.get(category, name, true, comment);
    prop.setRequiresMcRestart(true);
    configToggle.put(name, prop.getBoolean());
    prop = config.get(category, name + ".NeedsOP", defaultNeedsOp, "If true, only server OPs can run this command (or cheats enabled in single player)");
    prop.setRequiresMcRestart(true);
    commandNeedsOp.put(name, prop.getBoolean());
  }

  @Override
  public void syncConfig(Configuration config) {
    category = Const.ConfigCategory.commands;
    config.setCategoryComment(category, "Disable any command that was added");
    syncCommandConfig(config, CommandNbt.name, false, "Read NBT data from your held item");
    syncCommandConfig(config, CommandEnderChest.name, true, "Opens your ender chest");
    syncCommandConfig(config, CommandGetHome.name, false, "Get where your current spawn is set (by a bed)");
    syncCommandConfig(config, CommandHeal.name, true, "Heal yourself (or a target player) to full");
    syncCommandConfig(config, CommandHearts.name, true, "Increase the maximum hearts of a target player (lasts until death)");
    syncCommandConfig(config, CommandHome.name, true, "Teleport you to your current spawn (if its set)");
    syncCommandConfig(config, CommandPing.name, false, "Display your current coordinates");
    syncCommandConfig(config, CommandSearchItem.name, false, "Search for an item in nearby containers");
    syncCommandConfig(config, CommandSearchSpawner.name, true, "Search the world nearby for spawners (dungeons, etc)");
    syncCommandConfig(config, CommandSearchTrades.name, false, "Search nearby villagers for trades based on item names");
    syncCommandConfig(config, CommandTodoList.name, false, "Set reminders on screen for yourself");
    syncCommandConfig(config, CommandVillageInfo.name, false, "Get the stats on the nearest village (if any)");
    syncCommandConfig(config, CommandWorldHome.name, true, "Teleport to true worldspawn");
  }
}
