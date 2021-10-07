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

import com.lothrazar.cyclic.base.PacketBase;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilPlaceBlocks;
import com.lothrazar.cyclic.util.UtilSound;
import java.util.function.Supplier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketRotateBlock extends PacketBase {

  private BlockPos pos;
  private Direction side;
  private InteractionHand hand;

  public PacketRotateBlock(BlockPos mouseover, Direction s, InteractionHand hand) {
    pos = mouseover;
    side = s;
    this.hand = hand;
  }

  public static PacketRotateBlock decode(FriendlyByteBuf buf) {
    return new PacketRotateBlock(buf.readBlockPos(),
        Direction.values()[buf.readInt()],
        InteractionHand.values()[buf.readInt()]);
  }

  public static void encode(PacketRotateBlock msg, FriendlyByteBuf buf) {
    buf.writeBlockPos(msg.pos);
    buf.writeInt(msg.side.ordinal());
    buf.writeInt(msg.hand.ordinal());
  }

  public static void handle(PacketRotateBlock message, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      //rotate type
      Level world = ctx.get().getSender().level;
      boolean succ = UtilPlaceBlocks.rotateBlockValidState(world, message.pos, message.side);
      if (succ) {
        ServerPlayer player = ctx.get().getSender();
        ItemStack itemStackHeld = player.getItemInHand(message.hand);
        UtilItemStack.damageItem(player, itemStackHeld);
        if (world.getBlockState(message.pos).getSoundType() != null) {
          UtilSound.playSoundFromServer(player, world.getBlockState(message.pos).getSoundType().getPlaceSound());
        }
      }
    });
    message.done(ctx);
  }
}
