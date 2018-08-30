package com.lothrazar.cyclicmagic.gui.core;

import net.minecraft.client.gui.GuiButton;

public class ButtonTriggerWrapper {

  public static enum ButtonTriggerType {
    GREATER, LESS, EQUAL, NOTEQUAL;
  }

  public GuiButton btn;
  public ButtonTriggerWrapper.ButtonTriggerType trig;
  public int fld;
  public int triggerValue;

  public ButtonTriggerWrapper(GuiButton buttonIn, ButtonTriggerWrapper.ButtonTriggerType trigger, int fieldId, int tval) {
    this.btn = buttonIn;
    this.trig = trigger;
    this.fld = fieldId;
    this.triggerValue = tval;
  }
}