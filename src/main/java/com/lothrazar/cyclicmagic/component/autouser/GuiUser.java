package com.lothrazar.cyclicmagic.component.autouser;
import com.lothrazar.cyclicmagic.component.autouser.TileEntityUser.Fields;
import com.lothrazar.cyclicmagic.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.gui.GuiButtonTogglePreview;
import com.lothrazar.cyclicmagic.gui.GuiButtonToggleSize;
import com.lothrazar.cyclicmagic.gui.ProgressBar;
import com.lothrazar.cyclicmagic.net.PacketTileSizeToggle;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.Const.ScreenSize;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiUser extends GuiBaseContainer {
  private TileEntityUser tile;
  private ButtonUserAction actionBtn;
  private GuiButtonToggleSize btnSize;
  public GuiUser(InventoryPlayer inventoryPlayer, TileEntityUser tileEntity) {
    super(new ContainerUser(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
    screenSize = ScreenSize.STANDARD;
    this.xSize = screenSize.width();
    this.ySize = screenSize.height();
    this.fieldRedstoneBtn = Fields.REDSTONE.ordinal();
    this.progressBar = new ProgressBar(this, 10, 14 + 3 * Const.SQ, Fields.TIMER.ordinal(), TileEntityUser.TIMER_FULL);
    this.fieldPreviewBtn = Fields.RENDERPARTICLES.ordinal();
  }
  @Override
  public void initGui() {
    super.initGui();
    int btnId = 3;
    int x = this.guiLeft + Const.PAD + 20;
    int y = this.guiTop + Const.PAD * 2;
    actionBtn = new ButtonUserAction(btnId++,
        x,
        y, this.tile.getPos());
    this.buttonList.add(actionBtn);
    x += actionBtn.width + Const.PAD / 2;
    //    int y = this.guiTop + Const.PAD * 2 + 20;
    btnSize = new GuiButtonToggleSize(btnId++,
        x,
        y, this.tile.getPos());
    this.buttonList.add(btnSize);
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    for (int k = 0; k < tile.getSizeInventory(); k++) {
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerUser.SLOTX_START - 1 + k * Const.SQ, this.guiTop + ContainerUser.SLOTY - 1, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    actionBtn.displayString = UtilChat.lang("tile.block_user.action" + tile.getField(Fields.LEFTRIGHT.ordinal()));
    btnSize.displayString = UtilChat.lang("button.harvester.size" + tile.getField(Fields.SIZE.ordinal()));
    //    this.drawFieldAt(1,1,Fields.FUEL.ordinal());
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
  }
}
