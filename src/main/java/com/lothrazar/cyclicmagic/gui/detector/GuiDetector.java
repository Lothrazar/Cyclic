package com.lothrazar.cyclicmagic.gui.detector;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityDetector;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityDetector.EntityType;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityDetector.Fields;
import com.lothrazar.cyclicmagic.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiDetector extends GuiBaseContainer {
  static final int GUI_ROWS = 2;
  private TileEntityDetector tile;
  private int leftColX;
  private int sizeY;
  private int limitColX;
  private int[] yRows = new int[3];
  private ButtonDetector greaterLessBtn;
  private ButtonDetector entityBtn;
  public GuiDetector(InventoryPlayer inventoryPlayer, TileEntityDetector tileEntity) {
    super(new ContainerDetector(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
  }
  public String getTitle() {
    return "tile.entity_detector.name";
  }
  @Override
  public void initGui() {
    super.initGui();
    int id = 1;
    int vButtonSpacing = 12;
    sizeY = 58;//save now as reuse for textbox
    leftColX = 176 - 148;
    limitColX = leftColX + 108;
    addPatternButtonAt(id++, limitColX, sizeY - vButtonSpacing, true, Fields.LIMIT);
    addPatternButtonAt(id++, limitColX, sizeY + vButtonSpacing, false, Fields.LIMIT);
    int x = leftColX + 40;
    int y = sizeY - 5;
    this.greaterLessBtn = addPatternButtonAt(id++, x, y, true, Fields.GREATERTHAN, 60, 20);
    this.entityBtn = addPatternButtonAt(id++, x, 18, true, Fields.ENTITYTYPE, 40, 20);
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
  }
  private ButtonDetector addPatternButtonAt(int id, int x, int y, boolean isUp, Fields f) {
    return this.addPatternButtonAt(id, x, y, isUp, f, 15, 10);
  }
  private ButtonDetector addPatternButtonAt(int id, int x, int y, boolean isUp, Fields f, int w, int h) {
    ButtonDetector btn = new ButtonDetector(tile.getPos(), id,
        this.guiLeft + x,
        this.guiTop + y,
        isUp, f, w, h);
    this.buttonList.add(btn);
    return btn;
  }
  private void drawFieldAt(int x, int y, Fields f) {
    this.drawFieldAt(x, y, f.ordinal());
  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    //draw all text fields
    drawFieldAt(limitColX + 3, sizeY, Fields.LIMIT);
    drawFieldAt(leftColX, yRows[0], Fields.RANGEX);
    drawFieldAt(leftColX, yRows[1], Fields.RANGEY);
    drawFieldAt(leftColX, yRows[2], Fields.RANGEZ);
    //update button text
    EntityType t = this.tile.getEntityType();
    this.entityBtn.displayString = UtilChat.lang("tile.entity_detector." + t.name().toLowerCase());
    int greater = this.tile.getField(Fields.GREATERTHAN);
    String dir = (greater == 1) ? "greater" : "less";
    this.greaterLessBtn.displayString = UtilChat.lang("tile.entity_detector." + dir);
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
  }
}
