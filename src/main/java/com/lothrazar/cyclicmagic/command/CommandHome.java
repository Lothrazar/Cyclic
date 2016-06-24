package com.lothrazar.cyclicmagic.command;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilSound;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CommandHome extends BaseCommand implements ICommand {
  public static final String name = "home";
  public CommandHome(boolean op) {
    super(name, op);
  }
  @Override
  public void execute(MinecraftServer server, ICommandSender ic, String[] args) {
    if (ic instanceof EntityPlayer == false) { return; }
    EntityPlayer player = (EntityPlayer) ic;
    World world = player.worldObj;
    if (player.dimension != 0) {
      UtilChat.addChatMessage(player, "command.home.overworld");
      return;
    }
    BlockPos pos = player.getBedLocation(0);
    if (pos == null) {
      // has not been sent in a bed
      UtilChat.addChatMessage(ic, "command.gethome.bed");
      return;
    }
    IBlockState state = world.getBlockState(pos);
    Block block = (state == null) ? null : world.getBlockState(pos).getBlock();
    if (block != null && block.isBed(state, world, pos, player)) {
      // then move over according to how/where the bed wants me to spawn
      pos = block.getBedSpawnPosition(state, world, pos, null);
    }
    else {
      // spawn point was set, so the coords were not null, but player broke the
      // bed (probably recently)
      UtilChat.addChatMessage(player, "command.gethome.bed");
      return;
    }
    UtilEntity.teleportWallSafe(player, world, pos);
    UtilSound.playSound(player, pos, SoundEvents.ENTITY_ENDERMEN_TELEPORT);
  }
}
