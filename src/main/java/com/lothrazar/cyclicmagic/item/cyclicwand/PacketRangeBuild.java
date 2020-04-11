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
package com.lothrazar.cyclicmagic.item.cyclicwand;

import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.playerupgrade.spell.SpellRangeBuild;
import com.lothrazar.cyclicmagic.playerupgrade.spell.SpellRangeBuild.PlaceType;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilPlaceBlocks;
import com.lothrazar.cyclicmagic.util.UtilSound;
import com.lothrazar.cyclicmagic.util.UtilSpellCaster;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketRangeBuild implements IMessage, IMessageHandler<PacketRangeBuild, IMessage> {

  private BlockPos pos;
  private @Nullable EnumFacing face;
  private PlaceType type;
  private Vec3d hitVec;

  public PacketRangeBuild() {}

  public PacketRangeBuild(RayTraceResult ray, int spellid, PlaceType type) {
    this.pos = ray.getBlockPos();
    face = ray.sideHit;
    this.hitVec = new Vec3d(ray.hitVec.x - MathHelper.fastFloor(ray.hitVec.x),
        ray.hitVec.y - MathHelper.fastFloor(ray.hitVec.y),
        ray.hitVec.z - MathHelper.fastFloor(ray.hitVec.z));
    //    ModCyclic.logger.info("Test" + this.hitVec);
    this.type = type;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    this.hitVec = new Vec3d(
        tags.getDouble("hitx"),
        tags.getDouble("hity"),
        tags.getDouble("hitz"));
    int x = tags.getInteger("x");
    int y = tags.getInteger("y");
    int z = tags.getInteger("z");
    pos = new BlockPos(x, y, z);
    if (tags.hasKey("face"))
      face = EnumFacing.values()[tags.getInteger("face")];
    int t = tags.getInteger("placetype");
    this.type = PlaceType.values()[t];
  }

  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setDouble("hitx", hitVec.x);
    tags.setDouble("hity", hitVec.y);
    tags.setDouble("hitz", hitVec.z);
    tags.setInteger("placetype", type.ordinal());
    tags.setInteger("x", pos.getX());
    tags.setInteger("y", pos.getY());
    tags.setInteger("z", pos.getZ());
    if (face != null)
      tags.setInteger("face", face.ordinal());
    ByteBufUtils.writeTag(buf, tags);
  }

  @Override
  public IMessage onMessage(PacketRangeBuild message, MessageContext ctx) {
    checkThreadAndEnqueue(message, ctx);
    return null;
  }

  private void checkThreadAndEnqueue(final PacketRangeBuild message, final MessageContext ctx) {
    IThreadListener thread = ModCyclic.proxy.getThreadFromContext(ctx);
    thread.addScheduledTask(new Runnable() {

      @Override
      public void run() {
        if (ctx.side.isServer() && message != null && message.pos != null) {
          EntityPlayer p = ctx.getServerHandler().player;
          message.castFromServer(message.pos, message.face, p);
        }
      }
    });
  }

  private BlockPos getPosToPlaceAt(EntityPlayer p, BlockPos pos, EnumFacing sideMouseover) {
    BlockPos posToPlaceAt = null;
    EnumFacing facing = null;
    EnumFacing playerFacing = p.getHorizontalFacing();
    //using my spell direction build type
    switch (type) {
      case DOWN:
        facing = EnumFacing.DOWN;
      break;
      case UP:
        facing = EnumFacing.UP;
      break;
      case LEFT:
        switch (playerFacing) {
          case DOWN:
          break;
          case EAST:
            facing = EnumFacing.NORTH;
          break;
          case NORTH:
            facing = EnumFacing.WEST;
          break;
          case SOUTH:
            facing = EnumFacing.EAST;
          break;
          case UP:
          break;
          case WEST:
            facing = EnumFacing.SOUTH;
          break;
          default:
          break;
        }
      break;
      case RIGHT:
        switch (playerFacing) {
          case DOWN:
          break;
          case EAST:
            facing = EnumFacing.SOUTH;
          break;
          case NORTH:
            facing = EnumFacing.EAST;
          break;
          case SOUTH:
            facing = EnumFacing.WEST;
          break;
          case UP:
          break;
          case WEST:
            facing = EnumFacing.NORTH;
          break;
          default:
          break;
        }
      break;
      case PLACE:
      break;
    }
    if (facing == null) {
      posToPlaceAt = pos;
    }
    else {
      posToPlaceAt = UtilWorld.nextAirInDirection(p.world, pos.offset(facing), facing, SpellRangeBuild.max, null);
    }
    return posToPlaceAt;
  }

  public void castFromServer(BlockPos pos,
      @Nullable EnumFacing sideMouseover, EntityPlayer p) {
    World world = p.getEntityWorld();
    ItemStack heldWand = UtilSpellCaster.getPlayerWandIfHeld(p);
    if (heldWand.isEmpty()) {
      return;
    }
    int itemSlot = ItemCyclicWand.BuildType.getSlot(heldWand);
    ItemStack TEST = InventoryWand.getFromSlot(heldWand, itemSlot);
    IBlockState state = InventoryWand.getToPlaceFromSlot(heldWand, itemSlot);
    if (state == null || state.getBlock() == null) {
      //one last chance to update slot, in case something happened
      ItemCyclicWand.BuildType.setNextSlot(heldWand);
      itemSlot = ItemCyclicWand.BuildType.getSlot(heldWand);
      state = InventoryWand.getToPlaceFromSlot(heldWand, itemSlot);
      if (state == null || state.getBlock() == null) {
        UtilChat.sendStatusMessage(p, "wand.inventory.empty");
        return;
      }
    }
    BlockPos posToPlaceAt = getPosToPlaceAt(p, pos, sideMouseover);
    if (UtilPlaceBlocks.buildStackAsPlayer(world, p, posToPlaceAt, TEST, sideMouseover, this.hitVec)) {
      //      SpellRangeBuild.spawnParticle(world, p, pos);
      //      SpellRangeBuild.playSound(world, p, world.getBlockState(posToPlaceAt).getBlock(), posToPlaceAt);
      UtilSound.playSoundPlaceBlock(p, posToPlaceAt, world.getBlockState(posToPlaceAt));
      if (!p.isCreative()) {
        InventoryWand.decrementSlot(heldWand, itemSlot);
      }
    }
  }
}
