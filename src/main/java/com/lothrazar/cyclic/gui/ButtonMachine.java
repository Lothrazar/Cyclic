package com.lothrazar.cyclic.gui;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.opengl.GL11;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

public class ButtonMachine extends ExtendedButton implements IHasTooltip {

  BlockPos tilePos;
  private int tileField;
  private TextureEnum textureId;
  private List<ITextComponent> tooltip;

  public ButtonMachine(int xPos, int yPos, int width, int height, String displayString, IPressable handler) {
    super(xPos, yPos, width, height, new TranslationTextComponent(displayString), handler);
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

  public int getTileField() {
    return tileField;
  }

  public void setTileField(int tileField) {
    this.tileField = tileField;
  }
}
