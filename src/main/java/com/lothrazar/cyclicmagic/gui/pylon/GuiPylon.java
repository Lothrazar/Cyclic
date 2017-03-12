package com.lothrazar.cyclicmagic.gui.pylon;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityXpPylon;
import com.lothrazar.cyclicmagic.gui.GuiBaseContanerProgress;
import com.lothrazar.cyclicmagic.gui.GuiButtonMachineRedstone;
import com.lothrazar.cyclicmagic.net.PacketTilePylon;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiPylon extends GuiBaseContanerProgress {
  public static final ResourceLocation PROGEXP = new ResourceLocation(Const.MODID, "textures/gui/progress_exp.png");
  public static final ResourceLocation SLOT = new ResourceLocation(Const.MODID, "textures/gui/inventory_slot_bottle.png");
  private TileEntityXpPylon tile;
  boolean debugLabels = false;
  private GuiButtonMachineRedstone redstoneBtn;
  private GuiButton btnPreview;
  public GuiPylon(InventoryPlayer inventoryPlayer, TileEntityXpPylon tileEntity) {
    super(new ContainerPylon(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
  }
  @Override
  public ResourceLocation getProgressAsset() {
    return PROGEXP;
  }
  @Override
  public void initGui() {
    super.initGui();
    int btnId = 0;
    redstoneBtn = new GuiButtonMachineRedstone(btnId++,
        this.guiLeft + Const.padding,
        this.guiTop + Const.padding, this.tile.getPos());
    redstoneBtn.setTextureIndex(tile.getField(TileEntityXpPylon.Fields.REDSTONE.ordinal()));
    this.buttonList.add(redstoneBtn);
    int y = this.guiTop + Const.padding * 2 + 20;
    btnPreview = new GuiButton(btnId++,
        this.guiLeft + Const.padding,
        y, 40, 20, "");
    this.buttonList.add(btnPreview);
  }
  @Override
  protected void actionPerformed(GuiButton button) {
    if (button.id == btnPreview.id) {
      ModCyclic.network.sendToServer(new PacketTilePylon(tile.getPos(), 1, TileEntityXpPylon.Fields.MODE));
    }
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(SLOT);
    for (int k = 0; k < this.tile.getSizeInventory(); k++) { // x had - 3 ??
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerPylon.SLOTX_START - 1 + k * 2 * Const.SQ, this.guiTop + ContainerPylon.SLOTY - 1, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
  }
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    redstoneBtn.setState(tile.getField(TileEntityXpPylon.Fields.REDSTONE.ordinal()));
    btnPreview.displayString = UtilChat.lang("button.pylon.mode" + tile.getField(TileEntityXpPylon.Fields.MODE.ordinal()));
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
  }
  public int getProgressX() {
    return this.guiLeft + 10;
  }
  public int getProgressY() {
    return this.guiTop + 9 + 3 * Const.SQ + 5;
  }
  public int getProgressCurrent() {
    return tile.getField(TileEntityXpPylon.Fields.EXP.ordinal());
  }
  public int getProgressMax() {
    return TileEntityXpPylon.MAX_EXP_HELD;
  }
}
