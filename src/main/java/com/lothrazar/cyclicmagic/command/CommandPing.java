package com.lothrazar.cyclicmagic.command;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

public class CommandPing extends BaseCommand implements ICommand {
  public static final String name = "ping";
  public CommandPing(boolean op) {
    super(name, op);
  }
  @Override
  public String getUsage(ICommandSender sender) {
    return "/" + getName() + " <nether>";
  }
  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    //  if (sender instanceof EntityPlayer == false) { return; }
    // EntityPlayer player = (EntityPlayer) sender;
    BlockPos pos;
    if (args.length > 0 && args[0] != null && args[0].equalsIgnoreCase("nether")) {
      BlockPos p = sender.getPosition();
      // force doubles, otherwise int rounding makes it act like _/10
      double netherRatio = 8.0;
      double x = p.getX();
      double z = p.getZ();
      pos = new BlockPos(x / netherRatio, p.getY(), z / netherRatio);
      //UtilChat.addChatMessage(sender, UtilChat.blockPosToString(n));
    }
    else {
      pos = sender.getPosition();
    }
    Biome biome = sender.getEntityWorld().getBiome(pos);
    UtilChat.addChatMessage(sender, UtilChat.blockPosToString(pos) + " (" + biome.getBiomeName() + ")");
  }
}
