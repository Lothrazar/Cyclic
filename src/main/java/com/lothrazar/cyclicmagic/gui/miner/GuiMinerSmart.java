package com.lothrazar.cyclicmagic.gui.miner;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineMinerSmart;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineMinerSmart.Fields;
import com.lothrazar.cyclicmagic.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.gui.GuiButtonMachineRedstone;
import com.lothrazar.cyclicmagic.gui.GuiButtonSizePreview;
import com.lothrazar.cyclicmagic.gui.miner.ButtonMinerHeight;
import com.lothrazar.cyclicmagic.net.PacketTileSizeToggle;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiMinerSmart extends GuiBaseContainer {
  private TileMachineMinerSmart tile;
  private int xHeightTextbox = 176 - 26;
  private int yHeightTxtbox = 38;
  private ButtonMinerHeight btnHeightDown;
  private ButtonMinerHeight btnHeightUp;
  private GuiButtonMachineRedstone redstoneBtn;
  private GuiButtonSizePreview btnSize;
  private ButtonMinerHeight btnWhitelist;
  public GuiMinerSmart(InventoryPlayer inventoryPlayer, TileMachineMinerSmart tileEntity) {
    super(new ContainerMinerSmart(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
  }
  public String getTitle() {
    return "tile.block_miner_smart.name";
  }
  @Override
  public void initGui() {
    super.initGui();
    redstoneBtn = new GuiButtonMachineRedstone(0,
        this.guiLeft + 8,
        this.guiTop + 8, this.tile.getPos());
    redstoneBtn.setTextureIndex(tile.getField(TileMachineMinerSmart.Fields.REDSTONE.ordinal()));
    this.buttonList.add(redstoneBtn);
    //first the main top left type button
    int id = 2;
    int yOffset = 18;
    btnHeightDown = new ButtonMinerHeight(tile.getPos(), id++, this.guiLeft + xHeightTextbox,
        this.guiTop + yHeightTxtbox + yOffset, false, TileMachineMinerSmart.Fields.HEIGHT);
    this.buttonList.add(btnHeightDown);
    btnHeightUp = new ButtonMinerHeight(tile.getPos(), id++, this.guiLeft + xHeightTextbox,
        this.guiTop + yHeightTxtbox - yOffset, true, TileMachineMinerSmart.Fields.HEIGHT);
    this.buttonList.add(btnHeightUp);
    int x = this.guiLeft + 32;
    int y = this.guiTop + Const.padding * 2 + 4;
    btnWhitelist = new ButtonMinerHeight(tile.getPos(), id++,
        x, y, true, TileMachineMinerSmart.Fields.LISTTYPE);
    btnWhitelist.width = 46;
    btnWhitelist.height = 20;
    this.buttonList.add(btnWhitelist);
    x = this.guiLeft + Const.padding;
    y = this.guiTop + Const.padding * 2 + 44;
    btnSize = new GuiButtonSizePreview(id++,
        x, y, "", this.tile.getPos(),
        PacketTileSizeToggle.ActionType.SIZE);
    this.buttonList.add(btnSize);
    x = this.guiLeft + Const.padding * 2 + 40;
    GuiButtonSizePreview btnPreview = new GuiButtonSizePreview(id++,
        x, y, UtilChat.lang("button.harvester.preview"), this.tile.getPos(),
        PacketTileSizeToggle.ActionType.PREVIEW);
    this.buttonList.add(btnPreview);
  }
  private void updateDisabledButtons() {
    this.btnHeightDown.enabled = (this.tile.getHeight() > 1);
    this.btnHeightUp.enabled = (this.tile.getHeight() < TileMachineMinerSmart.maxHeight);
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
    redstoneBtn.setState(tile.getField(TileMachineMinerSmart.Fields.REDSTONE.ordinal()));
    btnSize.displayString = UtilChat.lang("button.harvester.size" + tile.getField(TileMachineMinerSmart.Fields.SIZE.ordinal()));
    btnWhitelist.displayString = UtilChat.lang("button.miner.whitelist." + tile.getField(Fields.LISTTYPE.ordinal()));
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    //    String s = UtilChat.lang("tile.block_miner_smart.blacklist");
    //    int x = ContainerMinerSmart.SLOTX_START - 2, 
    //    this.fontRendererObj.drawString(s, x, y, 4210752);
    int x = ContainerMinerSmart.SLOTEQUIP_X - 3;
    int y = 30;
    String s = UtilChat.lang("tile.block_miner_smart.tool");
    this.fontRendererObj.drawString(s, x, y, 4210752);
    String display = "" + this.tile.getHeight();
    //move it over if more than 1 digit
    x = (display.length() > 1) ? xHeightTextbox + 2 : xHeightTextbox + 3;
    this.fontRendererObj.drawString(display, x, yHeightTxtbox, 4210752);
    updateDisabledButtons();
  }
}
