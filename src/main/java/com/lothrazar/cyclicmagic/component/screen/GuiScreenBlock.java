package com.lothrazar.cyclicmagic.component.screen;
import java.io.IOException;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.base.ITileTextbox;
import com.lothrazar.cyclicmagic.component.password.PacketTilePassword;
import com.lothrazar.cyclicmagic.component.screen.TileEntityScreen.Fields;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.data.Const.ScreenSize;
import com.lothrazar.cyclicmagic.gui.GuiSliderInteger;
import com.lothrazar.cyclicmagic.gui.GuiTextFieldMulti;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import com.lothrazar.cyclicmagic.gui.button.ButtonTileEntityField;
import com.lothrazar.cyclicmagic.net.PacketTileSetField;
import com.lothrazar.cyclicmagic.net.PacketTileTextbox;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiScreenBlock extends GuiBaseContainer {
  private GuiTextFieldMulti txtInput;
  TileEntityScreen screen;
  private ButtonTileEntityField btnToggle;
  public GuiScreenBlock(InventoryPlayer inventoryPlayer, TileEntityScreen tileEntity) {
    super(new ContainerScreen(inventoryPlayer, tileEntity), tileEntity);
    screen = tileEntity;
    screenSize = ScreenSize.STANDARDPLAIN;
  }
  @Override
  public void initGui() {
    super.initGui();
    int id = 1;
    int width = 124;
    int xCenter = (xSize / 2 - width / 2);
    int h = 12;
    int x = this.guiLeft + 26;
    int y = this.guiTop + 15;
    GuiSliderInteger sliderX = new GuiSliderInteger(tile, id, x, y, width, h, 0, 255, Fields.RED.ordinal());
    sliderX.setTooltip("screen.red");
    sliderX.appendPlusSignLabel = false;
    this.addButton(sliderX);
    id++;
    y += h + 1;
    GuiSliderInteger sliderG = new GuiSliderInteger(tile, id, x, y, width, h, 0, 255, Fields.GREEN.ordinal());
    sliderG.setTooltip("screen.green");
    sliderG.appendPlusSignLabel = false;
    this.addButton(sliderG);
    id++;
    y += h + 1;
    GuiSliderInteger sliderB = new GuiSliderInteger(tile, id, x, y, width, h, 0, 255, Fields.BLUE.ordinal());
    sliderB.setTooltip("screen.blue");
    sliderB.appendPlusSignLabel = false;
    this.addButton(sliderB);
    //text box of course
    id++;
    y += h + 1;
    txtInput = new GuiTextFieldMulti(id, this.fontRenderer, xCenter, 58, width, h * 6);
    txtInput.setMaxStringLength(ScreenTESR.MAX_TOTAL);
    txtInput.setText(screen.getText());
    txtInput.setFocused(true);
    txtInput.setCursorPosition(tile.getField(Fields.CURSORPOS.ordinal()));
    id++;
    btnToggle = new ButtonTileEntityField(id++,
        this.guiLeft + 4,
        this.guiTop + Const.PAD / 2, this.tile.getPos(), Fields.JUSTIFICATION.ordinal(), 1);
    btnToggle.setTooltip("screen.justification");
    btnToggle.width = 20;// btnToggle.height = 20;
    this.addButton(btnToggle);
  }
  @Override
  public void onGuiClosed() {
    if (txtInput != null) {
      tile.setField(Fields.CURSORPOS.ordinal(), this.txtInput.getCursorPosition());
      ModCyclic.network.sendToServer(new PacketTileSetField(tile.getPos(), Fields.CURSORPOS.ordinal(), this.txtInput.getCursorPosition()));
    }
  }
 
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    //TODO: RENDER PREVIEW HERE!
    if (txtInput != null) {
      txtInput.drawTextBox();
      txtInput.setTextColor(screen.getColor());
    }
    btnToggle.setTextureIndex(8 + tile.getField(Fields.JUSTIFICATION.ordinal()));
//    btnToggle.displayString = "screen." +screen.getJustification().name().toLowerCase();
    //TODO: btnToggle text/tooltip/textureIndex
  }
  // http://www.minecraftforge.net/forum/index.php?topic=22378.0
  // below is all the stuff that makes the text box NOT broken
  @Override
  public void updateScreen() {
    super.updateScreen();
    if (txtInput != null) {
      txtInput.updateCursorCounter();
    }
  }
  @Override
  protected void keyTyped(char typedChar, int keyCode) throws IOException {
    if (this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode) == false) {
      super.keyTyped(typedChar, keyCode);
    }
    if (txtInput != null && txtInput.isFocused()) {
      txtInput.textboxKeyTyped(typedChar, keyCode);
      ((ITileTextbox) tile).setText(txtInput.getText());
      ModCyclic.network.sendToServer(new PacketTileTextbox(txtInput.getText(), tile.getPos()));
    }
  }
  @Override
  protected void mouseClicked(int x, int y, int btn) throws IOException {
    super.mouseClicked(x, y, btn);// x/y pos is 33/30
    if (txtInput != null) {
      txtInput.mouseClicked(x, y, btn);
      txtInput.setFocused(true);
    }
  }
  // ok end of textbox fixing stuff
}
