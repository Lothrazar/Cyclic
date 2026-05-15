package com.lothrazar.cyclic.item.datacard.filter;
import com.lothrazar.cyclic.data.CraftingActionEnum;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
public class PacketFilterCard implements CustomPacketPayload {
  public static final CustomPacketPayload.Type<PacketFilterCard> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(com.lothrazar.cyclic.ModCyclic.MODID, "packet_filter_card"));
  public static final StreamCodec<FriendlyByteBuf, PacketFilterCard> STREAM_CODEC = StreamCodec.of(PacketFilterCard::encode, PacketFilterCard::decode);
  @Override public CustomPacketPayload.Type<? extends CustomPacketPayload> type() { return TYPE; }
  private CraftingActionEnum action;
  public PacketFilterCard(CraftingActionEnum s) { action = s; }
  public static PacketFilterCard decode(FriendlyByteBuf buf) { return new PacketFilterCard(CraftingActionEnum.values()[buf.readInt()]); }
  public static void encode(FriendlyByteBuf buf, PacketFilterCard msg) { buf.writeInt(msg.action.ordinal()); }
  public static void handle(PacketFilterCard message, IPayloadContext ctx) {
    ctx.enqueueWork(() -> {
      ServerPlayer sender = (ServerPlayer) ctx.player();
      ItemStack filter = sender.getItemInHand(InteractionHand.MAIN_HAND);
      FilterCardItem.toggleFilterType(filter);
    });
  }
}
