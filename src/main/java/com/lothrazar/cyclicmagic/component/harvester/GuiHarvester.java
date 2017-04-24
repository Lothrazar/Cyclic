package com.lothrazar.cyclicmagic.component.harvester;
import com.lothrazar.cyclicmagic.gui.GuiBaseContanerProgress;
import com.lothrazar.cyclicmagic.gui.GuiButtonMachineRedstone;
import com.lothrazar.cyclicmagic.gui.GuiButtonSizePreview;
import com.lothrazar.cyclicmagic.net.PacketTileSizeToggle;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiHarvester extends GuiBaseContanerProgress {
  private TileEntityHarvester tile;
  boolean debugLabels = false;
  private GuiButtonMachineRedstone redstoneBtn;
  private GuiButtonSizePreview btnSize;
  public GuiHarvester(InventoryPlayer inventoryPlayer, TileEntityHarvester tileEntity) {
    super(new ContainerHarvester(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
  }
  @Override
  public void initGui() {
    super.initGui();
    int btnId = 0;
    redstoneBtn = new GuiButtonMachineRedstone(btnId++,
        this.guiLeft + Const.padding,
        this.guiTop + Const.padding, this.tile.getPos());
    this.buttonList.add(redstoneBtn);
    int y = this.guiTop + Const.padding * 2 + 20;
    btnSize = new GuiButtonSizePreview(btnId++,
        this.guiLeft + Const.padding,
        y, "", this.tile.getPos(),
        PacketTileSizeToggle.ActionType.SIZE);
    this.buttonList.add(btnSize);
    GuiButtonSizePreview btnPreview = new GuiButtonSizePreview(btnId++,
        this.guiLeft + Const.padding * 2 + 40,
        y, UtilChat.lang("button.harvester.preview"), this.tile.getPos(),
        PacketTileSizeToggle.ActionType.PREVIEW);
    this.buttonList.add(btnPreview);
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    for (int k = 0; k < this.tile.getSizeInventory(); k++) {
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerHarvester.SLOTX_START - 1 + k * Const.SQ, this.guiTop + ContainerHarvester.SLOTY - 1, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    redstoneBtn.setState(tile.getField(TileEntityHarvester.Fields.REDSTONE.ordinal()));
    btnSize.displayString = UtilChat.lang("button.harvester.size" + tile.getField(TileEntityHarvester.Fields.SIZE.ordinal()));
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
  }
  public int getProgressX() {
    return this.guiLeft + 10;
  }
  public int getProgressY() {
    return this.guiTop + 9 + 3 * Const.SQ + 5;
  }
  public int getProgressCurrent() {
    return tile.getField(TileEntityHarvester.Fields.TIMER.ordinal());
  }
  public int getProgressMax() {
    return TileEntityHarvester.TIMER_FULL;
  }
}
