package com.lothrazar.cyclic.block.soundmuff;

import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;

public class SoundVolumeControlled implements SoundInstance {

  public float volume;
  public SoundInstance sound;

  public SoundVolumeControlled(SoundInstance s) {
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
  public ResourceLocation getLocation() {
    return sound.getLocation();
  }

  @Override
  public WeighedSoundEvents resolve(SoundManager handler) {
    return sound.resolve(handler);
  }

  @Override
  public Sound getSound() {
    return sound.getSound();
  }

  @Override
  public SoundSource getSource() {
    return sound.getSource();
  }

  @Override
  public boolean isLooping() {
    return sound.isLooping();
  }

  @Override
  public int getDelay() {
    return sound.getDelay();
  }

  @Override
  public float getPitch() {
    return sound.getPitch();
  }

  @Override
  public Attenuation getAttenuation() {
    return sound.getAttenuation();
  }

  @Override
  public boolean isRelative() {
    return sound.isRelative();
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
