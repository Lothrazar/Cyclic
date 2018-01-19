package com.lothrazar.cyclicmagic.component.itemtransfer;
import java.util.Map;
import com.google.common.collect.Maps;
import com.lothrazar.cyclicmagic.component.autouser.TileEntityUser.Fields;
import com.lothrazar.cyclicmagic.component.uncrafter.ContainerUncrafting;
import com.lothrazar.cyclicmagic.component.uncrafter.TileEntityUncrafter;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.data.Const.ScreenSize;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import com.lothrazar.cyclicmagic.gui.button.ButtonTileEntityField;
import com.lothrazar.cyclicmagic.gui.slot.SlotSingleStack;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiItemPump extends GuiBaseContainer {
  TileEntityItemPump te;
  private ButtonTileEntityField btn;
  public GuiItemPump(InventoryPlayer inventoryPlayer, TileEntityItemPump tileEntity) {
    super(new ContainerItemPump(inventoryPlayer, tileEntity), tileEntity);
    te = tileEntity;
    this.fieldRedstoneBtn = TileEntityItemPump.Fields.REDSTONE.ordinal();
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
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
    btn.displayString = UtilChat.lang("button.itemfilter.type" + tile.getField(TileEntityItemPump.Fields.FILTERTYPE.ordinal()));

    btn.setTooltip(UtilChat.lang("button.itemfilter.tooltip.type" + tile.getField(TileEntityItemPump.Fields.FILTERTYPE.ordinal())));
  }
  @Override
  public void initGui() {
    super.initGui();
    int id = 2;
 
    btn = new ButtonTileEntityField(
        id++,
        this.guiLeft + 150,
        this.guiTop + Const.PAD/2 ,
        tile.getPos(), TileEntityItemPump.Fields.FILTERTYPE.ordinal(), 1,
        20,20 );
    this.addButton(btn);
  }
}
