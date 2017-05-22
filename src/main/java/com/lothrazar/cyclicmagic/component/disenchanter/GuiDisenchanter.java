package com.lothrazar.cyclicmagic.component.disenchanter;
import com.lothrazar.cyclicmagic.component.disenchanter.TileEntityDisenchanter.Fields;
import com.lothrazar.cyclicmagic.gui.GuiBaseContanerProgress;
import com.lothrazar.cyclicmagic.gui.GuiButtonMachineRedstone;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.Const.ScreenSize;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiDisenchanter extends GuiBaseContanerProgress {
  public static final int WIDTH = 176;
  public static final int HEIGHT = 212;
  public static final ResourceLocation SLOT_GLOWSTONE = new ResourceLocation(Const.MODID, "textures/gui/inventory_slot_glowstone.png");
  public static final ResourceLocation SLOT_EBOTTLE = new ResourceLocation(Const.MODID, "textures/gui/inventory_slot_ebottle.png");
  public static final ResourceLocation SLOT_BOOK = new ResourceLocation(Const.MODID, "textures/gui/inventory_slot_book.png");
  public static final ResourceLocation SLOT_REDST = new ResourceLocation(Const.MODID, "textures/gui/inventory_slot_redstone.png");
  
  private TileEntityDisenchanter tile;
  private GuiButtonMachineRedstone redstoneBtn;
  public GuiDisenchanter(InventoryPlayer inventoryPlayer, TileEntityDisenchanter tileEntity) {
    super(new ContainerDisenchanter(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
    this.xSize = WIDTH;
    this.ySize = HEIGHT;
    this.screenSize = ScreenSize.LARGE;
  }
  @Override
  public void initGui() {
    super.initGui();
    redstoneBtn = new GuiButtonMachineRedstone(0,
        this.guiLeft + 8,
        this.guiTop + 8, this.tile.getPos());
    this.buttonList.add(redstoneBtn);
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    int x = 0, y = 0, ystart = 20, spacing = 26;
    for (int i = 0; i < tile.getSizeInventory(); i++) {
      switch (i) {
        case TileEntityDisenchanter.SLOT_BOOK://center center
          this.mc.getTextureManager().bindTexture(SLOT_BOOK);
          x = GuiDisenchanter.WIDTH / 2;
          y = ystart + spacing;
        break;
        case TileEntityDisenchanter.SLOT_GLOWSTONE://left mid
          this.mc.getTextureManager().bindTexture(SLOT_GLOWSTONE);
          x = GuiDisenchanter.WIDTH / 4;
          y = ystart + spacing;
        break;
        case TileEntityDisenchanter.SLOT_BOTTLE://bottom center
          this.mc.getTextureManager().bindTexture(SLOT_EBOTTLE);
          x = GuiDisenchanter.WIDTH / 2;
          y = ystart + 2 * spacing;
        break;
        case TileEntityDisenchanter.SLOT_REDSTONE:// right mid
          this.mc.getTextureManager().bindTexture(SLOT_REDST);
          x = GuiDisenchanter.WIDTH - GuiDisenchanter.WIDTH / 4;
          y = ystart + spacing;
        break;
        case TileEntityDisenchanter.SLOT_INPUT://top center
          this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
          x = GuiDisenchanter.WIDTH / 2;
          y = ystart;
        break;
        default:
          this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
          x = Const.PAD + (i - 5) * Const.SQ;
          y = ystart + 3 * spacing - 1;
        break;
      }
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft - 1 + x, this.guiTop - 1 + y, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    redstoneBtn.setState(tile.getField(Fields.REDSTONE.ordinal()));
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
  }
  public int getProgressX() {
    return this.guiLeft + Const.PAD + 2;
  }
  public int getProgressY() {
    return this.guiTop + 6 * Const.SQ + 10;
  }
  public int getProgressCurrent() {
    return tile.getField(Fields.TIMER.ordinal());
  }
  public int getProgressMax() {
    return TileEntityDisenchanter.TIMER_FULL;
  }
}
