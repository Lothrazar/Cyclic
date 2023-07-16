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
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.library.packet.PacketFlib;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkEvent;

public class PacketFluidSync extends PacketFlib {

  private BlockPos pos;
  private FluidStack fluid;

  public PacketFluidSync(BlockPos p, FluidStack fluid) {
    pos = p;
    this.fluid = fluid;
  }

  public static void handle(PacketFluidSync message, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      doWork(message);
    });
    message.done(ctx);
  }

  private static void doWork(PacketFluidSync message) {
    BlockEntity te = Minecraft.getInstance().level.getBlockEntity(message.pos);
    if (te instanceof TileBlockEntityCyclic) {
      ((TileBlockEntityCyclic) te).setFluid(message.fluid);
    }
  }

  public static PacketFluidSync decode(FriendlyByteBuf buf) {
    PacketFluidSync msg = new PacketFluidSync(buf.readBlockPos(),
        FluidStack.loadFluidStackFromNBT(buf.readNbt()));
    return msg;
  }

  public static void encode(PacketFluidSync msg, FriendlyByteBuf buf) {
    buf.writeBlockPos(msg.pos);
    CompoundTag tags = new CompoundTag();
    if (msg.fluid != null) {
      msg.fluid.writeToNBT(tags);
    }
    buf.writeNbt(tags);
  }
}
