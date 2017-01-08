package com.lothrazar.cyclicmagic.gui.vector;
import java.io.IOException;
import java.util.ArrayList;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.tileentity.TileVector;
import com.lothrazar.cyclicmagic.block.tileentity.TileVector.Fields;
import com.lothrazar.cyclicmagic.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.net.PacketTileVector;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiVector extends GuiBaseContainer {
  static final int GUI_ROWS = 2;
  private TileVector tile;
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
    //angle text box
    int xAngle = 10;
    int yAngle = 40;
    GuiTextFieldInteger txtAngle = addTextbox(id++, xAngle, yAngle, tile.getAngle() + "", 2);
    txtAngle.setFocused(true);//default
    txtAngle.setMaxVal(TileVector.MAX_ANGLE);
    txtAngle.setMinVal(0);
    txtAngle.setTileFieldId(TileVector.Fields.ANGLE.ordinal());
    //then the power text box
    int x = 60, y = 40;
    GuiTextFieldInteger txtPower = addTextbox(id++, x, y, tile.getPower() + "", 2);
    txtPower.setMaxVal(TileVector.MAX_POWER);
    txtPower.setMinVal(1);
    txtPower.setTileFieldId(TileVector.Fields.POWER.ordinal());
    // yaw text box
    int xYaw = 110;
    int yYaw = 40;
    GuiTextFieldInteger txtYaw = addTextbox(id++, xYaw, yYaw, tile.getYaw() + "", 3);
    txtYaw.setMaxVal(TileVector.MAX_YAW);
    txtYaw.setMinVal(0);
    txtYaw.setTileFieldId(TileVector.Fields.YAW.ordinal());
    //now the YAW buttons
    int SOUTH = 0;
    int NORTH = 180;
    int EAST = 270;
    int WEST = 90;
    int btnYawSpacing = 22;
    addButtonAt(id++, xYaw + 5, yYaw + btnYawSpacing, SOUTH, Fields.YAW.ordinal()).displayString = "S";
    addButtonAt(id++, xYaw + 5, yYaw - btnYawSpacing, NORTH, Fields.YAW.ordinal()).displayString = "N";
    addButtonAt(id++, xYaw + btnYawSpacing + 10, yYaw, EAST, Fields.YAW.ordinal()).displayString = "E";
    addButtonAt(id++, xYaw - btnYawSpacing, yYaw, WEST, Fields.YAW.ordinal()).displayString = "W";
    addButtonAt(id++, xYaw + btnYawSpacing + 10, yYaw - btnYawSpacing, (NORTH + EAST) / 2, Fields.YAW.ordinal()).displayString = "NE";
    addButtonAt(id++, xYaw - btnYawSpacing, yYaw - btnYawSpacing, (NORTH + WEST) / 2, Fields.YAW.ordinal()).displayString = "NW";
    addButtonAt(id++, xYaw + btnYawSpacing + 10, yYaw + btnYawSpacing, (360 + EAST) / 2, Fields.YAW.ordinal()).displayString = "SE";
    addButtonAt(id++, xYaw - btnYawSpacing, yYaw + btnYawSpacing, (SOUTH + WEST) / 2, Fields.YAW.ordinal()).displayString = "SW";
    //angle buttons
//    addButtonAt(id++, xAngle, yAngle - btnYawSpacing, 90, Fields.ANGLE.ordinal());
//    addButtonAt(id++, xAngle, yAngle + btnYawSpacing, 0, Fields.ANGLE.ordinal());
  }
  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    super.actionPerformed(button);
    if (button instanceof ButtonVector) {
      ButtonVector btn = (ButtonVector) button;
      for (GuiTextFieldInteger txt : txtBoxes) { //push value to the matching textbox
        if (txt.getTileFieldId() == btn.getFieldId()) {
          txt.setText(btn.getValue() + "");
        }
      }
    }
  }
  private GuiTextFieldInteger addTextbox(int id, int x, int y, String text, int maxLen) {
    int width = 10 * maxLen, height = 20;
    GuiTextFieldInteger txt = new GuiTextFieldInteger(id, this.fontRendererObj, x, y, width, height);
    txt.setMaxStringLength(maxLen);
    txt.setText(text);
    txtBoxes.add(txt);
    return txt;
  }
  private ButtonVector addButtonAt(int id, int x, int y, int val, int f) {
    ButtonVector btn = new ButtonVector(tile.getPos(), id,
        this.guiLeft + x,
        this.guiTop + y,
        val, f);
    this.buttonList.add(btn);
    return btn;
  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    for (GuiTextField txt : txtBoxes) {
      if (txt != null) {
        txt.drawTextBox();
      }
    }
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
          //also set it clientisde to hopefully prevent desycn
          tile.setField(txt.getTileFieldId(), val);
          ModCyclic.network.sendToServer(new PacketTileVector(tile.getPos(), val, txt.getTileFieldId()));
        }
      }
      catch (NumberFormatException e) {
      }
      if (!yes && !newval.isEmpty()) {//allow empty string in case user is in middle of deleting all and retyping
        txt.setText(oldval);//rollback to the last valid value. ex if they type 'abc' revert to valid 
      }
    }
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
}
