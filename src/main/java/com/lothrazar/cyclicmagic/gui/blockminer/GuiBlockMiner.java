package com.lothrazar.cyclicmagic.gui.blockminer;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineBlockMiner;
import com.lothrazar.cyclicmagic.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.gui.GuiButtonMachineRedstone;
import com.lothrazar.cyclicmagic.gui.harvester.ContainerHarvester;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiBlockMiner extends GuiBaseContainer {
  static final int padding = 8;
  private TileMachineBlockMiner tile;
  boolean debugLabels = false;
  private GuiButtonMachineRedstone redstoneBtn;
  public GuiBlockMiner(InventoryPlayer inventoryPlayer, TileMachineBlockMiner tileEntity) {
    super(new ContainerBlockMiner(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
  }
 
  @Override
  public void initGui() {
    super.initGui();
    redstoneBtn = new GuiButtonMachineRedstone(0,
        this.guiLeft + 8,
        this.guiTop + 8, this.tile.getPos());
    redstoneBtn.setTextureIndex(tile.getField(TileMachineBlockMiner.Fields.REDSTONE.ordinal()));
    this.buttonList.add(redstoneBtn);
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
    int needsRed = tile.getField(TileMachineBlockMiner.Fields.REDSTONE.ordinal());
    redstoneBtn.setState(needsRed);
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
  }
}
