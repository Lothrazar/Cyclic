package com.lothrazar.cyclic.block.soundrecord;

import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.config.ConfigRegistry;
import com.lothrazar.cyclic.net.PacketRecordSound;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.library.util.BlockstatesUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.sound.PlaySoundEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

// 26.1: this used to be an instance method on BlockSoundRecorder itself, manually registered onto the
// event bus from the constructor via an FMLEnvironment dist check. That pattern crashes a dedicated
// server with NoClassDefFoundError: net/minecraft/client/multiplayer/ClientLevel - class verification
// loads every method of a class as soon as any instance is constructed, which happens on the server too
// since blocks exist on both sides. NeoForge's @OnlyIn dist-stripping isn't reliable for instance methods
// on a class also constructed on the other side; only the static + @EventBusSubscriber(Dist.CLIENT)
// pattern (already used by the sibling SoundmufflerBlock in this same package) actually avoids the crash,
// so this logic was moved into its own client-only class to match that proven-safe pattern.
@EventBusSubscriber(modid = ModCyclic.MODID, value = Dist.CLIENT)
public class BlockSoundRecorderClient {

  @SubscribeEvent
  public static void onPlaySound(PlaySoundEvent event) {
    ClientLevel clientWorld = Minecraft.getInstance().level;
    if (event.getSound() == null || event.getSound().getIdentifier() == null || event.getSound() instanceof TickableSoundInstance || clientWorld == null) {
      return;
    } //long term/repeating/music
    final boolean isPowered = false; // if im NOT powered, im running
    List<BlockPos> blocks = BlockstatesUtil.findBlocks(clientWorld,
        new BlockPos((int) event.getSound().getX(), (int) event.getSound().getY(), (int) event.getSound().getZ()), BlockRegistry.SOUND_RECORDER.get(),
        ConfigRegistry.RECORDER_RADIUS.get(), isPowered);
    for (BlockPos nearby : blocks) {
      String sid = event.getSound().getIdentifier().toString();
      ClientPacketDistributor.sendToServer(new PacketRecordSound(sid, nearby));
      //hack save to client. otherwise have to hard sync or reload world
      BlockEntity tile = clientWorld.getBlockEntity(nearby);
      if (tile instanceof TileSoundRecorder) {
        ((TileSoundRecorder) tile).onSoundHeard(sid);
      }
    }
  }
}
