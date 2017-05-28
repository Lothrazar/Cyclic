package com.lothrazar.cyclicmagic.component.autouser;
import com.lothrazar.cyclicmagic.component.autouser.TileEntityUser.Fields;
import com.lothrazar.cyclicmagic.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.gui.GuiButtonToggleSize;
import com.lothrazar.cyclicmagic.gui.ProgressBar;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.Const.ScreenSize;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiUser extends GuiBaseContainer {
private TileEntityUser tile;
  private ButtonUserAction actionBtn;
  private GuiButtonToggleSize btnSize;
  public GuiUser(InventoryPlayer inventoryPlayer, TileEntityUser tileEntity) {
    super(new ContainerUser(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
    this.screenSize = ScreenSize.LARGE;
    this.xSize = screenSize.width();
    this.ySize = screenSize.height();
    this.fieldRedstoneBtn = Fields.REDSTONE.ordinal();
    this.fieldPreviewBtn = Fields.RENDERPARTICLES.ordinal();
    this.progressBar = new ProgressBar(this, 10, ContainerUser.SLOTY + 22, Fields.TIMER.ordinal(), TileEntityUser.TIMER_FULL);
    this.setFieldFuel(Fields.FUEL.ordinal());
  }
  @Override
  public void initGui() {
    super.initGui();
    int btnId = 3;
    btnSize = new GuiButtonToggleSize(btnId++,
        this.guiLeft + 24 + Const.PAD,
        this.guiTop + Const.PAD + 18, this.tile.getPos());
    this.buttonList.add(btnSize);
    actionBtn = new ButtonUserAction(btnId++,
        this.guiLeft + 24 + Const.PAD,
        this.guiTop + Const.PAD * 6, this.tile.getPos());
    actionBtn.width = 44;
    this.buttonList.add(actionBtn);
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    for (int k = 0; k < tile.getSizeInventory() - 1; k++) {
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerUser.SLOTX_START - 1 + k * Const.SQ, this.guiTop + ContainerUser.SLOTY - 1, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT_COAL);
    Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerUser.SLOTX_FUEL - 1, this.guiTop + ContainerUser.SLOTY_FUEL - 1, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    actionBtn.displayString = UtilChat.lang("tile.block_user.action" + tile.getField(Fields.LEFTRIGHT.ordinal()));
    btnSize.displayString = UtilChat.lang("button.harvester.size" + tile.getField(Fields.SIZE.ordinal()));
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
  }
}
