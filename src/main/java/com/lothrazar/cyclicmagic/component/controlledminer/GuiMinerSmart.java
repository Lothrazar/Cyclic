package com.lothrazar.cyclicmagic.component.controlledminer;
import com.lothrazar.cyclicmagic.component.controlledminer.TileEntityControlledMiner.Fields;
import com.lothrazar.cyclicmagic.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.gui.GuiButtonTogglePreview;
import com.lothrazar.cyclicmagic.gui.GuiButtonToggleSize;
import com.lothrazar.cyclicmagic.net.PacketTileSizeToggle;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.Const.ScreenSize;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiMinerSmart extends GuiBaseContainer {
  private TileEntityControlledMiner tile;
  private int xHeightTextbox = 150;
  private int yHeightTxtbox = ContainerMinerSmart.SLOTY;
  private ButtonMinerHeight btnHeightDown;
  private ButtonMinerHeight btnHeightUp;
  private GuiButtonToggleSize btnSize;
  private ButtonMinerHeight btnWhitelist; 
  public GuiMinerSmart(InventoryPlayer inventoryPlayer, TileEntityControlledMiner tileEntity) {
    super(new ContainerMinerSmart(inventoryPlayer, tileEntity), tileEntity);
    this.screenSize=ScreenSize.LARGE;
    this.xSize = screenSize.width();
    this.ySize = screenSize.height();
    tile = tileEntity;
    this.fieldRedstoneBtn = TileEntityControlledMiner.Fields.REDSTONE.ordinal();
 this.fieldPreviewBtn = TileEntityControlledMiner.Fields.RENDERPARTICLES.ordinal();
  }

  @Override
  public void initGui() {
    super.initGui();
    //first the main top left type button
    int id = 2;
    int yOffset = 18;
    btnHeightDown = new ButtonMinerHeight(tile.getPos(), id++, this.guiLeft + xHeightTextbox,
        this.guiTop + yHeightTxtbox + yOffset, false, TileEntityControlledMiner.Fields.HEIGHT);
    this.buttonList.add(btnHeightDown);
    btnHeightUp = new ButtonMinerHeight(tile.getPos(), id++, this.guiLeft + xHeightTextbox,
        this.guiTop + yHeightTxtbox - yOffset, true, TileEntityControlledMiner.Fields.HEIGHT);
    this.buttonList.add(btnHeightUp);
    int x = this.guiLeft + ContainerMinerSmart.SLOTX_START + 24;
    int y = this.guiTop + ContainerMinerSmart.SLOTY-24;
    btnWhitelist = new ButtonMinerHeight(tile.getPos(), id++,
        x, y, true, TileEntityControlledMiner.Fields.LISTTYPE);
    btnWhitelist.width = 46;
    btnWhitelist.height = 20;
    this.buttonList.add(btnWhitelist);
    x = this.guiLeft + Const.PAD*4;
    y = this.guiTop + Const.PAD * 3+2 ;
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
    int y = ContainerMinerSmart.SLOTEQUIP_Y-14;
    String s = UtilChat.lang("tile.block_miner_smart.tool");
    this.fontRendererObj.drawString(s, x, y, 4210752);
    String display = "" + this.tile.getHeight();
    //move it over if more than 1 digit
    x = (display.length() > 1) ? xHeightTextbox + 2 : xHeightTextbox + 3;
    this.fontRendererObj.drawString(display, x, yHeightTxtbox, 4210752);
    updateDisabledButtons();
  }
}
