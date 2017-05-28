package com.lothrazar.cyclicmagic.component.miner;
import com.lothrazar.cyclicmagic.component.harvester.ContainerHarvester;
import com.lothrazar.cyclicmagic.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiBlockMiner extends GuiBaseContainer {
  private TileEntityBlockMiner tile;
  boolean debugLabels = false;
  public GuiBlockMiner(InventoryPlayer inventoryPlayer, TileEntityBlockMiner tileEntity) {
    super(new ContainerBlockMiner(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
    this.fieldRedstoneBtn = TileEntityBlockMiner.Fields.REDSTONE.ordinal();
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
    for (int k = 0; k < this.tile.getSizeInventory(); k++) { // x had - 3 ??
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerHarvester.SLOTX_START - 1 + k * Const.SQ, this.guiTop + ContainerHarvester.SLOTY - 1, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
  }
  //  @SideOnly(Side.CLIENT)
  //  @Override
  //  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) { 
  //    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
  //  }
}
