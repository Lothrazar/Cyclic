package com.lothrazar.cyclicmagic.block;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import net.minecraft.block.material.Material;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSoundSuppress extends BlockBase implements IHasRecipe {
  private static final int VOL_REDUCE_PER_BLOCK = 4;
  private static final int RADIUS = 6;
  //TODO: future upgrade: Filter by category. some types only filter some types of block categories?
  //filter by range? b power? different block types?
  public BlockSoundSuppress() {
    super(Material.CLAY);
    this.myTooltip = UtilChat.lang("tile.block_soundproofing.tooltip") + RADIUS;
  }
  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onPlaySound(PlaySoundEvent event) {
    if (event.getResultSound() == null || event.getResultSound() instanceof ITickableSound || ModCyclic.proxy.getClientWorld() == null) { return; } //long term/repeating/music
    ISound sound = event.getResultSound();
    List<BlockPos> blocks = UtilWorld.findBlocks(ModCyclic.proxy.getClientWorld(), new BlockPos(sound.getXPosF(), sound.getYPosF(), sound.getZPosF()), this, RADIUS);
    if (blocks == null || blocks.size() == 0) { return; }
    try {//WARNING": DO NOT USE getVolume anywhere here it just crashes
      //we do use it inside the sound class, but the engine callss tat later on, and our factor is tacked in
      SoundVolumeControlled newSound = new SoundVolumeControlled(sound);
      //the number of nearby blocks informs how much we muffle the sound by
      float pct = ((float) VOL_REDUCE_PER_BLOCK) / 100F;
      newSound.setVolume(pct / blocks.size());
      event.setResultSound(newSound);
    }
    catch (Exception e) {
      ModCyclic.logger.error("Error trying to detect volume of sound from 3rd party ");
      ModCyclic.logger.error(e.getMessage());
      e.printStackTrace();//getVolume() in naive Positioned sound event gives NPE
    }
  }
  //copy a sound and control its volume
  //because there is no setVolume() fn in ISound... we must clone it
  private static class SoundVolumeControlled implements ISound {
    public float volume;
    public ISound sound;
    public SoundVolumeControlled(ISound s) {
      sound = s;
    }
    public void setVolume(float v) {
      this.volume = v;
    }
    @Override
    public float getVolume() {
      return volume * sound.getVolume();//not from the input, our own control
    }
    @Override
    public ResourceLocation getSoundLocation() {
      return sound.getSoundLocation();
    }
    @Override
    public SoundEventAccessor createAccessor(SoundHandler handler) {
      return sound.createAccessor(handler);
    }
    @Override
    public Sound getSound() {
      return sound.getSound();
    }
    @Override
    public SoundCategory getCategory() {
      return sound.getCategory();
    }
    @Override
    public boolean canRepeat() {
      return sound.canRepeat();
    }
    @Override
    public int getRepeatDelay() {
      return sound.getRepeatDelay();
    }
    @Override
    public float getPitch() {
      return sound.getPitch();
    }
    @Override
    public float getXPosF() {
      return sound.getXPosF();
    }
    @Override
    public float getYPosF() {
      return sound.getYPosF();
    }
    @Override
    public float getZPosF() {
      return sound.getZPosF();
    }
    @Override
    public AttenuationType getAttenuationType() {
      return sound.getAttenuationType();
    }
  }
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ItemStack(this, 8),
        " s ",
        "sos",
        " s ",
        's', new ItemStack(Blocks.WOOL, 1, EnumDyeColor.ORANGE.getMetadata()),
        'o', Blocks.BONE_BLOCK);
  }
}
