/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic.util;

import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.net.PacketSound;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class UtilSound {

  //REF BROKEN http://www.minecraftforge.net/forum/index.php?topic=37547.0
  public static final float VOLUME = 1.0F;
  public static final float PITCH = 1.0F;
  public static final boolean distanceDelay = false;
  public static final int RANGE_DEFAULT = 18;

  public static void playSoundPlaceBlock(EntityPlayer player, BlockPos pos, Block block) {
    if (player == null) {
      return;
    }
    BlockPos here = (pos == null) ? player.getPosition() : pos;
    if (block == null) {
      return;
    }
    SoundType type = block.getSoundType(block.getDefaultState(), player.getEntityWorld(), here, player);
    if (type != null && type.getPlaceSound() != null) {
      UtilSound.playSound(player, here, type.getPlaceSound());
    }
  }

  public static void playSoundPlaceBlock(World world, BlockPos pos, Block block) {
    if (block == null) {
      return;
    }
    SoundType type = block.getSoundType(block.getDefaultState(), world, pos, null);
    if (type != null && type.getPlaceSound() != null) {
      UtilSound.playSound(world, pos, type.getPlaceSound(), SoundCategory.BLOCKS);
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
      return;
    }
    BlockPos here = (pos == null) ? player.getPosition() : pos;
    player.getEntityWorld().playSound(player, here, soundIn, cat, VOLUME, PITCH);
  }

  public static void playSound(EntityPlayer player, BlockPos pos, SoundEvent soundIn, SoundCategory cat, float volume) {
    if (player == null) {
      return;
    }
    player.getEntityWorld().playSound(player, pos, soundIn, cat, volume, PITCH);
  }

  public static void playSound(World worldObj, BlockPos pos, SoundEvent soundIn, SoundCategory category) {
    if (pos != null && soundIn != null && category != null)// https://github.com/PrinceOfAmber/Cyclic/issues/173
      worldObj.playSound(pos.getX(), pos.getY(), pos.getZ(), soundIn, category, VOLUME, PITCH, distanceDelay);
  }

  public static void playSound(EntityLivingBase villager, BlockPos position, SoundEvent sound) {
    villager.playSound(sound, VOLUME, PITCH);
  }

  public static void playSound(EntityLivingBase villager, SoundEvent sound) {
    villager.playSound(sound, VOLUME, PITCH);
  }

  public static SoundType getSoundFromBlockstate(IBlockState placeState, World world, BlockPos pos) {
    return placeState.getBlock().getSoundType(placeState, world, pos, null);
  }

  public static void playSoundFromServer(SoundEvent soundEvent, SoundCategory cat, BlockPos nextPos, int dim, int range) {
    //https://github.com/PrinceOfAmber/Cyclic/issues/506
    if (soundEvent != null && soundEvent.getRegistryName() != null) // dont crash trying to play invalid sound
      ModCyclic.network.sendToAllAround(new PacketSound(nextPos, soundEvent, cat),
          new NetworkRegistry.TargetPoint(dim, nextPos.getX(), nextPos.getY(), nextPos.getZ(), range));
  }
}
