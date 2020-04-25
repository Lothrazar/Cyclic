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
package com.lothrazar.cyclic.net;

import java.util.function.Supplier;
import com.lothrazar.cyclic.base.PacketBase;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilPlaceBlocks;
import com.lothrazar.cyclic.util.UtilSound;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketRotateBlock extends PacketBase {

  private BlockPos pos;
  private Direction side;
  private Hand hand;

  public PacketRotateBlock(BlockPos mouseover, Direction s, Hand hand) {
    pos = mouseover;
    side = s;
    this.hand = hand;
  }

  public static PacketRotateBlock decode(PacketBuffer buf) {
    return new PacketRotateBlock(buf.readBlockPos(),
        Direction.values()[buf.readInt()],
        Hand.values()[buf.readInt()]);
  }

  public static void encode(PacketRotateBlock msg, PacketBuffer buf) {
    buf.writeBlockPos(msg.pos);
    buf.writeInt(msg.side.ordinal());
    buf.writeInt(msg.hand.ordinal());
  }

  public static void handle(PacketRotateBlock message, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      //rotate type
      World world = ctx.get().getSender().world;
      boolean succ = UtilPlaceBlocks.rotateBlockValidState(world, message.pos, message.side);
      if (succ) {
        ServerPlayerEntity player = ctx.get().getSender();
        ItemStack itemStackHeld = player.getHeldItem(message.hand);
        UtilItemStack.damageItem(player, itemStackHeld);
        if (world.getBlockState(message.pos).getSoundType() != null)
          UtilSound.playSoundFromServer(player, world.getBlockState(message.pos).getSoundType().getPlaceSound());
      }
    });
    message.done(ctx);
  }
}
