package com.lothrazar.cyclic.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SPlaySoundEffectPacket;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

public class UtilSound {

  public static void playSound(PlayerEntity entityIn, SoundEvent soundIn) {
    playSound(entityIn, entityIn.getPosition(), soundIn);
  }

  public static void playSound(Entity entityIn, BlockPos position, SoundEvent soundIn) {
    if (entityIn != null) {
      entityIn.playSound(soundIn, 1.0F, 1.0F);
    }
  }

  public static void playSoundFromServer(ServerPlayerEntity entityIn, SoundEvent soundIn) {
    entityIn.connection.sendPacket(new SPlaySoundEffectPacket(
        soundIn,
        SoundCategory.BLOCKS,
        entityIn.lastTickPosX, entityIn.lastTickPosY, entityIn.lastTickPosZ,
        1.0f, 1.0f));
  }
}
