package com.lothrazar.cyclicmagic.gui.uncrafting;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineUncrafter;
import com.lothrazar.cyclicmagic.gui.GuiBaseContanerProgress;
import com.lothrazar.cyclicmagic.gui.GuiButtonTexture;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public class GuiUncrafting extends GuiBaseContanerProgress {
  private TileMachineUncrafter tile;
  public GuiUncrafting(InventoryPlayer inventoryPlayer, TileMachineUncrafter tileEntity) {
    super(new ContainerUncrafting(inventoryPlayer, tileEntity));
    tile = tileEntity;
  }
  public GuiUncrafting(Container c) {
    super(c);
  }
  public String getTitle() {
    return "tile.uncrafting_block.name";
  }
  @Override
  public void initGui() {
    super.initGui();
    GuiButtonTexture btn = new GuiButtonTexture(0,this.guiLeft,this.guiTop, "textures/gui/buttons.png","tile.uncrafting.toggle");
    this.buttonList.add(btn);
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    for (int k = 0; k < this.tile.getSizeInventory(); k++) {
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerUncrafting.SLOTX_START - 1 + k * Const.SQ, this.guiTop + ContainerUncrafting.SLOTY - 1, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
    
  }
  public int getProgressX() {
    return this.guiLeft + 10;
  }
  public int getProgressY() {
    return this.guiTop + 9 + 3 * Const.SQ + 5;
  }
  public int getProgressCurrent() {
    return tile.getTimer();
  }
  public int getProgressMax() {
    return TileMachineUncrafter.TIMER_FULL;
  }
}
