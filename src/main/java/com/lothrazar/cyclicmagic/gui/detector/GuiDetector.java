package com.lothrazar.cyclicmagic.gui.detector;
import org.lwjgl.opengl.GL11;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityDetector;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityPatternBuilder;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityDetector.Fields;
import com.lothrazar.cyclicmagic.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.gui.GuiButtonMachineRedstone;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiDetector extends GuiBaseContainer {
//  static final int texture_width = 176;
//  static final int texture_height = 166;
  static final int GUI_ROWS = 2;
  private TileEntityDetector tile;
  private int leftColX;
  private int sizeY;
  private int sizeColX;
  private int[] yRows = new int[3];
  public GuiDetector(InventoryPlayer inventoryPlayer, TileEntityDetector tileEntity) {
    super(new ContainerDetector(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
  }
  public String getTitle() {
    return "tile.entity_detector.name";
  }
//  @Override
//  public ResourceLocation getBackground() {
//    return Const.Res.TABLEDEFAULT;
//  }
  @Override
  public void initGui() { //BUTTON TO SWAP SOURCE AND DEST
    super.initGui();
    int id = 1;


    int vButtonSpacing = 12;
    sizeY = 46;//save now as reuse for textbox
    leftColX = 176 - 148;
    sizeColX = leftColX + 40;
    addPatternButtonAt(id++, sizeColX, sizeY - vButtonSpacing, true, Fields.LIMIT);
    addPatternButtonAt(id++, sizeColX, sizeY + vButtonSpacing, false, Fields.LIMIT);
//    heightColX = leftColX + 62;
//    addPatternButtonAt(id++, heightColX, sizeY - vButtonSpacing, true, Fields.HEIGHT);
//    addPatternButtonAt(id++, heightColX, sizeY + vButtonSpacing, false, Fields.HEIGHT);
    
    
    
    
    
//    int vButtonSpacing = 12;
    leftColX = 176 - 148;
    sizeColX = leftColX + 40;
    int xOffset = 18;
    int yOffset = 12;
    yRows[0] = 30 + yOffset;
    addPatternButtonAt(id++, leftColX + xOffset, yRows[0], true, Fields.RANGEX);
    addPatternButtonAt(id++, leftColX - xOffset - 4, yRows[0], false, Fields.RANGEX);
    yRows[1] = yRows[0] + yOffset;
    addPatternButtonAt(id++, leftColX + xOffset, yRows[1], true, Fields.RANGEY);
    addPatternButtonAt(id++, leftColX - xOffset - 4, yRows[1], false, Fields.RANGEY);
    yRows[2] = yRows[1] + yOffset;
    addPatternButtonAt(id++, leftColX + xOffset, yRows[2], true, Fields.RANGEZ);
    addPatternButtonAt(id++, leftColX - xOffset - 4, yRows[2], false, Fields.RANGEZ);

    
    
    

  //TODO: PREVIEW BUTTON
    //TODO: toggle Entity Type button
    //TODO: toggle GREATERTHAN button
    
  }
  private void addPatternButtonAt(int id, int x, int y, boolean isUp, Fields f) {
    ButtonDetector btn = new ButtonDetector(tile.getPos(), id,
        this.guiLeft + x,
        this.guiTop + y,
        isUp, f);
    this.buttonList.add(btn);
  }
  private void drawFieldAt(int x, int y, Fields f) {
    this.drawFieldAt(x, y, f.ordinal());
  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
 //   redstoneBtn.setState(tile.getField(TileEntityPatternBuilder.Fields.REDSTONE.ordinal()));
    //draw all text fields
    drawFieldAt(sizeColX + 3, sizeY, Fields.LIMIT);
    drawFieldAt(leftColX, yRows[0], Fields.RANGEX);
    drawFieldAt(leftColX, yRows[1], Fields.RANGEY);
    drawFieldAt(leftColX, yRows[2], Fields.RANGEZ);
//    Fields.ENTITYTYPE
//    Fields.GREATERTHANt
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
  }
//  @Override
//  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
//    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//    this.mc.getTextureManager().bindTexture(getBackground());
//    int thisX = (this.width - this.xSize) / 2;
//    int thisY = (this.height - this.ySize) / 2;
//    int u = 0, v = 0;
//    Gui.drawModalRectWithCustomSizedTexture(thisX, thisY, u, v, texture_width, texture_height, texture_width, texture_height);
//    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
//    int row = 0, col = 0;
//    for (int i = 0; i < tile.getSizeInventory(); i++) {
//      row = i / GUI_ROWS;// /3 will go 000, 111, 222
//      col = i % GUI_ROWS; // and %3 will go 012 012 012
//      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerPattern.SLOTX_START - 1 + row * Const.SQ, this.guiTop + ContainerPattern.SLOTY_START - 1 + col * Const.SQ, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
//    }
//  }
}
