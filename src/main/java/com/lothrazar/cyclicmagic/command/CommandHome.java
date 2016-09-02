package com.lothrazar.cyclicmagic.command;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
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
   
    UtilWorld.tryTpPlayerToBed(world, player);
  }
}
