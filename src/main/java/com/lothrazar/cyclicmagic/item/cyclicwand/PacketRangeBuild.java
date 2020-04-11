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
import com.lothrazar.cyclicmagic.playerupgrade.spell.ISpell;
import com.lothrazar.cyclicmagic.playerupgrade.spell.SpellRangeBuild;
import com.lothrazar.cyclicmagic.playerupgrade.spell.SpellRangeBuild.PlaceType;
import com.lothrazar.cyclicmagic.registry.SpellRegistry;
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
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketRangeBuild implements IMessage, IMessageHandler<PacketRangeBuild, IMessage> {

  private BlockPos pos;
  private BlockPos posOffset;
  private @Nullable EnumFacing face;
  private int spellID;
  private PlaceType type;

  public PacketRangeBuild() {}

  public PacketRangeBuild(RayTraceResult ray, int spellid, PlaceType type) {
    this.pos = ray.getBlockPos();
    spellID = spellid;
    face = ray.sideHit;
    if (pos != null && face != null) {
      posOffset = pos.offset(face);
    }
    this.type = type;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    int x = tags.getInteger("x");
    int y = tags.getInteger("y");
    int z = tags.getInteger("z");
    pos = new BlockPos(x, y, z);
    x = tags.getInteger("ox");
    y = tags.getInteger("oy");
    z = tags.getInteger("oz");
    posOffset = new BlockPos(x, y, z);
    spellID = tags.getInteger("spell");
    if (tags.hasKey("face"))
      face = EnumFacing.values()[tags.getInteger("face")];
    int t = tags.getInteger("placetype");
    this.type = PlaceType.values()[t];
  }

  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("placetype", type.ordinal());
    tags.setInteger("x", pos.getX());
    tags.setInteger("y", pos.getY());
    tags.setInteger("z", pos.getZ());
    tags.setInteger("ox", posOffset.getX());
    tags.setInteger("oy", posOffset.getY());
    tags.setInteger("oz", posOffset.getZ());
    tags.setInteger("spell", spellID);
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
          ISpell spell = SpellRegistry.getSpellFromID(message.spellID);
          if (spell != null && spell instanceof SpellRangeBuild) {
            message.castFromServer(message.pos, message.posOffset, message.face, p);
          }
          else {
            ModCyclic.logger.error("WARNING: Message from server: spell not found" + message.spellID);
          }
        }
      }
    });
  }

  private BlockPos getPosToPlaceAt(EntityPlayer p, BlockPos pos, BlockPos posOffset, EnumFacing sideMouseover) {
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
      posToPlaceAt = UtilWorld.nextAirInDirection(p.world, posOffset, facing, SpellRangeBuild.max, null);
    }
    return posToPlaceAt;
  }

  public void castFromServer(BlockPos pos, BlockPos posOffset,
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
    BlockPos posToPlaceAt = getPosToPlaceAt(p, posOffset, pos, sideMouseover);
    if (UtilPlaceBlocks.placeStateSafeTEST(world, p, posToPlaceAt, TEST, sideMouseover)) {
      System.out.println("yep the test worked");
      //      SpellRangeBuild.spawnParticle(world, p, pos);
      //      SpellRangeBuild.playSound(world, p, world.getBlockState(posToPlaceAt).getBlock(), posToPlaceAt);
      UtilSound.playSoundPlaceBlock(p, posToPlaceAt, world.getBlockState(posToPlaceAt));
      if (!p.isCreative()) {
        InventoryWand.decrementSlot(heldWand, itemSlot);
      }
    }
  }
}
