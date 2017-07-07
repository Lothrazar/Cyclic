package com.lothrazar.cyclicmagic.component.pylonexp;
import java.util.Arrays;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.gui.ProgressBar;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiPylon extends GuiBaseContainer {
  public static final ResourceLocation PROGEXP = new ResourceLocation(Const.MODID, "textures/gui/progress_exp.png");
  public static final ResourceLocation SLOT_BOTTLE = new ResourceLocation(Const.MODID, "textures/gui/inventory_slot_bottle.png");
  public static final ResourceLocation SLOT_EBOTTLE = new ResourceLocation(Const.MODID, "textures/gui/inventory_slot_ebottle.png");
  private static final int DEPOSIT_AMT = 50;
  private TileEntityXpPylon tile;
  boolean debugLabels = false;
  private GuiButton btnCollect;
  private GuiButton btnSpray;
  private GuiButton btnBottle;
  private GuiButton btnDeposit;
  private GuiButton btnDepositAll;
  public GuiPylon(InventoryPlayer inventoryPlayer, TileEntityXpPylon tileEntity) {
    super(new ContainerPylon(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
    this.progressBar = new ProgressBar(this, 10, 17 + 3 * Const.SQ, TileEntityXpPylon.Fields.EXP.ordinal(), TileEntityXpPylon.MAX_EXP_HELD);
    this.progressBar.asset = PROGEXP;
  }
  @Override
  public void initGui() {
    super.initGui();
    int btnId = 0;
    int w = 58, h = 20;
    int x = this.guiLeft + Const.PAD;
    int y = this.guiTop + Const.PAD * 2;
    btnCollect = new GuiButton(btnId++,
        x, y, w, h, "");
    this.buttonList.add(btnCollect);
    y += h + Const.PAD / 2;
    btnSpray = new GuiButton(btnId++,
        x, y, w, h, "");
    this.buttonList.add(btnSpray);
    x += w + Const.PAD;
    y = this.guiTop + Const.PAD * 2;
    btnBottle = new GuiButton(btnId++,
        x, y, w, h, "");
    this.buttonList.add(btnBottle);
    y += h + Const.PAD / 2;
    btnDeposit = new GuiButton(btnId++,
        x, y, w / 2, h, UtilChat.lang("button.exp_pylon.deposit"));
    this.buttonList.add(btnDeposit);
    btnDepositAll = new GuiButton(btnId++,
        x + w / 2 + 1, y, w / 2, h, UtilChat.lang("button.exp_pylon.depositall"));
    this.buttonList.add(btnDepositAll);
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
      //fake: exp really means deposit
      ModCyclic.network.sendToServer(new PacketTilePylon(tile.getPos(), DEPOSIT_AMT, TileEntityXpPylon.Fields.EXP));
    }
    else if (button.id == btnDepositAll.id) {
      //fake: exp really means deposit
      ModCyclic.network.sendToServer(new PacketTilePylon(tile.getPos(), -1, TileEntityXpPylon.Fields.EXP));
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
          tt = "button.exp_pylon.collect.tooltip" + TileEntityXpPylon.RADIUS;
        }
        else if (button.id == btnSpray.id) {
          tt = "button.exp_pylon.spray.tooltip";
        }
        else if (button.id == btnBottle.id) {
          tt = "button.exp_pylon.bottle.tooltip";
        }
        else if (button.id == btnDeposit.id) {
          tt = "button.exp_pylon.deposit.tooltip";
        }
        else if (button.id == btnDepositAll.id) {
          tt = "button.exp_pylon.depositall.tooltip";
        }
        this.drawHoveringText(Arrays.asList(UtilChat.lang(tt)), mouseX, mouseY, fontRenderer);
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
    btnCollect.displayString = UtilChat.lang("button.exp_pylon.collect" + tile.getField(TileEntityXpPylon.Fields.COLLECT.ordinal()));
    btnSpray.displayString = UtilChat.lang("button.exp_pylon.spray" + tile.getField(TileEntityXpPylon.Fields.SPRAY.ordinal()));
    btnBottle.displayString = UtilChat.lang("button.exp_pylon.bottle" + tile.getField(TileEntityXpPylon.Fields.BOTTLE.ordinal()));
    this.drawString(this.tile.getField(TileEntityXpPylon.Fields.EXP.ordinal()) + " / " + TileEntityXpPylon.MAX_EXP_HELD, this.xSize / 3, 62);
  }
}
