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
package com.lothrazar.cyclicmagic.command;

import com.lothrazar.cyclicmagic.core.util.UtilChat;
import com.lothrazar.cyclicmagic.core.util.UtilEntity;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry.IPlayerExtendedProperties;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandHearts extends BaseCommand implements ICommand {

  public static final String name = "sethearts";

  public CommandHearts(boolean op) {
    super(name, op);
    this.setUsernameIndex(0);
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/" + getName() + " <player> <hearts>";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    EntityPlayer ptarget = null;
    int hearts = 0;
    try {
      ptarget = super.getPlayerByUsername(server, args[0]);
      if (ptarget == null) {
        UtilChat.addChatMessage(sender, getUsage(sender));
        return;
      }
    }
    catch (Exception e) {
      UtilChat.addChatMessage(sender, getUsage(sender));
      return;
    }
    try {
      hearts = Integer.parseInt(args[1]);
    }
    catch (Exception e) {
      UtilChat.addChatMessage(sender, getUsage(sender));
      return;
    }
    if (hearts < 1) {
      hearts = 1;
    }
    int health = hearts * 2;
    IPlayerExtendedProperties prop = CapabilityRegistry.getPlayerProperties(ptarget);
    prop.setMaxHealth(health);
    UtilEntity.setMaxHealth(ptarget, health);
  }
}
