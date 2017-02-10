package com.lothrazar.cyclicmagic.net;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.gui.villager.ContainerMerchantBetter;
import com.lothrazar.cyclicmagic.util.Const;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketSyncVillager implements IMessage, IMessageHandler<PacketSyncVillager, IMessage> {
  private int career;
  public PacketSyncVillager() {}
  public PacketSyncVillager(int h) {
    career = h;
  }
  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    career = tags.getInteger("h");
  }
  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("h", career);
    ByteBufUtils.writeTag(buf, tags);
  }
  @Override
  public IMessage onMessage(PacketSyncVillager message, MessageContext ctx) {
    if (ctx.side == Side.CLIENT) {
      EntityPlayer player = ModCyclic.proxy.getPlayerEntity(ctx);
      System.out.println("PSV "+message.career);
      System.out.println("class "+ player.openContainer);
      player.getEntityData().setInteger(Const.MODID+"_VILLAGERHACK", message.career);
      if (player != null && player.openContainer instanceof ContainerMerchantBetter) {
        ContainerMerchantBetter c=(ContainerMerchantBetter)player.openContainer ;
  
        
 c.setCareer(message.career);
        
      } 
    }
    return null;
  }
}
