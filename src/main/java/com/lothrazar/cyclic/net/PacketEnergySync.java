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

import com.lothrazar.cyclic.base.PacketBase;
import com.lothrazar.cyclic.base.TileEntityBase;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Forge docs suggest using a direct packet to keep capabilities, such as power, in sync with the client according to https://mcforge.readthedocs.io/en/latest/datastorage/capabilities/
 */
public class PacketEnergySync extends PacketBase {

  private BlockPos pos;
  private int energy;

  public PacketEnergySync(BlockPos p, int fluid) {
    pos = p;
    this.energy = fluid;
  }

  public static void handle(PacketEnergySync message, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      doWork(message);
    });
    message.done(ctx);
  }

  private static void doWork(PacketEnergySync message) {
    TileEntity te = Minecraft.getInstance().world.getTileEntity(message.pos);
    if (te instanceof TileEntityBase) {
      ((TileEntityBase) te).setEnergy(message.energy);
    }
  }

  public static PacketEnergySync decode(PacketBuffer buf) {
    PacketEnergySync msg = new PacketEnergySync(buf.readBlockPos(),
        buf.readInt());
    return msg;
  }

  public static void encode(PacketEnergySync msg, PacketBuffer buf) {
    buf.writeBlockPos(msg.pos);
    buf.writeInt(msg.energy);
  }
}
