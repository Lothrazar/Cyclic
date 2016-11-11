package com.lothrazar.cyclicmagic.gui.harvester;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineHarvester;
import com.lothrazar.cyclicmagic.gui.GuiBaseContanerProgress;
import com.lothrazar.cyclicmagic.gui.GuiButtonMachineRedstone;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiHarvester extends GuiBaseContanerProgress {
  static final int padding = 8;
  private TileMachineHarvester tile;
  boolean debugLabels = false;
  private GuiButtonMachineRedstone redstoneBtn;
  private  GuiButtonHarvester btnSize ;
  public GuiHarvester(InventoryPlayer inventoryPlayer, TileMachineHarvester tileEntity) {
    super(new ContainerHarvester(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
  }
  @Override
  public void initGui() {
    super.initGui();
    redstoneBtn = new GuiButtonMachineRedstone(0,
        this.guiLeft + 8,
        this.guiTop + 8, this.tile.getPos());
    redstoneBtn.setTextureIndex(tile.getField(TileMachineHarvester.Fields.REDSTONE.ordinal()));
    this.buttonList.add(redstoneBtn);
    
    btnSize = new GuiButtonHarvester(1,
        this.guiLeft + 8,
        this.guiTop + 28, 40,20,"change size",this.tile.getPos());
    this.buttonList.add(btnSize);
    
    
//    redstoneBtn.setTextureIndex(tile.getField(TileMachineHarvester.Fields.REDSTONE.ordinal()));
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    for (int k = 0; k < this.tile.getSizeInventory(); k++) { // x had - 3 ??
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerHarvester.SLOTX_START - 1 + k * Const.SQ, this.guiTop + ContainerHarvester.SLOTY - 1, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    redstoneBtn.setState(tile.getField(TileMachineHarvester.Fields.REDSTONE.ordinal()));
    btnSize.displayString = "button.harvester.size" +  tile.getField(TileMachineHarvester.Fields.SIZE.ordinal());
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
  }
  public int getProgressX() {
    return this.guiLeft + 10;
  }
  public int getProgressY() {
    return this.guiTop + 9 + 3 * Const.SQ + 5;
  }
  public int getProgressCurrent() {
    return tile.getField(TileMachineHarvester.Fields.TIMER.ordinal());
  }
  public int getProgressMax() {
    return TileMachineHarvester.TIMER_FULL;
  }
}
