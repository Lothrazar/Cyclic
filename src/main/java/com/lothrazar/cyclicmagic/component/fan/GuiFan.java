package com.lothrazar.cyclicmagic.component.fan;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import com.lothrazar.cyclicmagic.gui.button.ButtonIncrementField;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiFan extends GuiBaseContainer {
  private TileEntityFan tile;
  boolean debugLabels = false;
  private ButtonIncrementField btnHeightDown;
  private ButtonIncrementField btnHeightUp;
  private int xHeightTextbox = 176 - 25;
  private int yHeightTxtbox = 38;
  //  private ButtonFan btnTogglePrt;
  private ButtonIncrementField btnTogglePush;
  public GuiFan(InventoryPlayer inventoryPlayer, TileEntityFan tileEntity) {
    super(new ContainerFan(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
    this.fieldRedstoneBtn = TileEntityFan.Fields.REDSTONE.ordinal();
    this.fieldPreviewBtn = TileEntityFan.Fields.PARTICLES.ordinal();
  }
  @Override
  public void initGui() {
    super.initGui();
    int id = 2;
    int w = 18, h = 10;
    int yOffset = 14;
    int x = this.guiLeft + xHeightTextbox;
    int y = this.guiTop + yHeightTxtbox + yOffset;
    btnHeightDown = new ButtonIncrementField(id++, x, y, tile.getPos(),
        TileEntityFan.Fields.RANGE.ordinal(), -1, w, h);
    btnHeightDown.displayString = "-1";
    this.buttonList.add(btnHeightDown);
    ButtonIncrementField btnHeightDown5 = new ButtonIncrementField(id++, x, y + h + 1, tile.getPos(),
        TileEntityFan.Fields.RANGE.ordinal(), -5, w, h);
    btnHeightDown5.displayString = "-5";
    this.buttonList.add(btnHeightDown5);
    y = this.guiTop + yHeightTxtbox - yOffset;
    btnHeightUp = new ButtonIncrementField(id++, x, y, tile.getPos(),
        TileEntityFan.Fields.RANGE.ordinal(), +1, w, h);
    btnHeightUp.displayString = "+1";
    this.buttonList.add(btnHeightUp);
    ButtonIncrementField btnHeightUp5 = new ButtonIncrementField(id++, x, y - h - 1, tile.getPos(),
        TileEntityFan.Fields.RANGE.ordinal(), +5, w, h);
    btnHeightUp5.displayString = "+5";
    this.buttonList.add(btnHeightUp5);
    // SPEED BUTTONS
    ButtonIncrementField btnSpeedDown5 = new ButtonIncrementField(id++, btnHeightDown5.x - 20, btnHeightDown5.y, tile.getPos(),
        TileEntityFan.Fields.SPEED.ordinal(), -5, w, h);
    btnSpeedDown5.displayString = "-5";
    this.buttonList.add(btnSpeedDown5);
    ButtonIncrementField btnSpeedDown = new ButtonIncrementField(id++, btnHeightDown.x - 20, btnHeightDown.y, tile.getPos(),
        TileEntityFan.Fields.SPEED.ordinal(), -1, w, h);
    btnSpeedDown.displayString = "-1";
    this.buttonList.add(btnSpeedDown);
    ButtonIncrementField btnSpeedUp = new ButtonIncrementField(id++, btnHeightUp.x - 20, btnHeightUp.y, tile.getPos(),
        TileEntityFan.Fields.SPEED.ordinal(), +1, w, h);
    btnSpeedUp.displayString = "+1";
    this.buttonList.add(btnSpeedUp);
    ButtonIncrementField btnSpeedUp5 = new ButtonIncrementField(id++, btnHeightUp5.x - 20, btnHeightUp5.y, tile.getPos(),
        TileEntityFan.Fields.SPEED.ordinal(), +5, w, h);
    btnSpeedUp5.displayString = "+5";
    this.buttonList.add(btnSpeedUp5);
    w = 70;
    h = 20;
    x = this.guiLeft + 50;
    y = this.guiTop + 38;
    btnTogglePush = new ButtonIncrementField(id++, x, y, tile.getPos(),
        TileEntityFan.Fields.PUSHPULL.ordinal(), +1, w, h);
    this.buttonList.add(btnTogglePush);
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    for (int k = 0; k < this.tile.getSizeInventory(); k++) {
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerFan.SLOTX_START - 1 + k * Const.SQ, this.guiTop + ContainerFan.SLOTY - 1, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    //    btnTogglePrt.updateDisplayStringWith(tile);
//    btnTogglePush.updateDisplayStringWith(tile);
    btnTogglePush.displayString = UtilChat.lang("button.fan.pushpull" + tile.getField( TileEntityFan.Fields.PUSHPULL.ordinal()));
    
    String display = "" + this.tile.getRange();
    int x = (display.length() > 1) ? xHeightTextbox + 2 : xHeightTextbox + 3;
    this.drawString(display, x, yHeightTxtbox);
    display = "" + this.tile.getSpeed();
    x -= 20;
    this.drawString(display, x, yHeightTxtbox);
    //    btnSize.displayString = UtilChat.lang("button.harvester.size" + tile.getField(TileMachineHarvester.Fields.SIZE.ordinal()));
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
  }
}
