package com.lothrazar.cyclicmagic.component.fan;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiFan extends GuiBaseContainer {
  private TileEntityFan tile;
  boolean debugLabels = false;
  private ButtonFan btnHeightDown;
  private ButtonFan btnHeightUp;
  private int xHeightTextbox = 176 - 25;
  private int yHeightTxtbox = 38;
//  private ButtonFan btnTogglePrt;
  private ButtonFan btnTogglePush; 
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
    btnHeightDown = new ButtonFan(tile.getPos(), id++, x, y, w, h, -1, TileEntityFan.Fields.RANGE);
    btnHeightDown.displayString = "-1";
    this.buttonList.add(btnHeightDown);
    ButtonFan btnHeightDown5 = new ButtonFan(tile.getPos(), id++, x, y + h + 1, w, h, -5, TileEntityFan.Fields.RANGE);
    btnHeightDown5.displayString = "-5";
    this.buttonList.add(btnHeightDown5);
    y = this.guiTop + yHeightTxtbox - yOffset;
    btnHeightUp = new ButtonFan(tile.getPos(), id++, x, y, w, h, +1, TileEntityFan.Fields.RANGE);
    btnHeightUp.displayString = "+1";
    this.buttonList.add(btnHeightUp);
    ButtonFan btnHeightUp5 = new ButtonFan(tile.getPos(), id++, x, y - h - 1, w, h, +5, TileEntityFan.Fields.RANGE);
    btnHeightUp5.displayString = "+5";
    this.buttonList.add(btnHeightUp5);
    
    // SPEED BUTTONS

    ButtonFan  btnSpeedDown5 = new ButtonFan(tile.getPos(), id++, btnHeightDown5.x-20, btnHeightDown5.y, w, h, -5, TileEntityFan.Fields.SPEED);
    btnSpeedDown5.displayString = "-5";
    this.buttonList.add(btnSpeedDown5);
    
    ButtonFan  btnSpeedDown = new ButtonFan(tile.getPos(), id++, btnHeightDown.x-20, btnHeightDown.y, w, h, -1, TileEntityFan.Fields.SPEED);
    btnSpeedDown.displayString = "-1";
    this.buttonList.add(btnSpeedDown);
    

    ButtonFan  btnSpeedUp = new ButtonFan(tile.getPos(), id++, btnHeightUp.x-20, btnHeightUp.y, w, h, +1, TileEntityFan.Fields.SPEED);
    btnSpeedUp.displayString = "+1";
    this.buttonList.add(btnSpeedUp);
    

    ButtonFan  btnSpeedUp5 = new ButtonFan(tile.getPos(), id++, btnHeightUp5.x-20, btnHeightUp5.y, w, h, +5, TileEntityFan.Fields.SPEED);
    btnSpeedUp5.displayString = "+5";
    this.buttonList.add(btnSpeedUp5);
    
    w = 70;
    h = 20;
    x = this.guiLeft + 50;
    y = this.guiTop + 20;
//    btnTogglePrt = new ButtonFan(tile.getPos(), id++, x, y, w, h, +1, TileEntityFan.Fields.PARTICLES);
//    this.buttonList.add(btnTogglePrt);
    y = this.guiTop + 48;
    btnTogglePush = new ButtonFan(tile.getPos(), id++, x, y, w, h, +1, TileEntityFan.Fields.PUSHPULL);
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
    btnTogglePush.updateDisplayStringWith(tile);
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
