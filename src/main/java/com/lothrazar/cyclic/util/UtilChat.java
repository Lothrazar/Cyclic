package com.lothrazar.cyclic.util;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class UtilChat {

  public static void addChatMessage(PlayerEntity player, IFormattableTextComponent message) {
    if (player.world.isRemote) {
      player.sendMessage(message, player.getUniqueID());
    }
  }

  public static void addChatMessage(PlayerEntity player, String message) {
    addChatMessage(player, new TranslationTextComponent(message));
  }

  public static void addServerChatMessage(PlayerEntity player, String message) {
    addServerChatMessage(player, new TranslationTextComponent(message));
  }

  public static void addServerChatMessage(PlayerEntity player, IFormattableTextComponent message) {
    if (!player.world.isRemote) {
      player.sendMessage(message, player.getUniqueID());
    }
  }

  public static String blockPosToString(BlockPos pos) {
    return pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
  }

  public static void sendStatusMessage(PlayerEntity player, String message) {
    player.sendStatusMessage(new TranslationTextComponent(message), true);
  }

  public static void sendStatusMessage(PlayerEntity player, ITextComponent nameTextComponent) {
    if (player.world.isRemote) {
      player.sendStatusMessage(nameTextComponent, true);
    }
  }

  public static TranslationTextComponent ilang(String message) {
    return new TranslationTextComponent(message);
  }

  public static String lang(String message) {
    TranslationTextComponent t = new TranslationTextComponent(message);
    return t.getString();
  }

  public static void sendFeedback(CommandContext<CommandSource> ctx, String string) {
    ctx.getSource().sendFeedback(new TranslationTextComponent(string), false);
  }
}
