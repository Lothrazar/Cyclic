package com.lothrazar.cyclic.gui;

import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;
import org.lwjgl.opengl.GL11;

/**
 * not bound to a tile entity unlike ButtonMachine
 * 
 */
public class ButtonTextured extends ExtendedButton implements IHasTooltip {

  private TextureEnum textureId;
  private List<ITextComponent> tooltip;

  public ButtonTextured(int xPos, int yPos, int width, int height, String displayString, IPressable handler) {
    super(xPos, yPos, width, height,
        new TranslationTextComponent(displayString), handler);
  }

  public ButtonTextured(int xPos, int yPos, int width, int height, TextureEnum tid, String tooltip, IPressable handler) {
    super(xPos, yPos, width, height, new TranslationTextComponent(""), handler);
    this.setTooltip(tooltip);
    this.setTextureId(tid);
  }

  public void setTextureId(TextureEnum id) {
    this.textureId = id;
  }

  @Override
  public void renderButton(MatrixStack ms, int mouseX, int mouseY, float partial) {
    super.renderButton(ms, mouseX, mouseY, partial);
    Minecraft minecraft = Minecraft.getInstance();
    minecraft.getTextureManager().bindTexture(TextureRegistry.WIDGETS);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, this.alpha);
    GL11.glEnable(GL11.GL_BLEND);
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    if (textureId != null) {
      this.blit(ms, this.x, this.y,
          textureId.getX(), textureId.getY(),
          width, height);
    }
    this.renderBg(ms, minecraft, mouseX, mouseY);
  }

  @Override
  public List<ITextComponent> getTooltip() {
    return tooltip;
  }

  @Override
  public void setTooltip(String ttIn) {
    tooltip = new ArrayList<>();
    tooltip.add(new TranslationTextComponent(ttIn));
  }
}
