package com.lothrazar.cyclic.net;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.config.ClientConfigCyclic;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Tells a single client to display a fluid-status message in their action bar.
 *
 * Why a packet: the original {@code displayClientFluidMessage} ran server-side and gated the
 * call on a CLIENT-type ModConfigSpec ({@link ClientConfigCyclic#FLUID_BLOCK_STATUS}). On a
 * dedicated server that config is never loaded, so calling {@code .get()} throws
 * {@code IllegalStateException: Cannot get config value before config is loaded.}, killing
 * the packet handler. Pushing the display decision to the client side fixes that *and* makes
 * the toggle a real per-client preference (instead of being shared with the server JVM).
 */
public class PacketDisplayFluidMessage implements CustomPacketPayload {

  public static final CustomPacketPayload.Type<PacketDisplayFluidMessage> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "packet_display_fluid_message"));
  public static final StreamCodec<RegistryFriendlyByteBuf, PacketDisplayFluidMessage> STREAM_CODEC = StreamCodec.of(PacketDisplayFluidMessage::encode, PacketDisplayFluidMessage::decode);

  private String translationKey;

  public PacketDisplayFluidMessage(String translationKey) {
    this.translationKey = translationKey;
  }

  public PacketDisplayFluidMessage() {}

  @Override
  public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(PacketDisplayFluidMessage message, IPayloadContext ctx) {
    ctx.enqueueWork(() -> {
      //runs on client thread: safe to read CLIENT-type config and call displayClientMessage
      if (!ClientConfigCyclic.FLUID_BLOCK_STATUS.get()) {
        return;
      }
      var player = ctx.player();
      if (player != null) {
        player.displayClientMessage(Component.translatable(message.translationKey), true);
      }
    });
  }

  public static PacketDisplayFluidMessage decode(RegistryFriendlyByteBuf buf) {
    PacketDisplayFluidMessage p = new PacketDisplayFluidMessage();
    p.translationKey = buf.readUtf(32767);
    return p;
  }

  public static void encode(RegistryFriendlyByteBuf buf, PacketDisplayFluidMessage msg) {
    buf.writeUtf(msg.translationKey);
  }
}
