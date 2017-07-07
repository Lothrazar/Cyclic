package com.lothrazar.cyclicmagic.component.builder;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketTileBuildType implements IMessage, IMessageHandler<PacketTileBuildType, IMessage> {
  private BlockPos pos;
  public PacketTileBuildType() {}
  public PacketTileBuildType(BlockPos p) {
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
  //  private boolean chat = false;
  @Override
  public IMessage onMessage(PacketTileBuildType message, MessageContext ctx) {
    EntityPlayerMP player = ctx.getServerHandler().playerEntity;
    TileEntityStructureBuilder tile = (TileEntityStructureBuilder) player.getEntityWorld().getTileEntity(message.pos);
    if (tile != null) {
      tile.toggleSizeShape();
      //      TileMachineStructureBuilder.BuildType old = tile.getBuildTypeEnum();
      //      TileMachineStructureBuilder.BuildType next = TileMachineStructureBuilder.BuildType.getNextType(old);
      //      tile.setBuildType(next.ordinal());
      //      tile.rebuildShape();
      tile.markDirty();
      if (player.openContainer != null) {
        player.openContainer.detectAndSendChanges();
        player.sendAllWindowProperties(player.openContainer, tile);
      }
      //      if (chat)
      //        UtilChat.addChatMessage(player, UtilChat.lang("buildertype." + next.name().toLowerCase() + ".name"));
    }
    return null;
  }
}
