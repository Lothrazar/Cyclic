package com.lothrazar.cyclicmagic.net;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.item.tool.ItemToolSwap;
import com.lothrazar.cyclicmagic.item.tool.ItemToolSwap.WandType;
import com.lothrazar.cyclicmagic.util.UtilInventory;
import com.lothrazar.cyclicmagic.util.UtilItem;
import com.lothrazar.cyclicmagic.util.UtilPlaceBlocks;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
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
  public PacketSwapBlock() {
  }
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
  public IMessage onMessage(final PacketSwapBlock message,final MessageContext ctx) {
    if (ctx.side.isServer() && message != null && message.pos != null) {
      MinecraftServer s = FMLCommonHandler.instance().getMinecraftServerInstance();
      // MinecraftServer.getServer().//doesnt exist anymore
      if(s == null){//this is never happening. ill keep it just in case
        handle(message,ctx);
      }
      else{
        //ONLY JAVA 8
       // s.addScheduledTask(() -> handle(message, ctx));
        s.addScheduledTask(new Runnable()
        {
          public void run()
          {
              handle(message,ctx);
          }
        });
      }
      
    }
    return null;
  }
  private void handle(PacketSwapBlock message, MessageContext ctx) {

    EntityPlayer player = ctx.getServerHandler().playerEntity;
    World worldObj = player.worldObj;
    List<BlockPos> places = new ArrayList<BlockPos>();
    int xMin = message.pos.getX();
    int yMin = message.pos.getY();
    int zMin = message.pos.getZ();
    int xMax = message.pos.getX();
    int yMax = message.pos.getY();
    int zMax = message.pos.getZ();
    boolean isVertical = (message.side == EnumFacing.UP || message.side == EnumFacing.DOWN);
    int offsetRadius = 0;
    switch (message.actionType) {
    case SINGLE:
      places.add(message.pos);
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
      else if (message.side == EnumFacing.EAST || message.side == EnumFacing.WEST) {
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
      places = UtilWorld.getPositionsInRange(message.pos, xMin, xMax, yMin, yMax, zMin, zMax);
    }
    //we already have center, now go around
    //      message.pos.offset(message.side.rotateAround(axis))
    IBlockState replaced;
    IBlockState newToPlace;
    IBlockState matched = null;
    if (message.wandType == WandType.MATCH) {
      matched = worldObj.getBlockState(message.pos);
    }
    Map<BlockPos, Integer> processed = new HashMap<BlockPos, Integer>();
    //TODO: maybe dont randomly take blocks from inventory. maybe do a pick block.. or an inventory..i dont know
    //seems ok, and also different enough to be fine
    BlockPos curPos;
    try {
      synchronized (places) {
        for (Iterator<BlockPos> i = places.iterator(); i.hasNext();) {
          curPos = i.next();
          // for (BlockPos curPos : places) {
          if (processed.containsKey(curPos) == false) {
            processed.put(curPos, 0);
          }
          if (processed.get(curPos) > 0) {
            continue; //dont process the same location more than once per click
          }
          processed.put(curPos, processed.get(curPos) + 1);// ++
          int slot = UtilInventory.getFirstSlotWithBlock(player);
          if (slot < 0) {
            continue;//you have no materials left
          }
          //        if (worldObj.isSideSolid(curPos, side) == false) { //trying to avoid the TickNextTick list out of synch
          //          continue;//dont  do nonsolid blocks ex: water/plants
          //        }
          if (worldObj.getTileEntity(curPos) != null) {
            continue;//ignore tile entities IE do not break chests / etc
          }
          replaced = worldObj.getBlockState(curPos);
          if (worldObj.isAirBlock(curPos) || replaced == null) {
            //dont build in air
            continue;
          }
          newToPlace = UtilInventory.getBlockstateFromSlot(player, slot);
          if (UtilItem.getPlayerRelativeBlockHardness(replaced.getBlock(), replaced, player, worldObj, curPos) < 0) {
            //is unbreakable ie bedrock
            continue;
          }
          //wait, do they match? are they the same? do not replace myself
          if (UtilWorld.doBlockStatesMatch(replaced, newToPlace)) {
            continue;
          }
          if (message.wandType == WandType.MATCH && matched != null &&
              !UtilWorld.doBlockStatesMatch(matched, replaced)) {
            //we have saved the one we clicked on so only that gets replaced
            continue;
          }
          //break it and drop the whatever
          //the destroy then set was causing exceptions, changed to setAir // https://github.com/PrinceOfAmber/Cyclic/issues/114
          Block block = replaced.getBlock();
          // if (worldObj.destroyBlock(curPos, true)) {
   
          if (UtilPlaceBlocks.placeStateOverwrite(worldObj, player, curPos, newToPlace)) {
            UtilInventory.decrStackSize(player, slot);
            block.dropBlockAsItem(worldObj, curPos, replaced, 0);//zero is fortune level
          }
        } // close off the for loop   
      }
    }
    catch (ConcurrentModificationException e) {
      //possible reason why i cant do a trycatch // http://stackoverflow.com/questions/18752320/trycatch-concurrentmodificationexception-catching-30-of-the-time
      ModMain.logger.warn("ConcurrentModificationException");
      ModMain.logger.warn(e.getMessage());// message is null??
      ModMain.logger.warn(e.getStackTrace().toString());
    }
  }

}
