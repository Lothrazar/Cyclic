package com.lothrazar.cyclicmagic.component.builder;
import com.lothrazar.cyclicmagic.component.builder.TileEntityStructureBuilder.Fields;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.data.Const.ScreenSize;
import com.lothrazar.cyclicmagic.gui.ProgressBar;
import com.lothrazar.cyclicmagic.gui.base.ContainerBaseMachine;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import com.lothrazar.cyclicmagic.gui.button.ButtonIncrementField;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiBuilder extends GuiBaseContainer {
  private TileEntityStructureBuilder tile;
  private ButtonIncrementField btn;
  private ButtonIncrementField btnSizeUp;
  private ButtonIncrementField btnSizeDown;
  private ButtonIncrementField btnHeightUp;
  private ButtonIncrementField btnHeightDown;
  private int xSizeTextbox;
  private int ySizeTxtbox;
  private int xHeightTextbox;
  private int yHeightTxtbox;
  private int yOffset = 10 + Const.PAD;
  private int xRotTextbox;
  public GuiBuilder(InventoryPlayer inventoryPlayer, TileEntityStructureBuilder tileEntity) {
    super(new ContainerBuilder(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
    setScreenSize(ScreenSize.LARGE);
    this.fieldRedstoneBtn = TileEntityStructureBuilder.Fields.REDSTONE.ordinal();
    this.progressBar = new ProgressBar(this, 10, ContainerBuilder.SLOTY + 22, TileEntityStructureBuilder.Fields.TIMER.ordinal(), TileEntityStructureBuilder.TIMER_FULL);
    this.fieldPreviewBtn = TileEntityStructureBuilder.Fields.RENDERPARTICLES.ordinal();
    this.setFieldFuel(TileEntityStructureBuilder.Fields.FUEL.ordinal());
  }
  @Override
  public void initGui() {
    super.initGui();
    //first the main top left type button
    int width = 50;
    int h = 15;
    int id = 2;
    int y = this.guiTop + yOffset + Const.PAD;
    btn = new ButtonIncrementField(id++,
        this.guiLeft + Const.PAD + h,
        y,
        tile.getPos(),
        TileEntityStructureBuilder.Fields.BUILDTYPE.ordinal(), 1,
        width, 20);
    btn.setTooltip("button.builder.tooltip");
    this.buttonList.add(btn);
    width = 15;
    TileEntityStructureBuilder.Fields fld = TileEntityStructureBuilder.Fields.SIZE;
    //size buttons
    xSizeTextbox = 176 - 52;
    // this.setTooltip("button." + fld.name().toLowerCase() + "." + (goUp ? "up" : "down"));
    btnSizeUp = new ButtonIncrementField(id++,
        this.guiLeft + xSizeTextbox,
        this.guiTop + yOffset,
        tile.getPos(),
        fld.ordinal(),
        1, width, h);
    btnSizeUp.setTooltip("button." + fld.name().toLowerCase() + "." + "up");
    btnSizeUp.displayString = "+";
    this.buttonList.add(btnSizeUp);
    btnSizeDown = new ButtonIncrementField(id++,
        this.guiLeft + xSizeTextbox,
        this.guiTop + 22 + yOffset + Const.PAD,
        tile.getPos(),
        fld.ordinal(),
        -1, width, h);
    btnSizeDown.setTooltip("button." + fld.name().toLowerCase() + "." + "down");
    btnSizeDown.displayString = "-";
    this.buttonList.add(btnSizeDown);
    xSizeTextbox += width / 2 - 2;
    ySizeTxtbox = 22;
    //HEIGHT BUTTONS
    fld = TileEntityStructureBuilder.Fields.HEIGHT;
    xHeightTextbox = xSizeTextbox - 28;
    btnHeightUp = new ButtonIncrementField(id++,
        this.guiLeft + xHeightTextbox, this.guiTop + yOffset,
        tile.getPos(),
        fld.ordinal(),
        1, width, h);
    btnHeightUp.setTooltip("button." + fld.name().toLowerCase() + "." + "up");
    btnHeightUp.displayString = "+";
    this.buttonList.add(btnHeightUp);
    btnHeightDown = new ButtonIncrementField(id++,
        this.guiLeft + xHeightTextbox, this.guiTop + ySizeTxtbox + yOffset + Const.PAD,
        tile.getPos(),
        fld.ordinal(),
        -1, width, h);
    btnHeightDown.setTooltip("button." + fld.name().toLowerCase() + "." + "down");
    btnHeightDown.displayString = "-";
    this.buttonList.add(btnHeightDown);
    xHeightTextbox += width / 2 - 2;
    yHeightTxtbox = ySizeTxtbox;
    //ROTATION BUTTONS
    fld = TileEntityStructureBuilder.Fields.ROTATIONS;
    xRotTextbox = xHeightTextbox - 28;
    ButtonIncrementField btnRotUp = new ButtonIncrementField(id++,
        this.guiLeft + xRotTextbox, this.guiTop + yOffset,
        tile.getPos(),
        fld.ordinal(),
        1, width, h);
    btnRotUp.setTooltip("button." + fld.name().toLowerCase() + "." + "up");
    btnRotUp.displayString = "+";
    this.buttonList.add(btnRotUp);
    ButtonIncrementField btnRotDown = new ButtonIncrementField(id++,
        this.guiLeft + xRotTextbox, this.guiTop + ySizeTxtbox + yOffset + Const.PAD,
        tile.getPos(),
        fld.ordinal(),
        -1, width, h);
    btnRotDown.setTooltip("button." + fld.name().toLowerCase() + "." + "down");
    btnRotDown.displayString = "-";
    this.buttonList.add(btnRotDown);
    xRotTextbox += width / 2 - 2;
    yHeightTxtbox = ySizeTxtbox;
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
      this.drawString(display, x, ySizeTxtbox + yOffset - 4);
    }
    if (this.tile.getHeight() > 0 && this.tile.getBuildTypeEnum().hasHeight()) {
      String display = "" + this.tile.getHeight();
      //move it over if more than 1 digit
      int x = (display.length() > 1) ? xHeightTextbox - 3 : xHeightTextbox;
      this.drawString(display, x, yHeightTxtbox + yOffset - 4);
    }
    String display = "" + this.tile.getField(Fields.ROTATIONS.ordinal());
    //move it over if more than 1 digit
    int x = (display.length() > 1) ? xRotTextbox - 3 : xRotTextbox;
    this.drawString(display, x, yHeightTxtbox + yOffset - 4);
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
    for (int k = 0; k < this.tile.getSizeInventory() - 1; k++) {
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerBuilder.SLOTX_START - 1 + k * Const.SQ, this.guiTop + ContainerBuilder.SLOTY - 1, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
    super.tryDrawFuelSlot(ContainerBaseMachine.SLOTX_FUEL - 1, ContainerBaseMachine.SLOTY_FUEL - 1);
    //    this.mc.getTextureManager().bindTexture(Const.Res.SLOT_COAL);
    //    Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerBaseMachine.SLOTX_FUEL - 1, this.guiTop + ContainerBaseMachine.SLOTY_FUEL - 1, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
  }
}
