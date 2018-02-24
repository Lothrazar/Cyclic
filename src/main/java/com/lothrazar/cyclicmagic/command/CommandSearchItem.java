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
import java.util.Arrays;
import java.util.Map;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.inventory.IInventory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandSearchItem extends BaseCommand implements ICommand {
  public static final String name = "searchitem";
  public static final int radius = 64;
  public CommandSearchItem(boolean op) {
    super(name, op);
  }
  @Override
  public String getUsage(ICommandSender arg0) {
    return "/" + getName() + " <item>";
  }
  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
    if (args.length < 1) {
      UtilChat.addChatMessage(sender, getUsage(sender));
      return;
    }
    String searchQuery = String.join(" ", Arrays.asList(args));
    if (searchQuery.length() == 0) {
      UtilChat.addChatMessage(sender, getUsage(sender));
    }
    Map<IInventory, BlockPos> tilesToSearch = UtilWorld.findTileEntityInventories(sender, radius);
    int foundQtyTotal;
    ArrayList<String> foundMessages = new ArrayList<String>();
    for (Map.Entry<IInventory, BlockPos> entry : tilesToSearch.entrySet()) {
      foundQtyTotal = 0;
      foundQtyTotal = UtilWorld.searchTileInventory(searchQuery, entry.getKey());
      if (foundQtyTotal > 0) {
        String totalsStr = foundQtyTotal + " : ";
        foundMessages.add(totalsStr + getCoordsOrReduced(sender, entry.getValue()));
      }
    }
    int ifound = foundMessages.size();
    if (ifound == 0) {
      UtilChat.addChatMessage(sender, UtilChat.lang("command.searchitem.none") + " : " + radius);
    }
    else {
      for (int i = 0; i < ifound; i++) {
        UtilChat.addChatMessage(sender, foundMessages.get(i));
      }
    }
  }
  public static String getCoordsOrReduced(ICommandSender player, BlockPos pos) {
    return UtilChat.getDirectionsString(player, pos) + " (" + UtilChat.blockPosToString(pos) + ")";
  }
}
