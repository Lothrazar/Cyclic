package com.lothrazar.cyclic.block.soundmuff;

import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.config.ConfigRegistry;
import com.lothrazar.cyclic.util.BlockstatesUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SoundmufflerBlock extends BlockCyclic {

  private static final int VOL_REDUCE_PER_BLOCK = 2;
  public static IntValue RADIUS; // 6

  public SoundmufflerBlock(Properties properties) {
    super(properties.strength(1F).sound(SoundType.SCAFFOLDING));
    MinecraftForge.EVENT_BUS.register(this);
  }

  @OnlyIn(Dist.CLIENT)
  @SubscribeEvent
  public void onPlaySound(PlaySoundEvent event) {
    ClientLevel clientWorld = Minecraft.getInstance().level;
    if (event.getSound() == null || clientWorld == null) {
      return;
    }
    SoundInstance sound = event.getSound();
    final boolean isPowered = false; // if im NOT powered, im running
    List<BlockPos> blocks = BlockstatesUtil.findBlocks(clientWorld, new BlockPos(sound.getX(), sound.getY(), sound.getZ()), this,
        RADIUS.get(),
        isPowered);
    if (blocks == null || blocks.size() == 0) {
      return;
    }
    float reduce = VOL_REDUCE_PER_BLOCK;
    float radius = ConfigRegistry.SOUND_RADIUS.get();
    //the number of nearby blocks informs how much we muffle the sound by
    //at 6 blocks, it caps off the reduction
    float volume = (float) (Math.min(reduce / radius, 1.0) / blocks.size());
    rebuildSoundWithVolume(event, sound, volume);
  }

  private static void rebuildSoundWithVolume(PlaySoundEvent event, SoundInstance sound, float newVolume) {
    try {
      //WARNING": DO NOT USE getVolume anywhere here it just crashes
      //we do use it inside the sound class, but the engine calls that later on, and our factor is tacked in
      SoundVolumeControlled newSound = new SoundVolumeControlled(sound);
      newSound.setVolume(newVolume);
      event.setSound(newSound);
    }
    catch (Exception e) {
      ModCyclic.LOGGER.error("Error trying to detect volume of sound " + sound, e);
    }
  }
}
