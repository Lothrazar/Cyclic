package com.lothrazar.cyclic.item.random;

import com.lothrazar.cyclic.base.PacketBase;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilPlaceBlocks;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

public class PacketRandomize extends PacketBase {

  private BlockPos pos;
  private Direction side;
  private InteractionHand hand;

  public PacketRandomize(BlockPos pos, Direction side, InteractionHand h) {
    this.pos = pos;
    this.side = side;
    hand = h;
  }

  public static PacketRandomize decode(FriendlyByteBuf buf) {
    PacketRandomize p = new PacketRandomize(buf.readBlockPos(),
        Direction.values()[buf.readInt()],
        InteractionHand.values()[buf.readInt()]);
    return p;
  }

  public static void encode(PacketRandomize msg, FriendlyByteBuf buf) {
    buf.writeBlockPos(msg.pos);
    buf.writeInt(msg.side.ordinal());
    buf.writeInt(msg.hand.ordinal());
  }

  public static void handle(PacketRandomize message, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      ServerPlayer player = ctx.get().getSender();
      Level world = player.getCommandSenderWorld();
      List<BlockPos> places = RandomizerItem.getPlaces(message.pos, message.side);
      List<BlockPos> rpos = new ArrayList<BlockPos>();
      List<BlockState> rstates = new ArrayList<BlockState>();
      //
      BlockState stateHere = null;
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
      Collections.shuffle(rpos, world.random);
      BlockPos swapPos;
      BlockState swapState;
      synchronized (rpos) { //just in case
        for (int i = 0; i < rpos.size(); i++) {
          swapPos = rpos.get(i);
          swapState = rstates.get(i);
          world.destroyBlock(swapPos, false);
          //playing sound here in large areas causes ConcurrentModificationException
          if (UtilPlaceBlocks.placeStateSafe(world, player, swapPos, swapState, false)) {
            atLeastOne = true;
          }
        }
      }
      if (atLeastOne) {
        UtilItemStack.damageItem(player, player.getItemInHand(message.hand));
      }
    });
    message.done(ctx);
  }
}
