package com.lothrazar.cyclicmagic.block.sound;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import com.lothrazar.cyclicmagic.core.block.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.core.util.UtilSound;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class TileEntitySoundPlayer extends TileEntityBaseMachineInvo implements ITileRedstoneToggle, ITickable {

  private static final int TIMER_MAX = 100;
  private int needsRedstone = 1;
  private int soundIndex = -1;

  public static enum Fields {
    REDSTONE, TIMER, SOUNDINDEX;
  }

  public TileEntitySoundPlayer() {
    super(0);
  }

  public static List<ResourceLocation> getSoundList() {
    // blacklisted:    minecraft:record
    List<ResourceLocation> allSounds = new ArrayList<>();
    for (ResourceLocation r : SoundEvent.REGISTRY.getKeys()) {
      if (!r.toString().contains("minecraft:record"))
        allSounds.add(r);
    }
    //    allSounds.addAll(SoundEvent.REGISTRY.getKeys());
    allSounds.sort(Comparator.comparing(ResourceLocation::toString));
    return allSounds;
  }

  @Override
  public void update() {
    if (isPowered() == false && this.onlyRunIfPowered()) {
      //i need signal to run. i dont have signal. set timer zero so pulse triggers right away
      timer = 0;
    }
    if (this.isRunning() == false) {
      return;
    }
    if (this.updateTimerIsZero()) {
      if (soundIndex >= 0 && soundIndex < SoundEvent.REGISTRY.getKeys().size()) {
        List<ResourceLocation> allSounds = getSoundList();
        //
        ResourceLocation sound = allSounds.get(soundIndex);
        //        ModCyclic.logger.log("SEL" + sound);
        if (sound != null && SoundEvent.REGISTRY.getObject(sound) != null) {
          UtilSound.playSound(world, pos, SoundEvent.REGISTRY.getObject(sound), SoundCategory.BLOCKS);
          timer = TIMER_MAX;
        }
      }
    }
  }

  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }

  @Override
  public void toggleNeedsRedstone() {
    int val = (this.needsRedstone + 1) % 2;
    this.setField(Fields.REDSTONE.ordinal(), val);
  }

  @Override
  public boolean onlyRunIfPowered() {
    return this.needsRedstone == 1;
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case TIMER:
        return timer;
      case REDSTONE:
        return this.needsRedstone;
      case SOUNDINDEX:
        return this.soundIndex;
      default:
      break;
    }
    return -1;
  }

  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case TIMER:
        this.timer = value;
      break;
      case REDSTONE:
        this.needsRedstone = value;
      break;
      case SOUNDINDEX:
        this.soundIndex = value;
      break;
    }
  }

  @Override
  public void readFromNBT(NBTTagCompound tagCompound) {
    super.readFromNBT(tagCompound);
    this.needsRedstone = tagCompound.getInteger(NBT_REDST);
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
    tagCompound.setInteger(NBT_REDST, this.needsRedstone);
    return super.writeToNBT(tagCompound);
  }
}
