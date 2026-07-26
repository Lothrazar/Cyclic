package com.lothrazar.cyclic.block.soundmuff;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.config.ConfigRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.sound.PlaySoundEvent;
import net.neoforged.fml.common.EventBusSubscriber;

// 26.1: moved out of SoundmufflerBlock itself - a class file referencing client-only types
// (ClientLevel/SoundInstance) in ANY of its methods, even static + @EventBusSubscriber(Dist.CLIENT)
// ones, crashes a dedicated server with NoClassDefFoundError as soon as that class is loaded/verified,
// which happens the moment any instance is constructed. Since SoundmufflerBlock is a Block and gets
// constructed on the server too, its client-only logic has to live in a fully separate class that the
// server never has a reason to load (matches the same fix applied to BlockSoundRecorder/
// BlockSoundRecorderClient in the sibling soundrecord package).
@EventBusSubscriber(value = Dist.CLIENT, modid = ModCyclic.MODID)
public class SoundmufflerBlockClient {

  private static final int VOL_REDUCE_PER_BLOCK = 2;

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
