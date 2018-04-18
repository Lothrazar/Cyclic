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

import com.lothrazar.cyclicmagic.registry.CapabilityRegistry;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry.IPlayerExtendedProperties;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CommandTodoList extends BaseCommand implements ICommand {

  public static final String name = "todo";

  public CommandTodoList(boolean op) {
    super(name, op);
  }

  private static final String MODE_ADD = "add";
  private static final String MODE_REMOVE = "delete";
  private static final String MODE_SET = "set";
  private static final String MODE_GET = "get";
  //private static final String	NBT_KEY			= Const.MODID + "_todo";
  public static boolean PERSIST_DEATH;

  @Override
  public String getUsage(ICommandSender s) {
    return "/" + getName() + " <" + MODE_GET + "|" + MODE_SET + "|" + MODE_ADD + "|" + MODE_REMOVE + "> <text>";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender icommandsender, String[] args) {
    if (icommandsender instanceof EntityPlayer == false) {
      return;
    }
    EntityPlayer player = (EntityPlayer) icommandsender;
    String todoCurrent = getTodoForPlayer(player);
    // is the first argument empty
    if (args == null || args.length == 0 || args[0] == null || args[0].isEmpty()) {
      // player.addChatMessage(new
      // ChatComponentTranslation(getCommandUsage(icommandsender)));
      UtilChat.addChatMessage(player, getUsage(icommandsender));
      return;
    }
    String message = "";
    if (args[0].equals(MODE_GET)) {
      // just display current in chat
      UtilChat.addChatMessage(player, getTodoForPlayer(player));
    }
    else if (args[0].equals(MODE_REMOVE)) {
      todoCurrent = "";
      args[0] = "";// remove the plus sign
    }
    else if (args[0].equals(MODE_ADD)) {
      for (int i = 1; i < args.length; i++) {
        message += " " + args[i];
      }
      if (todoCurrent.isEmpty())
        todoCurrent = message;
      else
        todoCurrent += " " + message;// so append
    }
    else if (args[0].equals(MODE_SET)) {
      // they just did /todo blah blah
      for (int i = 1; i < args.length; i++) {
        message += " " + args[i];
      }
      todoCurrent = message;// so replace
    }
    setTodoForPlayer(player, todoCurrent);
  }

  private void setTodoForPlayer(EntityPlayer player, String todoCurrent) {
    IPlayerExtendedProperties props = CapabilityRegistry.getPlayerProperties(player);
    props.setTODO(todoCurrent);
    if (player instanceof EntityPlayerMP) {
      CapabilityRegistry.syncServerDataToClient((EntityPlayerMP) player);
    }
  }

  public static String getTodoForPlayer(EntityPlayer player) {
    IPlayerExtendedProperties props = CapabilityRegistry.getPlayerProperties(player);
    return props.getTODO();
  }
}
