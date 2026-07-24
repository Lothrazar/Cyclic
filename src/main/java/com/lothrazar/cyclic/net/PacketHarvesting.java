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

import com.lothrazar.cyclic.util.HarvestUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import com.lothrazar.cyclic.ModCyclic;

public class PacketHarvesting implements CustomPacketPayload {

  public static final CustomPacketPayload.Type<PacketHarvesting> TYPE = new Type<>(Identifier.fromNamespaceAndPath(ModCyclic.MODID, "packet_harvesting"));

  public static final StreamCodec<RegistryFriendlyByteBuf, PacketHarvesting> STREAM_CODEC = StreamCodec.of(PacketHarvesting::encode, PacketHarvesting::decode);


  @Override
  public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }


  BlockPos pos;
  int radius;

  public PacketHarvesting(BlockPos pos, int radius) {
    this.pos = pos;
    this.radius = radius;
  }

  public static PacketHarvesting decode(RegistryFriendlyByteBuf buf) {
    CompoundTag tags = buf.readNbt();
    return new PacketHarvesting(new BlockPos(tags.getIntOr("x", 0), tags.getInt("y"), tags.getInt("z")), buf.readInt());
  }

  public static void encode(RegistryFriendlyByteBuf buf, PacketHarvesting msg) {
    CompoundTag tags = new CompoundTag();
    tags.putInt("x", msg.pos.getX());
    tags.putInt("y", msg.pos.getY());
    tags.putInt("z", msg.pos.getZ());
    buf.writeNbt(tags);
    buf.writeInt(msg.radius);
  }

  public static void handle(PacketHarvesting message, IPayloadContext ctx) {
    ctx.enqueueWork(() -> {
      ServerPlayer sender = (ServerPlayer) ctx.player();
      HarvestUtil.harvestShape(sender.level(), message.pos, message.radius);
    });
    
  }
}
