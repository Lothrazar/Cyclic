package com.lothrazar.cyclic.item.random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import com.lothrazar.library.util.BlockUtil;
import com.lothrazar.library.util.ItemStackUtil;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import com.lothrazar.cyclic.ModCyclic;

public class PacketRandomize implements CustomPacketPayload {
  public static final CustomPacketPayload.Type<PacketRandomize> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "packet_randomize"));

  public static final StreamCodec<FriendlyByteBuf, PacketRandomize> STREAM_CODEC = StreamCodec.of(
      PacketRandomize::encode,
      PacketRandomize::decode
  );

  private static final Random RND = new Random();
  private BlockPos pos;
  private Direction side;
  private InteractionHand hand;

  public PacketRandomize(BlockPos pos, Direction side, InteractionHand hand) {
    this.pos = pos;
    this.side = side;
    this.hand = hand;
  }

  public static PacketRandomize decode(FriendlyByteBuf buf) {
    return new PacketRandomize(buf.readBlockPos(),
        Direction.values()[buf.readInt()],
        InteractionHand.values()[buf.readInt()]);
  }

  public static void encode(FriendlyByteBuf buf, PacketRandomize msg) {
    buf.writeBlockPos(msg.pos);
    buf.writeInt(msg.side.ordinal());
    buf.writeInt(msg.hand.ordinal());
  }

  @Override
  public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

  public static void handle(PacketRandomize message, IPayloadContext ctx) {
    ctx.enqueueWork(() -> {
      Player player = ctx.player();
      Level world = player.getCommandSenderWorld();
      List<BlockPos> places = RandomizerItem.getPlaces(message.pos, message.side);
      List<BlockPos> rpos = new ArrayList<>();
      List<BlockState> rstates = new ArrayList<>();
      //
      BlockState stateHere;
      boolean atLeastOne = false;
      for (BlockPos p : places) {
        stateHere = world.getBlockState(p);
        boolean canMove = RandomizerItem.canMove(stateHere, world, p);
        //        if (stateHere.getBlock().getBlockHardness(stateHere, world, p) < 0) {
        //          continue;//skip unbreakable
        //        }
        if (canMove) {
          //removed world.isSideSolid(p, message.side) && as it was blocking stairs/slabs from moving
          rpos.add(p);
          rstates.add(stateHere);
        }
      }
      Collections.shuffle(rpos, RND);
      BlockPos swapPos;
      BlockState swapState;
      synchronized (rpos) { //just in case
        for (int i = 0; i < rpos.size(); i++) {
          swapPos = rpos.get(i);
          swapState = rstates.get(i);
          world.destroyBlock(swapPos, false);
          //playing sound here in large areas causes ConcurrentModificationException
          if (BlockUtil.placeStateSafe(world, player, swapPos, swapState, false)) {
            atLeastOne = true;
          }
        }
      }
      if (atLeastOne) {
        ItemStackUtil.damageItem(player, player.getItemInHand(message.hand));
      }
    });
  }
}
