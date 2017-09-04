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
  //private ButtonIncrementField btnHeightDown;
  //  private ButtonIncrementField btnHeightUp;
  private int xRange = 176 - 25;
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
    int x = this.guiLeft + xRange;
    int y = this.guiTop + yHeightTxtbox + yOffset;
    ButtonIncrementField btn = new ButtonIncrementField(id++, x, y, tile.getPos(),
        TileEntityFan.Fields.RANGE.ordinal(), -1, w, h);
    btn.setTooltip("button.fan.range.tooltip");
    btn.displayString = "-1";
    this.buttonList.add(btn);
    btn = new ButtonIncrementField(id++, x, y + h + 1, tile.getPos(),
        TileEntityFan.Fields.RANGE.ordinal(), -5, w, h);
    btn.setTooltip("button.fan.range.tooltip");
    btn.displayString = "-5";
    this.buttonList.add(btn);
    y = this.guiTop + yHeightTxtbox - yOffset;
    btn = new ButtonIncrementField(id++, x, y, tile.getPos(),
        TileEntityFan.Fields.RANGE.ordinal(), +1, w, h);
    btn.setTooltip("button.fan.range.tooltip");
    btn.displayString = "+1";
    this.buttonList.add(btn);
    btn = new ButtonIncrementField(id++, x, y - h - 1, tile.getPos(),
        TileEntityFan.Fields.RANGE.ordinal(), +5, w, h);
    btn.setTooltip("button.fan.range.tooltip");
    btn.displayString = "+5";
    this.buttonList.add(btn);
    // SPEED BUTTONS
    int xSpeed = this.guiLeft + xRange - 20;
    y = this.guiTop + yHeightTxtbox + yOffset;
    btn = new ButtonIncrementField(id++, xSpeed, y, tile.getPos(),
        TileEntityFan.Fields.SPEED.ordinal(), -5, w, h);
    btn.setTooltip("button.fan.speed.tooltip");
    btn.displayString = "-5";
    this.buttonList.add(btn);
    btn = new ButtonIncrementField(id++, xSpeed, y + h + 1, tile.getPos(),
        TileEntityFan.Fields.SPEED.ordinal(), -1, w, h);
    btn.setTooltip("button.fan.speed.tooltip");
    btn.displayString = "-1";
    this.buttonList.add(btn);
    y = this.guiTop + yHeightTxtbox - yOffset;
    btn = new ButtonIncrementField(id++, xSpeed, y, tile.getPos(),
        TileEntityFan.Fields.SPEED.ordinal(), +1, w, h);
    btn.setTooltip("button.fan.speed.tooltip");
    btn.displayString = "+1";
    this.buttonList.add(btn);
    btn = new ButtonIncrementField(id++, xSpeed, y - h - 1, tile.getPos(),
        TileEntityFan.Fields.SPEED.ordinal(), +5, w, h);
    btn.setTooltip("button.fan.speed.tooltip");
    btn.displayString = "+5";
    this.buttonList.add(btn);
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
    btnTogglePush.displayString = UtilChat.lang("button.fan.pushpull" + tile.getField(TileEntityFan.Fields.PUSHPULL.ordinal()));
    String display = "" + this.tile.getRange();
    int x = (display.length() > 1) ? xRange + 2 : xRange + 3;
    this.drawString(display, x, yHeightTxtbox);
    display = "" + this.tile.getSpeed();
    x -= 20;
    this.drawString(display, x, yHeightTxtbox);
    //    btnSize.displayString = UtilChat.lang("button.harvester.size" + tile.getField(TileMachineHarvester.Fields.SIZE.ordinal()));
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
  }
}
