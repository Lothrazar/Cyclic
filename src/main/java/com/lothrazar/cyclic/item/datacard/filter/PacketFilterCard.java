package com.lothrazar.cyclic.item.datacard.filter;

import com.lothrazar.cyclic.ModCyclic;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PacketFilterCard implements CustomPacketPayload {
  public static final int TOGGLE_IGNORE = 0;
  public static final int TOGGLE_TAGMATCH = 1;
  public static final CustomPacketPayload.Type<PacketFilterCard> TYPE = new Type<>(Identifier.fromNamespaceAndPath(ModCyclic.MODID, "packet_filter_card"));
  public static final StreamCodec<FriendlyByteBuf, PacketFilterCard> STREAM_CODEC = StreamCodec.of(PacketFilterCard::encode, PacketFilterCard::decode);

  @Override
  public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  private int toggleType;

  public PacketFilterCard(int toggleType) {
    this.toggleType = toggleType;
  }

  public static PacketFilterCard decode(FriendlyByteBuf buf) {
    return new PacketFilterCard(buf.readInt());
  }

  public static void encode(FriendlyByteBuf buf, PacketFilterCard msg) {
    buf.writeInt(msg.toggleType);
  }

  public static void handle(PacketFilterCard message, IPayloadContext ctx) {
    ctx.enqueueWork(() -> {
      ServerPlayer sender = (ServerPlayer) ctx.player();
      ItemStack filter = sender.getItemInHand(InteractionHand.MAIN_HAND);
      if (!(filter.getItem() instanceof FilterCardItem)) {
        filter = sender.getItemInHand(InteractionHand.OFF_HAND);
      }
      if (!(filter.getItem() instanceof FilterCardItem)) {
        return;
      }
      switch (message.toggleType) {
        case TOGGLE_TAGMATCH:
          FilterCardItem.toggleTagMatch(filter);
          break;
        case TOGGLE_IGNORE:
        default:
          FilterCardItem.toggleFilterType(filter);
          break;
      }
    });
  }
}
