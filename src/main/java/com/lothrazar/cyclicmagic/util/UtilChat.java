package com.lothrazar.cyclicmagic.util;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
//import net.minecraft.client.resources.I18n;
import net.minecraft.world.World;

public class UtilChat {
  public static void addChatMessage(EntityPlayer player, String text) {
    player.addChatMessage(new TextComponentTranslation(lang(text)));
  }
  public static void addChatMessage(ICommandSender sender, String text) {
    sender.addChatMessage(new TextComponentTranslation(lang(text)));
  }
  public static void addChatMessage(EntityPlayer player, ITextComponent textComponentTranslation) {
    player.addChatMessage(textComponentTranslation);
  }
  public static String blockPosToString(BlockPos pos) {
    return pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
  }
  public static String lang(String string) {
    //if we use the clientside one, it literally does not work & crashes on serverside run
    return I18n.translateToLocal(string);
  }
  public static void addChatMessage(World worldObj, ITextComponent textComponentTranslation) {
    if (worldObj.getMinecraftServer() != null) {
      worldObj.getMinecraftServer().addChatMessage(textComponentTranslation);
    }
    // else it is a client side world; cant do it
  }
  public static List<String> splitIntoLine(String input, int maxCharInLine) {
    // https://stackoverflow.com/questions/7528045/large-string-split-into-lines-with-maximum-length-in-java
    // better than spell.getInfo().split("(?<=\\G.{25})")
    StringTokenizer tok = new StringTokenizer(input, " ");
    StringBuilder output = new StringBuilder(input.length());
    int lineLen = 0;
    while (tok.hasMoreTokens()) {
      String word = tok.nextToken();
      while (word.length() > maxCharInLine) {
        output.append(word.substring(0, maxCharInLine - lineLen) + "\n");
        word = word.substring(maxCharInLine - lineLen);
        lineLen = 0;
      }
      if (lineLen + word.length() > maxCharInLine) {
        output.append("\n");
        lineLen = 0;
      }
      output.append(word + " ");
      lineLen += word.length() + 1;
    }
    // output.split();
    // return output.toString();
    return Arrays.asList(output.toString().split("\n"));
  }
}
