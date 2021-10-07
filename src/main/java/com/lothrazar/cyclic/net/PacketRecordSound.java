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
import com.lothrazar.cyclic.block.soundrecord.TileSoundRecorder;
import java.util.function.Supplier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketRecordSound extends PacketBase {

  private String sound;
  private BlockPos pos;

  public PacketRecordSound(String s, BlockPos n) {
    sound = s;
    pos = n;
  }

  public static PacketRecordSound decode(FriendlyByteBuf buf) {
    String s = buf.readUtf();
    CompoundTag tags = buf.readNbt();
    return new PacketRecordSound(s, new BlockPos(tags.getInt("x"), tags.getInt("y"), tags.getInt("z")));
  }

  public static void encode(PacketRecordSound msg, FriendlyByteBuf buf) {
    buf.writeUtf(msg.sound);
    CompoundTag tags = new CompoundTag();
    tags.putInt("x", msg.pos.getX());
    tags.putInt("y", msg.pos.getY());
    tags.putInt("z", msg.pos.getZ());
    buf.writeNbt(tags);
  }

  public static void handle(PacketRecordSound message, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      //rotate type
      ServerPlayer sender = ctx.get().getSender();
      BlockEntity tile = sender.level.getBlockEntity(message.pos);
      if (tile instanceof TileSoundRecorder) {
        ((TileSoundRecorder) tile).onSoundHeard(message.sound);
      }
    });
    message.done(ctx);
  }
}
