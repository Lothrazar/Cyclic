package com.lothrazar.cyclicmagic.component.beaconpotion;
import com.lothrazar.cyclicmagic.component.beaconpotion.TileEntityBeaconPotion.Fields;
import com.lothrazar.cyclicmagic.component.crafter.ContainerCrafter;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.data.Const.ScreenSize;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import com.lothrazar.cyclicmagic.gui.button.ButtonIncrementField;
import com.lothrazar.cyclicmagic.gui.button.GuiButtonToggleSize;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiBeaconPotion extends GuiBaseContainer {
  private GuiButtonToggleSize btnSize;
  private ButtonIncrementField btnEntityType;
  public GuiBeaconPotion(InventoryPlayer inventoryPlayer, TileEntityBeaconPotion tileEntity) {
    super(new ContainerBeaconPotion(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
    this.fieldRedstoneBtn = TileEntityBeaconPotion.Fields.REDSTONE.ordinal();
    this.setScreenSize(ScreenSize.LARGE);
  }
  @Override
  public void initGui() {
    super.initGui();
    int id = 1;
    int x = Const.PAD / 2, y = 106, w = 168, h = 20;
    TileEntityBeaconPotion.Fields f = TileEntityBeaconPotion.Fields.ENTITYTYPE;
    btnEntityType = new ButtonIncrementField(id,
        this.guiLeft + x,
        this.guiTop + y,
        tile.getPos(),
        f.ordinal(), 1, w, h);
    btnEntityType.setTooltip("tile.beacon_potion.entity.tooltip");
    this.addButton(btnEntityType);
    x = this.guiLeft + Const.PAD + 22;
    y = this.guiTop + Const.PAD * 3 + 2;
    id++;
//    btnSize = new GuiButtonToggleSize(id,
//        x, y, this.tile.getPos());
//    this.buttonList.add(btnSize);
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT_BOTTLE);
    for (int k = 0; k < 9; k++) {
      Gui.drawModalRectWithCustomSizedTexture(
          this.guiLeft + ContainerBeaconPotion.SLOTX_START - 1 + k * Const.SQ,
          this.guiTop + ContainerBeaconPotion.SLOTY - 1,
          u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
    super.tryDrawFuelSlot(ContainerCrafter.SLOTX_FUEL - 1, ContainerCrafter.SLOTY_FUEL - 1);
  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    TileEntityBeaconPotion.EntityType t = ((TileEntityBeaconPotion) this.tile).getEntityType();
    this.btnEntityType.displayString = UtilChat.lang("tile.beacon_potion." + t.name().toLowerCase());
    this.drawFieldAt(10, 10, Fields.FUEL.ordinal());
    this.drawFieldAt(50, 10, Fields.FUELMAX.ordinal());
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
  }
}
