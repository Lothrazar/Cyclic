package com.lothrazar.cyclicmagic.net;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.util.UtilSound;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketSound implements IMessage, IMessageHandler<PacketSound, IMessage> {
  //this is used by multiple because of interface ITileSizeToggle
  private BlockPos pos;
  private String domain;
  private String type;
  private String category;
  public PacketSound() {}
  public PacketSound(BlockPos p, SoundEvent t, SoundCategory cat) {
    pos = p;
    ResourceLocation r = t.getRegistryName();
    domain = r.getResourceDomain();
    type = r.getResourcePath();
    category = cat.getName(); //SoundCategory.BLOCKS.getName()
  }
  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    int x = tags.getInteger("x");
    int y = tags.getInteger("y");
    int z = tags.getInteger("z");
    pos = new BlockPos(x, y, z);
    type = tags.getString("t");
    domain = tags.getString("domain");
    category = tags.getString("cat");
  }
  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("x", pos.getX());
    tags.setInteger("y", pos.getY());
    tags.setInteger("z", pos.getZ());
    tags.setString("t", type);
    tags.setString("domain", domain);
    tags.setString("cat", category);
    ByteBufUtils.writeTag(buf, tags);
  }
  @Override
  public IMessage onMessage(PacketSound message, MessageContext ctx) {
    if (ctx.side == Side.CLIENT) {
      EntityPlayer p = ModCyclic.proxy.getPlayerEntity(ctx);
      if (p != null) {
        SoundEvent s = SoundEvent.REGISTRY.getObject(new ResourceLocation(message.domain, message.type));
        if (s != null) {
          SoundCategory cat = SoundCategory.getByName(message.category);
          if (cat == null) {
            cat = SoundCategory.BLOCKS;
          }
          UtilSound.playSound(p, message.pos, s, cat);
        }
      }
    }
    return null;
  }
}
