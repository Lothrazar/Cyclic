package com.lothrazar.cyclicmagic.gui.builder;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineStructureBuilder;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineStructureBuilder.BuildType;
import com.lothrazar.cyclicmagic.gui.GuiBaseContanerProgress;
import com.lothrazar.cyclicmagic.gui.GuiButtonMachineRedstone;
import com.lothrazar.cyclicmagic.gui.GuiButtonSizePreview;
import com.lothrazar.cyclicmagic.net.PacketTileSizeToggle;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiBuilder extends GuiBaseContanerProgress {
  static final int padding = 8;
  private TileMachineStructureBuilder tile;
  private ButtonBuilderType btn;
  private ButtonBuildSize btnSizeUp;
  private ButtonBuildSize btnSizeDown;
  private ButtonBuildSize btnHeightUp;
  private ButtonBuildSize btnHeightDown;
  private int xSizeTextbox;
  private int ySizeTxtbox;
  private int xHeightTextbox;
  private int yHeightTxtbox;
  private int yOffset = 10 + padding;
  private GuiButtonMachineRedstone redstoneBtn;
  public GuiBuilder(InventoryPlayer inventoryPlayer, TileMachineStructureBuilder tileEntity) {
    super(new ContainerBuilder(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
  }
  @Override
  public void initGui() {
    super.initGui();
    redstoneBtn = new GuiButtonMachineRedstone(0,
        this.guiLeft + 8,
        this.guiTop + 8, this.tile.getPos());
    redstoneBtn.setTextureIndex(tile.getField(TileMachineStructureBuilder.Fields.REDSTONE.ordinal()));
    this.buttonList.add(redstoneBtn);
    //first the main top left type button
    int width = 50;
    int id = 2;
    int y = this.guiTop + yOffset + 12;
    btn = new ButtonBuilderType(tile.getPos(), id++,
        this.guiLeft + padding,
        y, width);
    this.buttonList.add(btn);
    GuiButtonSizePreview btnPreview = new GuiButtonSizePreview(id++,
        this.guiLeft + Const.padding * 2 + 46,
        y, UtilChat.lang("button.harvester.preview"), this.tile.getPos(),
        PacketTileSizeToggle.ActionType.PREVIEW);
    this.buttonList.add(btnPreview);
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

    boolean isStairway = (this.tile.getBuildTypeEnum() == BuildType.STAIRWAY);
    if (this.tile.getHeight() > 0 && !isStairway) {
      String display = "" + this.tile.getHeight();
      //move it over if more than 1 digit
      int x = (display.length() > 1) ? xHeightTextbox - 3 : xHeightTextbox;
      this.fontRendererObj.drawString(display, x, yHeightTxtbox + yOffset - 4, 4210752);
    }
    int needsRed = tile.getField(TileMachineStructureBuilder.Fields.REDSTONE.ordinal());
    redstoneBtn.setState(needsRed);
    //      redstoneBtn.setTextureIndex(needsRed);
    //      redstoneBtn.setTooltips(Arrays.asList(UtilChat.lang("tile.redstone.button" + needsRed)));
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    updateDisabledButtons();
  }
  private void updateDisabledButtons() {
    this.btnSizeDown.enabled = (this.tile.getSize() > 1);
    this.btnSizeUp.enabled = (this.tile.getSize() < TileMachineStructureBuilder.maxSize);
    this.btnHeightDown.enabled = (this.tile.getHeight() > 1);
    this.btnHeightUp.enabled = (this.tile.getHeight() < TileMachineStructureBuilder.maxHeight);
    //a semi hack to hide btns
    boolean isStairway = (this.tile.getBuildTypeEnum() == BuildType.STAIRWAY);
    this.btnHeightDown.visible = !isStairway;
    this.btnHeightUp.visible = !isStairway;
    
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
  public int getProgressX() {
    return this.guiLeft + 10;
  }
  public int getProgressY() {
    return this.guiTop + 9 + 3 * Const.SQ + 10;
  }
  public int getProgressCurrent() {
    return tile.getTimer();
  }
  public int getProgressMax() {
    return TileMachineStructureBuilder.TIMER_FULL;
  }
}
