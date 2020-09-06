package com.lothrazar.cyclic.gui;

import com.lothrazar.cyclic.net.PacketTileData;
import com.lothrazar.cyclic.registry.PacketRegistry;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractSlider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class GuiSliderInteger extends AbstractSlider {

  private final double min;
  private final double max;
  private final BlockPos pos;
  private final int field;

  public GuiSliderInteger(int x, int y, int width, int height, int field,
      BlockPos pos, int min, int max,
      double initialVal) {
    super(x, y, width, height, StringTextComponent.EMPTY, 0);
    this.field = field;
    this.pos = pos;
    this.min = min;
    this.max = max;
    this.sliderValue = initialVal / max;
    this.func_230979_b_();
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    System.out.println("key " + keyCode);
    int left = 263;
    int right = 262;
    if (keyCode == left || keyCode == right) {
      if (Screen.hasShiftDown()) {
        //double 
        boolean flag = keyCode == left;
        float f = flag ? -5.0F : 5.0F;
        System.out.println("extra ");
        //yeah base class didnt make this a method sadly
        this.setSliderValue2(this.sliderValue + f / (this.width - 8));
      }
    }
    return super.keyPressed(keyCode, scanCode, modifiers);
  }

  private void setSliderValue2(double value) {
    double d0 = this.sliderValue;
    this.sliderValue = MathHelper.clamp(value, 0.0D, 1.0D);
    if (d0 != this.sliderValue) {
      this.func_230972_a_();
    }
    this.func_230979_b_();
  }

  @Override
  protected void func_230979_b_() {
    int val = getSliderValueActual();
    this.setMessage(new TranslationTextComponent("" + val));
  }

  @Override
  protected void func_230972_a_() {
    int val = getSliderValueActual();
    System.out.println("newval " + val);
    PacketRegistry.INSTANCE.sendToServer(new PacketTileData(this.field, val, pos));
  }

  private int getSliderValueActual() {
    int val = MathHelper.floor(MathHelper.clampedLerp(min, max, this.sliderValue));
    return val;
  }
}
