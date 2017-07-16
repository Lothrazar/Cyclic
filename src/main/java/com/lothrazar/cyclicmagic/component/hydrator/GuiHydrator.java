package com.lothrazar.cyclicmagic.component.hydrator;
import com.lothrazar.cyclicmagic.component.harvester.ContainerHarvester;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiHydrator extends GuiBaseContainer {
  private TileEntityHydrator tile;
  boolean debugLabels = false;
  public GuiHydrator(InventoryPlayer inventoryPlayer, TileEntityHydrator tileEntity) {
    super(new ContainerHydrator(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
    this.fieldRedstoneBtn = TileEntityHydrator.Fields.REDSTONE.ordinal();
  }
  //  @Override
  //  public void initGui() {
  //    super.initGui();
  //  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    for (int k = 0; k < this.tile.getSizeInventory(); k++) { 
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerHarvester.SLOTX_START - 1 + k * Const.SQ, this.guiTop + ContainerHarvester.SLOTY - 1, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
  }
  
}
