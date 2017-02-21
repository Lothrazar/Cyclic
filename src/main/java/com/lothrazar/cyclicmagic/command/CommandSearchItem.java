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
