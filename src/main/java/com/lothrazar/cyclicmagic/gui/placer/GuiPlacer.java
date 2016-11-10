package com.lothrazar.cyclicmagic.gui.placer;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachinePlacer;
import com.lothrazar.cyclicmagic.gui.GuiBaseContanerProgress;
import com.lothrazar.cyclicmagic.gui.uncrafting.GuiButtonUncraftingRedstone;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiPlacer extends GuiBaseContanerProgress {
  static final int padding = 8;
  private TileMachinePlacer tile;
  boolean debugLabels = false;
  private GuiButtonUncraftingRedstone redstoneBtn;
  public GuiPlacer(InventoryPlayer inventoryPlayer, TileMachinePlacer tileEntity) {
    super(new ContainerPlacer(inventoryPlayer, tileEntity));
    tile = tileEntity;
  }
  @Override
  public String getTitle() {
    return "tile.placer_block.name";
  }
  @Override
  public void initGui() {
    super.initGui();
    redstoneBtn = new GuiButtonUncraftingRedstone(0,
        this.guiLeft + 8,
        this.guiTop + 8, this.tile.getPos());
    redstoneBtn.setTextureIndex(tile.getField(TileMachinePlacer.Fields.REDSTONE.ordinal()));
    this.buttonList.add(redstoneBtn);
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    for (int k = 0; k < this.tile.getSizeInventory(); k++) { // x had - 3 ??
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerPlacer.SLOTX_START - 1 + k * Const.SQ, this.guiTop + ContainerPlacer.SLOTY - 1, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

    int needsRed = tile.getField(TileMachinePlacer.Fields.REDSTONE.ordinal());

    redstoneBtn.setState(needsRed);
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
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
    return TileMachinePlacer.TIMER_FULL;
  }
}
