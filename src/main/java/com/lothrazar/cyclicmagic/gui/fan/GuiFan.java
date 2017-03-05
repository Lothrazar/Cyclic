package com.lothrazar.cyclicmagic.gui.fan;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityFan;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineMinerSmart;
import com.lothrazar.cyclicmagic.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.gui.GuiButtonMachineRedstone;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiFan extends GuiBaseContainer {
  private TileEntityFan tile;
  boolean debugLabels = false;
  private GuiButtonMachineRedstone redstoneBtn;
  private ButtonFan btnHeightDown;
  private ButtonFan btnHeightUp;
  private int xHeightTextbox = 176 - 26;
  private int yHeightTxtbox = 38;
  private ButtonFan btnTogglePrt;
  private ButtonFan btnTogglePush;
  
  public GuiFan(InventoryPlayer inventoryPlayer, TileEntityFan tileEntity) {
    super(new ContainerFan(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
  }
  @Override
  public void initGui() {
    super.initGui();
    int id = 0;
    redstoneBtn = new GuiButtonMachineRedstone(id++,
        this.guiLeft + Const.padding,
        this.guiTop + Const.padding, this.tile.getPos());
    redstoneBtn.setTextureIndex(tile.getField(TileEntityFan.Fields.REDSTONE.ordinal()));
    this.buttonList.add(redstoneBtn);
    
    int w = 15, h = 10;

    int yOffset = 18;
    int x = this.guiLeft + xHeightTextbox;
    int y = this.guiTop + yHeightTxtbox + yOffset;
    btnHeightDown = new ButtonFan(tile.getPos(), id++,x,y,w,h, -1,TileEntityFan.Fields.RANGE);
    this.buttonList.add(btnHeightDown);
    y = this.guiTop + yHeightTxtbox - yOffset;
    btnHeightUp = new ButtonFan(tile.getPos(), id++,x,y, w,h,+1, TileEntityFan.Fields.RANGE);
    this.buttonList.add(btnHeightUp);
    w = 50;
    h = 20;
    
    
    x = this.guiLeft + 50;
    y = this.guiTop + 20;
    
    btnTogglePrt = new ButtonFan(tile.getPos(), id++,x,y, w,h,+1, TileEntityFan.Fields.PARTICLES);
    this.buttonList.add(btnTogglePrt);
    

//    x = this.guiLeft + 28;
    y = this.guiTop + 48;
    
    btnTogglePush = new ButtonFan(tile.getPos(), id++,x,y, w,h,+1, TileEntityFan.Fields.PUSHPULL);
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
    redstoneBtn.setState(tile.getField(TileEntityFan.Fields.REDSTONE.ordinal()));
    
    btnTogglePrt.updateDisplayStringWith(tile);
    btnTogglePush.updateDisplayStringWith(tile);
 
    
    String display = "" + this.tile.getRange();
  
    int  x = (display.length() > 1) ? xHeightTextbox + 2 : xHeightTextbox + 3;
 
    this.drawString(display, x, yHeightTxtbox);
    
    
//    btnSize.displayString = UtilChat.lang("button.harvester.size" + tile.getField(TileMachineHarvester.Fields.SIZE.ordinal()));
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
  }
}
