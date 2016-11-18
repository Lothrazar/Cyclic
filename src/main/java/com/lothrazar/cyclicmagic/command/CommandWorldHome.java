package com.lothrazar.cyclicmagic.command;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilSound;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.server.MinecraftServer;
//import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class CommandWorldHome extends BaseCommand implements ICommand {
  public static final String name = "worldhome";
  public CommandWorldHome(boolean op) {
    super(name, op);
  }
  @Override
  public void execute(MinecraftServer server, ICommandSender ic, String[] args) {
    if (ic instanceof EntityPlayer == false) { return; }
    World world = ic.getCommandSenderEntity().getEntityWorld();
    EntityPlayer player = (EntityPlayer) ic;
    if (player.dimension != 0) {
      // :"Can only teleport to worldhome in the overworld"
      UtilChat.addChatMessage(ic, "command.worldhome.dim");
      return;
    }
    // this tends to always get something at y=64, regardless if there is AIR or
    // not
    // so we need to safely push the player up out of any blocks they are in
    UtilSound.playSound(player, player.getPosition(), SoundEvents.ENTITY_ENDERMEN_TELEPORT, player.getSoundCategory());
    UtilEntity.teleportWallSafe(player, world, world.getSpawnPoint());
    UtilSound.playSound(player, world.getSpawnPoint(), SoundEvents.ENTITY_ENDERMEN_TELEPORT, player.getSoundCategory());
  }
}
