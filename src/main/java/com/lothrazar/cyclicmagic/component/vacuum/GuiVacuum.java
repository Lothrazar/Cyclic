package com.lothrazar.cyclicmagic.component.vacuum;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.data.Const.ScreenSize;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
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
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    int rows = 4, cols = 9;
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        Gui.drawModalRectWithCustomSizedTexture(
            this.guiLeft + Const.PAD + j * Const.SQ-1,
            this.guiTop + 72 + (i - 1) * Const.SQ - 1,
            u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
      }
    }
  }
}
