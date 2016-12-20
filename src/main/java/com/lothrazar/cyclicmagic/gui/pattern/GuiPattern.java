package com.lothrazar.cyclicmagic.gui.pattern;
import org.lwjgl.opengl.GL11;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityPatternBuilder;
import com.lothrazar.cyclicmagic.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiPattern extends GuiBaseContainer {
  public static final ResourceLocation GUI = new ResourceLocation(Const.MODID, Const.Res.folder + "pattern.png");
  static final int texture_width = 176;
  static final int texture_height = 212;
  static final int GUI_ROWS = 5;
  private TileEntityPatternBuilder tile;
  private int xTextbox;
  private int[] yRows = new int[4];
  public GuiPattern(InventoryPlayer inventoryPlayer, TileEntityPatternBuilder tileEntity) {
    super(new ContainerPattern(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
    this.xSize = texture_width;
    this.ySize = texture_height;
  }
  public String getTitle() {
    return "tile.builder_pattern.name";
  }
  @Override
  public ResourceLocation getBackground() {
    return GUI;
  }
  @Override
  public void initGui() {
    super.initGui();
    xTextbox = 176 - 144;
    int id = 2;
    int xOffset = 18;
    int yOffset = 12;
    yRows[0] = 30;
    ButtonPattern btn = new ButtonPattern(tile.getPos(), id++,
        this.guiLeft + xTextbox + xOffset,
        this.guiTop + yRows[0], true, TileEntityPatternBuilder.Fields.SIZER);
    this.buttonList.add(btn);
    btn = new ButtonPattern(tile.getPos(), id++,
        this.guiLeft + xTextbox - xOffset-4,
        this.guiTop + yRows[0], false, TileEntityPatternBuilder.Fields.SIZER);
    this.buttonList.add(btn);
    yRows[1] = yRows[0] + yOffset;
    btn = new ButtonPattern(tile.getPos(), id++,
        this.guiLeft + xTextbox + xOffset,
        this.guiTop + yRows[1], true, TileEntityPatternBuilder.Fields.OFFTARGX);
    this.buttonList.add(btn);
    btn = new ButtonPattern(tile.getPos(), id++,
        this.guiLeft + xTextbox - xOffset-4,
        this.guiTop + yRows[1], false, TileEntityPatternBuilder.Fields.OFFTARGX);
    this.buttonList.add(btn);
    yRows[2] = yRows[1] + yOffset;
    btn = new ButtonPattern(tile.getPos(), id++,
        this.guiLeft + xTextbox + xOffset,
        this.guiTop + yRows[2], true, TileEntityPatternBuilder.Fields.OFFTARGY);
    this.buttonList.add(btn);
    btn = new ButtonPattern(tile.getPos(), id++,
        this.guiLeft + xTextbox - xOffset-4,
        this.guiTop + yRows[2], false, TileEntityPatternBuilder.Fields.OFFTARGY);
    this.buttonList.add(btn);
    yRows[3] = yRows[2] + yOffset;
    btn = new ButtonPattern(tile.getPos(), id++,
        this.guiLeft + xTextbox + xOffset,
        this.guiTop + yRows[3], true, TileEntityPatternBuilder.Fields.OFFTARGZ);
    this.buttonList.add(btn);
    btn = new ButtonPattern(tile.getPos(), id++,
        this.guiLeft + xTextbox - xOffset-4,
        this.guiTop + yRows[3], false, TileEntityPatternBuilder.Fields.OFFTARGZ);
    this.buttonList.add(btn);
    //TODO: buttons for SRC versions
  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    String display = "" + this.tile.getField(TileEntityPatternBuilder.Fields.SIZER.ordinal());
    //move it over if more than 1 digit
    int x = (display.length() > 1) ? xTextbox - 3 : xTextbox;
    int y = yRows[0];
    this.fontRendererObj.drawString(display, x, y, 4210752);
    display = "" + this.tile.getField(TileEntityPatternBuilder.Fields.OFFTARGX.ordinal());
    //move it over if more than 1 digit
    x = (display.length() > 1) ? xTextbox - 3 : xTextbox;
    y = yRows[1];
    this.fontRendererObj.drawString(display, x, y, 4210752);
    display = "" + this.tile.getField(TileEntityPatternBuilder.Fields.OFFTARGY.ordinal());
    //move it over if more than 1 digit
    x = (display.length() > 1) ? xTextbox - 3 : xTextbox;
    y = yRows[2];
    this.fontRendererObj.drawString(display, x, y, 4210752);
    display = "" + this.tile.getField(TileEntityPatternBuilder.Fields.OFFTARGZ.ordinal());
    //move it over if more than 1 digit
    x = (display.length() > 1) ? xTextbox - 3 : xTextbox;
    y = yRows[3];
    this.fontRendererObj.drawString(display, x, y, 4210752);
    
    int xOtherbox = xTextbox + 36;
    
     x = (display.length() > 1) ? xOtherbox - 3 : xOtherbox;
     y = yRows[0];
    display = "" + this.tile.getField(TileEntityPatternBuilder.Fields.OFFSRCX.ordinal());
    //move it over if more than 1 digit
    x = (display.length() > 1) ? xOtherbox - 3 : xOtherbox;
    y = yRows[1];
    this.fontRendererObj.drawString(display, x, y, 4210752);
    display = "" + this.tile.getField(TileEntityPatternBuilder.Fields.OFFSRCY.ordinal());
    //move it over if more than 1 digit
    x = (display.length() > 1) ? xOtherbox - 3 : xOtherbox;
    y = yRows[2];
    this.fontRendererObj.drawString(display, x, y, 4210752);
    display = "" + this.tile.getField(TileEntityPatternBuilder.Fields.OFFSRCZ.ordinal());
    //move it over if more than 1 digit
    x = (display.length() > 1) ? xOtherbox - 3 : xOtherbox;
    y = yRows[3];
    this.fontRendererObj.drawString(display, x, y, 4210752);

  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(getBackground());
    int thisX = (this.width - this.xSize) / 2;
    int thisY = (this.height - this.ySize) / 2;
    int u = 0, v = 0;
    Gui.drawModalRectWithCustomSizedTexture(thisX, thisY, u, v, texture_width, texture_height, texture_width, texture_height);

    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    int row = 0, col = 0;
    for (int i = 0; i < tile.getSizeInventory(); i++) {
      row = i / GUI_ROWS;// /3 will go 000, 111, 222
      col = i % GUI_ROWS; // and %3 will go 012 012 012
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerPattern.SLOTX_START - 1 + row * Const.SQ, this.guiTop + ContainerPattern.SLOTY_START - 1 + col * Const.SQ, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
  }
}
