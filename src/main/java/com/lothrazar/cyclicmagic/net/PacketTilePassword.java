package com.lothrazar.cyclicmagic.net;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityPassword;
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
  private BlockPos pos;
  private String password;
  public PacketTilePassword() {
  }
  public PacketTilePassword(String pword, BlockPos p) {
    pos = p;
    password = pword;
  }
  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    int x = tags.getInteger("x");
    int y = tags.getInteger("y");
    int z = tags.getInteger("z");
    pos = new BlockPos(x, y, z);
    password = tags.getString("p");
  }
  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("x", pos.getX());
    tags.setInteger("y", pos.getY());
    tags.setInteger("z", pos.getZ());
    tags.setString("p", password);
    ByteBufUtils.writeTag(buf, tags);
  }
  @Override
  public IMessage onMessage(PacketTilePassword message, MessageContext ctx) {
    PacketTilePassword.checkThreadAndEnqueue(message, ctx);
    return null;
  }
  private static void checkThreadAndEnqueue(final PacketTilePassword message, final MessageContext ctx) {
    //copied in from my PacketSyncPlayerData
    IThreadListener thread = ModCyclic.proxy.getThreadFromContext(ctx);
    // pretty much copied straight from vanilla code, see {@link PacketThreadUtil#checkThreadAndEnqueue}
    thread.addScheduledTask(new Runnable() {
      public void run() {
        EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        World world = player.getEntityWorld();
        TileEntityPassword tile = (TileEntityPassword) world.getTileEntity(message.pos);
        if (tile != null) {
          tile.setMyPassword(message.password);
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
