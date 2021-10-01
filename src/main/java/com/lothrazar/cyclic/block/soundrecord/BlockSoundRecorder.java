package com.lothrazar.cyclic.block.soundrecord;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.net.PacketRecordSound;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.util.UtilBlockstates;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.block.SoundType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BlockSoundRecorder extends BlockBase {

  private static final int RADIUS = 8; // TODO: config

  public BlockSoundRecorder(Properties properties) {
    super(properties.hardnessAndResistance(1F).sound(SoundType.SCAFFOLDING));
    MinecraftForge.EVENT_BUS.register(this);
    this.setHasGui();
    NoteBlock xy;
  }

  @Override
  public void registerClient() {
    ScreenManager.registerFactory(ContainerScreenRegistry.SOUND_RECORDER, ScreenSoundRecorder::new);
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileSoundRecorder();
  }

  @OnlyIn(Dist.CLIENT)
  @SubscribeEvent
  public void onPlaySound(PlaySoundEvent event) {
    ClientWorld clientWorld = Minecraft.getInstance().world;
    if (event.getSound() == null || event.getSound().getSoundLocation() == null || event.getResultSound() instanceof ITickableSound || clientWorld == null) {
      return;
    } //long term/repeating/music
    List<BlockPos> blocks = UtilBlockstates.findBlocks(clientWorld, new BlockPos(event.getSound().getX(), event.getSound().getY(), event.getSound().getZ()), this, RADIUS);
    for (BlockPos nearby : blocks) {
      String sid = event.getSound().getSoundLocation().toString();
      //TODO: only send packet if block is in 'listening' / 'lit' blocksate?
      PacketRegistry.INSTANCE.sendToServer(new PacketRecordSound(sid, nearby));
      //hack save to client. otherwise have to hard sync or reload world
      TileEntity tile = clientWorld.getTileEntity(nearby);
      if (tile instanceof TileSoundRecorder) {
        ((TileSoundRecorder) tile).onSoundHeard(sid);
      }
    }
  }
}
