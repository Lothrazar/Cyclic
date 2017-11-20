package com.lothrazar.cyclicmagic.component.forester;
import com.lothrazar.cyclicmagic.component.forester.TileEntityForester.Fields;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.data.Const.ScreenSize;
import com.lothrazar.cyclicmagic.gui.ProgressBar;
import com.lothrazar.cyclicmagic.gui.base.ContainerBaseMachine;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer.ButtonTriggerWrapper.ButtonTriggerType;
import com.lothrazar.cyclicmagic.gui.button.ButtonTileEntityField;
import com.lothrazar.cyclicmagic.gui.button.GuiButtonToggleSize;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiForester extends GuiBaseContainer {
//  private TileEntityForester tile;
  private int xHeightTextbox = 100;
  private int yHeightTxtbox = 38;
  private ButtonTileEntityField btnHeightDown;
  private ButtonTileEntityField btnHeightUp;
 
  private ButtonTileEntityField btnWhitelist;
  public GuiForester(InventoryPlayer inventoryPlayer, TileEntityForester tileEntity) {
    super(new ContainerForester(inventoryPlayer, tileEntity), tileEntity);
    setScreenSize(ScreenSize.LARGE);
   
    this.fieldRedstoneBtn = Fields.REDSTONE.ordinal();
    this.fieldPreviewBtn = Fields.RENDERPARTICLES.ordinal();
//    this.progressBar = new ProgressBar(this, 10, ContainerForester.SLOTY + 22, Fields.TIMER.ordinal(), TileEntityForester.TIMER_FULL);
   this.setFieldFuel(Fields.FUEL.ordinal());
 
  }
  @Override
  public void initGui() {
    super.initGui();
    //first the main top left type button
    int id = 2;
    int yOffset = 16;
    int bSize = 14;
//    btnHeightDown = new ButtonTileEntityField(
//        id++,
//        this.guiLeft + xHeightTextbox,
//        this.guiTop + yHeightTxtbox + yOffset,
//        tile.getPos(), Fields.HEIGHT.ordinal(), -1, bSize, bSize);
//    btnHeightDown.setTooltip("button.height.down");
//    btnHeightDown.displayString = "-";
//    this.buttonList.add(btnHeightDown);
//    this.registerButtonDisableTrigger(btnHeightDown, ButtonTriggerType.EQUAL, Fields.HEIGHT.ordinal(), 1);
//    btnHeightUp = new ButtonTileEntityField(
//        id++, this.guiLeft + xHeightTextbox,
//        this.guiTop + yHeightTxtbox - yOffset - Const.PAD / 2,
//        tile.getPos(), Fields.HEIGHT.ordinal(), +1, bSize, bSize);
//    btnHeightUp.setTooltip("button.height.up");
//    btnHeightUp.displayString = "+";
//    this.buttonList.add(btnHeightUp);
//    this.registerButtonDisableTrigger(btnHeightUp, ButtonTriggerType.EQUAL, Fields.HEIGHT.ordinal(), TileEntityControlledMiner.maxHeight);
//    int x = this.guiLeft + ContainerMinerSmart.SLOTX_START + 24;
//    int y = this.guiTop + ContainerMinerSmart.SLOTY - 24;
//    btnWhitelist = new ButtonTileEntityField(id++,
//        x, y,
//        tile.getPos(), TileEntityControlledMiner.Fields.LISTTYPE.ordinal(), +1, bSize, bSize);
//    btnWhitelist.width = 50;
//    btnWhitelist.height = 20;
//    this.buttonList.add(btnWhitelist);
//    x = this.guiLeft + Const.PAD * 4;
//    y = this.guiTop + Const.PAD * 3 + 2;
//    btnSize = new GuiButtonToggleSize(id++,
//        x, y, this.tile.getPos());
//    this.buttonList.add(btnSize);
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0,x,y;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    for (int k = 0; k < TileEntityForester.INVENTORY_SIZE-1; k++) {
      
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerForester.SLOTX_START - 1 + (k%9) * Const.SQ, this.guiTop + ContainerForester.SLOTY - 1+((int)k/9) * Const.SQ, 
          u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
//    Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerMinerSmart.SLOTEQUIP_X - 1, this.guiTop + ContainerMinerSmart.SLOTEQUIP_Y - 1, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
//        this.mc.getTextureManager().bindTexture(Const.Res.SLOT_COAL);
//    super.tryDrawFuelSlot(ContainerBaseMachine.SLOTX_FUEL - 1, ContainerBaseMachine.SLOTY_FUEL - 1);//, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
//    btnSize.displayString = UtilChat.lang("button.harvester.size" + tile.getField(TileEntityControlledMiner.Fields.SIZE.ordinal()));
//    btnWhitelist.displayString = UtilChat.lang("button.miner.whitelist." + tile.getField(Fields.TYPEHARVEST.ordinal()));
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    //    String s = UtilChat.lang("tile.block_miner_smart.blacklist");
    //    int x = ContainerMinerSmart.SLOTX_START - 2, 
    //    this.fontRendererObj.drawString(s, x, y, 4210752);
//    int x = ContainerMinerSmart.SLOTEQUIP_X - 3;
//    int y = ContainerMinerSmart.SLOTEQUIP_Y - 14;
//    this.drawString("tile.block_miner_smart.tool", x, y);
//    String display = "" + this.tile.getHeight();
//    //move it over if more than 1 digit
//    x = (display.length() > 1) ? xHeightTextbox + 2 : xHeightTextbox + 3;
//    this.drawString(display, x, yHeightTxtbox);
  }
}
