/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic.item.random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.lothrazar.cyclicmagic.util.UtilPlaceBlocks;
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

public class PacketRandomize implements IMessage, IMessageHandler<PacketRandomize, IMessage> {

  private BlockPos pos;
  private ItemRandomizer.ActionType actionType;
  private EnumFacing side;

  public PacketRandomize() {}

  public PacketRandomize(BlockPos mouseover, EnumFacing s, ItemRandomizer.ActionType t) {
    pos = mouseover;
    actionType = t;
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
    actionType = ItemRandomizer.ActionType.values()[t];
  }

  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("x", pos.getX());
    tags.setInteger("y", pos.getY());
    tags.setInteger("z", pos.getZ());
    tags.setInteger("t", actionType.ordinal());
    tags.setInteger("s", side.ordinal());
    ByteBufUtils.writeTag(buf, tags);
  }

  @Override
  public IMessage onMessage(PacketRandomize message, MessageContext ctx) {
    if (ctx.side.isServer() && message != null && message.pos != null) {
      EntityPlayer player = ctx.getServerHandler().player;
      World world = player.getEntityWorld();
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
      List<BlockPos> rpos = new ArrayList<BlockPos>();
      List<IBlockState> rstates = new ArrayList<IBlockState>();
      //ignore liquid/tile entities IE do not break chests / etc
      IBlockState stateHere = null;
      for (BlockPos p : places) {
        stateHere = world.getBlockState(p);
        if (stateHere != null
            && world.getTileEntity(p) == null
            && world.isAirBlock(p) == false
            && stateHere.getMaterial().isLiquid() == false) {
          //removed world.isSideSolid(p, message.side) && as it was blocking stairs/slabs from moving
          rpos.add(p);
          rstates.add(stateHere);
        }
      }
      Collections.shuffle(rpos, world.rand);
      BlockPos swapPos;
      IBlockState swapState;
      synchronized (rpos) {//just in case
        for (int i = 0; i < rpos.size(); i++) {
          swapPos = rpos.get(i);
          swapState = rstates.get(i);
          world.destroyBlock(swapPos, false);
          //playing sound here in large areas causes ConcurrentModificationException
          UtilPlaceBlocks.placeStateSafe(world, player, swapPos, swapState, false);
        }
      }
    }
    return null;
  }
}
