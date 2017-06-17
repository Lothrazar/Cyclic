package com.lothrazar.cyclicmagic.net;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.item.ItemSleepingMat;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketSleepClient implements IMessage, IMessageHandler<PacketSleepClient, IMessage> {
  private BlockPos pos;
  public PacketSleepClient() {}
  public PacketSleepClient(BlockPos p) {
    pos = p;
  }
  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    int x = tags.getInteger("x");
    int y = tags.getInteger("y");
    int z = tags.getInteger("z");
    pos = new BlockPos(x, y, z);
  }
  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("x", pos.getX());
    tags.setInteger("y", pos.getY());
    tags.setInteger("z", pos.getZ());
    ByteBufUtils.writeTag(buf, tags);
  }
  @Override
  public IMessage onMessage(PacketSleepClient message, MessageContext ctx) {
    if (ctx.side == Side.CLIENT && message.pos != null) {
      EntityPlayer player = ModCyclic.proxy.getPlayerEntity(ctx);
      //when i force a sleep on serverside (ItemSleepingMat) then i need to make sure clientside bedLocation is non null
      //otherwise vanilla MC code gets NPE thrown
      player.bedLocation = message.pos;
      ItemSleepingMat.setRenderOffsetForSleep(player, player.getHorizontalFacing());
    }
    return null;
  }
}
