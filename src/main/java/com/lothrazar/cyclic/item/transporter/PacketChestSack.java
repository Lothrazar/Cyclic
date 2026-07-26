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
package com.lothrazar.cyclic.item.transporter;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import com.lothrazar.cyclic.ModCyclic;

public class PacketChestSack implements CustomPacketPayload {

  public static final CustomPacketPayload.Type<PacketChestSack> TYPE = new Type<>(Identifier.fromNamespaceAndPath(ModCyclic.MODID, "packet_chest_sack"));

  public static final StreamCodec<FriendlyByteBuf, PacketChestSack> STREAM_CODEC = StreamCodec.of(PacketChestSack::encode, PacketChestSack::decode);


  @Override
  public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }


  private BlockPos pos;

  public PacketChestSack(BlockPos mouseover) {
    pos = mouseover;
  }

  public static PacketChestSack decode(FriendlyByteBuf buf) {
    return new PacketChestSack(buf.readBlockPos());
  }

  public static void encode(FriendlyByteBuf buf, PacketChestSack msg) {
    buf.writeBlockPos(msg.pos);
  }

  public static void handle(PacketChestSack message, IPayloadContext ctx) {
    ctx.enqueueWork(() -> {
      ServerPlayer player = (ServerPlayer) ctx.player();
      BlockPos position = message.pos;
      Level world = player.level();
      BlockEntity tile = world.getBlockEntity(position);
      TileTransporterEmptyItem.gatherTileEntity(position, player, world, tile);
    });
    
  }
}
