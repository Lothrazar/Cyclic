package com.lothrazar.cyclicmagic.component.fluidstorage;
import com.lothrazar.cyclicmagic.ModCyclic;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketFluidSync implements IMessage, IMessageHandler<PacketFluidSync, IMessage> {
  private BlockPos pos;
  public FluidStack fluid;
  public PacketFluidSync() {}
  public PacketFluidSync(BlockPos p, FluidStack fluid) {
    pos = p;
    this.fluid = fluid;
  }
  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    int x = tags.getInteger("x");
    int y = tags.getInteger("y");
    int z = tags.getInteger("z");
    pos = new BlockPos(x, y, z);
    fluid = FluidStack.loadFluidStackFromNBT(tags);
  }
  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("x", pos.getX());
    tags.setInteger("y", pos.getY());
    tags.setInteger("z", pos.getZ());
    if (fluid != null) {
      fluid.writeToNBT(tags);
    }
    ByteBufUtils.writeTag(buf, tags);
  }
  @Override
  public IMessage onMessage(PacketFluidSync message, MessageContext ctx) {
    if (ctx.side == Side.CLIENT) {
      EntityPlayer p = ModCyclic.proxy.getPlayerEntity(ctx);
      if (p != null) {
        TileEntity te = p.world.getTileEntity(message.pos);
        if (te instanceof TileEntityBucketStorage) {
          ((TileEntityBucketStorage) te).updateFluidTo(message.fluid);
        }
      }
    }
    return null;
  }
}
