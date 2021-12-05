package com.lothrazar.cyclic.net;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

/**
 * Used by: Fan block; Launch enchant; Air charm; Climbing Glove; Scaffolding Block
 */
public class PacketPlayerFalldamage extends PacketBaseCyclic {

  public static final int TICKS_FALLDIST_SYNC = 22; //tick every so often

  public static void handle(PacketPlayerFalldamage message, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      ServerPlayer player = ctx.get().getSender();
      /**
       * if fall damage gets high, they take damage on landing
       */
      player.fallDistance = 0.0F;
      /**
       * Used to keep track of how the player is floating while gamerules should prevent that. Surpassing 80 ticks means kick
       */
      player.connection.aboveGroundTickCount = 0;
    });
    message.done(ctx);
  }

  public static PacketPlayerFalldamage decode(FriendlyByteBuf buf) {
    PacketPlayerFalldamage message = new PacketPlayerFalldamage();
    return message;
  }

  public static void encode(PacketPlayerFalldamage msg, FriendlyByteBuf buf) {}
}
