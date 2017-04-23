package com.lothrazar.cyclicmagic.gui.pylon;
import java.util.Arrays;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityXpPylon;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityXpPylon.Fields;
import com.lothrazar.cyclicmagic.gui.GuiBaseContanerProgress;
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
  private static final int DEPOSIT_AMT = 10;
  private TileEntityXpPylon tile;
  boolean debugLabels = false;
  private GuiButton btnCollect;
  private GuiButton btnSpray;
  private GuiButton btnBottle;
  private GuiButton btnDeposit;
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
    y += h + Const.padding / 2;
    btnSpray = new GuiButton(btnId++,
        x, y, w, h, "");
    this.buttonList.add(btnSpray);
    x += w + Const.padding;
    y = this.guiTop + Const.padding * 2;
    btnBottle = new GuiButton(btnId++,
        x, y, w, h, "");
    this.buttonList.add(btnBottle);
    y += h + Const.padding / 2;
    btnDeposit = new GuiButton(btnId++,
        x, y, w, h, UtilChat.lang("button.pylon.deposit") + DEPOSIT_AMT);
    this.buttonList.add(btnDeposit);
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
    else if (button.id == btnDeposit.id) {
      //fake: collect really means deposit
      ModCyclic.network.sendToServer(new PacketTilePylon(tile.getPos(), DEPOSIT_AMT, TileEntityXpPylon.Fields.EXP));
    }
  }
  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    super.drawScreen(mouseX, mouseY, partialTicks);
    GuiButton button;
    String tt = "";
    for (int i = 0; i < buttonList.size(); i++) {
      if (buttonList.get(i).isMouseOver()) {
        button = buttonList.get(i);
        if (button.id == btnCollect.id) {
          tt = "button.pylon.collect.tooltip" + TileEntityXpPylon.RADIUS;
        }
        else if (button.id == btnSpray.id) {
          tt = "button.pylon.spray.tooltip";
        }
        else if (button.id == btnBottle.id) {
          tt = "button.pylon.bottle.tooltip";
        }
        else if (button.id == btnDeposit.id) {
          tt = "button.pylon.deposit.tooltip";
        }
        this.drawHoveringText(Arrays.asList(UtilChat.lang(tt)), mouseX, mouseY, fontRendererObj);
        break;// cant hover on 2 at once
      }
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
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerPylon.SLOTX - 1, this.guiTop + ContainerPylon.SLOTY - 1 + k * (8 + Const.SQ), u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
  }
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    btnCollect.displayString = UtilChat.lang("button.pylon.collect" + tile.getField(TileEntityXpPylon.Fields.COLLECT.ordinal()));
    btnSpray.displayString = UtilChat.lang("button.pylon.spray" + tile.getField(TileEntityXpPylon.Fields.SPRAY.ordinal()));
    btnBottle.displayString = UtilChat.lang("button.pylon.bottle" + tile.getField(TileEntityXpPylon.Fields.BOTTLE.ordinal()));
    this.drawString(this.tile.getField(Fields.EXP.ordinal()) + " / " + TileEntityXpPylon.MAX_EXP_HELD, this.xSize / 3, 62);
  }
  public int getProgressX() {
    return this.guiLeft + 10;
  }
  public int getProgressY() {
    return this.guiTop + 9 + 3 * Const.SQ + 8;
  }
  public int getProgressCurrent() {
    return tile.getField(TileEntityXpPylon.Fields.EXP.ordinal());
  }
  public int getProgressMax() {
    return TileEntityXpPylon.MAX_EXP_HELD;
  }
}
