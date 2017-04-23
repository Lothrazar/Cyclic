package com.lothrazar.cyclicmagic.component.pattern;
import org.lwjgl.opengl.GL11;
import com.lothrazar.cyclicmagic.component.harvester.TileEntityHarvester;
import com.lothrazar.cyclicmagic.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.gui.GuiButtonMachineRedstone;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiPattern extends GuiBaseContainer {
  public static final ResourceLocation GUI = new ResourceLocation(Const.MODID, Const.Res.folder + "pattern.png");
  static final int WIDTH = 176;
  static final int HEIGHT = 212;
  static final int GUI_ROWS = 2;
  private TileEntityPatternBuilder tile;
  private int leftColX;
  private int[] yRows = new int[3];
  private int rightColX;
  private int sizeY;
  private GuiButtonMachineRedstone redstoneBtn;
  private int sizeColX;
  private int heightColX;
  public GuiPattern(InventoryPlayer inventoryPlayer, TileEntityPatternBuilder tileEntity) {
    super(new ContainerPattern(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
    this.xSize = WIDTH;
    this.ySize = HEIGHT;
  }
  //  public String getTitle() {
  //    return "tile.builder_pattern.name";
  //  }
  @Override
  public ResourceLocation getBackground() {
    return GUI;
  }
  @Override
  public void initGui() { //BUTTON TO SWAP SOURCE AND DEST
    super.initGui();
    int id = 1;
    /////redstone button
    redstoneBtn = new GuiButtonMachineRedstone(id++,
        this.guiLeft + Const.padding,
        this.guiTop + Const.padding, this.tile.getPos());
    redstoneBtn.setTextureIndex(tile.getField(TileEntityHarvester.Fields.REDSTONE.ordinal()));
    this.buttonList.add(redstoneBtn);
    sizeY = 46;//save now as reuse for textbox
    int vButtonSpacing = 12;
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
        this.guiLeft + 176 / 2 - 10,
        this.guiTop + yRows[2], this.tile.getPos());
    this.buttonList.add(bt);
    // ButtonToggleRender
    ButtonToggleRender btn = new ButtonToggleRender(id++,
        this.guiLeft + 176 - 20 - Const.padding,
        this.guiTop + Const.padding, this.tile.getPos());
    this.buttonList.add(btn);
  }
  private void addPatternButtonAt(int id, int x, int y, boolean isUp, TileEntityPatternBuilder.Fields f) {
    ButtonPattern btn = new ButtonPattern(tile.getPos(), id,
        this.guiLeft + x,
        this.guiTop + y,
        isUp, f);
    this.buttonList.add(btn);
  }
  private void drawFieldAt(int x, int y, TileEntityPatternBuilder.Fields f) {
    this.drawFieldAt(x, y, f.ordinal());
  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    redstoneBtn.setState(tile.getField(TileEntityPatternBuilder.Fields.REDSTONE.ordinal()));
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
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(getBackground());
    int thisX = this.getMiddleX();
    int thisY = this.getMiddleY();
    int u = 0, v = 0;
    Gui.drawModalRectWithCustomSizedTexture(thisX, thisY, u, v, WIDTH, HEIGHT, WIDTH, HEIGHT);
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    int row = 0, col = 0;
    for (int i = 0; i < tile.getSizeInventory(); i++) {
      row = i / GUI_ROWS;// /3 will go 000, 111, 222
      col = i % GUI_ROWS; // and %3 will go 012 012 012
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerPattern.SLOTX_START - 1 + row * Const.SQ, this.guiTop + ContainerPattern.SLOTY_START - 1 + col * Const.SQ, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
  }
}
