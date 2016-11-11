package com.lothrazar.cyclicmagic.util;
import com.lothrazar.cyclicmagic.ModCyclic;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UtilSound {
  //REF BROKEN http://www.minecraftforge.net/forum/index.php?topic=37547.0
  public static final float VOLUME = 1.0F;
  public static final float PITCH = 1.0F;
  public static final boolean distanceDelay = false;
  public static void playSoundPlaceBlock(EntityPlayer player, BlockPos pos, Block block) {
    if (player == null) {
      ModCyclic.logger.warn("Null player object attempting to play sound [1]");
      return;
    }
    BlockPos here = (pos == null) ? player.getPosition() : pos;
    if (block == null) { return; }
    SoundType type = block.getSoundType(block.getDefaultState(), player.getEntityWorld(), here, player);
    if (type != null && type.getPlaceSound() != null) {
      UtilSound.playSound(player, here, type.getPlaceSound());
    }
  }
  public static void playSound(EntityPlayer player, SoundEvent thunk) {
    playSound(player, player.getPosition(), thunk);
  }
  public static void playSound(EntityPlayer player, BlockPos pos, SoundEvent thunk) {
    BlockPos here = (pos == null) ? player.getPosition() : pos;
    playSound(player, here, thunk, player.getSoundCategory());
  }
  public static void playSound(EntityPlayer player, BlockPos pos, SoundEvent soundIn, SoundCategory cat) {
    if (player == null) {
      ModCyclic.logger.warn("Null player object attempting to play sound [2]");
      return;
    }
    BlockPos here = (pos == null) ? player.getPosition() : pos;
    player.worldObj.playSound(player, here, soundIn, cat, VOLUME, PITCH);
  }
  public static void playSound(EntityPlayer player, BlockPos pos, SoundEvent soundIn, SoundCategory cat, float volume) {
    if (player == null) {
      ModCyclic.logger.warn("Null player object attempting to play sound [3]");
      return;
    }
    player.worldObj.playSound(player, pos, soundIn, cat, volume, PITCH);
  }
  public static void playSound(World worldObj, BlockPos pos, SoundEvent soundIn, SoundCategory category) {
    worldObj.playSound(pos.getX(), pos.getY(), pos.getZ(), soundIn, category, VOLUME, PITCH, distanceDelay);
  }
  public static void playSound(EntityLivingBase villager, BlockPos position, SoundEvent sound) {
    villager.playSound(sound, VOLUME, PITCH);
  }
}
