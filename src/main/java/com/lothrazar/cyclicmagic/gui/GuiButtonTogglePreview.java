package com.lothrazar.cyclicmagic.gui;
import java.util.Arrays;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.gui.base.GuiButtonTexture;
import com.lothrazar.cyclicmagic.net.PacketTileSizeToggle;
import com.lothrazar.cyclicmagic.net.PacketTileSizeToggle.ActionType;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiButtonTogglePreview extends GuiButtonTexture {
  private BlockPos tilePos;
  public GuiButtonTogglePreview(int buttonId, int x, int y, BlockPos p) {
    super(buttonId, x, y, "textures/gui/buttons.png");//44, 20,
    this.tilePos = p;
    this.setTextureIndex(3);
  }
  @SideOnly(Side.CLIENT)
  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    if (pressed) {
      ModCyclic.network.sendToServer(new PacketTileSizeToggle(tilePos, ActionType.PREVIEW));
    }
    return pressed;
  }
  public void setStateOn() {
    this.setTextureIndex(3);
    this.setTooltips(Arrays.asList(UtilChat.lang("tile.preview.button.on")));
  }
  public void setStateOff() {
    this.setTextureIndex(4);
    this.setTooltips(Arrays.asList(UtilChat.lang("tile.preview.button.off")));
  }
}
