package com.lothrazar.cyclicmagic.net;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.lothrazar.cyclicmagic.ICanToggleOnOff;
import com.lothrazar.cyclicmagic.item.tool.ItemToolRandomize;
import com.lothrazar.cyclicmagic.util.UtilPlaceBlocks;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketItemToggle implements IMessage, IMessageHandler<PacketItemToggle, IMessage> {
  private int slot;
  public PacketItemToggle() {}
  public PacketItemToggle(int itemSlot) {
    slot = itemSlot;
  }
  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    slot = tags.getInteger("slot");
  }
  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("slot", slot);
    ByteBufUtils.writeTag(buf, tags);
  }
  @Override
  public IMessage onMessage(PacketItemToggle message, MessageContext ctx) {
    if (ctx.side.isServer()) {
      EntityPlayer player = ctx.getServerHandler().playerEntity;

      //      System.out.println("message.slot "+message.slot);
      if (player.openContainer != null
          && player.openContainer.getSlot(message.slot) != null
          &&
          player.openContainer.getSlot(message.slot).getStack() != null) {
        ItemStack maybeCharm = player.openContainer.getSlot(message.slot).getStack();
        if (maybeCharm.getItem() instanceof ICanToggleOnOff) {
          //example: is a charm or something
          ICanToggleOnOff c = (ICanToggleOnOff) maybeCharm.getItem();
          c.toggleOnOff(maybeCharm);
        }
      }
    }
    return null;
  }
}
