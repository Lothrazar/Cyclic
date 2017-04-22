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
  public static final ResourceLocation SLOT_BOTTLE = new ResourceLocation(Const.MODID, "textures/gui/inventory_slot_bottle.png");
  public static final ResourceLocation SLOT_EBOTTLE = new ResourceLocation(Const.MODID, "textures/gui/inventory_slot_ebottle.png");
  private TileEntityXpPylon tile;
  boolean debugLabels = false;
  private GuiButton btnCollect;
  private GuiButton btnSpray;
  private GuiButton btnBottle;
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
    int w = 58, h = 20;
    int x = this.guiLeft + Const.padding;
    int y = this.guiTop + Const.padding * 2;
    btnCollect = new GuiButton(btnId++,
        x, y, w, h, "");
    this.buttonList.add(btnCollect);
    y += h;
    btnSpray = new GuiButton(btnId++,
        x, y, w, h, "");
    this.buttonList.add(btnSpray);
    y += h;
    btnBottle = new GuiButton(btnId++,
        x, y, w, h, "");
    this.buttonList.add(btnBottle);
  }
  @Override
  protected void actionPerformed(GuiButton button) {
    if (button.id == btnCollect.id) {
      ModCyclic.network.sendToServer(new PacketTilePylon(tile.getPos(), 1, TileEntityXpPylon.Fields.COLLECT));
    }
    else if (button.id == btnSpray.id) {
      ModCyclic.network.sendToServer(new PacketTilePylon(tile.getPos(), 1, TileEntityXpPylon.Fields.SPRAY));
    }
    else if (button.id == btnBottle.id) {
      ModCyclic.network.sendToServer(new PacketTilePylon(tile.getPos(), 1, TileEntityXpPylon.Fields.BOTTLE));
    }
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(SLOT_BOTTLE);
    for (int k = 0; k < this.tile.getSizeInventory(); k++) {
      if (k == 0)
        this.mc.getTextureManager().bindTexture(SLOT_BOTTLE);
      else
        this.mc.getTextureManager().bindTexture(SLOT_EBOTTLE);
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerPylon.SLOTX - 1, this.guiTop + ContainerPylon.SLOTY - 1 + k * (8+Const.SQ), u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
  }
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

    btnCollect.displayString = UtilChat.lang("button.pylon.collect" + tile.getField(TileEntityXpPylon.Fields.COLLECT.ordinal()));
    btnSpray.displayString = UtilChat.lang("button.pylon.spray" + tile.getField(TileEntityXpPylon.Fields.SPRAY.ordinal()));
    btnBottle.displayString = UtilChat.lang("button.pylon.bottle" + tile.getField(TileEntityXpPylon.Fields.BOTTLE.ordinal())); 
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
