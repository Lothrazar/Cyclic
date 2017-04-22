package com.lothrazar.cyclicmagic.gui.disenchanter;
import org.lwjgl.opengl.GL11;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityDisenchanter;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityDisenchanter.Fields;
import com.lothrazar.cyclicmagic.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.gui.GuiBaseContanerProgress;
import com.lothrazar.cyclicmagic.gui.GuiButtonMachineRedstone;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiDisenchanter extends GuiBaseContanerProgress {
  public static final int WIDTH = 176;
  public static final int HEIGHT = 212;
  public static final ResourceLocation GUI = new ResourceLocation(Const.MODID, "textures/gui/pattern.png");
  private TileEntityDisenchanter tile;
  private GuiButtonMachineRedstone redstoneBtn;
  public GuiDisenchanter(InventoryPlayer inventoryPlayer, TileEntityDisenchanter tileEntity) {
    super(new ContainerDisenchanter(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
    this.xSize = WIDTH;
    this.ySize = HEIGHT;
  }
  @Override
  public void initGui() {
    super.initGui();
    redstoneBtn = new GuiButtonMachineRedstone(0,
        this.guiLeft + 8,
        this.guiTop + 8, this.tile.getPos());
    redstoneBtn.setTextureIndex(tile.getField(Fields.REDSTONE.ordinal()));
    this.buttonList.add(redstoneBtn);
    //    actionBtn = new ButtonUserAction(1,
    //        this.guiLeft + 8 + 50,
    //        this.guiTop + 8 + 8, this.tile.getPos());
    //    this.buttonList.add(actionBtn);
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
     super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
     int u = 0, v = 0;
//    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//    this.mc.getTextureManager().bindTexture(getBackground());
//    int thisX = this.getMiddleX();
//    int thisY = this.getMiddleY();
//    Gui.drawModalRectWithCustomSizedTexture(thisX, thisY, u, v, WIDTH, HEIGHT,WIDTH, HEIGHT);
    
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    int x = 0, y = 0, ystart = 26, spacing = 36;
    for (int i = 0; i < tile.getSizeInventory(); i++) {
      switch (i) {
        case TileEntityDisenchanter.SLOT_BOOK://center center
          x = GuiDisenchanter.WIDTH / 2;
          y = ystart + spacing;
        break;
        case TileEntityDisenchanter.SLOT_GLOWSTONE://left mid
          x = GuiDisenchanter.WIDTH / 4;
          y = ystart + spacing;
        break;
        case TileEntityDisenchanter.SLOT_BOTTLE://bottom center
          x = GuiDisenchanter.WIDTH / 2;
          y = ystart + 2 * spacing;
        break;
        case TileEntityDisenchanter.SLOT_REDSTONE:// right mid
          x = GuiDisenchanter.WIDTH - GuiDisenchanter.WIDTH / 4;
          y = ystart + spacing;
        break;
        case TileEntityDisenchanter.SLOT_INPUT://top center
          x = GuiDisenchanter.WIDTH / 2;
          y = ystart;
        break;
      }
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft - 1 + x, this.guiTop - 1 + y, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
  }
  public ResourceLocation getBackground() {
    return GUI;//can override
  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    redstoneBtn.setState(tile.getField(Fields.REDSTONE.ordinal()));
    //    actionBtn.displayString = UtilChat.lang("tile.block_user.action" + tile.getField(Fields.LEFTRIGHT.ordinal()));
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
  }
  public int getProgressX() {
    return this.guiLeft + Const.padding + 2;
  }
  public int getProgressY() {
    return this.guiTop + 6 * Const.SQ + 10 ;
  }
  public int getProgressCurrent() {
    return tile.getField(Fields.TIMER.ordinal());
  }
  public int getProgressMax() {
    return TileEntityDisenchanter.TIMER_FULL;
  }
}
