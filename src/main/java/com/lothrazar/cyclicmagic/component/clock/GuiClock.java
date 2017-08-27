package com.lothrazar.cyclicmagic.component.clock;
import com.lothrazar.cyclicmagic.component.harvester.ContainerHarvester;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiClock extends GuiBaseContainer {
 
  boolean debugLabels = false;
  public GuiClock(InventoryPlayer inventoryPlayer, TileEntityClock tileEntity) {
    super(new ContainerClock(inventoryPlayer, tileEntity), tileEntity);
  
//    this.fieldRedstoneBtn = TileEntityClock.Fields.REDSTONE.ordinal();
  }
  //  @Override
  //  public void initGui() {
  //    super.initGui();
  //  }
//  @Override
//  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
//    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
// 
//  }
  //  @SideOnly(Side.CLIENT)
  //  @Override
  //  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) { 
  //    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
  //  }
}
