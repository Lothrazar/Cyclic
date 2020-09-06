package com.lothrazar.cyclic.gui;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.net.PacketTileData;
import com.lothrazar.cyclic.registry.PacketRegistry;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractSlider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class GuiSliderInteger extends AbstractSlider implements IHasTooltip {

  public static final int ARROW_LEFT = 263;
  public static final int ARROW_RIGHT = 262;
  private final double min;
  private final double max;
  private final BlockPos pos;
  private final int field;
  private List<ITextComponent> tooltip;

  public GuiSliderInteger(int x, int y, int width, int height, int field,
      BlockPos pos, int min, int max,
      double initialVal) {
    super(x, y, width, height, StringTextComponent.EMPTY, 0);
    this.field = field;
    this.pos = pos;
    this.min = min;
    this.max = max;
    setSliderValueActual((int) initialVal);
  }

  @Override
  public List<ITextComponent> getTooltip() {
    return tooltip;
  }

  @Override
  public void setTooltip(String tt) {
    if (tooltip == null) {
      tooltip = new ArrayList<>();
    }
    this.tooltip.add(new TranslationTextComponent(tt));
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    if (keyCode == ARROW_LEFT || keyCode == ARROW_RIGHT) {
      // move from arrow keys
      int delta = (keyCode == ARROW_LEFT) ? -1 : 1;
      if (Screen.hasShiftDown()) {
        delta = delta * 5;
      }
      else if (Screen.hasAltDown()) {
        delta = delta * 10;
      }
      setSliderValueActual(this.getSliderValueActual() + delta);
      this.func_230979_b_();
      return true;
    }
    return super.keyPressed(keyCode, scanCode, modifiers);
  }

  /**
   * SAVE
   */
  @Override
  protected void func_230979_b_() {
    int val = getSliderValueActual();
    this.setMessage(new TranslationTextComponent("" + val));
  }

  /**
   * Update Display
   */
  @Override
  protected void func_230972_a_() {
    int val = getSliderValueActual();
    PacketRegistry.INSTANCE.sendToServer(new PacketTileData(this.field, val, pos));
  }

  private void setSliderValueActual(int val) {
    this.sliderValue = val / max;
    this.func_230979_b_();
    this.func_230972_a_();
  }

  private int getSliderValueActual() {
    int val = MathHelper.floor(MathHelper.clampedLerp(min, max, this.sliderValue));
    return val;
  }
}
