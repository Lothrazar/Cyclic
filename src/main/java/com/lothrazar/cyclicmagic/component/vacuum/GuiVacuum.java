package com.lothrazar.cyclicmagic.component.vacuum;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.data.Const.ScreenSize;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiVacuum extends GuiBaseContainer {
  public GuiVacuum(InventoryPlayer inventoryPlayer, TileEntityVacuum tileEntity) {
    super(new ContainerVacuum(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
    this.setScreenSize(ScreenSize.LARGE);
    this.fieldRedstoneBtn = TileEntityVacuum.Fields.REDSTONE.ordinal();
    this.fieldPreviewBtn = TileEntityVacuum.Fields.RENDERPARTICLES.ordinal();
  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    int xCenter = this.xSize / 2;
    String s = UtilChat.lang("tile.block_vacuum.filter");
    this.drawString(s, xCenter - 38 - this.fontRenderer.getStringWidth(s) / 2, 22);
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    for (int i = 0; i < TileEntityVacuum.ROWS; i++) {
      for (int j = 0; j < TileEntityVacuum.COLS; j++) {
        Gui.drawModalRectWithCustomSizedTexture(
            this.guiLeft + Const.PAD + j * Const.SQ - 1,
            this.guiTop + 72 + (i - 1) * Const.SQ - 1,
            u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
      }
    }
    int start = TileEntityVacuum.ROWS * TileEntityVacuum.COLS;
    for (int k = 0; k < TileEntityVacuum.FILTERSLOTS/2; k++) {
      Gui.drawModalRectWithCustomSizedTexture(
          this.guiLeft + Const.PAD + (k + 4) * Const.SQ - 1,
          this.guiTop + Const.SQ -2,
          u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
    for (int k = TileEntityVacuum.FILTERSLOTS/2; k < TileEntityVacuum.FILTERSLOTS; k++) {
      Gui.drawModalRectWithCustomSizedTexture(
          this.guiLeft + Const.PAD + (k - 1) * Const.SQ - 1,
          this.guiTop + 2*Const.SQ -2,
          u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
  }
}
