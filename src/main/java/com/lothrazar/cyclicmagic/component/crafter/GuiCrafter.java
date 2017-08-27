package com.lothrazar.cyclicmagic.component.crafter;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.data.Const.ScreenSize;
import com.lothrazar.cyclicmagic.gui.ProgressBar;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiCrafter extends GuiBaseContainer {
  public GuiCrafter(InventoryPlayer inventoryPlayer, TileEntityCrafter tileEntity) {
    super(new ContainerCrafter(inventoryPlayer, tileEntity), tileEntity);
    screenSize = ScreenSize.LARGE;
    this.xSize = screenSize.width();
    this.ySize = screenSize.height();
    this.fieldRedstoneBtn = TileEntityCrafter.Fields.REDSTONE.ordinal();
    this.progressBar = new ProgressBar(this, 10, 6 * Const.SQ + 10, TileEntityCrafter.Fields.TIMER.ordinal(), TileEntityCrafter.TIMER_FULL);
    this.setFieldFuel(TileEntityCrafter.Fields.FUEL.ordinal());
  }
  @Override
  public void initGui() {
    super.initGui();
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    //input
    int xPrefix = Const.PAD, yPrefix = 27;
    int rows = TileEntityCrafter.ROWS;
    int cols = TileEntityCrafter.COLS;
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + xPrefix - 1 + j * Const.SQ,
            this.guiTop + yPrefix - 1 + i * Const.SQ, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
      }
    }
    //grid
    rows = cols = 3;
    xPrefix = (screenSize.width() / 2 - (Const.SQ * 3) / 2);//calculate exact center
    yPrefix = 40;
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + xPrefix - 1 + j * Const.SQ,
            this.guiTop + yPrefix - 1 + i * Const.SQ, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
      }
    }
    //output
    xPrefix = 134;
    yPrefix = 27;
    rows = TileEntityCrafter.ROWS;
    cols = TileEntityCrafter.COLS;
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + xPrefix - 1 + j * Const.SQ,
            this.guiTop + yPrefix - 1 + i * Const.SQ, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
      }
    }
    super.tryDrawFuelSlot(ContainerCrafter.SLOTX_FUEL - 1, ContainerCrafter.SLOTY_FUEL - 1);
  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
  }
}
