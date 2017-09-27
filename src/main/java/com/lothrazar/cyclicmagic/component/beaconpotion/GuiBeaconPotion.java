package com.lothrazar.cyclicmagic.component.beaconpotion;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiBeaconPotion extends GuiBaseContainer {
  boolean debugLabels = false;
  public GuiBeaconPotion(InventoryPlayer inventoryPlayer, TileEntityBeaconPotion tileEntity) {
    super(new ContainerBeaconPotion(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
    //    this.fieldRedstoneBtn = TileEntityBlockMiner.Fields.REDSTONE.ordinal();
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
    int x = 176 / 2 - Const.SQ / 2;
    int y = 38;
    Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + x - 1, this.guiTop + y - 1, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
  }
 
}
