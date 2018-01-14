package com.lothrazar.cyclicmagic.net;
import java.util.List;
import com.lothrazar.cyclicmagic.item.ItemScythe;
import com.lothrazar.cyclicmagic.util.UtilScythe;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketScythe implements IMessage, IMessageHandler<PacketScythe, IMessage> {
  private BlockPos pos;
  private ItemScythe.ScytheType type;
  private int radius;
  public PacketScythe() {}
  public PacketScythe(BlockPos mouseover, ItemScythe.ScytheType t, int r) {
    pos = mouseover;
    type = t;
    radius = r;
  }
  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    int x = tags.getInteger("x");
    int y = tags.getInteger("y");
    int z = tags.getInteger("z");
    pos = new BlockPos(x, y, z);
    int t = tags.getInteger("t");
    type = ItemScythe.ScytheType.values()[t];
    radius = tags.getInteger("s");
  }
  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("x", pos.getX());
    tags.setInteger("y", pos.getY());
    tags.setInteger("z", pos.getZ());
    tags.setInteger("t", type.ordinal());
    tags.setInteger("s", radius);
    ByteBufUtils.writeTag(buf, tags);
  }
  @Override
  public IMessage onMessage(final PacketScythe message, final MessageContext ctx) {
    MinecraftServer s = FMLCommonHandler.instance().getMinecraftServerInstance();
    if (s == null) {//this is never happening. ill keep it just in case
      handle(message, ctx);
    }
    else {
      //only java 8
      //s.addScheduledTask(() -> handle(message, ctx));
      s.addScheduledTask(new Runnable() {
        public void run() {
          handle(message, ctx);
        }
      });
    }
    return null;
  }
  private void handle(PacketScythe message, MessageContext ctx) {
    if (ctx.side.isServer() && message != null && message.pos != null) {
      EntityPlayer player = ctx.getServerHandler().player;
      World world = player.getEntityWorld();
      List<BlockPos> shape = ItemScythe.getShape(message.pos, message.radius);
      for (BlockPos p : shape) {
        UtilScythe.harvestSingle(world, p, message.type);
      }
    }
  }
}
