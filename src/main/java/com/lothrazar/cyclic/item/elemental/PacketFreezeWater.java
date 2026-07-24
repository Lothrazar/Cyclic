package com.lothrazar.cyclic.item.elemental;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.library.util.ItemStackUtil;
import com.lothrazar.library.util.SoundUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PacketFreezeWater implements CustomPacketPayload {

  public static final Type<PacketFreezeWater> TYPE = new Type<>(
      Identifier.fromNamespaceAndPath(ModCyclic.MODID, "packet_freeze_water"));

  public static final StreamCodec<FriendlyByteBuf, PacketFreezeWater> STREAM_CODEC = StreamCodec.of(
      PacketFreezeWater::encode,
      PacketFreezeWater::decode);

  final BlockPos pos;
  final Direction side;
  final InteractionHand hand;

  public PacketFreezeWater(BlockPos pos, Direction side, InteractionHand hand) {
    this.pos = pos;
    this.side = side;
    this.hand = hand;
  }

  public static PacketFreezeWater decode(FriendlyByteBuf buf) {
    return new PacketFreezeWater(
        buf.readBlockPos(),
        Direction.values()[buf.readInt()],
        InteractionHand.values()[buf.readInt()]);
  }

  public static void encode(FriendlyByteBuf buf, PacketFreezeWater msg) {
    buf.writeBlockPos(msg.pos);
    buf.writeInt(msg.side != null ? msg.side.ordinal() : 0);
    buf.writeInt(msg.hand != null ? msg.hand.ordinal() : 0);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(PacketFreezeWater msg, IPayloadContext ctx) {
    ctx.enqueueWork(() -> {
      var player = ctx.player();
      BlockPos center = msg.side != null ? msg.pos.relative(msg.side) : msg.pos;
      if (IceWand.spreadWaterFromCenter(player.level(), center)) {
        SoundUtil.playSound(player, Blocks.ICE.defaultBlockState().getSoundType().getBreakSound());
        ItemStackUtil.damageItem(player, player.getItemInHand(msg.hand));
      }
    });
  }
}
