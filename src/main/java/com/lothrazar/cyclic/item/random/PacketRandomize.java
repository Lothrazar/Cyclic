package com.lothrazar.cyclic.item.random;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import com.lothrazar.cyclic.ModCyclic;
public class PacketRandomize implements CustomPacketPayload {
  public static final CustomPacketPayload.Type<PacketRandomize> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "packet_randomize"));
  public static final StreamCodec<FriendlyByteBuf, PacketRandomize> STREAM_CODEC = StreamCodec.of(PacketRandomize::encode, PacketRandomize::decode);
  @Override public CustomPacketPayload.Type<? extends CustomPacketPayload> type() { return TYPE; }
  private BlockPos pos;
  public PacketRandomize(BlockPos p) { pos = p; }
  public static PacketRandomize decode(FriendlyByteBuf buf) { return new PacketRandomize(buf.readBlockPos()); }
  public static void encode(FriendlyByteBuf buf, PacketRandomize msg) { buf.writeBlockPos(msg.pos); }
  public static void handle(PacketRandomize message, IPayloadContext ctx) {
    ctx.enqueueWork(() -> {
      ServerPlayer sender = (ServerPlayer) ctx.player();
    });
  }
}
