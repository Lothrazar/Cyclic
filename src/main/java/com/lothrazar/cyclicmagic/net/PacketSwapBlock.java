package com.lothrazar.cyclicmagic.net;
import java.util.ArrayList;
import com.lothrazar.cyclicmagic.item.tool.ItemToolPush;
import com.lothrazar.cyclicmagic.item.tool.ItemToolSwap;
import com.lothrazar.cyclicmagic.util.UtilInventory;
import com.lothrazar.cyclicmagic.util.UtilPlaceBlocks;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
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
  @SuppressWarnings("unused")
  @Override
  public IMessage onMessage(PacketSwapBlock message, MessageContext ctx) {
    if (ctx.side.isServer() && message != null && message.pos != null) {
      EntityPlayer player = ctx.getServerHandler().playerEntity;
      World worldObj = player.worldObj;
      BlockPos resultPosition = null;
      ArrayList<BlockPos> places = new ArrayList<BlockPos>();
      places.add(message.pos);
      switch (message.type) {
      case SINGLE:
        break;
      case X3:
        break;
      case X5:
        break;
      case X7:
        break;
      case X9:
        break;
      default:
        break;
      //      case PULL:
      //        resultPosition = UtilPlaceBlocks.pullBlock(worldObj, player, message.pos, message.side);
      //        break;
      //      case PUSH:
      //        resultPosition = UtilPlaceBlocks.pushBlock(worldObj, player, message.pos, message.side);
      //        break;
      //      case ROTATE:
      //        UtilPlaceBlocks.rotateBlockValidState(worldObj, player, message.pos, message.side);
      //        resultPosition = pos;
      //        break;
      //      default:
      //        break;
      }
      IBlockState replaced;
      IBlockState newToPlace;
      for (BlockPos p : places) {
        int slot = UtilInventory.getFirstSlotWithBlock(player);
        if (slot < 0) {
          continue;
        }
        replaced = worldObj.getBlockState(p);
        newToPlace = UtilInventory.getBlockstateFromSlot(player, slot);
        //wait, do they match? are they the same? do not replace myself
        if (replaced.getBlock() == newToPlace.getBlock() &&
            replaced.getBlock().getMetaFromState(replaced) == newToPlace.getBlock().getMetaFromState(newToPlace)) {
          continue;
        }
        //break it and drop the whatever
        if (replaced != null && worldObj.isAirBlock(p) == false) {
          worldObj.destroyBlock(p, true);
        }
        //set the new swap
        worldObj.setBlockState(p, newToPlace);
        UtilInventory.decrStackSize(player, slot);
      }
    }
    return null;
  }
}
