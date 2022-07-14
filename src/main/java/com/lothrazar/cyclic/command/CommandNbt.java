package com.lothrazar.cyclic.command;

import java.util.stream.Collectors;
import com.lothrazar.cyclic.util.ChatUtil;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CommandNbt {

  public static int executePrintNbt(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
    ServerPlayer player = ctx.getSource().getPlayerOrException();
    ItemStack held = player.getMainHandItem();
    if (held.hasTag()) {
      ChatUtil.sendFeedback(ctx, held.getTag().toString());
    }
    else {
      ChatUtil.sendFeedback(ctx, "command.cyclic.nbtprint.null");
    }
    return 0;
  }

  public static int executePrintTags(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
    ServerPlayer player = ctx.getSource().getPlayerOrException();
    ItemStack held = player.getMainHandItem();
    for (TagKey<Item> tag : held.getTags().collect(Collectors.toList())) {
      ChatUtil.sendFeedback(ctx, tag.toString());
    }
    return 0;
  }
}
