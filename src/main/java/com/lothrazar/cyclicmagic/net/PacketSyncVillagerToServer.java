package com.lothrazar.cyclicmagic.net;
import com.lothrazar.cyclicmagic.gui.villager.ContainerMerchantBetter;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketSyncVillagerToServer implements IMessage, IMessageHandler<PacketSyncVillagerToServer, IMessage> {
  private int selectedMerchantRecipe;
  public PacketSyncVillagerToServer() {}
  public PacketSyncVillagerToServer(int s) {
    selectedMerchantRecipe = s;
  }
  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    selectedMerchantRecipe = tags.getInteger("selectedMerchantRecipe");
  }
  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("selectedMerchantRecipe", selectedMerchantRecipe);
    ByteBufUtils.writeTag(buf, tags);
  }
  @Override
  public IMessage onMessage(PacketSyncVillagerToServer message, MessageContext ctx) {
    if (ctx.side == Side.SERVER) {
      EntityPlayer player = ctx.getServerHandler().playerEntity;// ModCyclic.proxy.getPlayerEntity(ctx);
      if (player != null && player.openContainer instanceof ContainerMerchantBetter) {
        ContainerMerchantBetter c = (ContainerMerchantBetter) player.openContainer;
        c.setCurrentRecipeIndex(message.selectedMerchantRecipe);
      }
    }
    return null;
  }
}
