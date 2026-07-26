package com.lothrazar.cyclic.capabilities.player;

import java.util.ArrayList;
import java.util.List;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class PlayerCyclicAttachment {

  // 26.1: AttachmentType.Builder#serialize now wants a MapCodec, not a plain Codec - use
  // RecordCodecBuilder.mapCodec(...) instead of .create(...) (record fields are already map-shaped).
  public static final MapCodec<PlayerCyclicAttachment> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
      Codec.BOOL.optionalFieldOf("stepHeight", false).forGetter(a -> a.stepHeight),
      Codec.BOOL.optionalFieldOf("stepHeightForceOff", false).forGetter(a -> a.stepHeightForceOff),
      Codec.STRING.listOf().optionalFieldOf("todoTasks", List.of()).forGetter(a -> a.todoTasks)
  ).apply(inst, PlayerCyclicAttachment::new));

  public boolean stepHeight;
  public boolean stepHeightForceOff;
  public List<String> todoTasks;

  public PlayerCyclicAttachment() {
    this(false, false, List.of());
  }

  public PlayerCyclicAttachment(boolean stepHeight, boolean stepHeightForceOff, List<String> todoTasks) {
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
