package com.lothrazar.cyclicmagic.command;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

public class CommandNbt extends BaseCommand implements ICommand {
  public static final String name = "nbtprint";
  public CommandNbt(boolean op) {
    super(name, op);
  }
  @Override
  public void execute(MinecraftServer server, ICommandSender ic, String[] args) {
    if (ic instanceof EntityPlayer == false) {
      return;
    }
    EntityPlayer player = (EntityPlayer) ic;
    ItemStack held = player.getHeldItemMainhand();
    if (UtilNBT.hasTagCompund(held)) {
      NBTTagCompound tags = UtilNBT.getItemStackNBT(held);
      UtilChat.addChatMessage(player, tags.toString());
    }
    else {
      UtilChat.addChatMessage(player, "command.nbtprint.null");
    }
  }
}