package com.lothrazar.cyclicmagic.gui.uncrafting;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineUncrafter;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiUncrafting extends GuiContainer {
  private static final String folder = "textures/gui/";
  private static final ResourceLocation table = new ResourceLocation(Const.MODID, folder + "table.png");
  private static final ResourceLocation slot = new ResourceLocation(Const.MODID, folder + "inventory_slot.png");
  private static final ResourceLocation progress = new ResourceLocation(Const.MODID, folder + "progress.png");
  private static final ResourceLocation progressCtr = new ResourceLocation(Const.MODID, folder + "progress_ctr.png");
  private static final int progressWidth = 156;
  private static final int progressH = 7;
  private TileMachineUncrafter tile;
  public GuiUncrafting(InventoryPlayer inventoryPlayer, TileMachineUncrafter tileEntity) {
    super(new ContainerUncrafting(inventoryPlayer, tileEntity));
    tile = tileEntity;
  }
  public GuiUncrafting(Container c) {
    super(c);
  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    String s = UtilChat.lang("tile.uncrafting_block.name");
    this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(table);
    int thisX = (this.width - this.xSize) / 2;
    int thisY = (this.height - this.ySize) / 2;
    int texture_width = 176;
    int texture_height = 166;
    int u = 0, v = 0;
    Gui.drawModalRectWithCustomSizedTexture(thisX, thisY, u, v, this.xSize, this.ySize, texture_width, texture_height);
    this.mc.getTextureManager().bindTexture(slot);
    for (int k = 0; k < this.tile.getSizeInventory(); k++) {
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerUncrafting.SLOTX_START - 1 + k * Const.SQ, this.guiTop + ContainerUncrafting.SLOTY - 1, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
    int belowSlots = this.guiTop + 9 + 3 * Const.SQ + 5;
    this.mc.getTextureManager().bindTexture(progressCtr);
    Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + 10, belowSlots, u, v, (int) progressWidth, progressH, progressWidth, progressH);
    if (tile.getTimer() > 0 && tile.getStackInSlot(0) != null) {
      this.mc.getTextureManager().bindTexture(progress);
      float percent = ((float) tile.getTimer()) / ((float) TileMachineUncrafter.TIMER_FULL);
      // maximum progress bar is 156, since the whole texture is 176 minus
      // 10 padding on each side
      // Args: x, y, u, v, width, height, textureWidth, textureHeight
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + 10, belowSlots, u, v, (int) (progressWidth * percent), progressH, progressWidth, progressH);
    }
  }
}
