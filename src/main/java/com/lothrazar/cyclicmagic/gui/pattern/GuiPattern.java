package com.lothrazar.cyclicmagic.gui.pattern;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityPatternBuilder;
import com.lothrazar.cyclicmagic.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiPattern extends GuiBaseContainer {
  public static final ResourceLocation SLOTFISH = new ResourceLocation(Const.MODID, "textures/gui/inventory_slot_fish.png");
  private TileEntityPatternBuilder tile;
  private int xTextbox;
  private int[] yRows = new int[4];
  public GuiPattern(InventoryPlayer inventoryPlayer, TileEntityPatternBuilder tileEntity) {
    super(new ContainerPattern(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
  }
  public String getTitle() {
    return "tile.builder_pattern.name";
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
        this.guiTop + yRows[1], true, TileEntityPatternBuilder.Fields.OFFX);
    this.buttonList.add(btn);
    btn = new ButtonPattern(tile.getPos(), id++,
        this.guiLeft + xTextbox - xOffset-4,
        this.guiTop + yRows[1], false, TileEntityPatternBuilder.Fields.OFFX);
    this.buttonList.add(btn);
    yRows[2] = yRows[1] + yOffset;
    btn = new ButtonPattern(tile.getPos(), id++,
        this.guiLeft + xTextbox + xOffset,
        this.guiTop + yRows[2], true, TileEntityPatternBuilder.Fields.OFFY);
    this.buttonList.add(btn);
    btn = new ButtonPattern(tile.getPos(), id++,
        this.guiLeft + xTextbox - xOffset-4,
        this.guiTop + yRows[2], false, TileEntityPatternBuilder.Fields.OFFY);
    this.buttonList.add(btn);
    yRows[3] = yRows[2] + yOffset;
    btn = new ButtonPattern(tile.getPos(), id++,
        this.guiLeft + xTextbox + xOffset,
        this.guiTop + yRows[3], true, TileEntityPatternBuilder.Fields.OFFZ);
    this.buttonList.add(btn);
    btn = new ButtonPattern(tile.getPos(), id++,
        this.guiLeft + xTextbox - xOffset-4,
        this.guiTop + yRows[3], false, TileEntityPatternBuilder.Fields.OFFZ);
    this.buttonList.add(btn);
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
    display = "" + this.tile.getField(TileEntityPatternBuilder.Fields.OFFX.ordinal());
    //move it over if more than 1 digit
    x = (display.length() > 1) ? xTextbox - 3 : xTextbox;
    y = yRows[1];
    this.fontRendererObj.drawString(display, x, y, 4210752);
    display = "" + this.tile.getField(TileEntityPatternBuilder.Fields.OFFY.ordinal());
    //move it over if more than 1 digit
    x = (display.length() > 1) ? xTextbox - 3 : xTextbox;
    y = yRows[2];
    this.fontRendererObj.drawString(display, x, y, 4210752);
    display = "" + this.tile.getField(TileEntityPatternBuilder.Fields.OFFZ.ordinal());
    //move it over if more than 1 digit
    x = (display.length() > 1) ? xTextbox - 3 : xTextbox;
    y = yRows[3];
    this.fontRendererObj.drawString(display, x, y, 4210752);
 
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    int row = 0, col = 0;
    for (int i = 0; i < tile.getSizeInventory(); i++) {
      row = i / 3;// /3 will go 000, 111, 222
      col = i % 3; // and %3 will go 012 012 012
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerPattern.SLOTX_FISH - 1 + row * Const.SQ, this.guiTop + ContainerPattern.SLOTY_FISH - 1 + col * Const.SQ, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
  }
}
