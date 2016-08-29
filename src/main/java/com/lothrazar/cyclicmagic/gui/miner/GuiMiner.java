package com.lothrazar.cyclicmagic.gui.miner;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineMinerSmart;
import com.lothrazar.cyclicmagic.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public class GuiMiner extends GuiBaseContainer {
  private TileMachineMinerSmart tile;
  public GuiMiner(InventoryPlayer inventoryPlayer, TileMachineMinerSmart tileEntity) {
    super(new ContainerMiner(inventoryPlayer, tileEntity));
    tile = tileEntity;
  }
  public GuiMiner(Container c) {
    super(c);
  }
  public String getTitle() {
    return "tile.block_miner_smart.name";
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
    for (int k = 0; k < ContainerMiner.SLOTID_EQUIP; k++) {
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerMiner.SLOTX_START - 1 + k * Const.SQ, this.guiTop + ContainerMiner.SLOTY - 1, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
//    int s = ContainerMiner.SLOTID_EQUIP;
    Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerMiner.SLOTEQUIP_X, this.guiTop + ContainerMiner.SLOTEQUIP_Y, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    
  }
}
