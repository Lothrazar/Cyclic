package com.lothrazar.cyclic.block.soundmuff;

import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.util.UtilBlockstates;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SoundmufflerBlock extends BlockCyclic {

  private static final int VOL_REDUCE_PER_BLOCK = 2;
  private static final int RADIUS = 6;

  public SoundmufflerBlock(Properties properties) {
    super(properties.strength(1F).sound(SoundType.SCAFFOLDING));
    MinecraftForge.EVENT_BUS.register(this);
  }

  @OnlyIn(Dist.CLIENT)
  @SubscribeEvent
  public void onPlaySound(PlaySoundEvent event) {
    ClientLevel clientWorld = Minecraft.getInstance().level;
    if (event.getResultSound() == null || event.getResultSound() instanceof TickableSoundInstance || clientWorld == null) {
      return;
    } //long term/repeating/music
    SoundInstance sound = event.getResultSound();
    List<BlockPos> blocks = UtilBlockstates.findBlocks(clientWorld, new BlockPos(sound.getX(), sound.getY(), sound.getZ()), this, RADIUS);
    if (blocks == null || blocks.size() == 0) {
      return;
    }
    try {
      //WARNING": DO NOT USE getVolume anywhere here it just crashes
      //we do use it inside the sound class, but the engine callss tat later on, and our factor is tacked in
      SoundVolumeControlled newSound = new SoundVolumeControlled(sound);
      //the number of nearby blocks informs how much we muffle the sound by
      float pct = (VOL_REDUCE_PER_BLOCK) / 6F;
      //at 6 blocks, it caps off the reduction
      newSound.setVolume((float) (Math.min(pct, 1.0) / blocks.size()));
      event.setResultSound(newSound);
    }
    catch (Exception e) {
      ModCyclic.LOGGER.error("Error trying to detect volume of sound from 3rd party ", e);
    }
  }
}
