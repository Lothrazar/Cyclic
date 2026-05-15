package com.lothrazar.cyclic.block.soundmuff;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.config.ConfigRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.sound.PlaySoundEvent;
import net.neoforged.fml.common.EventBusSubscriber;


@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME, modid = ModCyclic.MODID)
public class SoundmufflerBlock extends BlockCyclic {

  private static final int VOL_REDUCE_PER_BLOCK = 2;

  public SoundmufflerBlock(Properties properties) {
    super(properties.strength(1F).sound(SoundType.SCAFFOLDING));
  }

  @OnlyIn(Dist.CLIENT)
  @SubscribeEvent
  public static void onPlaySound(PlaySoundEvent event) {
    ClientLevel clientWorld = Minecraft.getInstance().level;
    if (event.getSound() == null || clientWorld == null) {
      return;
    }
    SoundInstance sound = event.getSound();
    
    int radius = ConfigRegistry.SOUND_RADIUS.get();
    int count = 0;
    BlockPos start = new BlockPos((int) sound.getX(), (int) sound.getY(), (int) sound.getZ());
    
    int xMin = start.getX() - radius;
    int xMax = start.getX() + radius;
    int yMin = start.getY() - radius;
    int yMax = start.getY() + radius;
    int zMin = start.getZ() - radius;
    int zMax = start.getZ() + radius;
    
    for (int xLoop = xMin; xLoop <= xMax; xLoop++) {
      for (int yLoop = yMin; yLoop <= yMax; yLoop++) {
        for (int zLoop = zMin; zLoop <= zMax; zLoop++) {
          BlockPos pos = new BlockPos(xLoop, yLoop, zLoop);
          if (clientWorld.getBlockState(pos).getBlock() instanceof SoundmufflerBlock) {
            if (!clientWorld.hasNeighborSignal(pos)) {
              count++;
            }
          }
        }
      }
    }
    
    if (count == 0) {
      return;
    }
    
    float reduce = VOL_REDUCE_PER_BLOCK;
    float volume = (float) (Math.min(reduce / radius, 1.0) / count);
    rebuildSoundWithVolume(event, sound, volume);
  }

  @OnlyIn(Dist.CLIENT)
  private static void rebuildSoundWithVolume(PlaySoundEvent event, SoundInstance sound, float newVolume) {
    try {
      SoundVolumeControlled newSound = new SoundVolumeControlled(sound);
      newSound.setVolume(newVolume);
      event.setSound(newSound);
    }
    catch (Exception e) {
      ModCyclic.LOGGER.error("Error trying to detect volume of sound " + sound, e);
    }
  }
}
