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

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.soundrecord.TileSoundRecorder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PacketRecordSound implements CustomPacketPayload {

  public static final CustomPacketPayload.Type<PacketRecordSound> TYPE = new Type<>(Identifier.fromNamespaceAndPath(ModCyclic.MODID, "packet_record_sound"));

  public static final StreamCodec<RegistryFriendlyByteBuf, PacketRecordSound> STREAM_CODEC = StreamCodec.of(PacketRecordSound::encode, PacketRecordSound::decode);


  @Override
  public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }


  private String sound;
  private BlockPos pos;

  public PacketRecordSound(String s, BlockPos n) {
    sound = s;
    pos = n;
  }

  public static PacketRecordSound decode(RegistryFriendlyByteBuf buf) {
    String s = buf.readUtf();
    CompoundTag tags = buf.readNbt();
    return new PacketRecordSound(s, new BlockPos(tags.getIntOr("x", 0), tags.getIntOr("y", 0), tags.getIntOr("z", 0)));
  }

  public static void encode(RegistryFriendlyByteBuf buf, PacketRecordSound msg) {
    buf.writeUtf(msg.sound);
    CompoundTag tags = new CompoundTag();
    tags.putInt("x", msg.pos.getX());
    tags.putInt("y", msg.pos.getY());
    tags.putInt("z", msg.pos.getZ());
    buf.writeNbt(tags);
  }

  public static void handle(PacketRecordSound message, IPayloadContext ctx) {
    ctx.enqueueWork(() -> {
      //rotate type
      ServerPlayer sender = (ServerPlayer) ctx.player();
      BlockEntity tile = sender.level().getBlockEntity(message.pos);
      if (tile instanceof TileSoundRecorder) {
        ((TileSoundRecorder) tile).onSoundHeard(message.sound);
      }
    });

  }
}
