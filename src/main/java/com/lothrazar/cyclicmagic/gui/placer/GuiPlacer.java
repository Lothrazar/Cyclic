package com.lothrazar.cyclicmagic.gui.placer;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachinePlacer;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiPlacer extends GuiContainer {
  private static final String folder = "textures/gui/";
  private static final ResourceLocation table = new ResourceLocation(Const.MODID, folder + "table.png");
  private static final ResourceLocation slot = new ResourceLocation(Const.MODID, folder + "inventory_slot.png");
  private static final ResourceLocation progress = new ResourceLocation(Const.MODID, folder + "progress.png");
  private static final int texture_width = 176;
  private static final int texture_height = 166;
  static final int padding = 8;
  private TileMachinePlacer tile;
  boolean debugLabels = false;
  public GuiPlacer(InventoryPlayer inventoryPlayer, TileMachinePlacer tileEntity) {
    super(new ContainerPlacer(inventoryPlayer, tileEntity));
    tile = tileEntity;
  }
  public GuiPlacer(Container c) {
    super(c);
  }
  @Override
  public void initGui() {
    super.initGui();
  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(table);
    int thisX = (this.width - this.xSize) / 2;
    int thisY = (this.height - this.ySize) / 2;
    int u = 0, v = 0;
    Gui.drawModalRectWithCustomSizedTexture(thisX, thisY, u, v, this.xSize, this.ySize, texture_width, texture_height);
    this.mc.getTextureManager().bindTexture(slot);
    for (int k = 0; k < this.tile.getSizeInventory(); k++) { // x had - 3 ??
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerPlacer.SLOTX_START - 1 + k * Const.SQ, this.guiTop + ContainerPlacer.SLOTY - 1, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
    if (tile.getTimer() > 0 && tile.getStackInSlot(0) != null) {
      this.mc.getTextureManager().bindTexture(progress);
      float percent = ((float) tile.getTimer()) / ((float) TileMachinePlacer.TIMER_FULL);
      // maximum progress bar is 156, since the whole texture is 176 minus
      // 10 padding on each side
      int belowSlots = this.guiTop + 9 + 3 * Const.SQ;
      // Args: x, y, u, v, width, height, textureWidth, textureHeight
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + 10, belowSlots + 5, u, v, (int) (156 * percent), 7, 156, 7);
    }
  }
}
