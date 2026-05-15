package com.lothrazar.cyclic.item.crafting;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import com.lothrazar.cyclic.ModCyclic;

public class PacketItemGui implements CustomPacketPayload {

    public static final Type<PacketItemGui> ID = new Type<>(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "packet_item_gui"));

    public static final StreamCodec<FriendlyByteBuf, PacketItemGui> STREAM_CODEC = StreamCodec.ofMember(
        PacketItemGui::write,
        PacketItemGui::new
    );

    public int slot;
    public net.minecraft.world.item.Item item;

    public PacketItemGui(int slot, net.minecraft.world.item.Item item) {
        this.slot = slot;
        this.item = item;
    }

    public PacketItemGui(FriendlyByteBuf buf) {
        this.slot = buf.readInt();
        this.item = net.minecraft.world.item.Items.AIR;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(slot);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
