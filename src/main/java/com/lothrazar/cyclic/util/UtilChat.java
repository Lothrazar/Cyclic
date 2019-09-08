package com.lothrazar.cyclic.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

public class UtilChat {

  public static void chatMessage(PlayerEntity player, String message) {
    if (player.world.isRemote) {
      player.sendMessage(new TranslationTextComponent((message)));
    }
  }

  public static void statusMessage(PlayerEntity player, String message) {
    if (player.world.isRemote) {
      player.sendStatusMessage(new TranslationTextComponent((message)), true);
    }
  }

  public static String lang(String message) {
    TranslationTextComponent t = new TranslationTextComponent(message);
    return t.getFormattedText();
  }
}
