package com.lothrazar.cyclic.net;

import java.util.function.Supplier;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

//e com.lothrazar.cyclicmagic.net;
public class PacketItemToggle {

  public static void handle(PacketPlayerFalldamage message, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      ServerPlayerEntity player = ctx.get().getSender();
      player.fallDistance = 0.0F;
    });
  }

  public static PacketPlayerFalldamage decode(PacketBuffer buf) {
    return new PacketPlayerFalldamage();
  }

  public static void encode(PacketPlayerFalldamage msg, PacketBuffer buf) {}
}
