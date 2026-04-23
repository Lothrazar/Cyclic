package com.lothrazar.cyclic.gui;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;

public class ButtonMachineField extends Button {
  public ButtonMachineField(int x, int y, int width, int height, Component title, OnPress onPress) { super(x, y, width, height, title, onPress, Button.DEFAULT_NARRATION); }

  public ButtonMachineField(int x, int y, int field, BlockPos pos) {
    super(x, y, 20, 20, Component.literal(""), b -> {}, Button.DEFAULT_NARRATION);
  }
  public ButtonMachineField(int x, int y, int field, BlockPos pos, int w, int h) {
    super(x, y, w, h, Component.literal(""), b -> {}, Button.DEFAULT_NARRATION);
  }
  public ButtonMachineField(int x, int y, int field, BlockPos pos, TextureEnum t1, TextureEnum t2, String tooltip) {
    super(x, y, 20, 20, Component.literal(""), b -> {}, Button.DEFAULT_NARRATION);
  }
  public void onValueUpdate(Object tile) {}
}
