package com.lothrazar.cyclicmagic.component.hydrator;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.data.Const.ScreenSize;
import com.lothrazar.cyclicmagic.gui.ProgressBar;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiHydrator extends GuiBaseContainer {
  private TileEntityHydrator tile;
  boolean debugLabels = false;
  public GuiHydrator(InventoryPlayer inventoryPlayer, TileEntityHydrator tileEntity) {
    super(new ContainerHydrator(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
    this.fieldRedstoneBtn = TileEntityHydrator.Fields.REDSTONE.ordinal();
    this.progressBar = new ProgressBar(this, 10, ContainerHydrator.SLOTY + 40, TileEntityHydrator.Fields.TIMER.ordinal(), TileEntityHydrator.TIMER_FULL);
  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
//    if (this.tile.tank.getFluid() != null) {
//      this.drawString(" " + this.tile.getCurrentFluid()
//          + "/" + this.tile.tank.getInfo().capacity, 30, 30);
//    }
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    int x = this.guiLeft + ContainerHydrator.SLOTX_START - 1;
    int y = this.guiTop + ContainerHydrator.SLOTY - 1;
    for (int k = 0; k < 4; k++) {
      Gui.drawModalRectWithCustomSizedTexture(
          x + k % 2 * Const.SQ,
          y + k / 2 * Const.SQ,
          u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
    x = this.guiLeft + 132;
    y = this.guiTop + ContainerHydrator.SLOTY - 1;
    for (int k = 0; k < 4; k++) {
      Gui.drawModalRectWithCustomSizedTexture(
          x + k % 2 * Const.SQ,
          y + k / 2 * Const.SQ,
          u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
    this.drawFluidBar();
  }
  private void drawFluidBar() {
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.FLUID);
    int pngWidth = 36, pngHeight = 124, f = 2, h= pngHeight / f;//f is scale factor. original is too big
    
    int x = this.guiLeft + 80, y = this.guiTop + 10;
    Gui.drawModalRectWithCustomSizedTexture(
        x, y, u, v,
        pngWidth / f, h,
        pngWidth / f, h);
    this.mc.getTextureManager().bindTexture(Const.Res.FLUID_WATER);
    float percent = ((float) tile.getCurrentFluid() / ((float) TileEntityHydrator.TANK_FULL));
    Gui.drawModalRectWithCustomSizedTexture(
        x + 1, y + 1, u, v,
        16, (int) (h * percent),
        16, h);
  }
}
