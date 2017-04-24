package com.lothrazar.cyclicmagic.component.crafter;
import com.lothrazar.cyclicmagic.gui.GuiBaseContanerProgress;
import com.lothrazar.cyclicmagic.gui.GuiButtonMachineRedstone;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiCrafter extends GuiBaseContanerProgress {
  public static final ResourceLocation GUI = new ResourceLocation(Const.MODID, "textures/gui/pattern.png");
  public static final int WIDTH = 176;
  public static final int HEIGHT = 212;
  private TileEntityCrafter tile;
  private GuiButtonMachineRedstone redstoneBtn;
  public GuiCrafter(InventoryPlayer inventoryPlayer, TileEntityCrafter tileEntity) {
    super(new ContainerCrafter(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
    this.xSize = WIDTH;
    this.ySize = HEIGHT;
  }
  @Override
  public void initGui() {
    super.initGui();
    redstoneBtn = new GuiButtonMachineRedstone(0,
        this.guiLeft + Const.padding / 2,
        this.guiTop + Const.padding / 2, this.tile.getPos());
    this.buttonList.add(redstoneBtn);
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    //input
    int xPrefix = Const.padding, yPrefix = 27;
    int rows = TileEntityCrafter.ROWS;
    int cols = TileEntityCrafter.COLS;
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + xPrefix - 1 + j * Const.SQ,
            this.guiTop + yPrefix - 1 + i * Const.SQ, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
      }
    }
    //grid
    rows = cols = 3;
    xPrefix = (WIDTH / 2 - (Const.SQ * 3) / 2);//calculate exact center
    yPrefix = 40;
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + xPrefix - 1 + j * Const.SQ,
            this.guiTop + yPrefix - 1 + i * Const.SQ, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
      }
    }
    //output
    xPrefix = 134;
    yPrefix = 27;
    rows = TileEntityCrafter.ROWS;
    cols = TileEntityCrafter.COLS;
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + xPrefix - 1 + j * Const.SQ,
            this.guiTop + yPrefix - 1 + i * Const.SQ, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
      }
    }
  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    redstoneBtn.setState(tile.getField(TileEntityCrafter.Fields.REDSTONE.ordinal()));
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
  }
  public int getProgressX() {
    return this.guiLeft + Const.padding + 2;
  }
  public int getProgressY() {
    return this.guiTop + 6 * Const.SQ + 10;
  }
  public int getProgressCurrent() {
    return tile.getField(TileEntityCrafter.Fields.TIMER.ordinal());
  }
  public int getProgressMax() {
    return TileEntityCrafter.TIMER_FULL;
  }
  public ResourceLocation getBackground() {
    return GUI;
  }
}
