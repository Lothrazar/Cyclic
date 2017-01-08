package com.lothrazar.cyclicmagic.net;
import com.lothrazar.cyclicmagic.item.tool.ItemToolPush;
import com.lothrazar.cyclicmagic.util.UtilPlaceBlocks;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketMoveBlock implements IMessage, IMessageHandler<PacketMoveBlock, IMessage> {
  private BlockPos pos;
  private ItemToolPush.ActionType type;
  private EnumFacing side;
  public PacketMoveBlock() {}
  public PacketMoveBlock(BlockPos mouseover, ItemToolPush.ActionType t, EnumFacing s) {
    pos = mouseover;
    type = t;
    side = s;
  }
  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    int x = tags.getInteger("x");
    int y = tags.getInteger("y");
    int z = tags.getInteger("z");
    pos = new BlockPos(x, y, z);
    int t = tags.getInteger("t");
    type = ItemToolPush.ActionType.values()[t];
    int s = tags.getInteger("s");
    side = EnumFacing.values()[s];
  }
  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("x", pos.getX());
    tags.setInteger("y", pos.getY());
    tags.setInteger("z", pos.getZ());
    tags.setInteger("t", type.ordinal());
    tags.setInteger("s", side.ordinal());
    ByteBufUtils.writeTag(buf, tags);
  }
  @Override
  public IMessage onMessage(final PacketMoveBlock message, final MessageContext ctx) {
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
  @SuppressWarnings("unused")
  private void handle(PacketMoveBlock message, MessageContext ctx) {
    if (ctx.side.isServer() && message != null && message.pos != null) {
      EntityPlayer player = ctx.getServerHandler().playerEntity;
      World worldObj = player.getEntityWorld();
      BlockPos resultPosition = null;
      switch (message.type) {
      case PULL:
        resultPosition = UtilPlaceBlocks.pullBlock(worldObj, player, message.pos, message.side);
        break;
      case PUSH:
        resultPosition = UtilPlaceBlocks.pushBlock(worldObj, player, message.pos, message.side);
        break;
      case ROTATE:
        UtilPlaceBlocks.rotateBlockValidState(worldObj, player, message.pos, message.side);
        resultPosition = pos;
        break;
      default:
        break;
      }
    }
  }
}
