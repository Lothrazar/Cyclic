package com.lothrazar.cyclic.gui;

import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class FuelProgress {

  protected final Screen parent;
  protected final int width = 14;
  protected final int height = 14;
  protected final int x;
  protected final int y;
  
  public int guiLeft;
  public int guiTop;
  public int max = 1;
  protected boolean visible = true;

  public FuelProgress(Screen parent, int x, int y) {
    this.parent = parent;
    this.x = x;
    this.y = y;
  }

  public boolean isMouseover(int mouseX, int mouseY) {
    return guiLeft + x <= mouseX && mouseX <= guiLeft + x + width
        && guiTop + y <= mouseY && mouseY <= guiTop + y + height;
  }

  public void draw(MatrixStack ms, float current) {
    if (!visible) {
      return;
    }
    int relX;
    int relY;
    parent.getMinecraft().getTextureManager().bindTexture(TextureRegistry.FUEL_PROG);
    relX = guiLeft + x;
    relY = guiTop + y;
    Screen.blit(ms, relX, relY, 0, 0, width, height, 14, 28);
    int rHeight = height - (int) (height * Math.min(current / max, 1.0F));
    Screen.blit(ms, relX, relY, 0, 14, width, rHeight, 14, 28);
  }

  public void renderHoveredToolTip(MatrixStack ms, int mouseX, int mouseY, int curr) {
  if (this.isMouseover(mouseX, mouseY) && this.visible) {
    String display = "";
	int seconds = curr / Const.TICKS_PER_SEC;
	if (curr > Const.TICKS_PER_SEC * 120) {
	  //if more than 120 secs which is two minutes
	  int minutes = seconds / 60;
	  int remainder = seconds % 60;
	    display = minutes + "m " + remainder + "s";
	  }
	  else if (curr > Const.TICKS_PER_SEC * 20) {
	    //if more than 20 seconds, show seconds not ticks 
	    display = seconds + "s";
	  }
	  else {
	    display = curr + "";
	  }
	  List<ITextComponent> list = new ArrayList<>();
	  list.add(new TranslationTextComponent(display));
	  parent.func_243308_b(ms, list, mouseX, mouseY);
	}
  }

}
