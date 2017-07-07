package com.lothrazar.cyclicmagic.command;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.Village;
import net.minecraft.world.World;

public class CommandVillageInfo extends BaseCommand implements ICommand {
  //5852309458819775221
  // is a seed with village at spawn; for testing
  public static final String name = "villageinfo";
  public CommandVillageInfo(boolean op) {
    super(name, op);
  }
  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    BlockPos pos = sender.getPosition();
    World world = sender.getEntityWorld();
    int dX, dZ;
    int range = 64;
    Village closest = world.villageCollectionObj.getNearestVillage(pos, range);
    if (closest == null) {
      UtilChat.addChatMessage(sender, "command.villageinfo.none");
    }
    else {
      int doors = closest.getNumVillageDoors();
      int villagers = closest.getNumVillagers();
      UtilChat.addChatMessage(sender, UtilChat.lang("command.villageinfo.popul") + String.format("%d", villagers));
      UtilChat.addChatMessage(sender, UtilChat.lang("command.villageinfo.doors") + String.format("%d", doors));
      if (sender instanceof EntityPlayer) {
        // command blocks/server controllers do not have reputation
        EntityPlayer player = (EntityPlayer) sender;
        int rep = closest.getPlayerReputation(player.getName());
        UtilChat.addChatMessage(sender, player.getName() + " " + UtilChat.lang("command.villageinfo.reputation") + String.format("%d", rep));
      }
      dX = pos.getX() - closest.getCenter().getX();
      dZ = pos.getZ() - closest.getCenter().getZ();
      int dist = MathHelper.floor(Math.sqrt(dX * dX + dZ * dZ));
      UtilChat.addChatMessage(sender, UtilChat.lang("command.villageinfo.center") + String.format("%d", dist));
    }
  }
}
