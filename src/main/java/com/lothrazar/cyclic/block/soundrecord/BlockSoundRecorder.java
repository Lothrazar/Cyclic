package com.lothrazar.cyclic.block.soundrecord;

import java.util.List;
import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.net.PacketRecordSound;
import com.lothrazar.cyclic.registry.MenuTypeRegistry;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.util.BlockstatesUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BlockSoundRecorder extends BlockCyclic {

  public static IntValue RADIUS;

  public BlockSoundRecorder(Properties properties) {
    super(properties.strength(1F).sound(SoundType.SCAFFOLDING));
    MinecraftForge.EVENT_BUS.register(this);
    this.setHasGui();
  }

  @Override
  public void registerClient() {
    MenuScreens.register(MenuTypeRegistry.SOUND_RECORDER.get(), ScreenSoundRecorder::new);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new TileSoundRecorder(pos, state);
  }

  @OnlyIn(Dist.CLIENT)
  @SubscribeEvent
  public void onPlaySound(PlaySoundEvent event) {
    ClientLevel clientWorld = Minecraft.getInstance().level;
    if (event.getSound() == null || event.getSound().getLocation() == null || event.getSound() instanceof TickableSoundInstance || clientWorld == null) {
      return;
    } //long term/repeating/music
    List<BlockPos> blocks = BlockstatesUtil.findBlocks(clientWorld,
        new BlockPos(event.getSound().getX(), event.getSound().getY(), event.getSound().getZ()), this, RADIUS.get());
    for (BlockPos nearby : blocks) {
      String sid = event.getSound().getLocation().toString();
      PacketRegistry.INSTANCE.sendToServer(new PacketRecordSound(sid, nearby));
      //hack save to client. otherwise have to hard sync or reload world
      BlockEntity tile = clientWorld.getBlockEntity(nearby);
      if (tile instanceof TileSoundRecorder) {
        ((TileSoundRecorder) tile).onSoundHeard(sid);
      }
    }
  }
}
