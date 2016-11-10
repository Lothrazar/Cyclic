package com.lothrazar.cyclicmagic.gui.miner;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineMinerSmart;
import com.lothrazar.cyclicmagic.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.gui.miner.ButtonMinerHeight;
import com.lothrazar.cyclicmagic.gui.uncrafting.GuiButtonUncraftingRedstone;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiMinerSmart extends GuiBaseContainer {
  private TileMachineMinerSmart tile;
  private int xHeightTextbox = 176 - 26;
  private int yHeightTxtbox = 38;
  private ButtonMinerHeight btnHeightDown;
  private ButtonMinerHeight btnHeightUp;
  private GuiButtonUncraftingRedstone redstoneBtn;
  public GuiMinerSmart(InventoryPlayer inventoryPlayer, TileMachineMinerSmart tileEntity) {
    super(new ContainerMinerSmart(inventoryPlayer, tileEntity));
    tile = tileEntity;
  }
  public GuiMinerSmart(Container c) {
    super(c);
  }
  public String getTitle() {
    return "tile.block_miner_smart.name";
  }
  @Override
  public void initGui() {
    super.initGui();
    redstoneBtn = new GuiButtonUncraftingRedstone(0,
        this.guiLeft + 8,
        this.guiTop + 8, this.tile.getPos());
    redstoneBtn.setTextureIndex(tile.getField(TileMachineMinerSmart.Fields.REDSTONE.ordinal()));
    this.buttonList.add(redstoneBtn);
    //first the main top left type button
    int id = 2;
    int yOffset = 18;
    btnHeightDown = new ButtonMinerHeight(tile.getPos(), id++, this.guiLeft + xHeightTextbox, this.guiTop + yHeightTxtbox + yOffset, false, "height");
    this.buttonList.add(btnHeightDown);
    btnHeightUp = new ButtonMinerHeight(tile.getPos(), id++, this.guiLeft + xHeightTextbox, this.guiTop + yHeightTxtbox - yOffset, true, "height");
    this.buttonList.add(btnHeightUp);
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
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    
    
    
    
    String s = UtilChat.lang("tile.block_miner_smart.blacklist");
    //      int x = this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, y = 18;
    int x = ContainerMinerSmart.SLOTX_START - 2, y = 30;
    this.fontRendererObj.drawString(s, x, y, 4210752);
    x = ContainerMinerSmart.SLOTEQUIP_X - 3;
    s = UtilChat.lang("tile.block_miner_smart.tool");
    this.fontRendererObj.drawString(s, x, y, 4210752);
    String display = "" + this.tile.getHeight();
    //move it over if more than 1 digit
    x = (display.length() > 1) ? xHeightTextbox + 2 : xHeightTextbox + 3;
    this.fontRendererObj.drawString(display, x, yHeightTxtbox, 4210752);
    updateDisabledButtons();
  }
}
