package com.lothrazar.cyclic.capabilities.player;

import java.util.ArrayList;
import java.util.List;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class PlayerCyclicAttachment {

  public static final Codec<PlayerCyclicAttachment> CODEC = RecordCodecBuilder.create(inst -> inst.group(
      Codec.BOOL.optionalFieldOf("storageVisible", false).forGetter(a -> a.storageVisible),
      Codec.BOOL.optionalFieldOf("stepHeight", false).forGetter(a -> a.stepHeight),
      Codec.BOOL.optionalFieldOf("stepHeightForceOff", false).forGetter(a -> a.stepHeightForceOff),
      Codec.STRING.listOf().optionalFieldOf("todoTasks", List.of()).forGetter(a -> a.todoTasks)
  ).apply(inst, PlayerCyclicAttachment::new));

  public boolean storageVisible;
  public boolean stepHeight;
  public boolean stepHeightForceOff;
  public List<String> todoTasks;

  public PlayerCyclicAttachment() {
    this(false, false, false, List.of());
  }

  public PlayerCyclicAttachment(boolean storageVisible, boolean stepHeight, boolean stepHeightForceOff, List<String> todoTasks) {
    this.storageVisible = storageVisible;
    this.stepHeight = stepHeight;
    this.stepHeightForceOff = stepHeightForceOff;
    this.todoTasks = new ArrayList<>(todoTasks);
  }

  public void toggleStepHeight() {
    this.stepHeight = !this.stepHeight;
    if (!this.stepHeight) {
      this.stepHeightForceOff = true;
    }
  }
}
