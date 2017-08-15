package com.lothrazar.cyclicmagic.component.merchant;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketVillagerTrade implements IMessage, IMessageHandler<PacketVillagerTrade, IMessage> {
  private static final String NBT_DUPE_BLOCKER = Const.MODID + "_iscurrentlytrading";
  private int selectedMerchantRecipe;
  public PacketVillagerTrade() {}
  public PacketVillagerTrade(int s) {
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
  public IMessage onMessage(PacketVillagerTrade message, MessageContext ctx) {
    if (ctx.side == Side.SERVER) {
      EntityPlayer player = ctx.getServerHandler().player;
      if (UtilNBT.getEntityBoolean(player, NBT_DUPE_BLOCKER)) {
        //once i got ConcurrentModificationException , this hopefully should fix it in theory. if its happneing where i think it is
        return null;
      } //dedupe fix: if player spams button, dont start second trade WHILE first is still processing
      UtilNBT.setEntityBoolean(player, NBT_DUPE_BLOCKER);
      try {
        if (player != null && player.openContainer instanceof ContainerMerchantBetter) {
          ContainerMerchantBetter c = (ContainerMerchantBetter) player.openContainer;
          c.setCurrentRecipeIndex(message.selectedMerchantRecipe); //TODO: well this duplicates packetsyncvilltoserv..so..
          c.doTrade(player, message.selectedMerchantRecipe);
        }
      }
      catch (Exception e) {
        ModCyclic.logger.error("Error trying to perform villager trade from Almanac: " + e.getMessage());
        e.printStackTrace();
      }
      finally {
        UtilNBT.setEntityBoolean(player, NBT_DUPE_BLOCKER, false);
      }
    }
    return null;
  }
}
