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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class BaseCommand implements ICommand {

  // https://github.com/LothrazarMinecraftMods/MinecraftSearchCommands/blob/master/src/main/java/com/lothrazar/searchcommands/command/CommandSearchTrades.java
  // https://github.com/PrinceOfAmber/SamsPowerups/tree/master/Commands/src/main/java/com/lothrazar/samscommands/command
  // CommandSimpleWaypoints removed TP feature -> we have ender book already
  private String name;
  private boolean requiresOP;
  public int usernameIndex = -1;
  private final static int OP = 2;
  protected ArrayList<String> aliases;

  public BaseCommand(String n, boolean op) {
    this(n, op, null);
  }

  public BaseCommand(String n, boolean op, ArrayList<String> paliases) {
    name = n;
    requiresOP = op;
    aliases = (paliases == null) ? new ArrayList<String>() : paliases;
    //    aliases.add(name.toUpperCase());
  }

  @Override
  public List<String> getAliases() {
    return this.aliases;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public int compareTo(ICommand arg) {
    return this.getName().compareTo(arg.getName());
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/" + this.getName();
  }

  @Override
  public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
    return (requiresOP) ? sender.canUseCommand(OP, this.getName()) : true;
  }

  @Override
  public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
    if (usernameIndex < 0) {
      return Collections.<String> emptyList();
    }
    return args.length == usernameIndex + 1 ? CommandBase.getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()) : Collections.<String> emptyList();
  }

  @Override
  public boolean isUsernameIndex(String[] args, int index) {
    return index == usernameIndex;
  }

  public EntityPlayerMP getPlayerByUsername(MinecraftServer server, String name) {
    return server.getPlayerList().getPlayerByUsername(name);
  }

  public void setUsernameIndex(int i) {
    usernameIndex = i;
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    //S ("Warning: command not implemented " + Const.MODID + " -> " + this.getCommandName());
  }
}
