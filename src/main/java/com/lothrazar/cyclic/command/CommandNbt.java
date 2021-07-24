package com.lothrazar.cyclic.command;

import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class CommandNbt {

  // TODO: send network packet for clipboard. maybe in future
  //      try {
  //        StringSelection selection = new StringSelection(held.getTag().toString()); 
  //        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
  //        clipboard.setContents(selection, selection);
  //      }
  //      catch (Exception e) {
  //        //
  //        ModCyclic.LOGGER.error("?", e);
  //      }
  public static int executePrintNbt(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
    ServerPlayerEntity player = ctx.getSource().asPlayer();
    ItemStack held = player.getHeldItemMainhand();
    if (held.hasTag()) {
      UtilChat.sendFeedback(ctx, held.getTag().toString());
    }
    else {
      UtilChat.sendFeedback(ctx, "command.cyclic.nbtprint.null");
    }
    return 0;
  }

  public static int executePrintTags(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
    ServerPlayerEntity player = ctx.getSource().asPlayer();
    ItemStack held = player.getHeldItemMainhand();
    for (ResourceLocation tag : held.getItem().getTags()) {
      UtilChat.sendFeedback(ctx, tag.toString());
    }
    return 0;
  }
}
