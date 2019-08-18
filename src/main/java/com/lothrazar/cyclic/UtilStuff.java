package com.lothrazar.cyclic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

public class UtilStuff {

  public static String lang(String message) {
    TranslationTextComponent t = new TranslationTextComponent(message);
    return t.getFormattedText();
  }

  public static void messageChat(PlayerEntity player, String message) {
    player.sendMessage(new TranslationTextComponent(message));
  }

  public static void messageStatus(PlayerEntity player, String message) {
    player.sendStatusMessage(new TranslationTextComponent(message), true);
  }
}
