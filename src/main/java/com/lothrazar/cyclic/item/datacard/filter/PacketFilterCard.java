package com.lothrazar.cyclic.item.datacard.filter;
import com.lothrazar.cyclic.data.CraftingActionEnum;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
public class PacketFilterCard implements net.minecraft.network.protocol.common.custom.CustomPacketPayload {
  public static final net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<PacketFilterCard> TYPE = new Type<>(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(com.lothrazar.cyclic.ModCyclic.MODID, "packet_filter_card"));
  public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.FriendlyByteBuf, PacketFilterCard> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.of(PacketFilterCard::encode, PacketFilterCard::decode);
  @Override public net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<? extends net.minecraft.network.protocol.common.custom.CustomPacketPayload> type() { return TYPE; }
  private CraftingActionEnum action;
  public PacketFilterCard(CraftingActionEnum s) { action = s; }
  public static PacketFilterCard decode(FriendlyByteBuf buf) { return new PacketFilterCard(CraftingActionEnum.values()[buf.readInt()]); }
  public static void encode(net.minecraft.network.FriendlyByteBuf buf, PacketFilterCard msg) { buf.writeInt(msg.action.ordinal()); }
  public static void handle(PacketFilterCard message, net.neoforged.neoforge.network.handling.IPayloadContext ctx) {
    ctx.enqueueWork(() -> {
      ServerPlayer sender = (ServerPlayer) ctx.player();
      ItemStack filter = sender.getItemInHand(InteractionHand.MAIN_HAND);
      FilterCardItem.toggleFilterType(filter);
    });
  }
}
