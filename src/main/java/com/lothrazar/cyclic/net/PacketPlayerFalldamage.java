package com.lothrazar.cyclic.net;

import java.util.function.Supplier;
import com.lothrazar.cyclic.base.PacketBase;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketPlayerFalldamage extends PacketBase {

  public static void handle(PacketPlayerFalldamage message, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      ServerPlayerEntity player = ctx.get().getSender();
      player.fallDistance = 0.0F;
    });
    message.done(ctx);
  }

  public static PacketPlayerFalldamage decode(PacketBuffer buf) {
    PacketPlayerFalldamage message = new PacketPlayerFalldamage();
    return message;
  }

  public static void encode(PacketPlayerFalldamage msg, PacketBuffer buf) {}
}
