package com.lothrazar.cyclicmagic.item.cannon;
import java.util.Random;
import com.lothrazar.cyclicmagic.proxy.ClientProxy;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageGolemLaserFX implements IMessage {
  public static Random random = new Random();
  public double posX = 0, posY = 0, posZ = 0;
  public double value = 0;
  public int r, g, b;
  public MessageGolemLaserFX() {
    super();
  }
  public MessageGolemLaserFX(double x, double y, double z, double value, int r, int g, int b) {
    super();
    this.posX = x;
    this.posY = y;
    this.posZ = z;
    this.r = r;
    this.g = g;
    this.b = b;
    this.value = value;
  }
  @Override
  public void fromBytes(ByteBuf buf) {
    posX = buf.readDouble();
    posY = buf.readDouble();
    posZ = buf.readDouble();
    value = buf.readDouble();
    r = buf.readInt();
    g = buf.readInt();
    b = buf.readInt();
  }
  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeDouble(posX);
    buf.writeDouble(posY);
    buf.writeDouble(posZ);
    buf.writeDouble(value);
    buf.writeInt(r);
    buf.writeInt(g);
    buf.writeInt(b);
  }
  public static class MessageHolder implements IMessageHandler<MessageGolemLaserFX, IMessage> {
    public static Random random = new Random();
    public static int counter = 0;
    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(final MessageGolemLaserFX message, final MessageContext ctx) {
      if (ctx.side == Side.CLIENT) {
        Minecraft.getMinecraft().addScheduledTask(() -> {
          World world = Minecraft.getMinecraft().world;
          for (int k = 0; k < 80; k++) {
            spawnParticleGlow(world, (float) message.posX, (float) message.posY, (float) message.posZ, ((float) message.value / 3.5f) * 0.125f * (random.nextFloat() - 0.5f), ((float) message.value / 3.5f) * 0.125f * (random.nextFloat() - 0.5f), ((float) message.value / 3.5f) * 0.125f * (random.nextFloat() - 0.5f),
                // 255, 64, 16, 
                message.r, message.g, message.b,
                1.0f, (float) message.value, 24);
          }
        });
      }
      return null;
    }
    @SideOnly(Side.CLIENT)
    public static void spawnParticleGlow(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale, int lifetime) {
      counter += random.nextInt(3);
      if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2 * Minecraft.getMinecraft().gameSettings.particleSetting) == 0) {
        ClientProxy.particleRenderer.addParticle(new ParticleGolemLaser(world, x, y, z, vx, vy, vz, r, g, b, a, scale, lifetime));
      }
    }
  }
}
