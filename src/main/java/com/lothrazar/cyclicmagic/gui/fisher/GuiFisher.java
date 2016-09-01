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
    for (int k = 0; k < this.tile.getSizeInventory(); k++) {
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerFisher.SLOTX_START - 1 + k * Const.SQ, this.guiTop + ContainerFisher.SLOTY - 1, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
  }

}
