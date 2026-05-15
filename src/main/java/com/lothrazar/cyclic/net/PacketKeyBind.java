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

import com.lothrazar.cyclic.event.PlayerDataEvents;
import com.lothrazar.cyclic.filesystem.CyclicFile;
import com.lothrazar.cyclic.item.food.inventorycake.ContainerProviderCake;
import com.lothrazar.library.util.ChatUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PacketKeyBind implements CustomPacketPayload {

  public static final CustomPacketPayload.Type<PacketKeyBind> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(com.lothrazar.cyclic.ModCyclic.MODID, "packet_key_bind"));

  public static final StreamCodec<RegistryFriendlyByteBuf, PacketKeyBind> STREAM_CODEC =
      StreamCodec.of(PacketKeyBind::encode, PacketKeyBind::decode);


  @Override
  public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }


  private String action;

  public PacketKeyBind(String s) {
    action = s;
  }

  public static PacketKeyBind decode(RegistryFriendlyByteBuf buf) {
    return new PacketKeyBind(buf.readUtf());
  }

  public static void encode(RegistryFriendlyByteBuf buf, PacketKeyBind msg) {
    buf.writeUtf(msg.action);
  }

  public static void handle(PacketKeyBind message, IPayloadContext ctx) {
    ctx.enqueueWork(() -> {
      //      ContainerCraf
      //rotate type
      ServerPlayer sender = (ServerPlayer) ctx.player();
      // datfile
      CyclicFile datFile = PlayerDataEvents.getOrCreate(sender);
      if (datFile.storageVisible) {
        sender.openMenu(new ContainerProviderCake());
      }
      else {
        ChatUtil.addServerChatMessage(sender, "cyclic.unlocks.extended.locked");
        //        Triple<String, Integer, ItemStack> result = CharmUtil.isCurioOrInventory(sender, ItemRegistry.CRAFTING_STICK.get());
        //        if (!result.getRight().isEmpty())
        //          NetworkHooks.openGui(sender, new CraftingStickContainerProvider(null), sender.blockPosition());
      }
    });
    
  }
}
