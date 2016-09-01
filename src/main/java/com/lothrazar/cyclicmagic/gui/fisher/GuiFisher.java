package com.lothrazar.cyclicmagic.gui.fisher;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityFishing;
import com.lothrazar.cyclicmagic.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public class GuiFisher extends GuiBaseContainer {
  private TileEntityFishing tile;
  public GuiFisher(InventoryPlayer inventoryPlayer, TileEntityFishing tileEntity) {
    super(new ContainerFisher(inventoryPlayer, tileEntity));
    tile = tileEntity;
  }
  public GuiFisher(Container c) {
    super(c);
  }
  public String getTitle() {
    return "tile.block_fishing.name";
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    for (int k = 0; k < TileEntityFishing.RODSLOT; k++) {
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerFisher.SLOTX_START - 1 + k * Const.SQ, this.guiTop + ContainerFisher.SLOTY - 1, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
    int row = 0, col = 0;
    for (int i = 0; i <   TileEntityFishing.FISHSLOTS; i++) {
      row = i / 3;// /3 will go 000, 111, 222
      col = i % 3;  // and %3 will go 012 012 012
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerFisher.SLOTX_FISH - 1 + row * Const.SQ, this.guiTop + ContainerFisher.SLOTY_FISH - 1+col * Const.SQ, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
  }

}
