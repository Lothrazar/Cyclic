package com.lothrazar.cyclicmagic.component.builder;
import com.lothrazar.cyclicmagic.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.gui.GuiButtonTogglePreview;
import com.lothrazar.cyclicmagic.gui.ProgressBar;
import com.lothrazar.cyclicmagic.net.PacketTileSizeToggle;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiBuilder extends GuiBaseContainer {
  private TileEntityStructureBuilder tile;
  private ButtonBuilderType btn;
  private ButtonBuildSize btnSizeUp;
  private ButtonBuildSize btnSizeDown;
  private ButtonBuildSize btnHeightUp;
  private ButtonBuildSize btnHeightDown;
  private int xSizeTextbox;
  private int ySizeTxtbox;
  private int xHeightTextbox;
  private int yHeightTxtbox;
  private int yOffset = 10 + Const.PAD;
  public GuiBuilder(InventoryPlayer inventoryPlayer, TileEntityStructureBuilder tileEntity) {
    super(new ContainerBuilder(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
    this.fieldRedstoneBtn = TileEntityStructureBuilder.Fields.REDSTONE.ordinal();
    this.progressBar = new ProgressBar(this, 10, 9 + 3 * Const.SQ + 10, TileEntityStructureBuilder.Fields.TIMER.ordinal(), TileEntityStructureBuilder.TIMER_FULL);
    this.fieldPreviewBtn = TileEntityStructureBuilder.Fields.RENDERPARTICLES.ordinal();
  }
  @Override
  public void initGui() {
    super.initGui();
    //first the main top left type button
    int width = 50;
    int id = 2;
    int y = this.guiTop + yOffset + 12;
    btn = new ButtonBuilderType(tile.getPos(), id++,
        this.guiLeft + Const.PAD,
        y, width);
    this.buttonList.add(btn);

    width = 15;
    //size buttons
    xSizeTextbox = 176 - 24;
    btnSizeUp = new ButtonBuildSize(tile.getPos(), id++, this.guiLeft + xSizeTextbox, this.guiTop + yOffset, width, true, "size");
    this.buttonList.add(btnSizeUp);
    btnSizeDown = new ButtonBuildSize(tile.getPos(), id++, this.guiLeft + xSizeTextbox, this.guiTop + 21 + yOffset, width, false, "size");
    this.buttonList.add(btnSizeDown);
    xSizeTextbox += width / 2 - 2;
    ySizeTxtbox = 16;
    //HEIGHT BUTTONS
    xHeightTextbox = 176 - 48;
    btnHeightUp = new ButtonBuildSize(tile.getPos(), id++, this.guiLeft + xHeightTextbox, this.guiTop + yOffset, width, true, "height");
    this.buttonList.add(btnHeightUp);
    btnHeightDown = new ButtonBuildSize(tile.getPos(), id++, this.guiLeft + xHeightTextbox, this.guiTop + 21 + yOffset, width, false, "height");
    this.buttonList.add(btnHeightDown);
    xHeightTextbox += width / 2 - 2;
    yHeightTxtbox = ySizeTxtbox;
  }
  public String getTitle() {
    return "tile.builder_block.name";
  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    this.btn.displayString = UtilChat.lang("buildertype." + this.tile.getBuildTypeEnum().name().toLowerCase() + ".name");
    if (this.tile.getSize() > 0) {
      String display = "" + this.tile.getSize();
      //move it over if more than 1 digit
      int x = (display.length() > 1) ? xSizeTextbox - 3 : xSizeTextbox;
      this.fontRendererObj.drawString(display, x, ySizeTxtbox + yOffset - 4, 4210752);
    }
    if (this.tile.getHeight() > 0 && this.tile.getBuildTypeEnum().hasHeight()) {
      String display = "" + this.tile.getHeight();
      //move it over if more than 1 digit
      int x = (display.length() > 1) ? xHeightTextbox - 3 : xHeightTextbox;
      this.fontRendererObj.drawString(display, x, yHeightTxtbox + yOffset - 4, 4210752);
    }
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    updateDisabledButtons();
  }
  private void updateDisabledButtons() {
    this.btnSizeDown.enabled = (this.tile.getSize() > 1);
    this.btnSizeUp.enabled = (this.tile.getSize() < TileEntityStructureBuilder.maxSize);
    this.btnHeightDown.enabled = (this.tile.getHeight() > 1);
    this.btnHeightUp.enabled = (this.tile.getHeight() < TileEntityStructureBuilder.maxHeight);
    //a semi hack to hide btns
    this.btnHeightDown.visible = this.tile.getBuildTypeEnum().hasHeight();
    this.btnHeightUp.visible = this.tile.getBuildTypeEnum().hasHeight();
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    for (int k = 0; k < this.tile.getSizeInventory(); k++) { // x had - 3 ??
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerBuilder.SLOTX_START - 1 + k * Const.SQ, this.guiTop + ContainerBuilder.SLOTY - 1, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
  }
}
