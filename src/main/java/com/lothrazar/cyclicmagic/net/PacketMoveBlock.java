package com.lothrazar.cyclicmagic.net;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.item.ItemToolPush;
import com.lothrazar.cyclicmagic.item.ItemToolPush.ActionType;
import com.lothrazar.cyclicmagic.registry.SpellRegistry;
import com.lothrazar.cyclicmagic.spell.ISpell;
import com.lothrazar.cyclicmagic.spell.ISpellFromServer;
import com.lothrazar.cyclicmagic.util.UtilPlaceBlocks;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketMoveBlock  implements IMessage, IMessageHandler<PacketMoveBlock, IMessage> {
  private BlockPos pos;
  private ItemToolPush.ActionType type;
  private EnumFacing side;
  public PacketMoveBlock() {
  }
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
  public IMessage onMessage(PacketMoveBlock message, MessageContext ctx) {
    if (ctx.side.isServer() && message != null && message.pos != null) {
      EntityPlayer player = ctx.getServerHandler().playerEntity;
      World worldObj = player.worldObj;

      BlockPos resultPosition = null;

      boolean success = false;
      switch (message.type) {
      case PULL:
        resultPosition = UtilPlaceBlocks.pullBlock(worldObj, player, message.pos, message.side);
        success = resultPosition != null;
        break;
      case PUSH:
        resultPosition = UtilPlaceBlocks.pushBlock(worldObj, player, message.pos, message.side);
        success = resultPosition != null;
        break;
      case ROTATE:
        success = UtilPlaceBlocks.rotateBlockValidState(pos, worldObj, message.side, player);
        resultPosition = pos;
        break;
      default:
        break;
      }
    }
    return null;
  }
}
