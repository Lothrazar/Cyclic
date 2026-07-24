package com.lothrazar.cyclic.item.elemental;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.library.util.EntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PacketWaterFlow implements CustomPacketPayload {

  public static final Type<PacketWaterFlow> TYPE = new Type<>(
      Identifier.fromNamespaceAndPath(ModCyclic.MODID, "packet_water_flow"));

  public static final StreamCodec<FriendlyByteBuf, PacketWaterFlow> STREAM_CODEC = StreamCodec.of(
      PacketWaterFlow::encode,
      PacketWaterFlow::decode);

  final BlockPos pos;
  final InteractionHand hand;

  public PacketWaterFlow(BlockPos pos, InteractionHand hand) {
    this.pos = pos;
    this.hand = hand;
  }

  public static PacketWaterFlow decode(FriendlyByteBuf buf) {
    return new PacketWaterFlow(
        buf.readBlockPos(),
        InteractionHand.values()[buf.readInt()]);
  }

  public static void encode(FriendlyByteBuf buf, PacketWaterFlow msg) {
    buf.writeBlockPos(msg.pos);
    buf.writeInt(msg.hand != null ? msg.hand.ordinal() : 0);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(PacketWaterFlow msg, IPayloadContext ctx) {
    ctx.enqueueWork(() -> {
      var player = ctx.player();
      if (WaterSpreaderItem.spreadWaterFromCenter(player.level(), msg.pos)) {
        EntityUtil.setCooldownItem(player, player.getItemInHand(msg.hand).getItem(), WaterSpreaderItem.COOLDOWN);
        player.swing(msg.hand);
      }
    });
  }
}
