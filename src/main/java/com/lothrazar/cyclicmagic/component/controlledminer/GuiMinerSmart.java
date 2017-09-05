package com.lothrazar.cyclicmagic.component.controlledminer;
import com.lothrazar.cyclicmagic.component.controlledminer.TileEntityControlledMiner.Fields;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.data.Const.ScreenSize;
import com.lothrazar.cyclicmagic.gui.ProgressBar;
import com.lothrazar.cyclicmagic.gui.base.ContainerBaseMachine;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import com.lothrazar.cyclicmagic.gui.button.ButtonIncrementField;
import com.lothrazar.cyclicmagic.gui.button.GuiButtonToggleSize;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiMinerSmart extends GuiBaseContainer {
  private TileEntityControlledMiner tile;
  private int xHeightTextbox = 100;
  private int yHeightTxtbox = 38;
  private ButtonIncrementField btnHeightDown;
  private ButtonIncrementField btnHeightUp;
  private GuiButtonToggleSize btnSize;
  private ButtonIncrementField btnWhitelist;
  public GuiMinerSmart(InventoryPlayer inventoryPlayer, TileEntityControlledMiner tileEntity) {
    super(new ContainerMinerSmart(inventoryPlayer, tileEntity), tileEntity);
    setScreenSize(ScreenSize.LARGE);
    tile = tileEntity;
    this.fieldRedstoneBtn = TileEntityControlledMiner.Fields.REDSTONE.ordinal();
    this.fieldPreviewBtn = TileEntityControlledMiner.Fields.RENDERPARTICLES.ordinal();
    this.progressBar = new ProgressBar(this, 10, ContainerMinerSmart.SLOTY + 22, TileEntityControlledMiner.Fields.TIMER.ordinal(), TileEntityControlledMiner.TIMER_FULL);
    this.setFieldFuel(TileEntityControlledMiner.Fields.FUEL.ordinal());
  }
  @Override
  public void initGui() {
    super.initGui();
    //first the main top left type button
    //    GuiUtils.drawContinuousTexturedBox(x, y, u, v, xHeightTextbox, xHeightTextbox, textureWidth, textureHeight, borderSize, xHeightTextbox);
    int id = 2;
    int yOffset = 16;
    //tooltips be like     setTooltip("button." + stype + "." + (goUp ? "up" : "down"));
    btnHeightDown = new ButtonIncrementField(
        id++,
        this.guiLeft + xHeightTextbox,
        this.guiTop + yHeightTxtbox + yOffset,
        tile.getPos(), TileEntityControlledMiner.Fields.HEIGHT.ordinal(), -1, 14, 14);
    btnHeightDown.setTooltip("button.height.down");
    btnHeightDown.displayString = "-";
    this.buttonList.add(btnHeightDown);
    btnHeightUp = new ButtonIncrementField(
        id++, this.guiLeft + xHeightTextbox,
        this.guiTop + yHeightTxtbox - yOffset - 4,
        tile.getPos(), TileEntityControlledMiner.Fields.HEIGHT.ordinal(), +1, 14, 14);
    btnHeightUp.setTooltip("button.height.up");
    btnHeightUp.displayString = "+";
    this.buttonList.add(btnHeightUp);
    int x = this.guiLeft + ContainerMinerSmart.SLOTX_START + 24;
    int y = this.guiTop + ContainerMinerSmart.SLOTY - 24;
    btnWhitelist = new ButtonIncrementField(id++,
        x, y,
        tile.getPos(), TileEntityControlledMiner.Fields.LISTTYPE.ordinal(), +1, 14, 14);
    btnWhitelist.width = 50;
    btnWhitelist.height = 20;
    this.buttonList.add(btnWhitelist);
    x = this.guiLeft + Const.PAD * 4;
    y = this.guiTop + Const.PAD * 3 + 2;
    btnSize = new GuiButtonToggleSize(id++,
        x, y, this.tile.getPos());
    this.buttonList.add(btnSize);
  }
  private void updateDisabledButtons() {
    this.btnHeightDown.enabled = (this.tile.getHeight() > 1);
    this.btnHeightUp.enabled = (this.tile.getHeight() < TileEntityControlledMiner.maxHeight);
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    for (int k = 0; k < ContainerMinerSmart.SLOTID_EQUIP; k++) {
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerMinerSmart.SLOTX_START - 1 + k * Const.SQ, this.guiTop + ContainerMinerSmart.SLOTY - 1, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
    Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerMinerSmart.SLOTEQUIP_X - 1, this.guiTop + ContainerMinerSmart.SLOTEQUIP_Y - 1, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    //    this.mc.getTextureManager().bindTexture(Const.Res.SLOT_COAL);
    super.tryDrawFuelSlot(ContainerBaseMachine.SLOTX_FUEL - 1, ContainerBaseMachine.SLOTY_FUEL - 1);//, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    btnSize.displayString = UtilChat.lang("button.harvester.size" + tile.getField(TileEntityControlledMiner.Fields.SIZE.ordinal()));
    btnWhitelist.displayString = UtilChat.lang("button.miner.whitelist." + tile.getField(Fields.LISTTYPE.ordinal()));
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    //    String s = UtilChat.lang("tile.block_miner_smart.blacklist");
    //    int x = ContainerMinerSmart.SLOTX_START - 2, 
    //    this.fontRendererObj.drawString(s, x, y, 4210752);
    int x = ContainerMinerSmart.SLOTEQUIP_X - 3;
    int y = ContainerMinerSmart.SLOTEQUIP_Y - 14;
    this.drawString("tile.block_miner_smart.tool", x, y);
    String display = "" + this.tile.getHeight();
    //move it over if more than 1 digit
    x = (display.length() > 1) ? xHeightTextbox + 2 : xHeightTextbox + 3;
    this.drawString(display, x, yHeightTxtbox);
    updateDisabledButtons();
  }
}
