package com.lothrazar.cyclicmagic.gui;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.gui.base.GuiButtonTooltip;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.GuiPageButtonList.GuiResponder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.GuiSlider;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class GuiSliderInteger extends GuiButtonExt implements ITooltipButton {
  private float sliderPosition = 1.0F;
  public boolean isMouseDown;
  private final float min;
  private final float max;
  private final GuiPageButtonList.GuiResponder responder;
  /**
   * mimic of net.minecraft.client.gui.GuiSlider;
   * uses integers instead of float
   */
  public GuiSliderInteger(GuiResponder guiResponder, int idIn, int x, int y,
      int widthIn, int heightIn,
      float minIn, float maxIn, float defaultValue) {
    super(idIn, x, y, widthIn, heightIn, "");
    this.updateDisplay();
    responder = guiResponder;
    this.min = minIn;
    this.max = maxIn;
    this.setSliderValue(defaultValue, false);
  }
  public void setSliderValue(float value, boolean notifyResponder) {
    this.sliderPosition = (value - this.min) / (this.max - this.min);
    this.updateDisplay();
    if (notifyResponder) {
      this.responder.setEntryValue(this.id, this.getSliderValue());
    }
  }
  public float getSliderValue() {
    float val = this.min + (this.max - this.min) * this.sliderPosition;
    return MathHelper.floor(val);
  }
  private void updateDisplay() {
    this.displayString = "" + (int) this.getSliderValue();
  }
  public void setTooltip(final String t) {
    List<String> remake = new ArrayList<String>();
    remake.add(UtilChat.lang(t));
    tooltip = remake;
  }
  private List<String> tooltip = new ArrayList<String>();
  @Override
  public List<String> getTooltips() {
    return tooltip;
  }
  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    if (super.mousePressed(mc, mouseX, mouseY)) {
      this.sliderPosition = (float) (mouseX - (this.x + 4)) / (float) (this.width - 8);
      if (this.sliderPosition < 0.0F) {
        this.sliderPosition = 0.0F;
      }
      if (this.sliderPosition > 1.0F) {
        this.sliderPosition = 1.0F;
      }
      this.updateDisplay();
      //          this.displayString = this.getDisplayString();
      this.responder.setEntryValue(this.id, this.getSliderValue());
      this.isMouseDown = true;
      return true;
    }
    else {
      return false;
    }
  }
  @Override
  protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
    super.mouseDragged(mc, mouseX, mouseY);
    if (this.visible) {
      if (this.isMouseDown) {
        this.sliderPosition = (float) (mouseX - (this.x + 4)) / (float) (this.width - 8);
        if (this.sliderPosition < 0.0F) {
          this.sliderPosition = 0.0F;
        }
        if (this.sliderPosition > 1.0F) {
          this.sliderPosition = 1.0F;
        }
        this.updateDisplay();
        this.responder.setEntryValue(this.id, this.getSliderValue());
      }
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.drawTexturedModalRect(this.x + (int) (this.sliderPosition * (float) (this.width - 8)), this.y, 0, 66, 4, height);
      this.drawTexturedModalRect(this.x + (int) (this.sliderPosition * (float) (this.width - 8)) + 4, this.y, 196, 66, 4, height);
    }
  }
  @Override
  public void mouseReleased(int mouseX, int mouseY) {
    super.mouseReleased(mouseX, mouseY);
    this.isMouseDown = false;
  }
  protected int getHoverState(boolean mouseOver)
  {
      return 0;
  }
}
