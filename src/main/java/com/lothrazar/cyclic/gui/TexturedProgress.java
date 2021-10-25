package com.lothrazar.cyclic.gui;

import com.lothrazar.cyclic.data.Const;
import com.mojang.blaze3d.matrix.MatrixStack;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class TexturedProgress {

  protected final Screen parent;
  protected final int x;
  protected final int y;
  protected final int width;
  protected final int height;
  protected final ResourceLocation texture;
  
  public int guiLeft;
  public int guiTop;
  public int max = 1;
  protected boolean topDown = true;

  public TexturedProgress(Screen parent, int x, int y, ResourceLocation texture) {
    this(parent, x, y, 14, 14, texture);
  }
  
  public TexturedProgress(Screen parent, int x, int y, int width, int height, ResourceLocation texture) {
    this.parent = parent;
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.texture = texture;
  }

  public boolean isMouseover(int mouseX, int mouseY) {
    return guiLeft + x <= mouseX && mouseX <= guiLeft + x + width
        && guiTop + y <= mouseY && mouseY <= guiTop + y + height;
  }

  public void draw(MatrixStack ms, float current) {
    int relX;
    int relY;
    parent.getMinecraft().getTextureManager().bindTexture(this.texture);
    relX = guiLeft + x;
    relY = guiTop + y;
    if(this.topDown) {
      Screen.blit(ms, relX, relY, 0, 0, width, height, width, height * 2);
      int rHeight = height - (int) (height * Math.min(current / max, 1.0F));
      Screen.blit(ms, relX, relY, 0, height, width, rHeight, width, height * 2);
    } else { //Left-Right mode
        Screen.blit(ms, relX, relY, 0, height, width, height, width, height * 2);
        int rWidth = (int) (width * Math.min(current / max, 1.0F));
        if(current != 0) Screen.blit(ms, relX, relY, 0, 0, width - rWidth, height, width, height * 2);
    }
  }

  public void renderHoveredToolTip(MatrixStack ms, int mouseX, int mouseY, int curr) {
  if (this.isMouseover(mouseX, mouseY) && curr > 0) {
    String display = "";
	int seconds = curr / Const.TICKS_PER_SEC;
	if (curr > Const.TICKS_PER_SEC * 60) {
	  //if more than 120 secs which is two minutes
	  int minutes = seconds / 60;
	  int remainder = seconds % 60;
	    display = minutes + "m " + remainder + "s";
	  }
	  else if (curr > Const.TICKS_PER_SEC * 5) {
	    //if more than 20 seconds, show seconds not ticks 
	    display = seconds + "s";
	  }
	  else {
	    display = curr + "t";
	  }
	  List<ITextComponent> list = new ArrayList<>();
	  list.add(new TranslationTextComponent(display));
	  parent.func_243308_b(ms, list, mouseX, mouseY);
	}
  }
  
  public void setTopDown(boolean topDown) {
	  this.topDown = topDown;
  }

}
