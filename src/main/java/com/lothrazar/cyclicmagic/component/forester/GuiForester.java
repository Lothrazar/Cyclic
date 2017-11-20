package com.lothrazar.cyclicmagic.component.forester;
import com.lothrazar.cyclicmagic.component.forester.TileEntityForester.Fields;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.data.Const.ScreenSize;
import com.lothrazar.cyclicmagic.gui.ProgressBar;
import com.lothrazar.cyclicmagic.gui.base.ContainerBaseMachine;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer.ButtonTriggerWrapper.ButtonTriggerType;
import com.lothrazar.cyclicmagic.gui.button.ButtonTileEntityField;
import com.lothrazar.cyclicmagic.gui.button.GuiButtonToggleSize;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiForester extends GuiBaseContainer {
  public GuiForester(InventoryPlayer inventoryPlayer, TileEntityForester tileEntity) {
    super(new ContainerForester(inventoryPlayer, tileEntity), tileEntity);
    setScreenSize(ScreenSize.LARGE);
    this.fieldRedstoneBtn = Fields.REDSTONE.ordinal();
    this.fieldPreviewBtn = Fields.RENDERPARTICLES.ordinal();
    this.setFieldFuel(Fields.FUEL.ordinal());
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0 ;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT_SAPLING);
    for (int k = 0; k < TileEntityForester.INVENTORY_SIZE - 1; k++) {
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerForester.SLOTX_START - 1 + (k % 9) * Const.SQ, this.guiTop + ContainerForester.SLOTY - 1 + ((int) k / 9) * Const.SQ,
          u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
  }
}
