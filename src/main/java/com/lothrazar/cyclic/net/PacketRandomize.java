package com.lothrazar.cyclic.net;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import com.lothrazar.cyclic.item.RandomizerItem;
import com.lothrazar.cyclic.util.UtilPlaceBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketRandomize {

  private BlockPos pos;
  private Direction side;

  public PacketRandomize(BlockPos pos, Direction side) {
    this.pos = pos;
    this.side = side;
  }

  public static PacketRandomize decode(PacketBuffer buf) {
    PacketRandomize p = new PacketRandomize(buf.readBlockPos(), Direction.values()[buf.readInt()]);
    return p;
  }

  public static void encode(PacketRandomize msg, PacketBuffer buf) {
    buf.writeBlockPos(msg.pos);
    buf.writeInt(msg.side.ordinal());
  }

  public static void handle(PacketRandomize message, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      ServerPlayerEntity player = ctx.get().getSender();
      World world = player.getEntityWorld();
      List<BlockPos> places = RandomizerItem.getPlaces(message.pos, message.side);
      List<BlockPos> rpos = new ArrayList<BlockPos>();
      List<BlockState> rstates = new ArrayList<BlockState>();
      //
      BlockState stateHere = null;
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
      Collections.shuffle(rpos, world.rand);
      BlockPos swapPos;
      BlockState swapState;
      synchronized (rpos) {//just in case
        for (int i = 0; i < rpos.size(); i++) {
          swapPos = rpos.get(i);
          swapState = rstates.get(i);
          world.destroyBlock(swapPos, false);
          //playing sound here in large areas causes ConcurrentModificationException
          UtilPlaceBlocks.placeStateSafe(world, player, swapPos, swapState, false);
        }
      }
    });
  }
}
