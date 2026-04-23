package com.lothrazar.cyclic.item.random;
import com.lothrazar.cyclic.data.CraftingActionEnum;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
public class PacketRandomize implements net.minecraft.network.protocol.common.custom.CustomPacketPayload {
  public static final net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<PacketRandomize> TYPE = new Type<>(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(com.lothrazar.cyclic.ModCyclic.MODID, "packet_randomize"));
  public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.FriendlyByteBuf, PacketRandomize> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.of(PacketRandomize::encode, PacketRandomize::decode);
  @Override public net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<? extends net.minecraft.network.protocol.common.custom.CustomPacketPayload> type() { return TYPE; }
  private BlockPos pos;
  public PacketRandomize(BlockPos p) { pos = p; }
  public static PacketRandomize decode(FriendlyByteBuf buf) { return new PacketRandomize(buf.readBlockPos()); }
  public static void encode(net.minecraft.network.FriendlyByteBuf buf, PacketRandomize msg) { buf.writeBlockPos(msg.pos); }
  public static void handle(PacketRandomize message, net.neoforged.neoforge.network.handling.IPayloadContext ctx) {
    ctx.enqueueWork(() -> {
      ServerPlayer sender = (ServerPlayer) ctx.player();
    });
  }
}
