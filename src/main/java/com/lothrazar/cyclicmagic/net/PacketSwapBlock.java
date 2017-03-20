package com.lothrazar.cyclicmagic.net;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.item.tool.ItemToolSwap;
import com.lothrazar.cyclicmagic.item.tool.ItemToolSwap.ActionType;
import com.lothrazar.cyclicmagic.item.tool.ItemToolSwap.WandType;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilPlaceBlocks;
import com.lothrazar.cyclicmagic.util.UtilPlayer;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSwapBlock implements IMessage, IMessageHandler<PacketSwapBlock, IMessage> {
  private BlockPos pos;
  private ItemToolSwap.ActionType actionType;
  private ItemToolSwap.WandType wandType;
  private EnumFacing side;
  public PacketSwapBlock() {}
  public PacketSwapBlock(BlockPos mouseover, EnumFacing s, ItemToolSwap.ActionType t, ItemToolSwap.WandType w) {
    pos = mouseover;
    actionType = t;
    wandType = w;
    side = s;
  }
  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    int x = tags.getInteger("x");
    int y = tags.getInteger("y");
    int z = tags.getInteger("z");
    pos = new BlockPos(x, y, z);
    int s = tags.getInteger("s");
    side = EnumFacing.values()[s];
    int t = tags.getInteger("t");
    actionType = ItemToolSwap.ActionType.values()[t];
    int w = tags.getInteger("w");
    wandType = ItemToolSwap.WandType.values()[w];
  }
  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("x", pos.getX());
    tags.setInteger("y", pos.getY());
    tags.setInteger("z", pos.getZ());
    tags.setInteger("t", actionType.ordinal());
    tags.setInteger("w", wandType.ordinal());
    tags.setInteger("s", side.ordinal());
    ByteBufUtils.writeTag(buf, tags);
  }
  @Override
  public IMessage onMessage(final PacketSwapBlock message, final MessageContext ctx) {
    if (ctx.side.isServer() && message != null && message.pos != null) {
      MinecraftServer s = FMLCommonHandler.instance().getMinecraftServerInstance();
      // MinecraftServer.getServer().//doesnt exist anymore
      if (s == null) {//this is never happening. ill keep it just in case
        handle(message, ctx);
      }
      else {
        //ONLY JAVA 8
        // s.addScheduledTask(() -> handle(message, ctx));
        s.addScheduledTask(new Runnable() {
          public void run() {
            handle(message, ctx);
          }
        });
      }
    }
    return null;
  }
  private void handle(PacketSwapBlock message, MessageContext ctx) {
    EntityPlayer player = ctx.getServerHandler().playerEntity;
    World world = player.getEntityWorld();
    //we already have center, now go around
    //      message.pos.offset(message.side.rotateAround(axis))
    IBlockState replaced;
    IBlockState newToPlace;
    IBlockState matched = null;
    if (message.wandType == WandType.MATCH) {
      matched = world.getBlockState(message.pos);
    }
    List<BlockPos> places = getSelectedBlocks(world, message.pos, message.actionType, message.wandType, message.side, matched);
    Map<BlockPos, Integer> processed = new HashMap<BlockPos, Integer>();
    // maybe dont randomly take blocks from inventory. maybe do a pick block.. or an inventory..i dont know
    //seems ok, and also different enough to be fine
    BlockPos curPos;
    try {
      synchronized (places) {
        for (Iterator<BlockPos> i = places.iterator(); i.hasNext();) {
          curPos = i.next();
          if (processed.containsKey(curPos) == false) {
            processed.put(curPos, 0);
          }
          if (processed.get(curPos) > 0) {
            continue; //dont process the same location more than once per click
          }
          processed.put(curPos, processed.get(curPos) + 1);// ++
          int slot = UtilPlayer.getFirstSlotWithBlock(player);
          if (slot < 0) {
            continue;//you have no materials left
          }
          if (world.getTileEntity(curPos) != null) {
            continue;//ignore tile entities IE do not break chests / etc
          }
          replaced = world.getBlockState(curPos);
          Block replacedBlock = replaced.getBlock();
          if (world.isAirBlock(curPos) || replaced == null) {
            continue;
          }
          //TODO: CLEANUP/REFACTOR THIS
          String itemName = UtilItemStack.getStringForBlock(replacedBlock);
          boolean isInBlacklist = false;
          for (String s : ItemToolSwap.swapBlacklist) {//dont use .contains on the list. must use .equals on string
            if (s != null && s.equals(itemName)) {
              isInBlacklist = true;
              break;
            }
          }
          if (isInBlacklist) {
            continue;
          }
          if (UtilItemStack.getBlockHardness(replaced, world, curPos) < 0) {
            continue;//since we know -1 is unbreakable
          }
          newToPlace = UtilPlayer.getBlockstateFromSlot(player, slot);
          //wait, do they match? are they the same? do not replace myself
          if (UtilWorld.doBlockStatesMatch(replaced, newToPlace)) {
            continue;
          }
          //break it and drop the whatever
          //the destroy then set was causing exceptions, changed to setAir // https://github.com/PrinceOfAmber/Cyclic/issues/114
          ItemStack cur = player.inventory.getStackInSlot(slot);
          if (cur == ItemStack.EMPTY || cur.getCount() <= 0) {
            continue;
          }
          world.setBlockToAir(curPos);
          boolean success = false;
          boolean ENABLEFANCY = false;//TODO: fix this. doing this makes player set HELD ITEM which is the tool/scepter to NULL. WTF
          ItemStack backup = player.getHeldItem(EnumHand.MAIN_HAND);
          if (ENABLEFANCY && cur.onItemUse(player, world, curPos, EnumHand.MAIN_HAND, message.side, 0.5F, 0.5F, 0.5F) == EnumActionResult.SUCCESS) {
            //then it owrked i guess eh
            player.setHeldItem(EnumHand.MAIN_HAND, backup);
            success = true;
            if (cur.getCount() == 0) {//double check hack for those red zeroes that always seem to come back
              player.inventory.setInventorySlotContents(slot, ItemStack.EMPTY);
            }
          }
          else {//do it the standard way
            success = UtilPlaceBlocks.placeStateSafe(world, player, curPos, newToPlace);
            if (success) {
              UtilPlayer.decrStackSize(player, slot);
            }
          }
          if (success) {//same old success method
            //            UtilSound.playSoundPlaceBlock(worldObj, curPos, newToPlace.getBlock());//fffk doesnt work
            replacedBlock.dropBlockAsItem(world, curPos, replaced, 0);//zero is fortune level
            //damage once per block 
            //TODO: CLEANUP?REFACTOR THIS
            ItemStack held = player.getHeldItemMainhand();
            if (held != ItemStack.EMPTY && held.getItem() instanceof ItemToolSwap) {
              UtilItemStack.damageItem(player, held);
            }
            else {
              held = player.getHeldItemOffhand();
              if (held != ItemStack.EMPTY && held.getItem() instanceof ItemToolSwap) {
                UtilItemStack.damageItem(player, held);
              }
            }
          }
        } // close off the for loop   
      }
    }
    catch (ConcurrentModificationException e) {
      //possible reason why i cant do a trycatch // http://stackoverflow.com/questions/18752320/trycatch-concurrentmodificationexception-catching-30-of-the-time
      ModCyclic.logger.error("ConcurrentModificationException");
      ModCyclic.logger.error(e.getMessage());// message is null??
      ModCyclic.logger.error(e.getStackTrace().toString());
    }
  }
  public static List<BlockPos> getSelectedBlocks(World world, BlockPos pos, ActionType actionType, WandType wandType, EnumFacing side, IBlockState matched) {
    List<BlockPos> places = new ArrayList<BlockPos>();
    int xMin = pos.getX();
    int yMin = pos.getY();
    int zMin = pos.getZ();
    int xMax = pos.getX();
    int yMax = pos.getY();
    int zMax = pos.getZ();
    boolean isVertical = (side == EnumFacing.UP || side == EnumFacing.DOWN);
    int offsetRadius = 0;
    switch (actionType) {
      case SINGLE:
        places.add(pos);
        offsetRadius = 0;
      break;
      case X3:
        offsetRadius = 1;
      break;
      case X5:
        offsetRadius = 2;
      break;
      case X7:
        offsetRadius = 3;
      break;
      case X9:
        offsetRadius = 4;
      break;
      default:
      break;
    }
    if (offsetRadius > 0) {
      if (isVertical) {
        //then we just go in all horizontal directions
        xMin -= offsetRadius;
        xMax += offsetRadius;
        zMin -= offsetRadius;
        zMax += offsetRadius;
      }
      //we hit a horizontal side
      else if (side == EnumFacing.EAST || side == EnumFacing.WEST) {
        //          WEST(4, 5, 1, "west", EnumFacing.AxisDirection.NEGATIVE, EnumFacing.Axis.X, new Vec3i(-1, 0, 0)),
        //          EAST(5, 4, 3, "east", EnumFacing.AxisDirection.POSITIVE, EnumFacing.Axis.X, new Vec3i(1, 0, 0));
        //now we go in a vertical plane
        zMin -= offsetRadius;
        zMax += offsetRadius;
        yMin -= offsetRadius;
        yMax += offsetRadius;
      }
      else {
        //axis hit was north/south, so we go in YZ
        xMin -= offsetRadius;
        xMax += offsetRadius;
        yMin -= offsetRadius;
        yMax += offsetRadius;
      }
      places = UtilWorld.getPositionsInRange(pos, xMin, xMax, yMin, yMax, zMin, zMax);
    }
    List<BlockPos> retPlaces = new ArrayList<BlockPos>();
    for (BlockPos p : places) {
      if (world.isAirBlock(p)) {
        continue;
      }
      if (wandType == WandType.MATCH && matched != null &&
          !UtilWorld.doBlockStatesMatch(matched, world.getBlockState(p))) {
        //we have saved the one we clicked on so only that gets replaced
        continue;
      }
      retPlaces.add(p);
    }
    return retPlaces;
  }
}
