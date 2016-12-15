package com.lothrazar.cyclicmagic.gui.pattern;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityPatternBuilder;
import com.lothrazar.cyclicmagic.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiPattern extends GuiBaseContainer {
  public static final ResourceLocation SLOTFISH = new ResourceLocation(Const.MODID, "textures/gui/inventory_slot_fish.png");
  private TileEntityPatternBuilder tile;
  private int xSizeTextbox;
  private int ySizeTxtbox;
  private int yOffset = 10 + Const.padding;
  public GuiPattern(InventoryPlayer inventoryPlayer, TileEntityPatternBuilder tileEntity) {
    super(new ContainerPattern(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
  }
  public String getTitle() {
    return "tile..name";
  }
  @Override
  public void initGui() {

    super.initGui();
    xSizeTextbox = 176 - 122;
    ySizeTxtbox = 16;
  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    

    String display = "" + this.tile.getField(TileEntityPatternBuilder.Fields.OFFX.ordinal());
    //move it over if more than 1 digit
    int x = (display.length() > 1) ? xSizeTextbox - 3 : xSizeTextbox;
    int y = ySizeTxtbox + yOffset - 4;
    this.fontRendererObj.drawString(display, x, y, 4210752);
    
    display = "" + this.tile.getField(TileEntityPatternBuilder.Fields.OFFY.ordinal());
    //move it over if more than 1 digit
    x = (display.length() > 1) ? xSizeTextbox - 3 : xSizeTextbox;
    y+=8;
    this.fontRendererObj.drawString(display, x, y, 4210752);
    
    display = "" + this.tile.getField(TileEntityPatternBuilder.Fields.OFFZ.ordinal());
    //move it over if more than 1 digit
    x = (display.length() > 1) ? xSizeTextbox - 3 : xSizeTextbox;
    y+=8;
    this.fontRendererObj.drawString(display, x, y, 4210752);
//    if (tile.isEquipmentValid() && tile.isValidPosition() == false) {
//      String s = UtilChat.lang("tile.block_fishing.invalidpos.gui1");
//      int x = 12 + this.xSize / 3 - this.fontRendererObj.getStringWidth(s);
//      int y = 42;
//      this.fontRendererObj.drawString(s, x, y, 4210752);
//      y += 14;
//      s = UtilChat.lang("tile.block_fishing.invalidpos.gui2");
//      this.fontRendererObj.drawString(s, x, y, 4210752);
//      y += 14;
//      s = UtilChat.lang("tile.block_fishing.invalidpos.gui3");
//      this.fontRendererObj.drawString(s, x, y, 4210752);
//    }
//    if (tile.isEquipmentValid() && tile.isValidPosition()) {
//      String s = UtilChat.lang("tile.block_fishing.progress");
//      int x = 4 + this.xSize / 3 - this.fontRendererObj.getStringWidth(s);
//      int y = 50;
//      this.fontRendererObj.drawString(s, x, y, 4210752);
//    }
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
 
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    int row = 0, col = 0;
    for (int i = 0; i < tile.getSizeInventory(); i++) {
      row = i / 3;// /3 will go 000, 111, 222
      col = i % 3; // and %3 will go 012 012 012
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerPattern.SLOTX_FISH - 1 + row * Const.SQ, this.guiTop + ContainerPattern.SLOTY_FISH - 1 + col * Const.SQ, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
  }
}
