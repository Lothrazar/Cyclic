package com.lothrazar.cyclicmagic.component.pylonexp;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilExperience;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketTilePylon implements IMessage, IMessageHandler<PacketTilePylon, IMessage> {
  private BlockPos pos;
  private int value;
  private TileEntityXpPylon.Fields type;
  public PacketTilePylon() {}
  public PacketTilePylon(BlockPos p, int s, TileEntityXpPylon.Fields spr) {
    pos = p;
    value = s;
    type = spr;
  }
  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    int x = tags.getInteger("x");
    int y = tags.getInteger("y");
    int z = tags.getInteger("z");
    pos = new BlockPos(x, y, z);
    value = tags.getInteger("value");
    type = TileEntityXpPylon.Fields.values()[tags.getInteger("t")];
  }
  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("x", pos.getX());
    tags.setInteger("y", pos.getY());
    tags.setInteger("z", pos.getZ());
    tags.setInteger("value", value);
    tags.setInteger("t", type.ordinal());
    ByteBufUtils.writeTag(buf, tags);
  }
  @Override
  public IMessage onMessage(PacketTilePylon message, MessageContext ctx) {
    EntityPlayerMP player = ctx.getServerHandler().player;
    TileEntityXpPylon tile = (TileEntityXpPylon) player.getEntityWorld().getTileEntity(message.pos);
    if (tile != null) {
      int pylonHas = tile.getField(message.type.ordinal());
      int pylonSpace = TileEntityXpPylon.TANK_FULL - pylonHas;
      if (message.type.ordinal() == TileEntityXpPylon.Fields.EXP.ordinal()) { //actually this is a deposit from the player
        int playerHas = (int) Math.floor(UtilExperience.getExpTotal(player));
         
        if (message.value >= 0) {
          int toDeposit;
          if (message.value == 0) {
            //deposit all
            toDeposit = Math.min(playerHas, pylonSpace) ;
          }
          else {//try deposit specified amt
            toDeposit = Math.min(message.value, pylonSpace);
          }
          if (pylonHas + toDeposit <= TileEntityXpPylon.TANK_FULL) {//is it full
            if (UtilExperience.drainExp(player, toDeposit)) {//does player have enough
              //then deposit that much into it if drain worked
              tile.setField(message.type.ordinal(), pylonHas + toDeposit);
            }
            else { //  not enouh
              UtilChat.addChatMessage(player, "tile.exp_pylon.notenough");
            }
          }
          else { //  full
            UtilChat.addChatMessage(player, "tile.exp_pylon.full");
          }
        }
        else { // so message.value < 0
          // so DRAIN FROM PYLON, add to PLAYER. BUT only if PYLON has enough
          int toDrain = message.value * -1;
          if (pylonHas >= toDrain) {
            tile.setField(message.type.ordinal(), pylonHas - toDrain);
            UtilExperience.incrementExp(player, toDrain);
          }
        }
      }
      else {//normal field toggle/value will be + or 1 something so increment by that
        tile.setField(message.type.ordinal(), pylonHas + message.value);
      }
      tile.markDirty();
      if (player.openContainer != null) {
        player.openContainer.detectAndSendChanges();
        player.sendAllWindowProperties(player.openContainer, tile);
      }
    }
    return null;
  }
}
