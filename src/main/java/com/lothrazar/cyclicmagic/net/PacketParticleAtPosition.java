package com.lothrazar.cyclicmagic.net;
//import com.lothrazar.util.Util;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketParticleAtPosition implements IMessage, IMessageHandler<PacketParticleAtPosition, IMessage> {
  private int x;
  private int y;
  private int z;
  private int particle;
  private int count;
  public PacketParticleAtPosition() {}
  public PacketParticleAtPosition(BlockPos p, int part, int c) {
    x = p.getX();
    y = p.getY();
    z = p.getZ();
    particle = part;
    count = c;
  }
  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    x = tags.getInteger("x");
    y = tags.getInteger("y");
    z = tags.getInteger("z");
    particle = tags.getInteger("p");
    count = tags.getInteger("c");
  }
  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("x", x);
    tags.setInteger("y", y);
    tags.setInteger("z", z);
    tags.setInteger("p", particle);
    tags.setInteger("c", count);
    ByteBufUtils.writeTag(buf, tags);
  }
  @Override
  public IMessage onMessage(PacketParticleAtPosition message, MessageContext ctx) {
    if (ctx.side.isClient() && Minecraft.getMinecraft().player != null) {
      // http://www.minecraftforge.net/forum/index.php?topic=21195.0
      //yes, this being null happened once
      World world = Minecraft.getMinecraft().player.getEntityWorld();
      UtilParticle.spawnParticle(world, EnumParticleTypes.getParticleFromId(message.particle), message.x + 0.5, message.y, message.z + 0.5, message.count);
    }
    return null;
  }
}
