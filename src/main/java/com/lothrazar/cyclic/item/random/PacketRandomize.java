package com.lothrazar.cyclic.item.random;
import com.lothrazar.cyclic.util.CapabilityUtil;
import com.lothrazar.library.util.BlockUtil;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import com.lothrazar.cyclic.ModCyclic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PacketRandomize implements CustomPacketPayload {
  public static final CustomPacketPayload.Type<PacketRandomize> TYPE = new Type<>(Identifier.fromNamespaceAndPath(ModCyclic.MODID, "packet_randomize"));
  public static final StreamCodec<RegistryFriendlyByteBuf, PacketRandomize> STREAM_CODEC =
      StreamCodec.of(PacketRandomize::encode, PacketRandomize::decode);

  private static final Random RND = new Random();

  @Override public CustomPacketPayload.Type<? extends CustomPacketPayload> type() { return TYPE; }
  private BlockPos pos;
  private Direction side;
  private InteractionHand hand;

  public PacketRandomize(BlockPos pos, Direction side, InteractionHand h) {
    this.pos = pos;
    this.side = side;
    hand = h;
  }
  public static PacketRandomize decode(RegistryFriendlyByteBuf buf) {
    PacketRandomize p = new PacketRandomize(buf.readBlockPos(),
        Direction.values()[buf.readInt()],
        InteractionHand.values()[buf.readInt()]);
    return p;


  }
  public static void encode(RegistryFriendlyByteBuf buf, PacketRandomize msg) {
    buf.writeBlockPos(msg.pos);
    buf.writeInt(msg.side.ordinal());
    buf.writeInt(msg.hand.ordinal());
  }
  public static void handle(PacketRandomize message, IPayloadContext ctx) {
    ctx.enqueueWork(() -> {
      var player = ctx.player();

      Level world = player.level();
      ItemStack held = player.getItemInHand(message.hand);
      IEnergyStorage storage = CapabilityUtil.energy(held);
      final int cost = RandomizerItem.COST.get();
      if (storage == null || storage.extractEnergy(cost, true) < cost) {
        return;
      }
      List<BlockPos> places = RandomizerItem.getPlaces(message.pos, message.side, held);
      List<BlockPos> rpos = new ArrayList<BlockPos>();
      List<BlockState> rstates = new ArrayList<BlockState>();
      //
      BlockState stateHere;
      for (BlockPos p : places) {
        stateHere = world.getBlockState(p);
        boolean canMove = RandomizerItem.canMove(stateHere, world, p);

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
          if (storage.extractEnergy(cost, true) < cost) {
            break;
          }
          swapPos = rpos.get(i);
          swapState = rstates.get(i);
          world.destroyBlock(swapPos, false);
          //playing sound here in large areas causes ConcurrentModificationException
          if (BlockUtil.placeStateSafe(world, player, swapPos, swapState, false)) {
            storage.extractEnergy(cost, false);
          }
        }
      }
    });
  }
}
