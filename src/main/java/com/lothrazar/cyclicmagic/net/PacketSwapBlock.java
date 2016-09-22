package com.lothrazar.cyclicmagic.net;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.item.tool.ItemToolSwap;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilInventory;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSwapBlock implements IMessage, IMessageHandler<PacketSwapBlock, IMessage> {
  private BlockPos pos;
  private ItemToolSwap.ActionType type;
  private EnumFacing side;
  public PacketSwapBlock() {
  }
  public PacketSwapBlock(BlockPos mouseover, ItemToolSwap.ActionType t, EnumFacing s) {
    pos = mouseover;
    type = t;
    side = s;
  }
  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    int x = tags.getInteger("x");
    int y = tags.getInteger("y");
    int z = tags.getInteger("z");
    pos = new BlockPos(x, y, z);
    int t = tags.getInteger("t");
    type = ItemToolSwap.ActionType.values()[t];
    int s = tags.getInteger("s");
    side = EnumFacing.values()[s];
  }
  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("x", pos.getX());
    tags.setInteger("y", pos.getY());
    tags.setInteger("z", pos.getZ());
    tags.setInteger("t", type.ordinal());
    tags.setInteger("s", side.ordinal());
    ByteBufUtils.writeTag(buf, tags);
  }
  @Override
  public IMessage onMessage(PacketSwapBlock message, MessageContext ctx) {
    if (ctx.side.isServer() && message != null && message.pos != null) {
      EntityPlayer player = ctx.getServerHandler().playerEntity;
      World worldObj = player.worldObj;
      List<BlockPos> places = new ArrayList<BlockPos>();
      //      EnumFacing.Axis axis = message.side.getAxis();
      //       System.out.println("axis is "+axis.getName());
      //stuff for anything not single
      //       UtilWorld.getPositionsInBounds.//amybe refactor into this
      int xMin = message.pos.getX();
      int yMin = message.pos.getY();
      int zMin = message.pos.getZ();
      int xMax = message.pos.getX();
      int yMax = message.pos.getY();
      int zMax = message.pos.getZ();
      boolean isVertical = (message.side == EnumFacing.UP || message.side == EnumFacing.DOWN);
      int offsetRadius = 0;
      switch (message.type) {
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
      //TODO: maybe dont randomly take blocks from inventory. maybe do a pick block.. or an inventory..i dont know
      //seems ok, and also different enough to be fine
      for (BlockPos p : places) {
        System.out.println("examine: "+UtilChat.blockPosToString(p));
        int slot = UtilInventory.getFirstSlotWithBlock(player);
        if (slot < 0) {
          continue;//you have no materials left
        }
        if (worldObj.getTileEntity(p) != null) {
          continue;//ignore tile entities IE do not break chests / etc
        }
        replaced = worldObj.getBlockState(p);
        if (worldObj.isAirBlock(p) || replaced == null) {
          //dont build in air
          continue;
        }
        newToPlace = UtilInventory.getBlockstateFromSlot(player, slot);
        if (replaced.getBlock().getBlockHardness(replaced, worldObj, p) < 0) {
          //is unbreakable ie bedrock
          continue;
        }
        //wait, do they match? are they the same? do not replace myself
        if (replaced.getBlock() == newToPlace.getBlock() &&
            replaced.getBlock().getMetaFromState(replaced) == newToPlace.getBlock().getMetaFromState(newToPlace)) {
          continue;
        }
        //break it and drop the whatever
        worldObj.destroyBlock(p, true);
        //set the new swap
        worldObj.setBlockState(p, newToPlace);
        UtilInventory.decrStackSize(player, slot);
      }
    }
    return null;
  }
}
