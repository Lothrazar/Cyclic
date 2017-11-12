package com.lothrazar.cyclicmagic.component.autouser;
import com.lothrazar.cyclicmagic.component.autouser.TileEntityUser.Fields;
import com.lothrazar.cyclicmagic.component.builder.TileEntityStructureBuilder;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.data.Const.ScreenSize;
import com.lothrazar.cyclicmagic.gui.ProgressBar;
import com.lothrazar.cyclicmagic.gui.base.ContainerBaseMachine;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer.ButtonTriggerWrapper.ButtonTriggerType;
import com.lothrazar.cyclicmagic.gui.button.ButtonTileEntityField;
import com.lothrazar.cyclicmagic.gui.button.GuiButtonToggleSize;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiUser extends GuiBaseContainer {
  private ButtonTileEntityField actionBtn;
  private GuiButtonToggleSize btnSize;
  private ButtonTileEntityField yOffsetBtn;
  private ButtonTileEntityField btnSpeed;
  public GuiUser(InventoryPlayer inventoryPlayer, TileEntityUser tileEntity) {
    super(new ContainerUser(inventoryPlayer, tileEntity), tileEntity);
    setScreenSize(ScreenSize.LARGE);
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
    this.addButton(btnSize);
    actionBtn = new ButtonTileEntityField(btnId++,
        this.guiLeft + 24 + Const.PAD,
        this.guiTop + Const.PAD * 6, this.tile.getPos(), Fields.LEFTRIGHT.ordinal());
    actionBtn.width = 44;
    actionBtn.setTooltip("tile.block_user.action");
    this.addButton(actionBtn);
    yOffsetBtn = new ButtonTileEntityField(btnId++,
        this.guiLeft + Const.PAD / 2,
        this.guiTop + Const.PAD * 6, this.tile.getPos(), Fields.Y_OFFSET.ordinal());
    yOffsetBtn.width = Const.SQ;
    yOffsetBtn.setTooltip("tile.block_user.yoffset");
    this.addButton(yOffsetBtn);
    btnSpeed = new ButtonTileEntityField(btnId++,
        this.guiLeft + 88,
        this.guiTop + Const.PAD * 8, this.tile.getPos(), Fields.SPEED.ordinal());
    btnSpeed.width = btnSpeed.height = 14;
    btnSpeed.displayString = "+";
    btnSpeed.setTooltip("tile.block_user.speed.tooltip");
    this.registerButtonDisableTrigger(btnSpeed, ButtonTriggerType.EQUAL, Fields.SPEED.ordinal(), TileEntityUser.MAX_SPEED);
    
    
    this.addButton(btnSpeed);
    ButtonTileEntityField btnSpeedD = new ButtonTileEntityField(btnId++,
        this.guiLeft + 88,
        btnSpeed.y + 28, this.tile.getPos(), Fields.SPEED.ordinal(), -1);
    btnSpeedD.width = btnSpeedD.height = btnSpeed.width;
    btnSpeedD.displayString = "-";
    btnSpeedD.setTooltip("tile.block_user.speed.tooltip");
    this.addButton(btnSpeedD);
    this.registerButtonDisableTrigger(btnSpeedD, ButtonTriggerType.EQUAL, Fields.SPEED.ordinal(), 1);
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    for (int k = 0; k < 3; k++) {
      Gui.drawModalRectWithCustomSizedTexture(
          this.guiLeft + ContainerUser.SLOTX_START - 1 + k * Const.SQ,
          this.guiTop + ContainerUser.SLOTY - 1, u, v,
          Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
    for (int k = 3; k < 6; k++) {
      Gui.drawModalRectWithCustomSizedTexture(
          this.guiLeft + ContainerUser.SLOTX_START - 1 + (k + 3) * Const.SQ,
          this.guiTop + ContainerUser.SLOTY - 1, u, v,
          Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
    for (int k = 6; k < 9; k++) {
      Gui.drawModalRectWithCustomSizedTexture(
          this.guiLeft + ContainerUser.SLOTX_START - 1 + k * Const.SQ,
          this.guiTop + ContainerUser.SLOTY - 1 - Const.SQ, u, v,
          Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
    super.tryDrawFuelSlot(ContainerBaseMachine.SLOTX_FUEL - 1, +ContainerBaseMachine.SLOTY_FUEL - 1);
  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    this.drawString("tile.block_user.input", 12, 82);
    this.drawString("tile.block_user.output", 122, 64);
    actionBtn.displayString = UtilChat.lang("tile.block_user.action" + tile.getField(Fields.LEFTRIGHT.ordinal()));
    btnSize.displayString = UtilChat.lang("button.harvester.size" + tile.getField(Fields.SIZE.ordinal()));
    yOffsetBtn.displayString = tile.getField(Fields.Y_OFFSET.ordinal()) + "";
    this.drawFieldAt(btnSpeed.x - this.guiLeft + 5, btnSpeed.y - this.guiTop + 18, Fields.SPEED.ordinal());
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
  }
}
