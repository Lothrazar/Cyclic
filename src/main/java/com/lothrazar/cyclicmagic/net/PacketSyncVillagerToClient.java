package com.lothrazar.cyclicmagic.net;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.component.merchant.ContainerMerchantBetter;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketSyncVillagerToClient implements IMessage, IMessageHandler<PacketSyncVillagerToClient, IMessage> {
  private int career;
  private MerchantRecipeList trades;
  public PacketSyncVillagerToClient() {}
  public PacketSyncVillagerToClient(int h, MerchantRecipeList merchantrecipelist) {
    career = h;
    trades = merchantrecipelist;
  }
  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    career = tags.getInteger("h");
    NBTTagCompound tradeTag = (NBTTagCompound) tags.getTag("trades");
    trades = new MerchantRecipeList();
    trades.readRecipiesFromTags(tradeTag);
  }
  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("h", career);
    NBTTagCompound tradeTag = trades.getRecipiesAsTags();
    tags.setTag("trades", tradeTag);
    ByteBufUtils.writeTag(buf, tags);
  }
  @Override
  public IMessage onMessage(PacketSyncVillagerToClient message, MessageContext ctx) {
    if (ctx.side == Side.CLIENT) {
      EntityPlayer player = ModCyclic.proxy.getPlayerEntity(ctx);
      //      player.getEntityData().setInteger(Const.MODID + "_VILLAGERHACK", message.career);//TODO: validate/delete
      if (player != null && player.openContainer instanceof ContainerMerchantBetter) {
        //TODO: this spams every second, not sure why
        ContainerMerchantBetter c = (ContainerMerchantBetter) player.openContainer;
        c.setCareer(message.career);
        if (message.trades != null) {
          c.setTrades(message.trades);
        }
      }
    }
    return null;
  }
}
