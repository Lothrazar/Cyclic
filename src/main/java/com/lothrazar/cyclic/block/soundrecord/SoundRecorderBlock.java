package com.lothrazar.cyclic.block.soundrecord;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.util.UtilBlockstates;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SoundRecorderBlock extends BlockBase {

  private static final int RADIUS = 2;

  public SoundRecorderBlock(Properties properties) {
    super(properties.hardnessAndResistance(1F).sound(SoundType.SCAFFOLDING));
    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new SoundRecorderTile();
  }

  @OnlyIn(Dist.CLIENT)
  @SubscribeEvent
  public void onPlaySound(PlaySoundEvent event) {
    ClientWorld clientWorld = Minecraft.getInstance().world;
    if (event.getResultSound() == null || event.getResultSound() instanceof ITickableSound || clientWorld == null) {
      return;
    } //long term/repeating/music
    ISound sound = event.getResultSound();
    List<BlockPos> blocks = UtilBlockstates.findBlocks(clientWorld, new BlockPos(sound.getX(), sound.getY(), sound.getZ()), this, RADIUS);
    if (blocks == null || blocks.size() == 0) {
      return;
    }
    BlockPos nearby = blocks.stream().findFirst().orElse(null);
    if (nearby != null) {
      //tile=clientWorld
      System.out.println(sound.getSound().getSoundLocation() + "Net packet with sound id to server on " + nearby);
    }
    //    try {
    //      //WARNING": DO NOT USE getVolume anywhere here it just crashes
    //      //we do use it inside the sound class, but the engine callss tat later on, and our factor is tacked in
    //      SoundVolumeControlled newSound = new SoundVolumeControlled(sound);
    //      //the number of nearby blocks informs how much we muffle the sound by
    //      float pct = (VOL_REDUCE_PER_BLOCK) / 6F;
    //      //at 6 blocks, it caps off the reduction
    //      newSound.setVolume((float) (Math.min(pct, 1.0) / blocks.size()));
    //      event.setResultSound(newSound);
    //    }
    //    catch (Exception e) {
    //      ModCyclic.LOGGER.error("Error trying to detect volume of sound from 3rd party ", e);
    //    }
  }
}
