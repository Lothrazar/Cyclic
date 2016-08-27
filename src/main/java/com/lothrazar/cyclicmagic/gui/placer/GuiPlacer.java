package com.lothrazar.cyclicmagic.gui.placer;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachinePlacer;
import com.lothrazar.cyclicmagic.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiPlacer extends GuiBaseContainer {
  private static final int progressWidth = 156;
  private static final int progressH = 7;
  static final int padding = 8;
  private TileMachinePlacer tile;
  boolean debugLabels = false;
  public GuiPlacer(InventoryPlayer inventoryPlayer, TileMachinePlacer tileEntity) {
    super(new ContainerPlacer(inventoryPlayer, tileEntity));
    tile = tileEntity;
  }
  @Override
  public String getTitle() {
    return "tile.placer_block.name";
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;

    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    for (int k = 0; k < this.tile.getSizeInventory(); k++) { // x had - 3 ??
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerPlacer.SLOTX_START - 1 + k * Const.SQ, this.guiTop + ContainerPlacer.SLOTY - 1, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }

    int belowSlots = this.guiTop + 9 + 3 * Const.SQ+5;
    this.mc.getTextureManager().bindTexture(Const.Res.PROGRESSCTR);
    Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + 10, belowSlots, u, v, (int) progressWidth, progressH, progressWidth, progressH);
    if (tile.getTimer() > 0 && tile.getStackInSlot(0) != null) {
      this.mc.getTextureManager().bindTexture(Const.Res.PROGRESS);
      float percent = ((float) tile.getTimer()) / ((float) TileMachinePlacer.TIMER_FULL);
      // maximum progress bar is 156, since the whole texture is 176 minus
      // 10 padding on each side
      // Args: x, y, u, v, width, height, textureWidth, textureHeight
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + 10, belowSlots, u, v, (int) (progressWidth * percent), progressH, progressWidth, progressH);
    }
  }
}
