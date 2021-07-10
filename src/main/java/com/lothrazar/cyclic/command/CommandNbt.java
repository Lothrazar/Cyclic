package com.lothrazar.cyclic.command;

import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;

public class CommandNbt {

  public static int executePrint(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
    ServerPlayerEntity player = ctx.getSource().asPlayer();
    ItemStack held = player.getHeldItemMainhand();
    if (held.hasTag()) {
      UtilChat.sendFeedback(ctx, held.getTag().toString());
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
    }
    else {
      UtilChat.sendFeedback(ctx, "command.cyclic.nbtprint.null");
    }
    return 1;
  }
}
