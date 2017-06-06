package com.lothrazar.cyclicmagic.component.fisher;
import com.lothrazar.cyclicmagic.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiFisher extends GuiBaseContainer {
  public static final ResourceLocation SLOTFISH = new ResourceLocation(Const.MODID, "textures/gui/inventory_slot_fish.png");
  private TileEntityFishing tile;
  public GuiFisher(InventoryPlayer inventoryPlayer, TileEntityFishing tileEntity) {
    super(new ContainerFisher(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
  }
  public String getTitle() {
    return "tile.block_fishing.name";
  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    if (tile.isEquipmentValid() && tile.isValidPosition() == false) {
      String s = UtilChat.lang("tile.block_fishing.invalidpos.gui1");
      int x = 13 + this.xSize / 3 - this.fontRendererObj.getStringWidth(s);
      int y = 42;
      this.fontRendererObj.drawString(s, x, y, 4210752);
      y += 14;
      s = UtilChat.lang("tile.block_fishing.invalidpos.gui2");
      s = s + TileEntityFishing.MINIMUM_WET_SIDES + "+";
      this.fontRendererObj.drawString(s, x, y, 4210752);
      y += 14;
      s = UtilChat.lang("tile.block_fishing.invalidpos.gui3");
      this.fontRendererObj.drawString(s, x, y, 4210752);
    }
    if (tile.isEquipmentValid() && tile.isValidPosition()) {
      String s = UtilChat.lang("tile.block_fishing.progress");
      int x = 4 + this.xSize / 3 - this.fontRendererObj.getStringWidth(s);
      int y = 50;
      this.fontRendererObj.drawString(s, x, y, 4210752);
      y += 14;
      double fs = tile.getFishSpeed() * 100.0;
      String displaySp = String.format("%.2f", fs);//split up in case crashes again.  //java.util.IllegalFormatConversionException: f != java.lang.Integer
      this.fontRendererObj.drawString("(" + displaySp + " Hz)", x, y, 4210752);
    }
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(SLOTFISH);
    for (int k = 0; k < TileEntityFishing.RODSLOT; k++) {
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerFisher.SLOTX_START - 1 + k * Const.SQ, this.guiTop + ContainerFisher.SLOTY - 1, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    int row = 0, col = 0;
    for (int i = 0; i < TileEntityFishing.FISHSLOTS; i++) {
      row = i / 3;// /3 will go 000, 111, 222
      col = i % 3; // and %3 will go 012 012 012
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerFisher.SLOTX_FISH - 1 + row * Const.SQ, this.guiTop + ContainerFisher.SLOTY_FISH - 1 + col * Const.SQ, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
  }
}
