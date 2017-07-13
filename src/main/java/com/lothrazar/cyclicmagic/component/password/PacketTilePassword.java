package com.lothrazar.cyclicmagic.component.password;
import com.lothrazar.cyclicmagic.ModCyclic;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketTilePassword implements IMessage, IMessageHandler<PacketTilePassword, IMessage> {
  public static enum PacketType {
    PASSTEXT, ACTIVETYPE, USERSALLOWED, USERCLAIM;
  }
  private BlockPos pos;
  private String password;
  private PacketType type = null;
  public PacketTilePassword() {}
  public PacketTilePassword(PacketType t, String pword, BlockPos p) {
    pos = p;
    password = pword;
    type = t;
  }
  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    type = PacketType.values()[tags.getInteger("t")];
    int x = tags.getInteger("x");
    int y = tags.getInteger("y");
    int z = tags.getInteger("z");
    pos = new BlockPos(x, y, z);
    password = tags.getString("p");
  }
  @Override
  public void toBytes(ByteBuf buf) {
    if (type == null) {
      type = PacketType.PASSTEXT;//legacy safety
    }
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("x", pos.getX());
    tags.setInteger("y", pos.getY());
    tags.setInteger("z", pos.getZ());
    tags.setInteger("t", type.ordinal());
    tags.setString("p", password);
    ByteBufUtils.writeTag(buf, tags);
  }
  @Override
  public IMessage onMessage(PacketTilePassword message, MessageContext ctx) {
    PacketTilePassword.checkThreadAndEnqueue(message, ctx);
    return null;
  }
  private static void checkThreadAndEnqueue(final PacketTilePassword message, final MessageContext ctx) {
    if (message.type == null) {
      message.type = PacketType.PASSTEXT;//legacy safety
    }
    //copied in from my PacketSyncPlayerData
    IThreadListener thread = ModCyclic.proxy.getThreadFromContext(ctx);
    // pretty much copied straight from vanilla code, see {@link PacketThreadUtil#checkThreadAndEnqueue}
    thread.addScheduledTask(new Runnable() {
      public void run() {
        EntityPlayerMP player = ctx.getServerHandler().player;
        World world = player.getEntityWorld();
        TileEntityPassword tile = (TileEntityPassword) world.getTileEntity(message.pos);
        if (tile != null) {
          switch (message.type) {
            case ACTIVETYPE:
              tile.toggleActiveType();
            break;
            case PASSTEXT:
              tile.setMyPassword(message.password);
            break;
            case USERSALLOWED:
              tile.toggleUserType();
            break;
            case USERCLAIM:
              tile.toggleClaimedHash(player);
            break;
          }
          tile.markDirty();
          player.getEntityWorld().markChunkDirty(message.pos, tile);
          final IBlockState oldState = world.getBlockState(message.pos);
          //http://www.minecraftforge.net/forum/index.php?topic=38710.0
          world.notifyBlockUpdate(message.pos, oldState, oldState, 3);
        }
      }
    });
  }
}
