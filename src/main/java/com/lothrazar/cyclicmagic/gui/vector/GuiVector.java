package com.lothrazar.cyclicmagic.gui.vector;
import java.io.IOException;
import java.util.ArrayList;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.tileentity.TileVector;
import com.lothrazar.cyclicmagic.block.tileentity.TileVector.Fields;
import com.lothrazar.cyclicmagic.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.gui.GuiButtonMachineRedstone;
import com.lothrazar.cyclicmagic.net.PacketTileVector;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiVector extends GuiBaseContainer {
  private static final int SOUTH = 0;
  private static final int NORTH = 180;
  private static final int EAST = 270;
  private static final int WEST = 90;
  private TileVector tile;
  private int xAngle = 14;
  private int yAngle = 56;//aka pitch
  private int xPower = 14;
  private int yPower = 98;
  private int xYaw = 116;
  private int yYaw = 60;
  private ArrayList<GuiTextFieldInteger> txtBoxes = new ArrayList<GuiTextFieldInteger>();
  private ButtonVector soundBtn;
  private GuiButtonMachineRedstone redstoneBtn;
  private GuiTextFieldInteger txtYaw;
  private GuiTextFieldInteger txtAngle;
  public GuiVector(InventoryPlayer inventoryPlayer, TileVector tileEntity) {
    super(new ContainerVector(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
  }
  public String getTitle() {
    return "tile.plate_vector.name";
  }
  @Override
  public ResourceLocation getBackground() {
    return Const.Res.TABLEPLAIN;
  }
  @Override
  public void initGui() {
    super.initGui();
    redstoneBtn = new GuiButtonMachineRedstone(0,
        this.guiLeft + 6,
        this.guiTop + 6, this.tile.getPos());
    redstoneBtn.setTextureIndex(tile.getField(TileVector.Fields.REDSTONE.ordinal()));
    this.buttonList.add(redstoneBtn);
    int id = 1;
    //angle text box
    txtAngle = addTextbox(id++, xAngle, yAngle, tile.getAngle() + "", 2);
    txtAngle.setFocused(true);//default
    txtAngle.setMaxVal(TileVector.MAX_ANGLE);
    txtAngle.setMinVal(0);
    txtAngle.setTileFieldId(TileVector.Fields.ANGLE.ordinal());
    //then the power text box
    GuiTextFieldInteger txtPower = addTextbox(id++, xPower, yPower, tile.getPower() + "", 3);
    txtPower.setMaxVal(TileVector.MAX_POWER);
    txtPower.setMinVal(1);
    txtPower.setTileFieldId(TileVector.Fields.POWER.ordinal());
    // yaw text box
    txtYaw = addTextbox(id++, xYaw, yYaw, tile.getYaw() + "", 3);
    txtYaw.setMaxVal(TileVector.MAX_YAW);
    txtYaw.setMinVal(0);
    txtYaw.setTileFieldId(TileVector.Fields.YAW.ordinal());
    //now the YAW buttons
    int btnYawSpacing = 22;
    addButtonAt(id++, xYaw + 5, yYaw + btnYawSpacing, SOUTH, Fields.YAW.ordinal()).displayString = "S";
    addButtonAt(id++, xYaw + 5, yYaw - btnYawSpacing, NORTH, Fields.YAW.ordinal()).displayString = "N";
    addButtonAt(id++, xYaw + btnYawSpacing + 10, yYaw, EAST, Fields.YAW.ordinal()).displayString = "E";
    addButtonAt(id++, xYaw - btnYawSpacing, yYaw, WEST, Fields.YAW.ordinal()).displayString = "W";
    addButtonAt(id++, xYaw + btnYawSpacing + 10, yYaw - btnYawSpacing, (NORTH + EAST) / 2, Fields.YAW.ordinal()).displayString = "NE";
    addButtonAt(id++, xYaw - btnYawSpacing, yYaw - btnYawSpacing, (NORTH + WEST) / 2, Fields.YAW.ordinal()).displayString = "NW";
    addButtonAt(id++, xYaw + btnYawSpacing + 10, yYaw + btnYawSpacing, (360 + EAST) / 2, Fields.YAW.ordinal()).displayString = "SE";
    addButtonAt(id++, xYaw - btnYawSpacing, yYaw + btnYawSpacing, (SOUTH + WEST) / 2, Fields.YAW.ordinal()).displayString = "SW";
    soundBtn = addButtonAt(id++, 134, 110, 0, Fields.SOUND.ordinal());
    soundBtn.setWidth(34);
    //angle buttons
    addButtonAt(id++, xAngle, yAngle - btnYawSpacing, 90, Fields.ANGLE.ordinal()).displayString = "^";
    addButtonAt(id++, xAngle + 24, yAngle - btnYawSpacing, 45, Fields.ANGLE.ordinal()).displayString = "/";
    addButtonAt(id++, xAngle + 24, yAngle, 0, Fields.ANGLE.ordinal()).displayString = "->";
  }
  private ButtonVector addButtonAt(int id, int x, int y, int val, int f) {
    ButtonVector btn = new ButtonVector(tile.getPos(), id,
        this.guiLeft + x,
        this.guiTop + y,
        val, f);
    this.buttonList.add(btn);
    return btn;
  }
  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    super.actionPerformed(button);
    if (button instanceof ButtonVector) {
      ButtonVector btn = (ButtonVector) button;
      if (btn.getFieldId() == Fields.SOUND.ordinal()) {//workaround so we can use the same button for sound as for others
        int newVal = (this.tile.getField(Fields.SOUND.ordinal()) + 1) % 2;
        ModCyclic.network.sendToServer(new PacketTileVector(tile.getPos(), newVal, Fields.SOUND.ordinal()));
      }
      else {
        for (GuiTextFieldInteger txt : txtBoxes) { //push value to the matching textbox
          if (txt.getTileFieldId() == btn.getFieldId()) {
            txt.setText(btn.getValue() + "");
          }
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
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    for (GuiTextField txt : txtBoxes) {
      if (txt != null) {
        txt.drawTextBox();
      }
    }
    for (GuiButton btn : this.buttonList) {
      if (btn instanceof ButtonVector) {
        ButtonVector btnv = (ButtonVector) btn;
        if (btnv.getFieldId() == Fields.YAW.ordinal()) {
          btnv.enabled = !txtYaw.getText().equals(btnv.getValue() + "");//tile.getYaw();
        }
        else if (btnv.getFieldId() == Fields.ANGLE.ordinal()) {
          btnv.enabled = !txtAngle.getText().equals(btnv.getValue() + "");
        }
      }
    }
    redstoneBtn.setState(tile.getField(TileVector.Fields.REDSTONE.ordinal()));
    soundBtn.displayString = UtilChat.lang("tile.plate_vector.gui.sound" + tile.getField(Fields.SOUND.ordinal()));
    renderString("tile.plate_vector.gui.power", xPower + 14, yPower + 26);
    renderString("tile.plate_vector.gui.angle", xAngle + 18, yAngle + 26);
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
  }
  private void renderString(String s, int x, int y) {
    String str = UtilChat.lang(s);
    int strWidth = this.fontRendererObj.getStringWidth(str);
    this.fontRendererObj.drawString(str, x - strWidth / 2, y, 4210752);
  }
  @Override
  public void updateScreen() { // http://www.minecraftforge.net/forum/index.php?topic=22378.0
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
      catch (NumberFormatException e) {}
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
