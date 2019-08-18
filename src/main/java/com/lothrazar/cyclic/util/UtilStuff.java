package com.lothrazar.cyclic.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
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

  public static Direction getFacingFromEntity(BlockPos clickedBlock, LivingEntity entity) {
    return Direction.getFacingFromVector((float) (entity.posX - clickedBlock.getX()), (float) (entity.posY - clickedBlock.getY()), (float) (entity.posZ - clickedBlock.getZ()));
  }

  public static Direction getFacingFromEntityHorizontal(BlockPos clickedBlock, LivingEntity entity) {
    Direction d = getFacingFromEntity(clickedBlock, entity);
    //if only horizontal is allowed
    if (d == Direction.UP || d == Direction.DOWN) {
      return entity.getHorizontalFacing().getOpposite();
    }
    return d;
  }
}
