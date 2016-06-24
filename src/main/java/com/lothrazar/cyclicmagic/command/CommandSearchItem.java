package com.lothrazar.cyclicmagic.command;
import java.util.ArrayList;
import java.util.HashMap;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilSearchWorld;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CommandSearchItem extends BaseCommand implements ICommand {
  public static final String name = "searchitem";
  public CommandSearchItem(boolean op) {
    super(name, op);
  }
  @Override
  public String getCommandUsage(ICommandSender arg0) {
    return "/" + getCommandName() + " <itemname> [radius]";
  }
  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
    //if (!(sender instanceof EntityPlayerMP)) { return; }
    World world = sender.getEntityWorld();
    //EntityPlayerMP player = (EntityPlayerMP) sender;
    if (args.length < 1) {
      UtilChat.addChatMessage(sender, getCommandUsage(sender));
      return;
    }
    int radius = 0;
    if (args.length > 1) {
      radius = Integer.parseInt(args[1]);
    }
    if (radius > 128) {
      radius = 128;
    } // Maximum //
    if (radius <= 0) {
      radius = 64;
    } // default
    String searchQuery = args[0].trim().toLowerCase(); // args[0] is the command
    // name or alias used
    // such as "is"
    ArrayList<IInventory> tilesToSearch = new ArrayList<IInventory>();
    HashMap<IInventory, BlockPos> dictionary = new HashMap<IInventory, BlockPos>();
    IInventory tile;
    ArrayList<BlockPos> foundChests = UtilSearchWorld.findBlocks(world,
        sender.getPosition(), Blocks.CHEST, radius);
    for (BlockPos pos : foundChests) {
      tile = (IInventory) world.getTileEntity(pos);
      tilesToSearch.add(tile);
      dictionary.put(tile, pos);
    }
    ArrayList<BlockPos> foundTrapChests = UtilSearchWorld.findBlocks(world,
        sender.getPosition(), Blocks.TRAPPED_CHEST, radius);
    for (BlockPos pos : foundTrapChests) {
      tile = (IInventory) world.getTileEntity(pos);
      tilesToSearch.add(tile);
      dictionary.put(tile, pos);
    }
    ArrayList<BlockPos> foundDisp = UtilSearchWorld.findBlocks(sender.getEntityWorld(),
        sender.getPosition(), Blocks.DISPENSER, radius);
    for (BlockPos pos : foundDisp) {
      tile = (IInventory) world.getTileEntity(pos);
      tilesToSearch.add(tile);
      dictionary.put(tile, pos);
    }
    int foundQtyTotal;
    ArrayList<String> foundMessages = new ArrayList<String>();
    for (IInventory inventory : tilesToSearch) {
      foundQtyTotal = 0;
      foundQtyTotal = UtilSearchWorld.searchTileInventory(searchQuery, inventory);
      if (foundQtyTotal > 0) {
        String totalsStr = foundQtyTotal + " : ";
        // TODO we COULD have configs for each of these, that is, one config
        // flag for SEND_CHAT
        // and one for SEND_PARTICLES
        // ModCommands.spawnParticlePacketByID(dictionary.get(inventory),EnumParticleTypes.CRIT_MAGIC.getParticleID());
        foundMessages.add(totalsStr + getCoordsOrReduced(dictionary.get(inventory)));
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
  public static String getCoordsOrReduced(BlockPos pos) {
    // boolean showCoords =
    // !player.worldObj.getGameRules().getGameRuleBooleanValue("reducedDebugInfo");
    // if(showCoords)
    return pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
    // else
    // return ModCommands.getDirectionsString(player, pos);
  }
}
