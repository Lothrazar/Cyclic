package com.lothrazar.cyclic.command;

import com.lothrazar.cyclic.item.heart.HeartItem;
import com.mojang.brigadier.context.CommandContext;
import java.util.Collection;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.ServerPlayerEntity;

public class CommandHealth {

  public static int execute(CommandContext<CommandSource> ctx, Collection<ServerPlayerEntity> players, float newlevel) {
    for (ServerPlayerEntity player : players) {
      player.setHealth(newlevel);
    }
    return 0;
  }

  public static int executeHearts(CommandContext<CommandSource> x, Collection<ServerPlayerEntity> players, int finalHearts) {
    int modifiedHearts = finalHearts - 10;
    //
    //
    for (ServerPlayerEntity playerIn : players) {
      ModifiableAttributeInstance healthAttribute = playerIn.getAttribute(Attributes.MAX_HEALTH);
      //get attribute modif by id
      //      AttributeModifier oldHealthModifier = healthAttribute.getModifier(HeartItem.ID);
      healthAttribute.removeModifier(HeartItem.ID);
      //just remove and replace the modifier
      AttributeModifier healthModifier = new AttributeModifier(HeartItem.ID, "HP Bonus from Cyclic", (modifiedHearts * 2), AttributeModifier.Operation.ADDITION);
      healthAttribute.applyPersistentModifier(healthModifier);
      //finish up
    }
    return 0;
  }
}
