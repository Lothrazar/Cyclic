package com.lothrazar.cyclicmagic.component.peat.generator;
import com.lothrazar.cyclicmagic.component.pump.item.ContainerItemPump;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiPeatGenerator extends GuiBaseContainer {
  public GuiPeatGenerator(InventoryPlayer inventoryPlayer, TileEntityPeatGenerator te) {
    super(new ContainerPeatGenerator(inventoryPlayer, te), te);
  }
  @Override
  public void initGui() {
    super.initGui();
    // BUTTONS! 
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    //    Gui.drawModalRectWithCustomSizedTexture(
    //        this.guiLeft + ContainerItemPump.SLOTX_START + (j - 1) * Const.SQ - 1,
    //        this.guiTop + ContainerItemPump.SLOTY - 1,
    //        u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    
    //DRAW ENERGY BAR
  }
}
