package com.lothrazar.cyclicmagic.component.disenchanter;
import com.lothrazar.cyclicmagic.gui.ProgressBar;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.Const.ScreenSize;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiDisenchanter extends GuiBaseContainer {
  public static final ResourceLocation SLOT_GLOWSTONE = new ResourceLocation(Const.MODID, "textures/gui/inventory_slot_glowstone.png");
  public static final ResourceLocation SLOT_EBOTTLE = new ResourceLocation(Const.MODID, "textures/gui/inventory_slot_ebottle.png");
  public static final ResourceLocation SLOT_BOOK = new ResourceLocation(Const.MODID, "textures/gui/inventory_slot_book.png");
  public static final ResourceLocation SLOT_REDST = new ResourceLocation(Const.MODID, "textures/gui/inventory_slot_redstone.png");
  private TileEntityDisenchanter tile;
  public GuiDisenchanter(InventoryPlayer inventoryPlayer, TileEntityDisenchanter tileEntity) {
    super(new ContainerDisenchanter(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
    this.screenSize = ScreenSize.LARGE;
    this.xSize = screenSize.width();
    this.ySize = screenSize.height();
    this.fieldRedstoneBtn = TileEntityDisenchanter.Fields.REDSTONE.ordinal();
    this.progressBar = new ProgressBar(this, 10, 6 * Const.SQ + 10, TileEntityDisenchanter.Fields.TIMER.ordinal(), TileEntityDisenchanter.TIMER_FULL);
  }
  @Override
  public void initGui() {
    super.initGui();
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
          x = screenSize.width() / 2;
          y = ystart + spacing;
        break;
        case TileEntityDisenchanter.SLOT_GLOWSTONE://left mid
          this.mc.getTextureManager().bindTexture(SLOT_GLOWSTONE);
          x = screenSize.width() / 4;
          y = ystart + spacing;
        break;
        case TileEntityDisenchanter.SLOT_BOTTLE://bottom center
          this.mc.getTextureManager().bindTexture(SLOT_EBOTTLE);
          x = screenSize.width() / 2;
          y = ystart + 2 * spacing;
        break;
        case TileEntityDisenchanter.SLOT_REDSTONE:// right mid
          this.mc.getTextureManager().bindTexture(SLOT_REDST);
          x = screenSize.width() - screenSize.width() / 4;
          y = ystart + spacing;
        break;
        case TileEntityDisenchanter.SLOT_INPUT://top center
          this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
          x = screenSize.width() / 2;
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
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
  }
}
