package com.lothrazar.cyclic.command;

import com.lothrazar.cyclic.item.heart.HeartItem;
import com.mojang.brigadier.context.CommandContext;
import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class CommandHealth {

  public static int execute(CommandContext<CommandSourceStack> ctx, Collection<ServerPlayer> players, float newlevel) {
    for (ServerPlayer player : players) {
      player.setHealth(newlevel);
    }
    return 0;
  }

  public static int executeHearts(CommandContext<CommandSourceStack> x, Collection<ServerPlayer> players, int finalHearts) {
    int modifiedHearts = finalHearts - 10;
    //
    //
    for (ServerPlayer playerIn : players) {
      AttributeInstance healthAttribute = playerIn.getAttribute(Attributes.MAX_HEALTH);
      //get attribute modif by id
      //      AttributeModifier oldHealthModifier = healthAttribute.getModifier(HeartItem.ID);
      healthAttribute.removeModifier(HeartItem.ID);
      //just remove and replace the modifier
      AttributeModifier healthModifier = new AttributeModifier(HeartItem.ID, "HP Bonus from Cyclic", (modifiedHearts * 2), AttributeModifier.Operation.ADDITION);
      healthAttribute.addPermanentModifier(healthModifier);
      //finish up
    }
    return 0;
  }
}
