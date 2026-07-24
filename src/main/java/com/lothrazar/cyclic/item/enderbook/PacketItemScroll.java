package com.lothrazar.cyclic.item.enderbook;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.registry.ItemRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PacketItemScroll implements CustomPacketPayload {

  public static final CustomPacketPayload.Type<PacketItemScroll> TYPE = new Type<>(Identifier.fromNamespaceAndPath(ModCyclic.MODID, "packet_item_scroll"));
  public static final StreamCodec<RegistryFriendlyByteBuf, PacketItemScroll> STREAM_CODEC = StreamCodec.of(PacketItemScroll::encode, PacketItemScroll::decode);

  private int slot;
  private boolean isDown;

  public PacketItemScroll() {}

  public PacketItemScroll(int slot, boolean isDown) {
    this.slot = slot;
    this.isDown = isDown;
  }

  @Override
  public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(PacketItemScroll message, IPayloadContext context) {
    context.enqueueWork(() -> {
      ServerPlayer player = (ServerPlayer) context.player();
      if (player != null) {
        player.getCooldowns().addCooldown(new ItemStack(ItemRegistry.ENDER_BOOK.get()), 5);
        EnderBookItem.scroll(player, message.slot, message.isDown);
      }
    });
  }

  public static PacketItemScroll decode(RegistryFriendlyByteBuf buf) {
    return new PacketItemScroll(buf.readInt(), buf.readBoolean());
  }

  public static void encode(RegistryFriendlyByteBuf buf, PacketItemScroll msg) {
    buf.writeInt(msg.slot);
    buf.writeBoolean(msg.isDown);
  }
}
