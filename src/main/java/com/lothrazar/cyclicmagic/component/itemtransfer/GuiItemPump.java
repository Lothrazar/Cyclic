package com.lothrazar.cyclicmagic.component.itemtransfer;
import java.util.Map;
import com.google.common.collect.Maps;
import com.lothrazar.cyclicmagic.component.uncrafter.ContainerUncrafting;
import com.lothrazar.cyclicmagic.component.uncrafter.TileEntityUncrafter;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.data.Const.ScreenSize;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import com.lothrazar.cyclicmagic.gui.button.ButtonTileEntityField;
import com.lothrazar.cyclicmagic.gui.slot.SlotSingleStack;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiItemPump extends GuiBaseContainer {
  //  private Map<EnumFacing, ButtonTileEntityField> btnMap = Maps.newHashMap();
  TileEntityItemPump te;
  public GuiItemPump(InventoryPlayer inventoryPlayer, TileEntityItemPump tileEntity) {
    super(new ContainerItemPump(inventoryPlayer, tileEntity), tileEntity);
    te = tileEntity;
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
//    this.drawDefaultBackground();//dim the background as normal

    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    for (int j = 1; j < 10; j++) {
      Gui.drawModalRectWithCustomSizedTexture(
          this.guiLeft + ContainerItemPump.SLOTX_START + (j - 1) * Const.SQ - 1,
          this.guiTop + ContainerItemPump.SLOTY - 1,
          u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
 
  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);

 
  }
  @Override
  public void initGui() {
    super.initGui();
    //    int id = 2;
    //    ButtonTileEntityField btn;
    //    for (EnumFacing f : EnumFacing.values()) {
    //      btn = new ButtonTileEntityField(
    //          id++,
    //          this.guiLeft + Const.PAD - 2,
    //          this.guiTop + f.ordinal() * Const.SQ + 17,
    //          tile.getPos(), f.ordinal(), 1,
    //          Const.SQ, Const.SQ);
    //      this.addButton(btn);
    //      btnMap.put(f, btn);
    //    }
  }
}
