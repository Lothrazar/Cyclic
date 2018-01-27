package com.lothrazar.cyclicmagic.component.anvil;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.gui.base.ContainerBaseMachine;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiAnvilAuto extends GuiBaseContainer {
  public GuiAnvilAuto(InventoryPlayer inventoryPlayer, TileEntityAnvilAuto tileEntity) {
    super(new ContainerAnvilAuto(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
    this.fieldRedstoneBtn = TileEntityAnvilAuto.Fields.REDSTONE.ordinal();
    this.setFieldFuel(TileEntityAnvilAuto.Fields.FUEL.ordinal());
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    Gui.drawModalRectWithCustomSizedTexture(
        this.guiLeft + 50 - 1,
        this.guiTop + ContainerAnvilAuto.SLOTY - 1,
        u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    Gui.drawModalRectWithCustomSizedTexture(
        this.guiLeft + 110 - 1,
        this.guiTop + ContainerAnvilAuto.SLOTY - 1,
        u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    super.tryDrawFuelSlot(ContainerBaseMachine.SLOTX_FUEL - 1, +ContainerBaseMachine.SLOTY_FUEL - 1);
    this.drawFluidBar();
  }
  private void drawFluidBar() {
    int u = 0, v = 0;
    int currentFluid = tile.getField(TileEntityAnvilAuto.Fields.FLUID.ordinal());
    this.mc.getTextureManager().bindTexture(Const.Res.FLUID);
    int pngWidth = 36, pngHeight = 124, f = 2, h = pngHeight / f;//f is scale factor. original is too big
    int fuelWidth = 16;
    int x = this.guiLeft + this.xSize / 2 - fuelWidth / 2 - 1, y = this.guiTop + fuelWidth;
    Gui.drawModalRectWithCustomSizedTexture(
        x, y, u, v,
        pngWidth / f, h,
        pngWidth / f, h);
    h -= 2;// inner texture is 2 smaller, one for each border
    this.mc.getTextureManager().bindTexture(Const.Res.FLUID_LAVA);
    float percent = ((float) currentFluid / ((float) TileEntityAnvilAuto.TANK_FULL));
    int hpct = (int) (h * percent);
    Gui.drawModalRectWithCustomSizedTexture(
        x + 1, y + 1 + h - hpct,
        u, v,
        fuelWidth, hpct,
        fuelWidth, h);
  }
}
