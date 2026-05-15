package com.lothrazar.cyclic.item.builder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import com.lothrazar.cyclic.ModCyclic;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PacketSwapBlock implements CustomPacketPayload {

    public static final Type<PacketSwapBlock> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "packet_swap_block"));

    public static final StreamCodec<FriendlyByteBuf, PacketSwapBlock> STREAM_CODEC = StreamCodec.of(
        PacketSwapBlock::encode,
        PacketSwapBlock::decode
    );

    public BlockPos pos;
    public BuilderActionType actionType;
    public Direction side;
    public InteractionHand hand;

    public PacketSwapBlock(BlockPos pos, BuilderActionType actionType, Direction side, InteractionHand hand) {
        this.pos = pos;
        this.actionType = actionType;
        this.side = side;
        this.hand = hand;
    }

    public static PacketSwapBlock decode(FriendlyByteBuf buf) {
        return new PacketSwapBlock(
            buf.readBlockPos(),
            BuilderActionType.values()[buf.readInt()],
            Direction.values()[buf.readInt()],
            InteractionHand.values()[buf.readInt()]
        );
    }

    public static void encode(FriendlyByteBuf buf, PacketSwapBlock msg) {
        buf.writeBlockPos(msg.pos);
        buf.writeInt(msg.actionType.ordinal());
        buf.writeInt(msg.side != null ? msg.side.ordinal() : 0);
        buf.writeInt(msg.hand != null ? msg.hand.ordinal() : 0);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(PacketSwapBlock message, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            // Stubbed payload handler
        });
    }
}
