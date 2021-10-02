package com.lothrazar.cyclic.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SPlaySoundEffectPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;

public class UtilSound {

  public static void playSound(World world, BlockPos pos, SoundEvent soundIn) {
    world.playSound(pos.getX(), pos.getY(), pos.getZ(), soundIn, SoundCategory.BLOCKS, 0.5F, 0.5F, false);
  }

  public static void playSound(World world, BlockPos pos, SoundEvent soundIn, float volume) {
    world.playSound(pos.getX(), pos.getY(), pos.getZ(), soundIn, SoundCategory.BLOCKS, volume, 0.5F, false);
  }

  public static void playSound(Entity entityIn, SoundEvent soundIn) {
    playSound(entityIn, soundIn, 1.0F, 1.0F);
  }

  public static void playSound(Entity entityIn, SoundEvent soundIn, float volume) {
    playSound(entityIn, soundIn, volume, 1.0F);
  }

  public static void playSound(Entity entityIn, SoundEvent soundIn, float volume, float pitch) {
    if (entityIn != null && entityIn.world.isRemote) {
      entityIn.playSound(soundIn, volume, pitch);
    }
  }

  public static void playSoundFromServer(ServerPlayerEntity entityIn, BlockPos pos, SoundEvent soundIn) {
    if (soundIn == null || entityIn == null) {
      return;
    }
    entityIn.connection.sendPacket(new SPlaySoundEffectPacket(
        soundIn,
        SoundCategory.BLOCKS,
        pos.getX(), pos.getY(), pos.getZ(),
        1.0f, 1.0f));
  }

  public static void playSoundFromServer(ServerPlayerEntity entityIn, SoundEvent soundIn) {
    if (soundIn == null || entityIn == null) {
      return;
    }
    entityIn.connection.sendPacket(new SPlaySoundEffectPacket(
        soundIn,
        SoundCategory.BLOCKS,
        entityIn.lastTickPosX, entityIn.lastTickPosY, entityIn.lastTickPosZ,
        1.0f, 1.0f));
  }

  public static void playSoundFromServer(ServerWorld world, BlockPos pos, SoundEvent soundIn) {
    for (ServerPlayerEntity sp : world.getPlayers()) {
      playSoundFromServer(sp, pos, soundIn);
    }
  }

  public static void playSoundFromServerById(ServerWorld world, BlockPos pos, String sid) {
    SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(sid));
    if (sound != null) {
      for (ServerPlayerEntity sp : world.getPlayers()) {
        playSoundFromServer(sp, pos, sound);
      }
    }
  }

  public static void playSoundById(PlayerEntity player, String sid) {
    //do the thing
    SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(sid));
    if (sound != null && player.world.isRemote) {
      UtilSound.playSound(player, sound);
    }
  }
}
