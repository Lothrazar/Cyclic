package com.lothrazar.cyclicmagic.component.pattern;
import com.lothrazar.cyclicmagic.component.pattern.TileEntityPatternBuilder.Fields;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.data.Const.ScreenSize;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import com.lothrazar.cyclicmagic.gui.button.ButtonTileEntityField;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiPattern extends GuiBaseContainer {
  static final int GUI_ROWS = 2;
  private TileEntityPatternBuilder tile;
  private int leftColX;
  private int[] yRows = new int[3];
  private int rightColX;
  private int sizeY;
  private int sizeColX;
  private int heightColX;
  private ButtonTileEntityField btnRotation;
  private ButtonTileEntityField btnFlipZ;
  private ButtonTileEntityField btnFlipY;
  private ButtonTileEntityField btnFlipX;
  public GuiPattern(InventoryPlayer inventoryPlayer, TileEntityPatternBuilder tileEntity) {
    super(new ContainerPattern(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
    screenSize = ScreenSize.LARGE;
    this.xSize = screenSize.width();
    this.ySize = screenSize.height();
    this.fieldRedstoneBtn = TileEntityPatternBuilder.Fields.REDSTONE.ordinal();
    this.fieldPreviewBtn = TileEntityPatternBuilder.Fields.RENDERPARTICLES.ordinal();
  }
  @Override
  public void initGui() {
    super.initGui();
    int id = 2;
    /////redstone button
    //button rotation 
    btnRotation = new ButtonTileEntityField(id++,
        this.guiLeft + 26, this.guiTop + 15, tile.getPos(),
        Fields.ROTATION.ordinal(), 1, 40, 16);
    btnRotation.setTooltip("tile.builder_pattern.rotation");
    this.addButton(btnRotation);
    // flips
    btnFlipX = new ButtonTileEntityField(id++,
        btnRotation.x + btnRotation.width + 4, this.guiTop + 15, tile.getPos(),
        Fields.FLIPX.ordinal(), 1, 20, 16);
    btnFlipX.setTooltip("tile.builder_pattern.flipaxis");
    this.addButton(btnFlipX);
    // y
    btnFlipY = new ButtonTileEntityField(id++,
        btnFlipX.x + btnFlipX.width + 4, btnFlipX.y, tile.getPos(),
        Fields.FLIPY.ordinal(), 1, 20, 16);
    btnFlipY.setTooltip("tile.builder_pattern.flipaxis");
    this.addButton(btnFlipY);
    // z
    btnFlipZ = new ButtonTileEntityField(id++,
        btnFlipY.x + btnFlipY.width + 4, btnFlipY.y, tile.getPos(),
        Fields.FLIPZ.ordinal(), 1, 20, 16);
    btnFlipZ.setTooltip("tile.builder_pattern.flipaxis");
    this.addButton(btnFlipZ);
    //all the small buttons
    int vButtonSpacing = 12;
    sizeY = 46;
    leftColX = 176 - 148;
    sizeColX = leftColX + 40;
    addPatternButtonAt(id++, sizeColX, sizeY - vButtonSpacing, true, TileEntityPatternBuilder.Fields.SIZER);
    addPatternButtonAt(id++, sizeColX, sizeY + vButtonSpacing, false, TileEntityPatternBuilder.Fields.SIZER);
    heightColX = leftColX + 62;
    addPatternButtonAt(id++, heightColX, sizeY - vButtonSpacing, true, TileEntityPatternBuilder.Fields.HEIGHT);
    addPatternButtonAt(id++, heightColX, sizeY + vButtonSpacing, false, TileEntityPatternBuilder.Fields.HEIGHT);
    int xOffset = 18;
    int yOffset = 12;
    yRows[0] = 40 + yOffset;
    addPatternButtonAt(id++, leftColX + xOffset, yRows[0], true, TileEntityPatternBuilder.Fields.OFFTARGX);
    addPatternButtonAt(id++, leftColX - xOffset - 4, yRows[0], false, TileEntityPatternBuilder.Fields.OFFTARGX);
    yRows[1] = yRows[0] + yOffset;
    addPatternButtonAt(id++, leftColX + xOffset, yRows[1], true, TileEntityPatternBuilder.Fields.OFFTARGY);
    addPatternButtonAt(id++, leftColX - xOffset - 4, yRows[1], false, TileEntityPatternBuilder.Fields.OFFTARGY);
    yRows[2] = yRows[1] + yOffset;
    addPatternButtonAt(id++, leftColX + xOffset, yRows[2], true, TileEntityPatternBuilder.Fields.OFFTARGZ);
    addPatternButtonAt(id++, leftColX - xOffset - 4, yRows[2], false, TileEntityPatternBuilder.Fields.OFFTARGZ);
    rightColX = 108;
    addPatternButtonAt(id++, leftColX + xOffset + rightColX, yRows[0], true, TileEntityPatternBuilder.Fields.OFFSRCX);
    addPatternButtonAt(id++, leftColX - xOffset - 4 + rightColX, yRows[0], false, TileEntityPatternBuilder.Fields.OFFSRCX);
    addPatternButtonAt(id++, leftColX + xOffset + rightColX, yRows[1], true, TileEntityPatternBuilder.Fields.OFFSRCY);
    addPatternButtonAt(id++, leftColX - xOffset - 4 + rightColX, yRows[1], false, TileEntityPatternBuilder.Fields.OFFSRCY);
    addPatternButtonAt(id++, leftColX + xOffset + rightColX, yRows[2], true, TileEntityPatternBuilder.Fields.OFFSRCZ);
    addPatternButtonAt(id++, leftColX - xOffset - 4 + rightColX, yRows[2], false, TileEntityPatternBuilder.Fields.OFFSRCZ);
    //flip button in bottom center
    ButtonFlipRegions bt = new ButtonFlipRegions(id++,
        this.guiLeft + screenSize.width() / 2 - 12,
        this.guiTop + yRows[2], this.tile.getPos());
    bt.displayString = "<->";
    bt.setTooltip("tile.builder_pattern.flip");
    //24, 12,
    this.buttonList.add(bt);
  }
  private void addPatternButtonAt(int id, int x, int y, boolean isUp, TileEntityPatternBuilder.Fields f) {
    ButtonTileEntityField btn = new ButtonTileEntityField(id,
        this.guiLeft + x, this.guiTop + y, tile.getPos(),
        f.ordinal(), (isUp) ? 1 : -1,
        15, 10);
    btn.displayString = (isUp) ? "+" : "-";
    //15, 10
    btn.setTooltip("tile.builder_pattern." + f.name().toLowerCase() + (isUp ? "up" : "down"));
    this.buttonList.add(btn);
  }
  private void drawFieldAt(int x, int y, TileEntityPatternBuilder.Fields f) {
    this.drawFieldAt(x, y, f.ordinal());
  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    btnRotation.displayString = this.tile.getRotationName();
    btnFlipX.displayString = ((tile.getField(Fields.FLIPX) == 1) ? "^" : "") + "X";
    btnFlipY.displayString = ((tile.getField(Fields.FLIPY) == 1) ? "^" : "") + "Y";
    btnFlipZ.displayString = ((tile.getField(Fields.FLIPZ) == 1) ? "^" : "") + "Z";
    //draw all text fields
    drawFieldAt(sizeColX + 3, sizeY, TileEntityPatternBuilder.Fields.SIZER);
    drawFieldAt(leftColX, yRows[0], TileEntityPatternBuilder.Fields.OFFTARGX);
    drawFieldAt(leftColX, yRows[1], TileEntityPatternBuilder.Fields.OFFTARGY);
    drawFieldAt(leftColX, yRows[2], TileEntityPatternBuilder.Fields.OFFTARGZ);
    drawFieldAt(heightColX + 3, sizeY, TileEntityPatternBuilder.Fields.HEIGHT);
    int xOtherbox = leftColX + rightColX + 5;
    drawFieldAt(xOtherbox, yRows[0], TileEntityPatternBuilder.Fields.OFFSRCX);
    drawFieldAt(xOtherbox, yRows[1], TileEntityPatternBuilder.Fields.OFFSRCY);
    drawFieldAt(xOtherbox, yRows[2], TileEntityPatternBuilder.Fields.OFFSRCZ);
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    int row = 0, col = 0;
    for (int i = 0; i < tile.getSizeInventory(); i++) {
      row = i / GUI_ROWS;// /3 will go 000, 111, 222
      col = i % GUI_ROWS; // and %3 will go 012 012 012
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerPattern.SLOTX_START - 1 + row * Const.SQ, this.guiTop + ContainerPattern.SLOTY_START - 1 + col * Const.SQ, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
  }
}
