package com.lothrazar.cyclic.item.builder;

import com.lothrazar.library.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import com.lothrazar.cyclic.ModCyclic;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.*;

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

      var player = ctx.player();
      ItemStack itemStackHeld = player.getItemInHand(message.hand);
      BlockState targetState = BuilderActionType.getBlockState(player.level(), itemStackHeld);
      if (targetState == null || itemStackHeld.getItem() instanceof BuilderItem == false) {
        return;
      }
      BuildStyle buildStyle = ((BuilderItem) itemStackHeld.getItem()).style;
      Level world = player.getCommandSenderWorld();
      BlockState replacedBlockState;
      List<BlockPos> places = getSelectedBlocks(world, message.pos, message.actionType, message.side, buildStyle);
      Map<BlockPos, Integer> processed = new HashMap<BlockPos, Integer>();
      BlockPos curPos;
      boolean atLeastOne = false;
      synchronized (places) {
        for (Iterator<BlockPos> i = places.iterator(); i.hasNext();) {
          curPos = i.next();
          if (processed.containsKey(curPos) == false) {
            processed.put(curPos, 0);
          }
          if (processed.get(curPos) > 0) {
            continue; //dont process the same location more than once per click
          }
          processed.put(curPos, processed.get(curPos) + 1);
          int slot = PlayerUtil.getFirstSlotWithBlock(player, targetState);
          if (slot < 0) {
            //nothign found. is that ok?
            if (!player.isCreative()) {
              ChatUtil.sendStatusMessage(player, "scepter.cyclic.empty");
              break;
              //you have no materials left
            }
          }
          if (world.getBlockEntity(curPos) != null) {
            continue;
            //ignore tile entities IE do not break chests / etc
          }
          replacedBlockState = world.getBlockState(curPos);
          Block replacedBlock = replacedBlockState.getBlock();
          boolean isInBlacklist = false;
          //          for (String s : ItemBuildSwapper.swapBlacklist) {//dont use .contains on the list. must use .equals on string
          //            if (s != null && s.equals(itemName)) {
          //              isInBlacklist = true;
          //              break;
          //            }
          //          }
          if (isInBlacklist) {
            continue;
          }
          if (replacedBlockState.getDestroySpeed(world, curPos) < 0) {
            continue; //since we know -1 is unbreakable
          }
          //wait, do they match? are they the same? do not replace myself
          if (LevelWorldUtil.doBlockStatesMatch(replacedBlockState, targetState)) {
            continue;
          }
          //break it and drop the whatever
          //the destroy then set was causing exceptions, changed to setAir // https://github.com/PrinceOfAmber/Cyclic/issues/114
          world.setBlock(curPos, Blocks.AIR.defaultBlockState(), 0);
          boolean success = false;
          //place item block gets slabs in top instead of bottom. but tries to do facing stairs
          // success = UtilPlaceBlocks.placeItemblock(world, curPos, stackBuildWith, player);
          if (!success) {
            success = BlockUtil.placeStateSafe(world, player, curPos, targetState);
          }
          if (success) {
            atLeastOne = true;
            PlayerUtil.decrStackSize(player, slot);
            world.levelEvent(2001, curPos, Block.getId(targetState));
            //always break with PLAYER CONTEXT in mind
            replacedBlock.playerDestroy(world, player, curPos, replacedBlockState, null, itemStackHeld);
          }
        } // close off the for loop
      }
      if (atLeastOne) {
        ItemStackUtil.damageItem(player, itemStackHeld);
      }
    });
  }
  public static List<BlockPos> getSelectedBlocks(Level world, BlockPos pos, BuilderActionType actionType, Direction side, BuildStyle style) {
    List<BlockPos> places = new ArrayList<BlockPos>();
    int xMin = pos.getX();
    int yMin = pos.getY();
    int zMin = pos.getZ();
    int xMax = pos.getX();
    int yMax = pos.getY();
    int zMax = pos.getZ();
    boolean isVertical = (side == Direction.UP || side == Direction.DOWN);
    int offsetH = 0;
    int offsetW = 0;
    switch (actionType) {
      case SINGLE:
        places.add(pos);
        offsetW = offsetH = 0;
        break;
      case X3:
        offsetW = offsetH = 1;
        break;
      case X5:
        offsetW = offsetH = 2;
        break;
      case X7:
        offsetW = offsetH = 3;
        break;
      case X9:
        offsetW = offsetH = 4;
        break;
      case X19:
        offsetH = 0;
        offsetW = 4;
        break;
      case X91:
        offsetH = 4;
        offsetW = 0;
        break;
      default:
        break;
    }
    if (actionType != BuilderActionType.SINGLE) {
      if (isVertical) {
        //then we just go in all horizontal directions
        xMin -= offsetH;
        xMax += offsetH;
        zMin -= offsetW;
        zMax += offsetW;
      }
      //we hit a horizontal side
      else if (side == Direction.EAST || side == Direction.WEST) {
        //now we go in a vertical plane
        zMin -= offsetH;
        zMax += offsetH;
        yMin -= offsetW;
        yMax += offsetW;
      }
      else {
        //axis hit was north/south, so we go in YZ
        xMin -= offsetH;
        xMax += offsetH;
        yMin -= offsetW;
        yMax += offsetW;
      }
      places = LevelWorldUtil.getPositionsInRange(pos, xMin, xMax, yMin, yMax, zMin, zMax);
    }
    List<BlockPos> retPlaces = new ArrayList<BlockPos>();
    for (BlockPos p : places) {
      if (!world.getBlockState(p).canBeReplaced()) {
        //i am not replaceable. so i am a solid block or somethign
        if (!style.isReplaceable()) {
          continue;
        }
      }
      //      if (wandType == WandType.MATCH && matched != null &&
      //          !UtilWorld.doBlockStatesMatch(matched, world.getBlockState(p))) {
      //        //we have saved the one we clicked on so only that gets replaced
      //        continue;
      //      }
      retPlaces.add(p);
    }
    return retPlaces;
  }
}
