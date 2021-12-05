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

import com.lothrazar.cyclic.data.CyclicFile;
import com.lothrazar.cyclic.event.PlayerDataEvents;
import com.lothrazar.cyclic.item.inventorycake.ContainerProviderCake;
import com.lothrazar.cyclic.util.UtilChat;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;

public class PacketKeyBind extends PacketBaseCyclic {

  private String action;

  public PacketKeyBind(String s) {
    action = s;
  }

  public static PacketKeyBind decode(FriendlyByteBuf buf) {
    return new PacketKeyBind(buf.readUtf());
  }

  public static void encode(PacketKeyBind msg, FriendlyByteBuf buf) {
    buf.writeUtf(msg.action);
  }

  public static void handle(PacketKeyBind message, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      //rotate type
      ServerPlayer sender = ctx.get().getSender();
      // datfile
      CyclicFile datFile = PlayerDataEvents.getOrCreate(sender);
      if (datFile.storageVisible) {
        NetworkHooks.openGui(sender, new ContainerProviderCake(), sender.blockPosition());
      }
      else {
        UtilChat.addServerChatMessage(sender, "cyclic.unlocks.extended.locked");
      }
    });
    message.done(ctx);
  }
}
