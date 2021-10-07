package com.lothrazar.cyclic.util;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class UtilChat {

  public static void addChatMessage(Player player, MutableComponent message) {
    if (player.level.isClientSide) {
      player.sendMessage(message, player.getUUID());
    }
  }

  public static void addChatMessage(Player player, String message) {
    addChatMessage(player, new TranslatableComponent(message));
  }

  public static void addServerChatMessage(Player player, String message) {
    addServerChatMessage(player, new TranslatableComponent(message));
  }

  public static void addServerChatMessage(Player player, MutableComponent message) {
    if (!player.level.isClientSide) {
      player.sendMessage(message, player.getUUID());
    }
  }

  public static String blockPosToString(BlockPos pos) {
    return pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
  }

  public static void sendStatusMessage(Player player, String message) {
    player.displayClientMessage(new TranslatableComponent(message), true);
  }

  public static void sendStatusMessage(Player player, Component nameTextComponent) {
    if (player.level.isClientSide) {
      player.displayClientMessage(nameTextComponent, true);
    }
  }

  public static TranslatableComponent ilang(String message) {
    return new TranslatableComponent(message);
  }

  public static String lang(String message) {
    TranslatableComponent t = new TranslatableComponent(message);
    return t.getString();
  }

  public static void sendFeedback(CommandContext<CommandSourceStack> ctx, String string) {
    ctx.getSource().sendSuccess(new TranslatableComponent(string), false);
  }
}
