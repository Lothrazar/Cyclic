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
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandSearchSpawner extends BaseCommand implements ICommand {

  public static final String name = "searchspawner";

  public CommandSearchSpawner(boolean op) {
    super(name, op);
  }

  public static final int MAXRADIUS = 128;
  // yes no?
  public static final int DEFAULTRADIUS = 64;

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    int radius = 0;
    if (args.length > 0) {
      radius = Integer.parseInt(args[0]);
    }
    if (radius > MAXRADIUS) {
      radius = MAXRADIUS;
    }
    if (radius <= 0) {
      radius = DEFAULTRADIUS;
    }
    ArrayList<BlockPos> founds = UtilWorld.findBlocks(sender.getEntityWorld(),
        sender.getPosition(), Blocks.MOB_SPAWNER, radius);
    if (founds.size() == 0) {
      UtilChat.addChatMessage(sender, UtilChat.lang("command.searchspawner.none") + radius);
    }
    else {
      for (BlockPos found : founds)
        if (found != null) {
          UtilChat.addChatMessage(sender, UtilChat.blockPosToString(found));
        }
    }
  }
}
