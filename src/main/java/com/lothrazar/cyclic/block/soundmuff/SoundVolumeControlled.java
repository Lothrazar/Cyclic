package com.lothrazar.cyclic.block.soundmuff;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

//copy a sound and control its volume
//because there is no setVolume() fn in ISound... we must clone it
public class SoundVolumeControlled implements ISound {

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
    return volume * sound.getVolume(); //not from the input, our own control
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
  public AttenuationType getAttenuationType() {
    return sound.getAttenuationType();
  }

  @Override
  public boolean isGlobal() {
    return sound.isGlobal();
  }

  @Override
  public double getX() {
    return sound.getX();
  }

  @Override
  public double getY() {
    return sound.getY();
  }

  @Override
  public double getZ() {
    return sound.getZ();
  }
}