package com.lothrazar.cyclicmagic.component.screen;
import java.io.IOException;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.component.screen.TileEntityScreen.Fields;
import com.lothrazar.cyclicmagic.data.Const.ScreenSize;
import com.lothrazar.cyclicmagic.gui.GuiSliderInteger;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiScreenBlock extends GuiBaseContainer {
  private GuiTextField txtPassword;
  public GuiScreenBlock(InventoryPlayer inventoryPlayer, TileEntityScreen tileEntity) {
    super(new ContainerScreen(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
    screenSize = ScreenSize.STANDARDPLAIN;
  }
  @Override
  public void initGui() {
    super.initGui();
    int id = 1;
    
    int width = 124;
    int xCenter=(xSize / 2 - width / 2);
    int h = 12;
    int x = this.guiLeft + 26;
    int y = this.guiTop + 15;
    GuiSliderInteger sliderX = new GuiSliderInteger(tile, id, x, y, width, h, 0, 255, Fields.RED.ordinal());
    sliderX.setTooltip("screen.red");
    
    this.addButton(sliderX);
    id++;
    y += h + 1;
    GuiSliderInteger sliderG = new GuiSliderInteger(tile, id, x, y, width, h, 0, 255, Fields.GREEN.ordinal());
    sliderX.setTooltip("screen.green");
    this.addButton(sliderG);
    id++;
    y += h + 1;
    GuiSliderInteger sliderB = new GuiSliderInteger(tile, id, x, y, width, h, 0, 255, Fields.BLUE.ordinal());
    sliderX.setTooltip("screen.blue");
    this.addButton(sliderB);
    //text box of course
    id++;
    y += h + 1;
    txtPassword = new GuiTextField(id, this.fontRenderer, xCenter, 58, width, h*4);
    txtPassword.setMaxStringLength(50);
    txtPassword.setText(((TileEntityScreen) tile).getText());
    txtPassword.setFocused(true);
    
  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    if (txtPassword != null) {
      txtPassword.drawTextBox();
    }
  }
  // http://www.minecraftforge.net/forum/index.php?topic=22378.0
  // below is all the stuff that makes the text box NOT broken
  @Override
  public void updateScreen() {
    super.updateScreen();
    if (txtPassword != null) {
      txtPassword.updateCursorCounter();
    }
  }
  @Override
  protected void keyTyped(char typedChar, int keyCode) throws IOException {
    if (this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode) == false) {
      super.keyTyped(typedChar, keyCode);
    }
    if (txtPassword != null && txtPassword.isFocused()) {
      txtPassword.textboxKeyTyped(typedChar, keyCode);
      //   ModCyclic.network.sendToServer(new PacketTilePassword(PacketType.PASSTEXT, txtPassword.getText(), ctr.tile.getPos()));
    }
  }
  @Override
  protected void mouseClicked(int x, int y, int btn) throws IOException {
    super.mouseClicked(x, y, btn);// x/y pos is 33/30
    if (txtPassword != null) {
      txtPassword.mouseClicked(x, y, btn);
      txtPassword.setFocused(true);
    }
  }
  // ok end of textbox fixing stuff
}
