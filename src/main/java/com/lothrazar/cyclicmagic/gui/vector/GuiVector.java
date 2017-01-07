package com.lothrazar.cyclicmagic.gui.vector;
import java.io.IOException;
import java.util.ArrayList;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.tileentity.TileVector;
import com.lothrazar.cyclicmagic.block.tileentity.TileVector.Fields;
import com.lothrazar.cyclicmagic.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.net.PacketTileVector;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiVector extends GuiBaseContainer {
  static final int GUI_ROWS = 2;
  private TileVector tile;
  private int leftColX;
  private int sizeY;
  private int limitColX;
  private int[] yRows = new int[3];
  private ButtonVector greaterLessBtn;
  private ButtonVector entityBtn;
  private ArrayList<GuiTextFieldInteger> txtBoxes = new ArrayList<GuiTextFieldInteger>();
  public GuiVector(InventoryPlayer inventoryPlayer, TileVector tileEntity) {
    super(new ContainerVector(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
  }
  public String getTitle() {
    return "tile.plate_vector.name";
  }
  @Override
  public void initGui() {
    super.initGui();
    int id = 1;
    //    int vButtonSpacing = 12;
    //    sizeY = 58;//save now as reuse for textbox
    //    leftColX = 176 - 148;
    //    limitColX = leftColX + 108;
    int x = 20, y = 40;
    GuiTextFieldInteger  txtAngle = addTextbox(id++, x, y, tile.getAngle() + "",2);
    txtAngle.setFocused(true);//default
    txtAngle.setMaxVal(TileVector.MAX_ANGLE);
    txtAngle.setMinVal(0);
    txtAngle.setTileFieldId(TileVector.Fields.ANGLE.ordinal());
    x += 40;
    GuiTextFieldInteger  txtPower = addTextbox(id++, x, y, tile.getPower() + "",2);
    txtPower.setMaxVal(TileVector.MAX_POWER);
    txtPower.setMinVal(0);
    txtPower.setTileFieldId(TileVector.Fields.POWER.ordinal());
System.out.println("tile.getYaw()"+tile.getYaw());
    x += 40;
    GuiTextFieldInteger  txtYaw = addTextbox(id++, x, y, tile.getYaw() + "",3);
    txtYaw.setMaxVal(TileVector.MAX_YAW);
    txtYaw.setMinVal(0);
    txtYaw.setTileFieldId(TileVector.Fields.YAW.ordinal());
    //    addPatternButtonAt(id++, limitColX, sizeY - vButtonSpacing, true, Fields.LIMIT);
    //    addPatternButtonAt(id++, limitColX, sizeY + vButtonSpacing, false, Fields.LIMIT);
    //    int x = leftColX + 40;
    //    int y = sizeY - 5;
    //    this.greaterLessBtn = addPatternButtonAt(id++, x, y, true, Fields.GREATERTHAN, 60, 20);
    //    this.entityBtn = addPatternButtonAt(id++, x, 18, true, Fields.ENTITYTYPE, 60, 20);
    //    int xOffset = 18;
    //    int yOffset = 12;
    //    yRows[0] = 30 + yOffset;
    //    addPatternButtonAt(id++, leftColX + xOffset, yRows[0], true, Fields.RANGEX);
    //    addPatternButtonAt(id++, leftColX - xOffset - 4, yRows[0], false, Fields.RANGEX);
    //    yRows[1] = yRows[0] + yOffset;
    //    addPatternButtonAt(id++, leftColX + xOffset, yRows[1], true, Fields.RANGEY);
    //    addPatternButtonAt(id++, leftColX - xOffset - 4, yRows[1], false, Fields.RANGEY);
    //    yRows[2] = yRows[1] + yOffset;
    //    addPatternButtonAt(id++, leftColX + xOffset, yRows[2], true, Fields.RANGEZ);
    //    addPatternButtonAt(id++, leftColX - xOffset - 4, yRows[2], false, Fields.RANGEZ);
    //TODO: PREVIEW BUTTON
  }
  private GuiTextFieldInteger addTextbox(int id, int x, int y, String text, int maxLen) {
    int width = 10*maxLen, height = 20;
    
    GuiTextFieldInteger txt = new GuiTextFieldInteger(id, this.fontRendererObj, x, y, width, height);
    txt.setMaxStringLength(maxLen);
    txt.setText(text);
    txtBoxes.add(txt);
    return txt;
  }
  //  private ButtonVector addPatternButtonAt(int id, int x, int y, boolean isUp, Fields f, int w, int h) {
  //    ButtonVector btn = new ButtonVector(tile.getPos(), id,
  //        this.guiLeft + x,
  //        this.guiTop + y,
  //        isUp, f, w, h);
  //    this.buttonList.add(btn);
  //    return btn;
  //  }
  //  private ButtonVector addPatternButtonAt(int id, int x, int y, boolean isUp, Fields f) {
  //    return this.addPatternButtonAt(id, x, y, isUp, f, 15, 10);
  //  }
  //  private void drawFieldAt(int x, int y, Fields f) {
  //    this.drawFieldAt(x, y, f.ordinal());
  //  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    for (GuiTextField txt : txtBoxes) {
      if (txt != null) {
        txt.drawTextBox();
      }
    }
    //draw all text fields
    //    drawFieldAt(limitColX + 3, sizeY, Fields.LIMIT);
    //    drawFieldAt(leftColX, yRows[0], Fields.RANGEX);
    //    drawFieldAt(leftColX, yRows[1], Fields.RANGEY);
    //    drawFieldAt(leftColX, yRows[2], Fields.RANGEZ);
    //    //update button text
    //    EntityType t = this.tile.getEntityType();
    //    this.entityBtn.displayString = UtilChat.lang("tile.entity_detector." + t.name().toLowerCase());
    //    int greater = this.tile.getField(Fields.GREATERTHAN);
    //    String dir = CompareType.values()[greater].name().toLowerCase();
    //    this.greaterLessBtn.displayString = UtilChat.lang("tile.entity_detector." + dir);
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
  }
  // http://www.minecraftforge.net/forum/index.php?topic=22378.0
  // below is all the stuff that makes the text box NOT broken
  @Override
  public void updateScreen() {
    super.updateScreen();
    for (GuiTextField txt : txtBoxes) {
      if (txt != null) {
        txt.updateCursorCounter();
      }
    }
  }
  @Override
  protected void keyTyped(char pchar, int keyCode) throws IOException {
    super.keyTyped(pchar, keyCode);
    for (GuiTextFieldInteger txt : txtBoxes) {
      String oldval = txt.getText();
      txt.textboxKeyTyped(pchar, keyCode);
      String newval = txt.getText();
      boolean yes = false;
      try {
        int val = Integer.parseInt(newval);
        if (val <= txt.getMaxVal() && val >= txt.getMinVal()) {
          yes = true;
          ModCyclic.network.sendToServer(new PacketTileVector(tile.getPos(), val, txt.getTileFieldId()));
        }
      }
      catch (NumberFormatException e) {
      }
      if (!yes && !newval.isEmpty()) {//allow empty string in case user is in middle of deleting all and retyping
        txt.setText(oldval);//rollback
      }
    }
    
    
    
//    if (txtAngle != null && txtAngle.isFocused()) {
//      String oldval = txtAngle.getText();
//      txtAngle.textboxKeyTyped(pchar, keyCode);
//      String newval = txtAngle.getText();
//      boolean yes = false;
//      try {
//        //textbox needs these property:
//        //min value; max value, field integer... 
//        int val = Integer.parseInt(newval);
//        if (val <= txtAngle.getMaxVal() && val >= txtAngle.getMinVal()) {
//          yes = true;
//          ModCyclic.network.sendToServer(new PacketTileVector(tile.getPos(), val, txtAngle.getTileFieldId()));
//        }
//      }
//      catch (NumberFormatException e) {
//      }
//      if (!yes && !newval.isEmpty()) {//allow empty string in case user is in middle of deleting all and retyping
//        txtAngle.setText(oldval);//rollback
//      }
//    }
//    if (txtPower != null && txtPower.isFocused()) {
//      txtPower.textboxKeyTyped(pchar, keyCode);
//      System.out.println("TODO: save txtPower" + txtPower.getText());
//      // ModCyclic.network.sendToServer(new PacketTilePassword(txtPassword.getText(), ctr.tile.getPos()));
//    }
  }
  @Override
  protected void mouseClicked(int mouseX, int mouseY, int btn) throws IOException {
    super.mouseClicked(mouseX, mouseY, btn);// x/y pos is 33/30
    for (GuiTextField txt : txtBoxes) {
      txt.mouseClicked(mouseX, mouseY, btn);
      if (btn == 0) {//basically left click
        boolean flag = mouseX >= this.guiLeft + txt.xPosition && mouseX < this.guiLeft + txt.xPosition + txt.width
            && mouseY >= this.guiTop + txt.yPosition && mouseY < this.guiTop + txt.yPosition + txt.height;
        txt.setFocused(flag);
      }
    }
  }
  // ok end of textbox fixing stuff
}
