/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclic.net;

import java.util.function.Supplier;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.tank.TileTank;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketFluidSync {

  private BlockPos pos;
  public FluidStack fluid;

  public PacketFluidSync() {}

  public PacketFluidSync(BlockPos p, FluidStack fluid) {
    pos = p;
    this.fluid = fluid;
  }

  public static void handle(PacketFluidSync message, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      PlayerEntity player = ModCyclic.proxy.getClientPlayer();
      TileEntity te = player.world.getTileEntity(message.pos);
      if (te instanceof TileTank) {
        ((TileTank) te).setFluid(message.fluid);
      }
      //      @Override
      //      public IMessage onMessage(PacketFluidSync message, MessageContext ctx) {
      //        if (ctx.side == Side.CLIENT) {
      //          EntityPlayer p = ModCyclic.proxy.getPlayerEntity(ctx);
      //          if (p != null) {
      //            TileEntity te = p.world.getTileEntity(message.pos);
      //            if (te instanceof TileEntityBaseMachineFluid) {
      //              ((TileEntityBaseMachineFluid) te).updateFluidTo(message.fluid);
      //            }
      //          }
      //        }
      //        return null;
      //      }
      //      ServerPlayerEntity player = ctx.get().getSender();
      //      if (player.openContainer == null) {
      //        return;
      //      }
      //      int scount = player.openContainer.inventorySlots.size();
      //      //this is an edge case but it DID happen: put charmin your hotbar and then open a creative inventory tab. avoid index OOB
      //      if (message.slot >= scount) {
      //        //will NOT work in creative mode. slots are messed up
      //        return;
      //      }
      //      Slot slotObj = player.openContainer.getSlot(message.slot);
      //      ModCyclic.LOGGER.error(message.slot + " slotObjslotObj stack" + slotObj.getStack());
      //      if (slotObj != null
      //          && !slotObj.getStack().isEmpty()) {
      //        ModCyclic.LOGGER.error("packetde " + slotObj.getStack());
      //        ItemStack maybeCharm = slotObj.getStack();
      //        if (maybeCharm.getItem() instanceof IHasClickToggle) {
      //          //example: is a charm or something
      //          IHasClickToggle c = (IHasClickToggle) maybeCharm.getItem();
      //          c.toggle(player, maybeCharm);
      //        }
      //      }
    });
  }

  public static PacketFluidSync decode(PacketBuffer buf) {
    PacketFluidSync msg = new PacketFluidSync();
    CompoundNBT tags = buf.readCompoundTag();
    msg.pos = new BlockPos(tags.getInt("x"), tags.getInt("y"), tags.getInt("z"));
    tags = buf.readCompoundTag();
    msg.fluid = FluidStack.loadFluidStackFromNBT(tags);
    return msg;
  }

  public static void encode(PacketFluidSync msg, PacketBuffer buf) {
    CompoundNBT tags = new CompoundNBT();
    tags.putInt("x", msg.pos.getX());
    tags.putInt("y", msg.pos.getY());
    tags.putInt("z", msg.pos.getZ());
    buf.writeCompoundTag(tags);
    tags = new CompoundNBT();
    if (msg.fluid != null)
      msg.fluid.writeToNBT(tags);
    buf.writeCompoundTag(tags);
  }
  //  @Override
  //  public void fromBytes(ByteBuf buf) {
  //    NBTTagCompound tags = ByteBufUtils.readTag(buf);
  //    int x = tags.getInteger("x");
  //    int y = tags.getInteger("y");
  //    int z = tags.getInteger("z");
  //    pos = new BlockPos(x, y, z);
  //    fluid = FluidStack.loadFluidStackFromNBT(tags);
  //  }
  //
  //  @Override
  //  public void toBytes(ByteBuf buf) {
  //    NBTTagCompound tags = new NBTTagCompound();
  //    tags.setInteger("x", pos.getX());
  //    tags.setInteger("y", pos.getY());
  //    tags.setInteger("z", pos.getZ());
  //    if (fluid != null) {
  //      fluid.writeToNBT(tags);
  //    }
  //    ByteBufUtils.writeTag(buf, tags);
  //  }
}
