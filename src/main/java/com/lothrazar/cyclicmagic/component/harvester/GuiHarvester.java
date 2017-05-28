package com.lothrazar.cyclicmagic.component.harvester;
import com.lothrazar.cyclicmagic.component.autouser.ContainerUser;
import com.lothrazar.cyclicmagic.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.gui.GuiButtonToggleSize;
import com.lothrazar.cyclicmagic.gui.ProgressBar;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiHarvester extends GuiBaseContainer {
  private TileEntityHarvester tile;
  boolean debugLabels = false;
  private GuiButtonToggleSize btnSize;
  public GuiHarvester(InventoryPlayer inventoryPlayer, TileEntityHarvester tileEntity) {
    super(new ContainerHarvester(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
    this.fieldRedstoneBtn = TileEntityHarvester.Fields.REDSTONE.ordinal();
    this.fieldPreviewBtn = TileEntityHarvester.Fields.RENDERPARTICLES.ordinal();
    this.progressBar = new ProgressBar(this, 10, 14 + 3 * Const.SQ, TileEntityHarvester.Fields.TIMER.ordinal(), TileEntityHarvester.TIMER_FULL);
    this.setFieldFuel(TileEntityHarvester.Fields.FUEL.ordinal());
  }
  @Override
  public void initGui() {
    super.initGui();
    int btnId = 2;
    btnSize = new GuiButtonToggleSize(btnId++,
        this.guiLeft + Const.PAD + 22,
        this.guiTop + Const.PAD * 3 + 2, this.tile.getPos());
    this.buttonList.add(btnSize);
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
//    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
//    for (int k = 0; k < this.tile.getSizeInventory(); k++) {
//      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerHarvester.SLOTX_START - 1 + k * Const.SQ, this.guiTop + ContainerHarvester.SLOTY - 1, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
//    }
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT_COAL);
    Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerUser.SLOTX_FUEL - 1, this.guiTop + ContainerUser.SLOTY_FUEL - 1, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);

  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    btnSize.displayString = UtilChat.lang("button.harvester.size" + tile.getField(TileEntityHarvester.Fields.SIZE.ordinal()));
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
  }
}
