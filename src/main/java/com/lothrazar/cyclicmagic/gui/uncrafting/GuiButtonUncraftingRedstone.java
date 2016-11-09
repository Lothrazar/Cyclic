package com.lothrazar.cyclicmagic.gui.uncrafting;
import java.util.Arrays;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.gui.GuiButtonTexture;
import com.lothrazar.cyclicmagic.net.PacketTileRedstoneToggle;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiButtonUncraftingRedstone extends GuiButtonTexture {
  private BlockPos tilePos;
  public GuiButtonUncraftingRedstone(int buttonId, int x, int y, BlockPos p) {
    super(buttonId, x, y, "textures/gui/buttons.png");
    this.tilePos = p;
  }
  @SideOnly(Side.CLIENT)
  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    if (pressed) {
      ModMain.network.sendToServer(new PacketTileRedstoneToggle(tilePos));
    }
    return pressed;
  }
  public void setState(int state) {
    this.setTextureIndex(state);
    this.setTooltips(Arrays.asList(UtilChat.lang("tile.redstone.button" + state)));
  }
}
