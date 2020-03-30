package com.lothrazar.cyclic.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import com.lothrazar.cyclic.item.BuilderItem;
import com.lothrazar.cyclic.item.BuilderItem.ActionType;
import com.lothrazar.cyclic.util.UtilChat;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilPlaceBlocks;
import com.lothrazar.cyclic.util.UtilPlayer;
import com.lothrazar.cyclic.util.UtilWorld;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketSwapBlock {

  //  private ItemBuildSwapper.WandType wandType;
  private BlockPos pos;
  private BuilderItem.ActionType actionType;
  private Direction side;
  private Hand hand;

  public PacketSwapBlock(BlockPos readBlockPos, BuilderItem.ActionType type, Direction dir, Hand h) {
    pos = readBlockPos;
    actionType = type;
    side = dir;
    hand = h;
  }

  public static PacketSwapBlock decode(PacketBuffer buf) {
    return new PacketSwapBlock(buf.readBlockPos(),
        BuilderItem.ActionType.values()[buf.readInt()],
        Direction.values()[buf.readInt()],
        Hand.values()[buf.readInt()]);
  }

  public static void encode(PacketSwapBlock msg, PacketBuffer buf) {
    buf.writeBlockPos(msg.pos);
    buf.writeInt(msg.actionType.ordinal());
    buf.writeInt(msg.side.ordinal());
    buf.writeInt(msg.hand.ordinal());
  }

  public static void handle(PacketSwapBlock message, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      ServerPlayerEntity player = ctx.get().getSender();
      BlockState targetState = BuilderItem.ActionType.getBlockState(player.getHeldItem(message.hand));
      if (targetState == null) {
        return;
      }
      //      BlockPos position = message.pos;
      World world = player.getEntityWorld();
      BlockState replacedBlockState;
      BlockState newToPlace;
      //      IBlockState matched = null; 
      List<BlockPos> places = getSelectedBlocks(world, message.pos, message.actionType, message.side, null);
      Map<BlockPos, Integer> processed = new HashMap<BlockPos, Integer>();
      BlockPos curPos;
      synchronized (places) {
        for (Iterator<BlockPos> i = places.iterator(); i.hasNext();) {
          curPos = i.next();
          //          ModCyclic.LOGGER.info("EARLY y value of build " + curPos.getY());
          if (processed.containsKey(curPos) == false) {
            processed.put(curPos, 0);
          }
          if (processed.get(curPos) > 0) {
            continue; //dont process the same location more than once per click
          }
          processed.put(curPos, processed.get(curPos) + 1);// ++
          int slot = UtilPlayer.getFirstSlotWithBlock(player, targetState);
          if (slot < 0) {
            UtilChat.sendStatusMessage(player, "scepter.cyclic.empty");
            continue;//you have no materials left
          }
          if (world.getTileEntity(curPos) != null) {
            continue;//ignore tile entities IE do not break chests / etc
          }
          replacedBlockState = world.getBlockState(curPos);
          Block replacedBlock = replacedBlockState.getBlock();
          //          }
          //TODO: CLEANUP/REFACTOR THIS
          //          String itemName = UtilItemStack.getStringForBlock(replacedBlock);
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
          if (UtilItemStack.getBlockHardness(replacedBlockState, world, curPos) < 0) {
            continue;//since we know -1 is unbreakable
          }
          newToPlace = UtilPlayer.getBlockstateFromSlot(player, slot);
          //wait, do they match? are they the same? do not replace myself
          if (UtilWorld.doBlockStatesMatch(replacedBlockState, newToPlace)) {
            continue;
          }
          //break it and drop the whatever
          //the destroy then set was causing exceptions, changed to setAir // https://github.com/PrinceOfAmber/Cyclic/issues/114
          ItemStack stackBuildWith = player.inventory.getStackInSlot(slot);
          if (stackBuildWith.isEmpty() || stackBuildWith.getCount() <= 0) {
            continue;
          }
          world.setBlockState(curPos, Blocks.AIR.getDefaultState());
          boolean success = false;
          ItemStack itemStackHeld = player.getHeldItem(message.hand);
          //TODO: maybe toggle between
          //place item block gets slabs in top instead of bottom. but tries to do facing stairs
          // success = UtilPlaceBlocks.placeItemblock(world, curPos, stackBuildWith, player);
          if (!success) {
            //            ModCyclic.LOGGER.info("y value of build " + curPos.getY());
            success = UtilPlaceBlocks.placeStateSafe(world, player, curPos, newToPlace);
          }
          if (success) {
            UtilPlayer.decrStackSize(player, slot);
          }
          if (success) {
            world.playEvent(2001, curPos, Block.getStateId(replacedBlockState));
            //always break with PLAYER CONTEXT in mind
            replacedBlock.harvestBlock(world, player, curPos, replacedBlockState, null, itemStackHeld);
            ItemStack held = player.getHeldItem(message.hand);
            if (!held.isEmpty() && held.getItem() instanceof BuilderItem) {
              //              UtilItemStack.damageItem(player, held);
            }
          }
        } // close off the for loop   
      }
    });
  }

  public static List<BlockPos> getSelectedBlocks(World world, BlockPos pos, ActionType actionType, Direction side, BlockState matched) {
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
    if (actionType != ActionType.SINGLE) {
      if (isVertical) {
        //then we just go in all horizontal directions
        xMin -= offsetH;
        xMax += offsetH;
        zMin -= offsetW;
        zMax += offsetW;
      }
      //we hit a horizontal side
      else if (side == Direction.EAST || side == Direction.WEST) {
        //          WEST(4, 5, 1, "west", EnumFacing.AxisDirection.NEGATIVE, EnumFacing.Axis.X, new Vec3i(-1, 0, 0)),
        //          EAST(5, 4, 3, "east", EnumFacing.AxisDirection.POSITIVE, EnumFacing.Axis.X, new Vec3i(1, 0, 0));
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
      places = UtilWorld.getPositionsInRange(pos, xMin, xMax, yMin, yMax, zMin, zMax);
    }
    List<BlockPos> retPlaces = new ArrayList<BlockPos>();
    for (BlockPos p : places) {
      if (
      //!world.isAirBlock(p) ||
      !world.getBlockState(p).getMaterial().isReplaceable()
      //&& wandType == WandType.MATCH
      ) {
        //if its either not-air OR not-replaceable
        //cannot match with air
        continue;
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
