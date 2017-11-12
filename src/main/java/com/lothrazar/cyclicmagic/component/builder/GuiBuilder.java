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
  private final static int yRowTextbox = 20;
  private int xControlsStart = 134;
  private final static int xControlsSpacing = 28;
  private int yOffset = 10 + Const.PAD;
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
    int x = this.guiLeft + Const.PAD + h;
    int y = this.guiTop + yOffset + Const.PAD;
    btn = new ButtonIncrementField(id++,
        x,
        y,
        tile.getPos(),
        TileEntityStructureBuilder.Fields.BUILDTYPE.ordinal(), 1,
        width, 20);
    btn.setTooltip("button.builder.tooltip");
    this.buttonList.add(btn);
    width = 12;
    h = width;
    TileEntityStructureBuilder.Fields fld = TileEntityStructureBuilder.Fields.SIZE;
    //////// all the control groups
    int yTopRow = this.guiTop + yOffset;
    int yBottomRow = this.guiTop + yRowTextbox + yOffset + Const.PAD;
    ////////// SIZE 
    x = this.guiLeft + xControlsStart;
    btnSizeUp = new ButtonIncrementField(id++,
        x,
        yTopRow,
        tile.getPos(),
        fld.ordinal(),
        1, width, h);
    btnSizeUp.setTooltip("button." + fld.name().toLowerCase() + "." + "up");
    btnSizeUp.displayString = "+";
    this.buttonList.add(btnSizeUp);
    btnSizeDown = new ButtonIncrementField(id++,
        x,
        yBottomRow,
        tile.getPos(),
        fld.ordinal(),
        -1, width, h);
    btnSizeDown.setTooltip("button." + fld.name().toLowerCase() + "." + "down");
    btnSizeDown.displayString = "-";
    this.buttonList.add(btnSizeDown);
    //////////////HEIGHT BUTTONS
    fld = TileEntityStructureBuilder.Fields.HEIGHT;
    x = this.guiLeft + xControlsStart - xControlsSpacing;
    btnHeightUp = new ButtonIncrementField(id++,
        x,
        yTopRow,
        tile.getPos(),
        fld.ordinal(),
        1, width, h);
    btnHeightUp.setTooltip("button." + fld.name().toLowerCase() + "." + "up");
    btnHeightUp.displayString = "+";
    this.buttonList.add(btnHeightUp);
    btnHeightDown = new ButtonIncrementField(id++,
        x,
        yBottomRow,
        tile.getPos(),
        fld.ordinal(),
        -1, width, h);
    btnHeightDown.setTooltip("button." + fld.name().toLowerCase() + "." + "down");
    btnHeightDown.displayString = "-";
    this.buttonList.add(btnHeightDown);
    //////////////////ROTATION BUTTONS
    fld = TileEntityStructureBuilder.Fields.ROTATIONS;
    x = this.guiLeft + xControlsStart - 2 * xControlsSpacing;
    ButtonIncrementField btnRotUp = new ButtonIncrementField(id++,
        x,
        this.guiTop + yOffset,
        tile.getPos(),
        fld.ordinal(),
        1, width, h);
    btnRotUp.setTooltip("button." + fld.name().toLowerCase() + "." + "up");
    btnRotUp.displayString = "+";
    this.buttonList.add(btnRotUp);
    ButtonIncrementField btnRotDown = new ButtonIncrementField(id++,
        x,
        yBottomRow,
        tile.getPos(),
        fld.ordinal(),
        -1, width, h);
    btnRotDown.setTooltip("button." + fld.name().toLowerCase() + "." + "down");
    btnRotDown.displayString = "-";
    this.buttonList.add(btnRotDown);
  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    this.btn.displayString = UtilChat.lang("buildertype." + this.tile.getBuildTypeEnum().name().toLowerCase() + ".name");
    int sp = Const.PAD / 2;
    int x = xControlsStart + sp;
    int y = yRowTextbox + yOffset - sp;
    if (this.tile.getSize() > 0) {
      String display = "" + this.tile.getSize();
      //move it over if more than 1 digit 
      this.drawStringCenteredCheckLength(display, x, y);
    }
    x = xControlsStart - xControlsSpacing + sp;
    if (this.tile.getHeight() > 0 && this.tile.getBuildTypeEnum().hasHeight()) {
      String display = "" + this.tile.getHeight();
      //move it over if more than 1 digit 
      this.drawStringCenteredCheckLength(display, x, y);
    }
    x = xControlsStart - 2 * xControlsSpacing + sp;
    String display = "" + this.tile.getField(Fields.ROTATIONS.ordinal());
    //move it over if more than 1 digit 
    this.drawStringCenteredCheckLength(display, x, y);
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
