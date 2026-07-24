package com.lothrazar.cyclic.item.datacard.fluid;

import com.lothrazar.cyclic.ModCyclic;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PacketFluidFilterCard implements CustomPacketPayload {

  public static final int TOGGLE_IGNORE = 0;
  public static final int TOGGLE_TAGMATCH = 1;
  public static final CustomPacketPayload.Type<PacketFluidFilterCard> TYPE = new Type<>(Identifier.fromNamespaceAndPath(ModCyclic.MODID, "packet_fluid_filter_card"));
  public static final StreamCodec<FriendlyByteBuf, PacketFluidFilterCard> STREAM_CODEC = StreamCodec.of(PacketFluidFilterCard::encode, PacketFluidFilterCard::decode);

  @Override
  public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  private int toggleType;

  public PacketFluidFilterCard(int toggleType) {
    this.toggleType = toggleType;
  }

  public static PacketFluidFilterCard decode(FriendlyByteBuf buf) {
    return new PacketFluidFilterCard(buf.readInt());
  }

  public static void encode(FriendlyByteBuf buf, PacketFluidFilterCard msg) {
    buf.writeInt(msg.toggleType);
  }

  public static void handle(PacketFluidFilterCard message, IPayloadContext ctx) {
    ctx.enqueueWork(() -> {
      ServerPlayer sender = (ServerPlayer) ctx.player();
      ItemStack filter = sender.getItemInHand(InteractionHand.MAIN_HAND);
      if (!(filter.getItem() instanceof FluidFilterCardItem)) {
        filter = sender.getItemInHand(InteractionHand.OFF_HAND);
      }
      if (!(filter.getItem() instanceof FluidFilterCardItem)) {
        return;
      }
      switch (message.toggleType) {
        case TOGGLE_TAGMATCH:
          FluidFilterCardItem.toggleTagMatch(filter);
          break;
        case TOGGLE_IGNORE:
        default:
          FluidFilterCardItem.toggleFilterType(filter);
          break;
      }
    });
  }
}
